package com.tokopedia.shop.common.graphql.data.shopnote.gql

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopNoteUseCase @Inject constructor(
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<List<ShopNoteModel>>() {

    var params = mapOf<String, Any>()
    var isFromCacheFirst: Boolean = true

    override suspend fun executeOnBackground(): List<ShopNoteModel> {
        val gqlRequest = GraphqlRequest(QUERY, ShopNoteQuery::class.java, params)
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(gqlRequest)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopNoteQuery::class.java)
        if (error == null || error.isEmpty()){
            val shopNoteQueryResult = (gqlResponse.getData(ShopNoteQuery::class.java) as ShopNoteQuery).result ?:
                throw MessageErrorException()

            if (shopNoteQueryResult.graphQLDataError != null && !shopNoteQueryResult.graphQLDataError?.message.isNullOrEmpty()){
                throw MessageErrorException(shopNoteQueryResult.graphQLDataError?.message)
            } else {
                return shopNoteQueryResult.result?.toList() ?: listOf()
            }
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val SHOP_ID = "shopId"
        private const val NOTE_ID = "id"
        private const val IS_TERM = "isTerm"
        private const val SLUG = "slug"
        private const val QUERY = """
            query getShopNotesByShopId(${'$'}shopId: String!, ${'$'}id: String, ${'$'}isTerm: Boolean, ${'$'}slug: String){
                shopNotesByShopID(shopID: ${'$'}shopId, id: ${'$'}id, isTerms: ${'$'}isTerm, slug: ${'$'}slug){
                    result{
                      id
                      title
                      content
                      isTerms
                      updateTime
                      updateTimeUTC
                      position
                      url
                    }
                    error {
                      message
                    }
                }
            }
        """

        @JvmStatic
        fun createParams(
                shopId: String,
                noteId: String = "",
                isTerm: Boolean = false,
                slug: String = ""
        ) = mapOf(
                SHOP_ID to shopId,
                NOTE_ID to noteId,
                IS_TERM to isTerm,
                SLUG to slug
        )
    }
}