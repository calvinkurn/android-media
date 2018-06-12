package com.tokopedia.settingbank.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.settingbank.domain.usecase.GetBankListUseCase
import com.tokopedia.settingbank.view.listener.SettingBankContract
import com.tokopedia.user.session.UserSession

/**
 * @author by nisie on 6/7/18.
 */
class SettingBankPresenter(val userSession: UserSession,
                           val getBankAccountUseCase: GetBankListUseCase) :
        SettingBankContract.Presenter,
        BaseDaggerPresenter<SettingBankContract.View>() {


    override fun getBankList() {
//        var page : Int = 0
//        getBankAccountUseCase.execute(GetBankListUseCase.getParam(
//                userSession.user
//        ), object : Subscriber<ChangePasswordDomain>() {
//            override fun onCompleted() {
//
//            }
//
//            override fun onError(e: Throwable) {
//                view.onErrorChangePassword(e.toString())
//            }
//
//            override fun onNext(changePasswordDomain: ChangePasswordDomain) {
//                if (changePasswordDomain.is_success) {
//                    view.onSuccessChangePassword()
//                } else {
//                    view.onErrorChangePassword("")
//                }
//            }
//        })
    }

    override fun detachView() {
        super.detachView()
        getBankAccountUseCase.unsubscribe()
    }

}