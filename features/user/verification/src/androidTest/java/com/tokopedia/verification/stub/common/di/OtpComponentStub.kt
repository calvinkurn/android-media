package com.tokopedia.verification.stub.common.di

import com.tokopedia.verification.common.di.OtpComponent
import com.tokopedia.verification.common.di.OtpModule
import com.tokopedia.verification.common.di.OtpScope
import com.tokopedia.verification.common.di.OtpViewModelModule
import com.tokopedia.verification.verification.base.VerificationTest
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
