package com.tokopedia.loginregister.login.view.listener

import android.content.Context
import androidx.fragment.app.Fragment
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.view.banner.data.DynamicBannerDataModel
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.discover.data.DiscoverItemDataModel
import com.tokopedia.loginregister.login.domain.StatusFingerprint
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.StatusPinData
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import java.util.*

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

        fun onSuccessLoginEmail(loginTokenPojo: LoginTokenPojo? = null)

        fun onSuccessReloginAfterSQ(loginTokenPojo: LoginTokenPojo)

        fun showLoadingDiscover()

        fun dismissLoadingDiscover()

        fun onErrorDiscoverLogin(throwable: Throwable)

        fun onSuccessDiscoverLogin(providers: ArrayList<DiscoverItemDataModel>)

        fun getFacebookCredentialListener(): GetFacebookCredentialSubscriber.GetFacebookCredentialListener

        fun isFromRegister(): Boolean

        fun setSmartLock()

        fun stopTrace()

        fun setLoginSuccessSellerApp()

        fun onErrorLoginEmail(email: String): Function1<Throwable, Unit>

        fun onErrorReloginAfterSQ(): Function1<Throwable, Unit>

        fun onErrorLoginFacebook(email: String): Function1<Throwable, Unit>

        fun onSuccessLoginFacebookPhone(): Function1<LoginTokenPojo, Unit>

        fun onErrorLoginFacebookPhone(): Function1<Throwable, Unit>

        fun onErrorLoginGoogle(email: String?): Function1<Throwable, Unit>

        fun onSuccessGetUserInfo(): Function1<ProfilePojo, Unit>

        fun onErrorGetUserInfo(): Function1<Throwable, Unit>

        fun showPopup(): Function1<PopupError, Unit>

        fun onGoToActivationPage(email: String)

        fun onGoToSecurityQuestion(email: String): Function0<Unit>

        fun onGoToActivationPageAfterRelogin(): Function1<MessageErrorException, Unit>

        fun onGoToSecurityQuestionAfterRelogin(): Function0<Unit>

        fun onGoToForbiddenPage()

        fun onErrorValidateRegister(throwable: Throwable)

        fun onErrorEmptyEmailPhone()

        fun goToLoginPhoneVerifyPage(phoneNumber: String)

        fun goToRegisterPhoneVerifyPage(phoneNumber: String)

        fun onEmailExist(email: String)

        fun showNotRegisteredEmailDialog(email: String, isPending: Boolean)

        fun onBackPressed()

        fun trackSuccessValidate()

        fun onSuccessGetTickerInfo(listTickerInfo: List<TickerInfoPojo>)

        fun onErrorGetTickerInfo(error: Throwable)

        fun onGetDynamicBannerSuccess(dynamicBannerDataModel: DynamicBannerDataModel)

        fun onGetDynamicBannerError(throwable: Throwable)

        fun onErrorCheckStatusFingerprint(e: Throwable)

        fun onSuccessCheckStatusFingerprint(data: StatusFingerprint)

        fun goToFingerprintRegisterPage()

        fun getFingerprintConfig(): Boolean

        fun routeToVerifyPage(phoneNumber: String, requestCode: Int, otpType: Int)

        fun goToChooseAccountPage(accessToken: String, phoneNumber: String)

        fun goToChooseAccountPageFacebook(accessToken: String)

        fun goToAddPin2FA(enableSkip2FA: Boolean)

        fun goToAddNameFromRegisterPhone(uuid: String, msisdn: String)

        fun onGoToChangeName()

        fun goToForgotPassword()

        fun goToTokopediaCareWebview()

        fun goToRegisterInitial(source: String)

        fun openGoogleLoginIntent()

        fun onSuccessActivateUser(activateUserData: ActivateUserData)

        fun onFailedActivateUser(throwable: Throwable)

        fun showLocationAdminPopUp()

        fun showGetAdminTypeError(throwable: Throwable)
    }

    interface Presenter : CustomerPresenter<View> {
        fun loginEmail(email: String, password: String, isSmartLock : Boolean = false)

        fun loginEmailV2(email: String, password: String, isSmartLock : Boolean = false, useHash: Boolean = false)

        fun loginGoogle(accessToken: String, email: String)

        fun getFacebookCredential(fragment: Fragment, callbackManager: CallbackManager)

        fun getUserInfo()

        fun getUserInfoFingerprint()

        fun discoverLogin(context: Context)

        fun loginFacebook(context: Context, accessToken: AccessToken, email: String)

        fun loginFacebookPhone(context: Context, accessToken: AccessToken, phone: String)

        fun reloginAfterSQ(validateToken: String)

        fun getTickerInfo()

        fun checkStatusPin(onSuccess: (StatusPinData) -> kotlin.Unit, onError: (kotlin.Throwable) -> kotlin.Unit)

        fun checkStatusFingerprint()

        fun registerCheck(id: String, onSuccess: (RegisterCheckData) -> kotlin.Unit, onError: (kotlin.Throwable) -> kotlin.Unit)

        fun removeFingerprintData()

        fun getDynamicBanner(page: String)

        fun cancelJobs()
    }
}