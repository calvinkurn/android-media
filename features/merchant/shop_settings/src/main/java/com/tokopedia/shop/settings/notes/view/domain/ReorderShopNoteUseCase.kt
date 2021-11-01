package com.tokopedia.shop.settings.notes.view.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopnote.gql.ReorderShopNoteMutation
import com.tokopedia.usecase.RequestParams
import java.util.ArrayList
import javax.inject.Inject

class ReorderShopNoteUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<ReorderShopNoteMutation>(graphqlRepository) {

    companion object {
        const val IDS = "ids"
        const val query = """
            mutation reorderShopNote(${'$'}ids:[String!]!){
             reorderShopNote(input: {
              ids: ${'$'}ids
              }) {
                success
                message
              }
            }
        """
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(ReorderShopNoteMutation::class.java)
    }

    suspend fun execute(noteIdList: ArrayList<String>): String {
        setRequestParams(RequestParams.create().apply {
            putObject(IDS, noteIdList)
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