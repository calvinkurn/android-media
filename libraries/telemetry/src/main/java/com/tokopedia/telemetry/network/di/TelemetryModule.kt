package com.tokopedia.telemetry.network.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.encryption.security.AESEncryptorGCM
import com.tokopedia.encryption.security.RSA
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class TelemetryModule(val context: Context) {

    @TelemetryScope
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @TelemetryScope
    @Provides
    fun provideRsa(): RSA {
        return RSA()
    }

    @TelemetryScope
    @Provides
    fun provideAesGcm(): AESEncryptorGCM {
        val source = (('0'..'9') + ('a'..'z') + ('A'..'Z')).toList()
        val randomNonce = (1..12).map { source.random() }.joinToString("")
        return AESEncryptorGCM(randomNonce, true)
    }

    @TelemetryScope
    @Provides
    fun provideSession(): UserSessionInterface {
        return UserSession(context)
    }

    @TelemetryScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository =
        GraphqlInteractor.getInstance().graphqlRepository

}
