package com.tokopedia.oneclickcheckout.payment.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.oneclickcheckout.common.PAYMENT_LISTING_URL
import com.tokopedia.oneclickcheckout.payment.list.domain.GetPaymentListingParamUseCase
import com.tokopedia.oneclickcheckout.payment.list.domain.GetPaymentListingParamUseCaseImpl
import com.tokopedia.oneclickcheckout.payment.topup.domain.GetOvoTopUpUrlUseCase
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class PaymentModule {

    @PaymentScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @PaymentScope
    @Provides
    fun provideGetPaymentListingParamUseCase(@ApplicationContext graphqlRepository: GraphqlRepository): GetPaymentListingParamUseCase {
        return GetPaymentListingParamUseCaseImpl(GraphqlUseCase(graphqlRepository))
    }

    @PaymentScope
    @Provides
    @Named(PAYMENT_LISTING_URL)
    fun providePaymentListingUrl(): String {
        return "${TokopediaUrl.getInstance().PAY}/v2/payment/register/listing"
    }

    @PaymentScope
    @Provides
    fun provideGetOvoTopUpUrlUseCase(@ApplicationContext graphqlRepository: GraphqlRepository): GetOvoTopUpUrlUseCase {
        return GetOvoTopUpUrlUseCase(GraphqlUseCase(graphqlRepository))
    }
}