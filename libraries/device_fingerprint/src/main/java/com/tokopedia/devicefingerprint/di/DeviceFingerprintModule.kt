package com.tokopedia.devicefingerprint.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.devicefingerprint.submitdevice.utils.DeviceInfoPayloadCreator
import com.tokopedia.encryption.security.RSA
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class DeviceFingerprintModule(val context: Context) {

    @DeviceFingerprintScope
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @DeviceFingerprintScope
    @Provides
    fun provideRsa(): RSA {
        return RSA()
    }

    @DeviceFingerprintScope
    @Provides
    fun provideFusedLocationProviderClient(): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @DeviceFingerprintScope
    @Provides
    fun provideSession(): UserSessionInterface {
        return UserSession(context)
    }

    @DeviceFingerprintScope
    @Provides
    fun provideDeviceInfoPayloadCreator(
            fusedLocationProviderClient: FusedLocationProviderClient
    ): DeviceInfoPayloadCreator {
        return DeviceInfoPayloadCreator(context, UserSession(context), fusedLocationProviderClient)
    }

    @DeviceFingerprintScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

}
