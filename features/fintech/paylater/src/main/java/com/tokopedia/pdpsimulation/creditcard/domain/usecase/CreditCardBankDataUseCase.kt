package com.tokopedia.pdpsimulation.creditcard.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardBankCardResponse
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardBankData
import com.tokopedia.pdpsimulation.common.constants.GQL_CREDIT_CARD_BANK_LIST

import javax.inject.Inject

@GqlQuery("CreditCardBankListQuery", GQL_CREDIT_CARD_BANK_LIST)
class CreditCardBankDataUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<CreditCardBankCardResponse>(graphqlRepository) {

    fun getBankCardList(
            onSuccess: (CreditCardBankData?) -> Unit,
            onError: (Throwable) -> Unit,
    ) {
        try {
            this.setTypeClass(CreditCardBankCardResponse::class.java)
            this.setGraphqlQuery(CreditCardBankListQuery.GQL_QUERY)
            this.execute(
                    { result ->
                        onSuccess(result.creditCardBankData)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }
}