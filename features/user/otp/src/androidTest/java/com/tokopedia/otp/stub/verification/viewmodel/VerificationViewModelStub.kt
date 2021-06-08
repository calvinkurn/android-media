package com.tokopedia.otp.stub.verification.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.otp.verification.domain.usecase.*
import com.tokopedia.otp.verification.viewmodel.VerificationViewModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface

class VerificationViewModelStub(
        getVerificationMethodUseCase: GetVerificationMethodUseCase,
        getVerificationMethodUseCase2FA: GetVerificationMethodUseCase2FA,
        otpValidateUseCase: OtpValidateUseCase,
        otpValidateUseCase2FA: OtpValidateUseCase2FA,
        sendOtpUseCase: SendOtpUseCase, sendOtpUseCase2FA: SendOtp2FAUseCase,
        userSession: UserSessionInterface, remoteConfig: RemoteConfig,
        dispatcherProvider: CoroutineDispatchers
) : VerificationViewModel(getVerificationMethodUseCase, getVerificationMethodUseCase2FA, otpValidateUseCase, otpValidateUseCase2FA, sendOtpUseCase, sendOtpUseCase2FA, userSession, remoteConfig, dispatcherProvider) {

}