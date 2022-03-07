package com.tokopedia.shop.settings.notes.view.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.graphql.data.shopnote.gql.ShopNoteQuery
import com.tokopedia.usecase.RequestParams
import java.util.ArrayList
import javax.inject.Inject

class GetShopNoteUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<ShopNoteQuery>(graphqlRepository) {

    companion object {
        const val query = """
            query ShopNotes {
              shopNotes {
                result {
                  id
                  title
                  content
                  isTerms
                  updateTime
                }
                error {
                  message
                }
              }
            }
        """
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(ShopNoteQuery::class.java)
    }

    suspend fun execute(): List<ShopNoteModel> {
        setRequestParams(RequestParams.create().parameters)
        return executeOnBackground().result?.result.orEmpty()
    }
}