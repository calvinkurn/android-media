package com.tokopedia.emoney.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.encryption.security.AESEncryptorGCM
import com.tokopedia.encryption.security.RSA
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class DigitalEmoneyModule {

    @DigitalEmoneyScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @DigitalEmoneyScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @DigitalEmoneyScope
    @Provides
    fun provideRSA(): RSA {
        return RSA()
    }

    @DigitalEmoneyScope
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().disableHtmlEscaping().create()
    }

    @DigitalEmoneyScope
    @Provides
    fun provideAESEncryptorGCM(): AESEncryptorGCM {
        val source = (('0'..'9') + ('a'..'z') + ('A'..'Z')).toList()
        val randomNonce = (1..12).map { source.random() }.joinToString("")
        return AESEncryptorGCM(randomNonce, true)
    }
}
