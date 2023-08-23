package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.GQL_THANK_YOU_PAGE_DATA
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.model.ThanksPageResponse
import javax.inject.Inject
import javax.inject.Named

class ThanksPageDataUseCase @Inject constructor(
        @Named(GQL_THANK_YOU_PAGE_DATA) val query: String, graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<ThanksPageResponse>(graphqlRepository) {

    fun getThankPageData(onSuccess: (ThanksPageData) -> Unit,
                         onError: (Throwable) -> Unit, paymentId: String, merchant: String) {
        try {
            this.setTypeClass(ThanksPageResponse::class.java)
            this.setRequestParams(getRequestParams(paymentId, merchant))
            this.setGraphqlQuery(query)
            this.execute(
                    { result ->
                        onSuccess(result.thanksPageData)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(paymentId: String, merchant: String): Map<String, Any> {
        return mapOf(PARAM_PAYMENT_ID to paymentId,
                PARAM_MERCHANT to merchant)
    }

    companion object {
        const val PARAM_PAYMENT_ID = "paymentIDStr"
        const val PARAM_MERCHANT = "merchant"
    }
}
