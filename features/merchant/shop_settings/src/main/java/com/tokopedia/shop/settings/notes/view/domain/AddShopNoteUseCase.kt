package com.tokopedia.shop.settings.notes.view.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopnote.gql.AddShopNoteMutation
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class AddShopNoteUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<AddShopNoteMutation>(graphqlRepository) {

    companion object {
        const val TITLE = "title"
        const val CONTENT = "content"
        const val IS_TERMS = "isTerms"
        const val query = """
            mutation addShopNote(${'$'}title:String!, ${'$'}content:String!, ${'$'}isTerms:Boolean!){
              addShopNote(input: {
                  title: ${'$'}title,
                  content: ${'$'}content,
                  isTerms: ${'$'}isTerms
                }) {
                  success
                  message
                }
            }
        """
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(AddShopNoteMutation::class.java)
    }

    suspend fun execute(title: String, content: String, isTerms: Boolean): String {
        setRequestParams(RequestParams.create().apply {
            putString(TITLE, title)
            putString(CONTENT, content)
            putBoolean(IS_TERMS, isTerms)
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