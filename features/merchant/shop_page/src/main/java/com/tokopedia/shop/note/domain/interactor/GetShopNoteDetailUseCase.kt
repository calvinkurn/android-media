package com.tokopedia.shop.note.domain.interactor;

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.graphql.data.shopnote.gql.ShopNoteQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

/**
 * Created by normansyahputa on 2/8/18.
 */

class GetShopNoteDetailUseCase @Inject constructor(
        val graphqlUseCase: GraphqlUseCase
) : UseCase<ShopNoteModel>() {

    companion object {
        private const val SHOP_ID = "shopID"
        private const val NOTE_ID = "id"
        private const val QUERY = """
            query shopNotesByShopID(${'$'}shopID: String!, ${'$'}id: String!){
              shopNotesByShopID(shopID: ${'$'}shopID, id: ${'$'}id){
                  result {
                      id
                      title
                      content
                      isTerms
                      updateTime
                      updateTimeUTC
                      url
                      position
                  }
                  error {
                    message
                  }
              }
            }
        """
        @JvmStatic
        fun createRequestParams(shopId: String, noteId: String): RequestParams {
            return RequestParams.create().apply {
                putString(SHOP_ID, shopId)
                putString(NOTE_ID, noteId)
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<ShopNoteModel> {
        val graphqlRequest = GraphqlRequest(QUERY, ShopNoteQuery::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {
            val data = ( it.getData(ShopNoteQuery::class.java) as ShopNoteQuery).result?.result
            val error: List<GraphqlError> = it.getError(GraphqlError::class.java) ?: listOf()
            if (data == null) {
                throw RuntimeException()
            } else if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error.first().message)
            }
            data.first()
        }
    }
}
