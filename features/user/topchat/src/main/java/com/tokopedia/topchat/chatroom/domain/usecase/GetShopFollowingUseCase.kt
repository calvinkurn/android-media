package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author by nisie on 16/01/19.
 */

open class GetShopFollowingUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ShopFollowingPojo>,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    private val paramShopIDs = "shopIDs"
    private val paramInputFields = "inputFields"

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun getStatus(
            shopId: Long,
            onError: (Throwable) -> Unit,
            onSuccessGetShopFollowingStatus: (Boolean) -> Unit
    ) {
        launchCatchError(
                dispatchers.io,
                {
                    val params = generateParam(shopId)
                    val response = gqlUseCase.apply {
                        setTypeClass(ShopFollowingPojo::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    withContext(dispatchers.main) {
                        onSuccessGetShopFollowingStatus(response.isFollow)
                    }
                },
                {
                    withContext(dispatchers.main) {
                        onError(it)
                    }
                }
        )
    }

    fun safeCancel() {
        if (coroutineContext.isActive) {
            cancel()
        }
    }

    private fun generateParam(shopId: Long): Map<String, Any> {
        val shopIds = listOf(shopId)
        val inputFields = listOf(DEFAULT_FAVORITE)
        return mapOf<String, Any>(
                PARAM_SHOP_IDS to shopIds,
                PARAM_INPUT_FIELDS to inputFields
        )
    }

    private val query = """
        query get_shop_following_status(
            $$paramShopIDs: [Int!]!, 
            $$paramInputFields: [String!]!
        ) {
          shopInfoByID(
            input: {
                shopIDs: $$paramShopIDs,
                fields: $$paramInputFields
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
        private val PARAM_SHOP_IDS: String = "shopIDs"
        private val PARAM_INPUT_FIELDS: String = "inputFields"
        private val DEFAULT_FAVORITE: String = "favorite"
    }
}