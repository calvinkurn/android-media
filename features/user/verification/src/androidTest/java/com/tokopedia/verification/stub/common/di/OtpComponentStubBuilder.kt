package com.tokopedia.verification.stub.common.di

import android.content.Context
import com.tokopedia.verification.common.di.OtpModule

object OtpComponentStubBuilder {

    fun getComponent(applicationContext: Context, activityContext: Context): OtpComponentStub =
            DaggerOtpComponentStub.builder()
                    .otpFakeUseCaseModule(OtpFakeUseCaseModule())
                    .otpModule(OtpModule(activityContext))
                    .fakeBaseAppComponent(FakeBaseAppComponentBuilder.getComponent(applicationContext))
                    .build()
}
