package com.tokopedia.loginHelper.di.module

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.loginHelper.data.api.LoginHelperApiService
import com.tokopedia.loginHelper.data.repository.LoginHelperRepositoryImpl
import com.tokopedia.loginHelper.di.scope.LoginHelperScope
import com.tokopedia.loginHelper.util.ENCRYPTION_KEY
import com.tokopedia.loginHelper.util.LOGIN_HELPER_BASE_URL
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.crypto.SecretKey

@Module
class LoginHelperModule {

    @LoginHelperScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @LoginHelperScope
    @Provides
    fun provideInterceptors(loggingInterceptor: HttpLoggingInterceptor): MutableList<Interceptor> {
        return mutableListOf(loggingInterceptor)
    }

    @LoginHelperScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @LoginHelperScope
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @LoginHelperScope
    @Provides
    fun provideEncryptor(): AESEncryptorCBC {
        return AESEncryptorCBC(ENCRYPTION_KEY)
    }

    @LoginHelperScope
    @Provides
    fun provideEncryptSecretKey(aesEncryptorCBC: AESEncryptorCBC): SecretKey {
        return aesEncryptorCBC.generateKey(ENCRYPTION_KEY)
    }

    @LoginHelperScope
    @Provides
    fun provideProfileService(retrofit: Retrofit): LoginHelperApiService = retrofit.create(
        LoginHelperApiService::class.java
    )

    @LoginHelperScope
    @Provides
    fun provideNetwork(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOGIN_HELPER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @LoginHelperScope
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(loggingInterceptor)
        client.hostnameVerifier { _, _ -> true }
        return client.build()
    }

    @LoginHelperScope
    @Provides
    fun provideLoginHelperRepository(
        api: LoginHelperApiService,
        aesEncryptorCBC: AESEncryptorCBC,
        secretKey: SecretKey
    ): LoginHelperRepositoryImpl {
        return LoginHelperRepositoryImpl(
            api,
            aesEncryptorCBC,
            secretKey
        )
    }
}
