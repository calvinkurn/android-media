package com.tokopedia.rechargeocr.di

import com.tokopedia.permissionchecker.PermissionCheckerHelper
import dagger.Module
import dagger.Provides

@Module
class RechargeCameraModule {

    @RechargeCameraScope
    @Provides
    fun providePermissionCheckerHelper(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }
}