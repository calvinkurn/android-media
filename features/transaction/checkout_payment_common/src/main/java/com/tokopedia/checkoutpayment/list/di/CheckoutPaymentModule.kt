package com.tokopedia.checkoutpayment.list.di

import com.google.gson.Gson
import com.tokopedia.checkoutpayment.list.data.PaymentListingParamGqlResponse
import com.tokopedia.checkoutpayment.list.domain.GetPaymentListingParamUseCase
import com.tokopedia.checkoutpayment.list.domain.GetPaymentListingParamUseCaseImpl
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.url.TokopediaUrl
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [PurchasePlatformBaseModule::class])
class CheckoutPaymentModule {

    @CheckoutPaymentScope
    @Provides
    fun provideGetPaymentListingParamUseCase(
            graphqlUseCase: GraphqlUseCase<PaymentListingParamGqlResponse>,
            gson: Gson
    ): GetPaymentListingParamUseCase {
        return GetPaymentListingParamUseCaseImpl(graphqlUseCase, gson)
    }

    @CheckoutPaymentScope
    @Provides
    @Named(PAYMENT_LISTING_URL)
    fun providePaymentListingUrl(): String {
        return "${TokopediaUrl.getInstance().PAY}/v2/payment/register/listing"
    }
}

const val PAYMENT_LISTING_URL = "PaymentListingUrl"
