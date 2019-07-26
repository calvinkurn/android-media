package com.tokopedia.loginphone.choosetokocashaccount.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.loginphone.choosetokocashaccount.data.AccountList
import com.tokopedia.loginphone.choosetokocashaccount.data.UserDetail
import com.tokopedia.loginphone.choosetokocashaccount.domain.GetAccountsListUseCase
import com.tokopedia.loginphone.choosetokocashaccount.view.listener.ChooseTokocashAccountContract
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.user.session.UserSessionInterface

import javax.inject.Inject
import javax.inject.Named

import rx.Subscriber

/**
 * @author by nisie on 12/4/17.
 */

class ChooseTokocashAccountPresenter @Inject
constructor(private val getAccountsListUseCase: GetAccountsListUseCase,
            @param:Named(SessionModule.SESSION_MODULE) private val userSessionInterface: UserSessionInterface,
            private val loginTokenUseCase: LoginTokenUseCase,
            private val getProfileUseCase: GetProfileUseCase) : BaseDaggerPresenter<ChooseTokocashAccountContract.View>(), ChooseTokocashAccountContract.Presenter {

    override fun detachView() {
        super.detachView()
        getAccountsListUseCase.unsubscribe()
        loginTokenUseCase.unsubscribe()
        getProfileUseCase.unsubscribe()
    }

    override fun loginWithTokocash(key: String, accountTokocash: UserDetail, phoneNumber: String) {
        view.showLoadingProgress()
        loginTokenUseCase.executeLoginPhoneNumber(LoginTokenUseCase.generateParamLoginPhone(
                key,
                accountTokocash.email,
                phoneNumber), LoginTokenSubscriber(userSessionInterface,
                view.onSuccessLoginToken(),
                view.onErrorLoginToken(),
                view.onGoToActivationPage(),
                view.onGoToSecurityQuestion()))
    }

    override fun getAccountList(validateToken: String, phoneNumber: String) {
        getAccountsListUseCase.execute(GetAccountsListUseCase.getParam(validateToken,
                phoneNumber),
                object : Subscriber<AccountList>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view.onErrorGetAccountList(e)
                    }

                    override fun onNext(accountList: AccountList) {
                        if (accountList.accountListPojo.errors.isEmpty()) {
                            view.onSuccessGetAccountList(accountList)
                        } else {
                            view.onErrorGetAccountList(MessageErrorException(accountList.accountListPojo.errors[0].message))
                        }
                    }
                })
    }

    override fun getUserInfo() {
        getProfileUseCase.execute(GetProfileSubscriber(userSessionInterface,
                view.onSuccessGetUserInfo(),
                view.onErrorGetUserInfo(),
                view.onGoToCreatePassword(),
                view.onGoToPhoneVerification(),
                false))
    }
}
