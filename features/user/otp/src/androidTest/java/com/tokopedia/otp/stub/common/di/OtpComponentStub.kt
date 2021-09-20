package com.tokopedia.otp.stub.common.di

import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.common.di.OtpModule
import com.tokopedia.otp.common.di.OtpScope
import com.tokopedia.otp.common.di.OtpViewModelModule
import com.tokopedia.otp.verification.base.VerificationTest
import dagger.Component

@OtpScope
@Component(
        modules = [
            OtpModule::class,
            OtpViewModelModule::class,
            OtpFakeUseCaseModule::class,
            OtpResponseModule::class
        ],
        dependencies = [FakeBaseAppComponent::class]
)
interface OtpComponentStub : OtpComponent {
    fun inject(verificationTest: VerificationTest)
}