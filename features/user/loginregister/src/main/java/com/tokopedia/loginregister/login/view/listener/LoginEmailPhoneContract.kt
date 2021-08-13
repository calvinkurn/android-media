package com.tokopedia.loginregister.login.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserData
import com.tokopedia.loginregister.common.view.banner.data.DynamicBannerDataModel
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.discover.data.DiscoverItemDataModel
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

        fun stopTrace()

        fun setLoginSuccessSellerApp()

        fun onErrorLoginEmail(email: String): Function1<Throwable, Unit>

        fun onErrorReloginAfterSQ(): Function1<Throwable, Unit>

        fun onErrorLoginFacebook(email: String): Function1<Throwable, Unit>

        fun onSuccessLoginFacebookPhone(): Function1<LoginTokenPojo, Unit>

        fun onErrorLoginFacebookPhone(): Function1<Throwable, Unit>

        fun onErrorLoginGoogle(email: String?): Function1<Throwable, Unit>

        fun onSuccessGetUserInfo(profilePojo: ProfilePojo)

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

        fun goToFingerprintRegisterPage()

        fun getFingerprintConfig(): Boolean

        fun routeToVerifyPage(phoneNumber: String, requestCode: Int, otpType: Int)

        fun goToChooseAccountPage(accessToken: String, phoneNumber: String)

        fun goToChooseAccountPageFacebook(accessToken: String)

        fun goToChooseAccountPageFingerprint(validateToken: String)

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
}