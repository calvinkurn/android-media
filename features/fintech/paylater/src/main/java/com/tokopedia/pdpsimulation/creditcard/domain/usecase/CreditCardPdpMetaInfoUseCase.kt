package com.tokopedia.pdpsimulation.creditcard.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdpsimulation.common.constants.GQL_CREDIT_CARD_PDP_META_INFO
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardPDPInfoMetadataResponse
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardPdpMetaData
import javax.inject.Inject

@GqlQuery("CreditCardPdpMetaInfoQuery", GQL_CREDIT_CARD_PDP_META_INFO)
class CreditCardPdpMetaInfoUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<CreditCardPDPInfoMetadataResponse>(graphqlRepository) {

    fun getPdpMetaData(
            onSuccess: (CreditCardPdpMetaData?) -> Unit,
            onError: (Throwable) -> Unit,
    ) {
        try {
            this.setTypeClass(CreditCardPDPInfoMetadataResponse::class.java)
            this.setGraphqlQuery(CreditCardPdpMetaInfoQuery.GQL_QUERY)
            this.execute(
                    { result ->
                        onSuccess(result.creditCardPDPInfoMetadataResponse)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }
}