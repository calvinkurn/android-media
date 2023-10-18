package com.tokopedia.profilecompletion.di

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.interceptors.authenticator.TkpdAuthenticatorGql
import com.tokopedia.interceptors.refreshtoken.RefreshTokenGql
import com.tokopedia.network.NetworkRouter
import com.tokopedia.profilecompletion.common.LoadingDialog
import com.tokopedia.profilecompletion.common.analytics.TrackingPinUtil
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class ProfileCompletionSettingModule(private val context: Context) {

    @Provides
    @ProfileCompletionContext
    fun provideContext(): Context {
        return context
    }

    @Provides
    open fun provideGraphQlRepository(): GraphqlRepository =
        GraphqlInteractor.getInstance().graphqlRepository

    @ProfileCompletionSettingScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    open fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface =
        UserSession(context)

    @Provides
    fun provideTrackingPinUtil() = TrackingPinUtil()

    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    fun provideLoadingDialog(): LoadingDialog = LoadingDialog(context)

    @Provides
    @ProfileCompletionSettingScope
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @ProfileCompletionSettingScope
    fun provideTkpdAuthGql(
        @ApplicationContext context: Context,
        networkRouter: NetworkRouter,
        userSessionInterface: UserSessionInterface
    ): TkpdAuthenticatorGql {
        return TkpdAuthenticatorGql(
            application = context as Application,
            networkRouter = networkRouter,
            userSession = userSessionInterface,
            RefreshTokenGql()
        )
    }

    @Provides
    @ProfileCompletionSettingScope
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        profileManagementInterceptor: ProfileManagementInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        tkpdAuthenticatorGql: TkpdAuthenticatorGql
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(profileManagementInterceptor)
        builder.authenticator(tkpdAuthenticatorGql)

        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
            builder.addInterceptor(ChuckerInterceptor(context))
        }

        return builder.build()
    }

    @Provides
    @ProfileCompletionSettingScope
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TokopediaUrl.getInstance().GOTO_ACCOUNTS)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @ProfileCompletionSettingScope
    fun provideProfileManagementApi(retrofit: Retrofit): ProfileManagementApi {
        return retrofit.create(ProfileManagementApi::class.java)
    }
}
