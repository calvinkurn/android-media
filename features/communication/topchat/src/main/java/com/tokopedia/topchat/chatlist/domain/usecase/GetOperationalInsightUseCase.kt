package com.tokopedia.topchat.chatlist.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatlist.domain.pojo.operational_insight.ShopChatMetricResponse
import javax.inject.Inject

open class GetOperationalInsightUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<String, ShopChatMetricResponse>(dispatcher.io) {

    override fun graphqlQuery(): String ="""
        query getShopChatTicker($$PARAM_SHOP_ID:String!) {
          GetShopChatTicker(shopID:$$PARAM_SHOP_ID) {
            Date
            Data {
              ChatReplied
              ChatSpeed
              DiscussionReplied
              DiscussionSpeed
            }
            ColorLight {
              ChatRepliedIndicatorLight
              ChatSpeedIndicatorLight
              DiscussionRepliedIndicatorLight
              DiscussionSpeedIndicatorLight
            }
            ColorDark {
              ChatRepliedIndicatorDark
              ChatSpeedIndicatorDark
              DiscussionRepliedIndicatorDark
              DiscussionSpeedIndicatorDark
            }
            Target {
              ChatRepliedTarget
              ChatSpeedTarget
            }
            ShopAppLink
            OperationalAppLink
            IsMaintain
            ShowTicker
          }
        }
    """.trimIndent()

    override suspend fun execute(params: String): ShopChatMetricResponse {
        return repository.request(graphqlQuery(), generateParams(params))
    }

    private fun generateParams(shopId: String): Map<String, Any?> {
        return mapOf(
            PARAM_SHOP_ID to shopId
        )
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
    }
}