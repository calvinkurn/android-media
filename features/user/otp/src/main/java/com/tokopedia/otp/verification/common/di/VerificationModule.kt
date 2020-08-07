package com.tokopedia.otp.verification.common.di

import android.content.Context
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.otp.verification.common.DispatcherProvider
import com.tokopedia.otp.verification.view.viewbinding.OnboardingMisscallViewBinding
import com.tokopedia.otp.verification.view.viewbinding.VerificationMethodViewBinding
import com.tokopedia.otp.verification.view.viewbinding.VerificationViewBinding
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Ade Fulki on 2019-10-20.
 * ade.hadian@tokopedia.com
 */

@VerificationScope
@Module
class VerificationModule{

    @VerificationScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @VerificationScope
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = object : DispatcherProvider {
        override fun ui(): CoroutineDispatcher = Dispatchers.Main

        override fun io(): CoroutineDispatcher = Dispatchers.IO
    }

    @VerificationScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @VerificationScope
    @Provides
    fun provideOnboardingMisscallViewBinding() = OnboardingMisscallViewBinding()

    @VerificationScope
    @Provides
    fun provideVerificationViewBinding() = VerificationViewBinding()

    @VerificationScope
    @Provides
    fun provideVerificationMethodViewBinding() = VerificationMethodViewBinding()

    @VerificationScope
    @Provides
    fun provideSmsRetriever(@ApplicationContext context: Context): SmsRetrieverClient = SmsRetriever.getClient(context)
}