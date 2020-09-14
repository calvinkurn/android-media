package com.tokopedia.otp.common.di

import android.content.Context
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.otp.common.DispatcherProvider
import com.tokopedia.otp.verification.view.viewbinding.OnboardingMisscallViewBinding
import com.tokopedia.otp.verification.view.viewbinding.VerificationMethodViewBinding
import com.tokopedia.otp.verification.view.viewbinding.VerificationViewBinding
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@OtpScope
@Module
class OtpModule {

    @OtpScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @OtpScope
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = object : DispatcherProvider {
        override fun ui(): CoroutineDispatcher = Dispatchers.Main

        override fun io(): CoroutineDispatcher = Dispatchers.IO
    }

    @OtpScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @OtpScope
    @Provides
    fun provideOnboardingMisscallViewBinding() = OnboardingMisscallViewBinding()

    @OtpScope
    @Provides
    fun provideVerificationViewBinding() = VerificationViewBinding()

    @OtpScope
    @Provides
    fun provideVerificationMethodViewBinding() = VerificationMethodViewBinding()

    @OtpScope
    @Provides
    fun provideSmsRetriever(@ApplicationContext context: Context): SmsRetrieverClient = SmsRetriever.getClient(context)
}