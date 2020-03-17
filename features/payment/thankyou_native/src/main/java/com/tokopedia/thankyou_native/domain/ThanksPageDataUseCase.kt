package com.tokopedia.thankyou_native.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.GQL_THANK_YOU_PAGE_DATA
import javax.inject.Inject
import javax.inject.Named

class ThanksPageDataUseCase @Inject constructor(
        @Named(GQL_THANK_YOU_PAGE_DATA) val query: String, graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<ThanksPageResponse>(graphqlRepository) {

    fun getThankPageData(onSuccess: (ThanksPageData) -> Unit,
                         onError: (Throwable) -> Unit, paymentId: Long, merchant: String) {
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
            throw throwable
        }
    }

    private fun getRequestParams(paymentId: Long, merchant: String): Map<String, Any> {
        return mapOf(PARAM_PAYMENT_ID to paymentId,
                PARAM_MERCHANT to merchant)
    }

    companion object {
        const val PARAM_PAYMENT_ID = "paymentID"
        const val PARAM_MERCHANT = "merchant"
    }
}