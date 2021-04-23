package com.tokopedia.loginregister.login.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.loginregister.common.data.model.DynamicBannerDataModel
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.StatusPinData
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.profile.ProfilePojo

/**
 * @author by nisie on 18/01/19.
 */
interface LoginEmailPhoneContract {

    interface View : CustomerView {
        fun resetError()

        fun showLoadingLogin()

        fun showErrorPassword(resId: Int)

        fun showErrorEmail(resId: Int)

        fun dismissLoadingLogin()

        fun onSuccessLogin()

        fun onSuccessLoginEmail()

        fun isFromRegister(): Boolean

        fun stopTrace()

        fun onErrorLoginEmail(email: String): Function1<Throwable, Unit>

        fun onErrorReloginAfterSQ(validateToken: String): Function1<Throwable, Unit>

        fun onSuccessGetUserInfo(): Function1<ProfilePojo, Unit>

        fun onErrorGetUserInfo(): Function1<Throwable, Unit>

        fun onSuccessGetUserInfoAddPin(): Function1<ProfilePojo, Unit>

        fun onGoToActivationPageAfterRelogin(): Function1<MessageErrorException, Unit>

        fun onGoToSecurityQuestionAfterRelogin(): Function0<Unit>

        fun onGoToForbiddenPage()

        fun onErrorValidateRegister(throwable: Throwable)

        fun onErrorEmptyEmailPhone()

        fun onEmailExist(email: String)

        fun showNotRegisteredEmailDialog(email: String, isPending: Boolean)

        fun onBackPressed()

        fun onSuccessGetTickerInfo(listTickerInfo: List<TickerInfoPojo>)

        fun onErrorGetTickerInfo(error: Throwable)

        fun onGetDynamicBannerSuccess(dynamicBannerDataModel: DynamicBannerDataModel)

        fun onGetDynamicBannerError(throwable: Throwable)
    }

    interface Presenter : CustomerPresenter<View> {
        fun loginEmail(email: String, password: String, isSmartLock : Boolean = false)

        fun getUserInfo()

        fun getUserInfoAddPin()

        fun reloginAfterSQ(validateToken: String)

        fun getTickerInfo()

        fun checkStatusPin(onSuccess: (StatusPinData) -> kotlin.Unit, onError: (kotlin.Throwable) -> kotlin.Unit)

        fun registerCheck(id: String, onSuccess: (RegisterCheckData) -> kotlin.Unit, onError: (kotlin.Throwable) -> kotlin.Unit)

        fun getDynamicBanner(page: String)
    }
}