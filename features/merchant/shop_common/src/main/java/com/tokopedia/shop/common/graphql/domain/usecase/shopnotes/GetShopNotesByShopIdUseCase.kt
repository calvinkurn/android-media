package com.tokopedia.shop.common.graphql.domain.usecase.shopnotes

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.graphql.data.shopnote.gql.ShopNoteQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class GetShopNotesByShopIdUseCase (private val gqlQuery: String,
                                   private val gqlUseCase: MultiRequestGraphqlUseCase): UseCase<List<ShopNoteModel>>() {

    var params: RequestParams = RequestParams.EMPTY
    var isFromCacheFirst: Boolean = true

    override suspend fun executeOnBackground(): List<ShopNoteModel> {
        val gqlRequest = GraphqlRequest(gqlQuery, ShopNoteQuery::class.java, params.parameters)
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(gqlRequest)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopNoteQuery::class.java)
        if (error == null || error.isEmpty()){
            val shopNoteQueryResult = (gqlResponse.getData(ShopNoteQuery::class.java) as ShopNoteQuery).result ?:
                throw MessageErrorException()

            if (shopNoteQueryResult.graphQLDataError != null && !shopNoteQueryResult.graphQLDataError.message.isNullOrEmpty()){
                return shopNoteQueryResult.result?.toList() ?: listOf()
            } else {
                throw MessageErrorException(shopNoteQueryResult.graphQLDataError?.message)
            }
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val SHOP_ID = "shopId"
        private const val NOTE_ID = "id"

        @JvmStatic
        fun createParams(shopId: String, noteId: String? = null) = RequestParams.create().apply {
            putString(SHOP_ID, shopId)
            putString(NOTE_ID, noteId)
        }
    }
}