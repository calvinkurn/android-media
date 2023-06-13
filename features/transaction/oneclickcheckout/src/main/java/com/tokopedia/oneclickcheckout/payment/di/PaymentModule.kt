package com.tokopedia.oneclickcheckout.payment.di

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.oneclickcheckout.common.PAYMENT_LISTING_URL
import com.tokopedia.oneclickcheckout.payment.list.data.PaymentListingParamGqlResponse
import com.tokopedia.oneclickcheckout.payment.list.domain.GetPaymentListingParamUseCase
import com.tokopedia.oneclickcheckout.payment.list.domain.GetPaymentListingParamUseCaseImpl
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.url.TokopediaUrl
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [PurchasePlatformBaseModule::class])
class PaymentModule {

    @PaymentScope
    @Provides
    fun provideGetPaymentListingParamUseCase(
        graphqlUseCase: GraphqlUseCase<PaymentListingParamGqlResponse>
    ): GetPaymentListingParamUseCase {
        return GetPaymentListingParamUseCaseImpl(graphqlUseCase)
    }

    @PaymentScope
    @Provides
    @Named(PAYMENT_LISTING_URL)
    fun providePaymentListingUrl(): String {
        return "${TokopediaUrl.getInstance().PAY}/v2/payment/register/listing"
    }
}
