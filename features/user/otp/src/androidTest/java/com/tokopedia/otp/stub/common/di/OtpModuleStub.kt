package com.tokopedia.otp.stub.common.di

import com.tokopedia.otp.common.di.OtpModule
import dagger.Module

@Module(
        includes = [
            OtpModule::class
        ]
)
class OtpModuleStub {
}