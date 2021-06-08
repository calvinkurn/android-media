package com.tokopedia.otp.stub.common.di

import com.tokopedia.otp.common.di.OtpViewModelModule
import dagger.Module

@Module(
        includes = [
            OtpViewModelModule::class
        ]
)
class OtpViewModelModuleStub