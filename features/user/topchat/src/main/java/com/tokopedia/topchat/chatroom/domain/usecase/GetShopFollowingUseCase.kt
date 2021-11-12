package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import javax.inject.Inject

open class GetShopFollowingUseCase@Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<Long, ShopFollowingPojo>(dispatcher.io) {

    override suspend fun execute(params: Long): ShopFollowingPojo {
        val param = generateParam(params)
        return repository.request(graphqlQuery(), param)
    }

    private fun generateParam(shopId: Long): Map<String, Any> {
        val shopIds = listOf(shopId)
        val inputFields = listOf(DEFAULT_FAVORITE)
        return mapOf<String, Any>(
            PARAM_SHOP_IDS to shopIds,
            PARAM_INPUT_FIELDS to inputFields
        )
    }

    override fun graphqlQuery(): String = """
        query get_shop_following_status(
            $$PARAM_SHOP_IDS: [Int!]!, 
            $$PARAM_INPUT_FIELDS: [String!]!
        ) {
          shopInfoByID(
            input: {
                shopIDs: $$PARAM_SHOP_IDS,
                fields: $$PARAM_INPUT_FIELDS
            }
          ) {
            result {
              favoriteData {
                alreadyFavorited
              }
            }
          }
        }

    """.trimIndent()

    companion object {
        private const val PARAM_SHOP_IDS = "shopIDs"
        private const val PARAM_INPUT_FIELDS = "inputFields"
        private const val DEFAULT_FAVORITE: String = "favorite"
    }
}