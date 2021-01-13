package com.tokopedia.paylater.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.paylater.domain.model.PayLaterActivityResponse
import com.tokopedia.paylater.domain.query.GQL_CREDIT_CARD_PDP_META_INFO
import javax.inject.Inject

@GqlQuery("CreditCardPdpMetaInfoQuery", GQL_CREDIT_CARD_PDP_META_INFO)
class CreditCardPdpMetaInfoUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<PayLaterActivityResponse>(graphqlRepository) {
/*
    fun getPayLaterData(
            onSuccess: (PayLaterProductData?) -> Unit,
            onError: (Throwable) -> Unit,
    ) {
        try {
            this.setTypeClass(PayLaterActivityResponse::class.java)
            this.setGraphqlQuery(CreditCardPdpMetaInfoQuery.GQL_QUERY)
            this.execute(
                    { result ->
                        onSuccess(result.productData)

                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }*/
}