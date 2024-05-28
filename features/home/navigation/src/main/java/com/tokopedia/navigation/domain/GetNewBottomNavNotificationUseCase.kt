package com.tokopedia.navigation.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.navigation.data.entity.NotificationEntity
import com.tokopedia.navigation.data.mapper.NotificationRequestMapper
import com.tokopedia.navigation.domain.model.Notification
import javax.inject.Inject

class GetNewBottomNavNotificationUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<String, Notification>(dispatchers.io) {

    override fun graphqlQuery(): String {
        return """
            query Notification(${'$'}cursor: String, ${'$'}input: NotificationRequest) {
              status
              userShopInfo {
                info {
                  shop_id
                }
              }
            
              notifications(input: ${'$'}input){
                total_cart
                inbox {
                  talk
                  ticket
                  review
                }
                chat {
                  unreads
                  unreadsSeller
                  unreadsUser
                }
                inbox_counter{
                  all{
                    total_int
                    notifcenter_int
                  }
                }
                resolutionAs {
                  buyer
                  seller
                }
                sellerInfo {
                  notification
                }
                buyerOrderStatus {
                  paymentStatus
                  confirmed
                  processed
                  shipped
                  arriveAtDestination
                }
                sellerOrderStatus {
                  newOrder
                  readyToShip
                  shipped
                  arriveAtDestination
                }
              }
            
              notifcenter_unread {
                notif_unread
              }
            }
        """
    }

    override suspend fun execute(params: String): Notification {
        val response: NotificationEntity = graphqlRepository.request(
            graphqlQuery(),
            mapOf(
                PARAM_INPUT to mapOf(
                    PARAM_SHOP_ID to params
                )
            )
        )
        return NotificationRequestMapper.notificationMapper(
            response.notifications,
            response.notifcenterUnread
        )
    }
}

private const val PARAM_INPUT = "input"
private const val PARAM_SHOP_ID = "shop_id"
