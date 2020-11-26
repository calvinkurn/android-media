package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.GetLiveFollowersResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by jegul on 12/06/20
 */
class GetLiveFollowersDataUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<GetLiveFollowersResponse>() {

    private val query = """
        query GetLiveFollowerData(${'$'}shopID: String!, ${'$'}shopIDs: [Int!]!, ${'$'}perPage: Int!, ${'$'}fields: [String!]!, ${'$'}source: String!) {
            shopFollowerList(shopID: ${'$'}shopID, perPage: ${'$'}perPage) {
                data {
                  photo
                }
            }
            shopInfoByID(input: {shopIDs: ${'$'}shopIDs, fields:${'$'}fields, source: ${'$'}source}) {
                result {
                    favoriteData {
                        totalFavorite
                    }
                }
            }
        }
    """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): GetLiveFollowersResponse {
        val gqlRequest = GraphqlRequest(query, GetLiveFollowersResponse::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<GetLiveFollowersResponse>(GetLiveFollowersResponse::class.java)
        if (response != null) {
            return response
        } else {
            throw MessageErrorException("Error when getting followers data")
        }
    }

    companion object {

        private const val PARAMS_SHOP_ID = "shopID"
        private const val PARAMS_SHOP_IDS = "shopIDs"
        private const val PARAMS_PER_PAGE = "perPage"
        private const val PARAMS_FIELDS = "fields"
        private const val PARAMS_SOURCE = "source"

        private const val FIELDS_FAVORITE = "favorite"
        private const val SOURCE_PLAY_BROADCAST = "play-broadcast"

        fun createParams(
                shopId: String,
                perPage: Int
        ): Map<String, Any> = mapOf(
                PARAMS_SHOP_ID to shopId,
                PARAMS_SHOP_IDS to listOf(shopId.toLong()),
                PARAMS_PER_PAGE to perPage,
                PARAMS_FIELDS to listOf(FIELDS_FAVORITE),
                PARAMS_SOURCE to SOURCE_PLAY_BROADCAST
        )
    }
}