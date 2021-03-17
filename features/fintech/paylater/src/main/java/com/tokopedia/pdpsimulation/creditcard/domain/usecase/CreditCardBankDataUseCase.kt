package com.tokopedia.pdpsimulation.creditcard.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdpsimulation.common.constants.GQL_CREDIT_CARD_BANK_LIST
import com.tokopedia.pdpsimulation.creditcard.domain.model.BankCardListItem
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardBankCardResponse
import javax.inject.Inject

@GqlQuery("CreditCardBankListQuery", GQL_CREDIT_CARD_BANK_LIST)
class CreditCardBankDataUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<CreditCardBankCardResponse>(graphqlRepository) {
    private val BANK_CARD_DATA_FAILURE = "NULL DATA"

    fun getBankCardList(
            onSuccess: (ArrayList<BankCardListItem>) -> Unit,
            onError: (Throwable) -> Unit,
    ) {
        try {
            this.setTypeClass(CreditCardBankCardResponse::class.java)
            this.setGraphqlQuery(CreditCardBankListQuery.GQL_QUERY)
            this.execute(
                    { result ->
                        if (result.creditCardBankData.bankCardList.isNullOrEmpty())
                            onError(Throwable(NullPointerException(BANK_CARD_DATA_FAILURE)))
                        else
                            onSuccess(result.creditCardBankData.bankCardList)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }
}