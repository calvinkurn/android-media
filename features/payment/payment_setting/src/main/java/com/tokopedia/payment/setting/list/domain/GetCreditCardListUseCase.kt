package com.tokopedia.payment.setting.list.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.payment.setting.list.model.GQLPaymentQueryResponse
import com.tokopedia.payment.setting.list.model.PaymentQueryResponse
import com.tokopedia.payment.setting.util.GQL_GET_CREDIT_CARD_LIST
import javax.inject.Inject

@GqlQuery("GetCreditCardList", GQL_GET_CREDIT_CARD_LIST)
class GetCreditCardListUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<GQLPaymentQueryResponse>(graphqlRepository) {

    fun getCreditCardList(
            onSuccess: (PaymentQueryResponse) -> Unit,
            onError: (Throwable) -> Unit,
    ) {
        try {
            this.setTypeClass(GQLPaymentQueryResponse::class.java)
            this.setGraphqlQuery(GetCreditCardList.GQL_QUERY)
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