package com.tokopedia.oneclickcheckout.preference.edit.di

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.oneclickcheckout.common.PAYMENT_LISTING_URL
import com.tokopedia.oneclickcheckout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.oneclickcheckout.preference.edit.data.payment.OvoTopUpUrlGqlResponse
import com.tokopedia.oneclickcheckout.preference.edit.data.payment.PaymentListingParamGqlResponse
import com.tokopedia.oneclickcheckout.preference.edit.domain.payment.GetOvoTopUpUrlUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.payment.GetPaymentListingParamUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.payment.GetPaymentListingParamUseCaseImpl
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
open class PreferenceEditModule(private val activity: Activity) {

    @PreferenceEditScope
    @Provides
    fun provideContext(): Context = activity

    @PreferenceEditScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @PreferenceEditScope
    @Provides
    internal fun providesGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @PreferenceEditScope
    @Provides
    fun provideScheduler(): SchedulerProvider {
        return MainScheduler()
    }

    @PreferenceEditScope
    @Provides
    fun providePreferenceListAnalytics(): PreferenceListAnalytics {
        return PreferenceListAnalytics()
    }

    @PreferenceEditScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @PreferenceEditScope
    @Provides
    fun provideGetPaymentListingGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<PaymentListingParamGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceEditScope
    @Provides
    fun provideGetPaymentListingParamUseCase(graphqlUseCase: GraphqlUseCase<PaymentListingParamGqlResponse>): GetPaymentListingParamUseCase {
        return GetPaymentListingParamUseCaseImpl(graphqlUseCase)
    }

    @PreferenceEditScope
    @Provides
    @Named(PAYMENT_LISTING_URL)
    open fun providePaymentListingUrl(): String {
        return "${TokopediaUrl.getInstance().PAY}/v2/payment/register/listing"
    }

    @PreferenceEditScope
    @Provides
    fun provideGetOvoTopUpUrlUseCase(graphqlUseCase: GraphqlUseCase<OvoTopUpUrlGqlResponse>): GetOvoTopUpUrlUseCase {
        return GetOvoTopUpUrlUseCase(graphqlUseCase)
    }
}