package com.tokopedia.settingbank.choosebank.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.settingbank.addeditaccount.view.listener.ChooseBankContract
import com.tokopedia.settingbank.choosebank.domain.usecase.GetBankListUseCase
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.user.session.UserSession
import rx.Subscriber

/**
 * @author by nisie on 7/2/18.
 */
class ChooseBankPresenter(private val userSession: UserSession,
                          private val getBankListUseCase: GetBankListUseCase) :
        ChooseBankContract.Presenter,
        BaseDaggerPresenter<ChooseBankContract.View>() {

    override fun getBankList(query: String) {

        view.showLoading()
        getBankListUseCase.execute(GetBankListUseCase.getParam(query,
                1,
                userSession.userId,
                userSession.deviceId
        ), object :
                Subscriber<BankListViewModel>
                () {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.hideLoading()
                val errorMessage: String = ErrorHandler.getErrorMessage(view.getContext(), e)

                view.onErrorGetBankList(errorMessage)
            }

            override fun onNext(listBank: BankListViewModel) {
                view.hideLoading()
                view.onSuccessGetBankList(listBank)
            }
        })
    }

}