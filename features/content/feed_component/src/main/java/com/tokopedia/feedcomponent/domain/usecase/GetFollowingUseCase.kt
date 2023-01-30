package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedcomponent.domain.model.ShopFollowingEntity
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kotlin.extensions.view.toIntSafely
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 16/01/23
 */
open class GetFollowingUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<List<Pair<String, Int>>, ShopFollowingEntity>(dispatcher.io) {

    override suspend fun execute(params: List<Pair<String, Int>>): ShopFollowingEntity {
        val param = generateParam(params)
        return repository.request(graphqlQuery(), param)
    }

    private fun generateParam(shopIds: List<Pair<String, Int>>): Map<String, Any> {
        val inputFields = listOf(DEFAULT_FAVORITE)
        return mapOf<String, Any>(
            PARAM_SHOP_IDS to shopIds.filter { it.second == SHOP_TYPE }
                .map { it.first.toIntSafely() }.toList(),
            PARAM_USER_IDS to shopIds.filter { it.second != SHOP_TYPE }.map { it.first }.toList(),
            PARAM_INPUT_FIELDS to inputFields
        )
    }

    override fun graphqlQuery(): String = """
        query get_following_status(
            $$PARAM_SHOP_IDS: [Int!]!, 
            $$PARAM_USER_IDS: [String!]!,
            $$PARAM_INPUT_FIELDS: [String!]!
        ) {
          shopInfoByID(
            input: {
                shopIDs: $$PARAM_SHOP_IDS,
                fields: $$PARAM_INPUT_FIELDS
            }
          ) {
            result {
              shopCore {
                shopID
              }
              favoriteData {
                alreadyFavorited
              }
            }
          }
          feedXProfileIsFollowing(followingUserIDs: $$PARAM_USER_IDS){
            isUserFollowing {
              userID
              status
            }
          }
        }

    """.trimIndent()

    companion object {
        private const val PARAM_SHOP_IDS = "shopIDs"
        private const val PARAM_USER_IDS = "followingUserIDs"
        private const val PARAM_INPUT_FIELDS = "inputFields"
        private const val DEFAULT_FAVORITE: String = "favorite"

        const val SHOP_TYPE = 2
    }
}
