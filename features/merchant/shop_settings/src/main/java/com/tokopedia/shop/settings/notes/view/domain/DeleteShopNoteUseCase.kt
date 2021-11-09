package com.tokopedia.shop.settings.notes.view.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopnote.gql.DeleteShopNoteMutation
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class DeleteShopNoteUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<DeleteShopNoteMutation>(graphqlRepository) {

    companion object {
        const val ID = "id"
        const val query = """
            mutation deleteShopNote(${'$'}id:String!){
              deleteShopNote(input: {
               id:${'$'}id
              }){
                  success
                  message
                }
            }
        """
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(DeleteShopNoteMutation::class.java)
    }

    suspend fun execute(id: String): String {
        setRequestParams(RequestParams.create().apply {
            putString(ID, id)
        }.parameters)
        executeOnBackground().apply {
            if (graphQLSuccessMessage?.isSuccess == true) {
                return graphQLSuccessMessage?.message.orEmpty()
            } else {
                throw MessageErrorException(executeOnBackground().graphQLSuccessMessage?.message)
            }
        }
    }
}