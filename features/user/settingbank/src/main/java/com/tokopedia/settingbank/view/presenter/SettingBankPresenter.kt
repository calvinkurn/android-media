package com.tokopedia.settingbank.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.settingbank.domain.usecase.GetBankListUseCase
import com.tokopedia.settingbank.view.listener.SettingBankContract
import com.tokopedia.settingbank.view.viewmodel.BankAccountListViewModel
import com.tokopedia.user.session.UserSession
import rx.Subscriber

/**
 * @author by nisie on 6/7/18.
 */
class SettingBankPresenter(val userSession: UserSession,
                           val getBankAccountUseCase: GetBankListUseCase) :
        SettingBankContract.Presenter,
        BaseDaggerPresenter<SettingBankContract.View>() {


    override fun getBankList() {
        val page = 0
        getBankAccountUseCase.execute(GetBankListUseCase.getParam(
                userSession.userId,
                page,
                userSession.deviceId
        ), object : Subscriber<BankAccountListViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.onErrorGetListBank(e.toString())
            }

            override fun onNext(bankAccountList: BankAccountListViewModel) {
                if (bankAccountList.list?.isNotEmpty()!!) {
                    view.onSuccessGetListBank(bankAccountList)
                }
            }
        })
    }

    override fun detachView() {
        super.detachView()
        getBankAccountUseCase.unsubscribe()
    }

}