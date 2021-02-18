package com.tokopedia.payment.setting.detail.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.payment.setting.detail.model.DataResponseDeleteCC
import com.tokopedia.payment.setting.util.GQL_DELETE_CREDIT_CARD_LIST
import javax.inject.Inject

@GqlQuery("DeleteCreditCardList", GQL_DELETE_CREDIT_CARD_LIST)
class GQLDeleteCreditCardQueryUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<DataResponseDeleteCC>(graphqlRepository) {

    fun deleteCreditCard(
            onSuccess: (DataResponseDeleteCC?) -> Unit,
            onError: (Throwable) -> Unit, token: String,
    ) {
        try {
            this.setTypeClass(DataResponseDeleteCC::class.java)
            this.setRequestParams(getRequestParams(token))
            this.setGraphqlQuery(DeleteCreditCardList.GQL_QUERY)
            this.execute(
                    { result ->
                        onSuccess(result)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(token: String): Map<String, Any> {
        return mapOf(TOKEN to token)
    }

    companion object {
        const val TOKEN = "token_id"
    }


}