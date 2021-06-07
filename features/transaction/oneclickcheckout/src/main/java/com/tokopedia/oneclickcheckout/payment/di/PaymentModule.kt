package com.tokopedia.oneclickcheckout.payment.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.oneclickcheckout.common.PAYMENT_LISTING_URL
import com.tokopedia.oneclickcheckout.payment.analytics.PaymentListingAnalytics
import com.tokopedia.oneclickcheckout.payment.data.OvoTopUpUrlGqlResponse
import com.tokopedia.oneclickcheckout.payment.domain.GetPaymentListingParamUseCase
import com.tokopedia.oneclickcheckout.payment.domain.GetPaymentListingParamUseCaseImpl
import com.tokopedia.oneclickcheckout.payment.list.data.PaymentListingParamGqlResponse
import com.tokopedia.oneclickcheckout.payment.topup.domain.GetOvoTopUpUrlUseCase
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
open class PaymentModule {

    @PaymentScope
    @Provides
    internal fun providesGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @PaymentScope
    @Provides
    fun providePaymentListingAnalytics(): PaymentListingAnalytics {
        return PaymentListingAnalytics()
    }

    @PaymentScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @PaymentScope
    @Provides
    fun provideGetPaymentListingGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<PaymentListingParamGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PaymentScope
    @Provides
    fun provideGetPaymentListingParamUseCase(graphqlUseCase: GraphqlUseCase<PaymentListingParamGqlResponse>): GetPaymentListingParamUseCase {
        return GetPaymentListingParamUseCaseImpl(graphqlUseCase)
    }

    @PaymentScope
    @Provides
    @Named(PAYMENT_LISTING_URL)
    open fun providePaymentListingUrl(): String {
        return "${TokopediaUrl.getInstance().PAY}/v2/payment/register/listing"
    }

    @PaymentScope
    @Provides
    fun provideGetOvoTopUpUrlUseCase(graphqlUseCase: GraphqlUseCase<OvoTopUpUrlGqlResponse>): GetOvoTopUpUrlUseCase {
        return GetOvoTopUpUrlUseCase(graphqlUseCase)
    }
}