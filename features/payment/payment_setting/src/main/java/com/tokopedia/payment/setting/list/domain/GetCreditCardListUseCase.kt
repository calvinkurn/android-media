package com.tokopedia.payment.setting.list.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.payment.setting.list.model.GQLPaymentQueryResponse
import com.tokopedia.payment.setting.list.model.PaymentQueryResponse
import com.tokopedia.payment.setting.util.GQL_GET_CREDIT_CARD_LIST
import javax.inject.Inject
import javax.inject.Named

class GetCreditCardListUseCase @Inject constructor(
        @Named(GQL_GET_CREDIT_CARD_LIST) val query: String,
        private val graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<GQLPaymentQueryResponse>(graphqlRepository) {

    fun getCreditCardList(
            onSuccess: (PaymentQueryResponse) -> Unit,
            onError: (Throwable) -> Unit,
    ) {
        try {
            this.setTypeClass(GQLPaymentQueryResponse::class.java)
            this.setGraphqlQuery(query)
            this.execute(
                    { result ->
                        onSuccess(result.paymentQueryResponse)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }
}