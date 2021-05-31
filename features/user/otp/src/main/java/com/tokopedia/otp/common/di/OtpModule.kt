package com.tokopedia.otp.common.di

import android.content.Context
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.otp.common.LoadingDialog
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class OtpModule (val context: Context) {

    @OtpScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @OtpScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @OtpScope
    @Provides
    fun provideSmsRetriever(@ApplicationContext context: Context): SmsRetrieverClient = SmsRetriever.getClient(context)

    @OtpScope
    @Provides
    fun provideLoadingDialog(): LoadingDialog = LoadingDialog(context)

    @OtpScope
    @Provides
    fun provideRemoteConfig(): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}