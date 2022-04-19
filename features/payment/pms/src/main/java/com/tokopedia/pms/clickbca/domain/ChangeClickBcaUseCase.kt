package com.tokopedia.pms.clickbca.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pms.clickbca.data.model.DataEditKlikBca
import com.tokopedia.pms.clickbca.data.model.EditKlikbca
import javax.inject.Inject

@GqlQuery("EditClickBcaQuery", ChangeClickBcaUseCase.GQL_EDIT_KLIC_BCA)
class ChangeClickBcaUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<DataEditKlikBca>(graphqlRepository) {

    fun changeClickBcaId(
        onSuccess: (EditKlikbca) -> Unit,
        onError: (Throwable) -> Unit,
        transactionId: String,
        merchantCode: String,
        newClickBcaUserId: String
    ) {
        this.setTypeClass(DataEditKlikBca::class.java)
        this.setRequestParams(getRequestParams(transactionId, merchantCode, newClickBcaUserId))
        this.setGraphqlQuery(EditClickBcaQuery.GQL_QUERY)
        this.execute(
            { result ->
                onSuccess(result.editKlikbca)
            }, { error ->
                onError(error)
            }
        )
    }

    private fun getRequestParams(
        transactionId: String,
        merchantCode: String,
        newClickBcaUserId: String
    ) = mapOf(
        TRANSACTION_ID to transactionId,
        MERCHANT_CODE to merchantCode,
        NEW_KLIKBCA_USER_ID to newClickBcaUserId
    )

    companion object {
        const val TRANSACTION_ID = "transactionID"
        const val MERCHANT_CODE = "merchantCode"
        const val NEW_KLIKBCA_USER_ID = "newKlikbcaUserID"
        const val GQL_EDIT_KLIC_BCA = """
            mutation editKlikbca(${'$'}transactionID: String!,
                ${'$'}merchantCode:String!,${'$'}newKlikbcaUserID: String!){
                editKlikbca(transactionID: ${'$'}transactionID, merchantCode:${'$'}merchantCode,
                newKlikbcaUserID: ${'$'}newKlikbcaUserID){
                    success
                    message
               }  
            }
        """
    }
}