package com.tokopedia.shop.settings.notes.view.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopnote.gql.UpdateShopNoteMutation
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class UpdateShopNoteUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<UpdateShopNoteMutation>(graphqlRepository) {

    companion object {
        const val ID = "id"
        const val TITLE = "title"
        const val CONTENT = "content"
        const val query = """
            mutation UpdateShopNote(${'$'}id:String!, ${'$'}title:String!, ${'$'}content:String!){
              updateShopNote(input: {
                  id: ${'$'}id,
                  title: ${'$'}title,
                  content: ${'$'}content
                }) {
                  success
                  message
                }
            }
        """
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(UpdateShopNoteMutation::class.java)
    }

    suspend fun execute(id: String, title: String, content: String): String {
        setRequestParams(RequestParams.create().apply {
            putString(ID, id)
            putString(TITLE, title)
            putString(CONTENT, content)
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