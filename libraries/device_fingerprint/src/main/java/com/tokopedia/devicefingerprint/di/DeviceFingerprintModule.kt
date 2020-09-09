package com.tokopedia.devicefingerprint.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.devicefingerprint.utils.DeviceInfoPayloadCreator
import com.tokopedia.encryption.security.RSA
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class DeviceFingerprintModule {

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    fun provideRsa(): RSA {
        return RSA()
    }

    @Provides
    fun provideFusedLocationProviderClient(
            @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun provideSession(
            @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideDeviceInfoPayloadCreator(
            @ApplicationContext context: Context,
            fusedLocationProviderClient: FusedLocationProviderClient
    ): DeviceInfoPayloadCreator {
        return DeviceInfoPayloadCreator(context, UserSession(context), fusedLocationProviderClient)
    }

}
