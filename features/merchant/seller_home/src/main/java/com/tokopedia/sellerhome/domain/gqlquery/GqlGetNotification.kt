package com.tokopedia.sellerhome.domain.gqlquery

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by @ilhamsuaib on 30/03/22.
 */

object GqlGetNotification : GqlQueryInterface {

    private const val TOP_OPERATION_NAME = "getNotifications"
    private const val OPERATION_NAME = "notifications"
    private val QUERY = """
        query $TOP_OPERATION_NAME(${'$'}typeId: Int!) {
          $OPERATION_NAME {
            chat {
              unreadsSeller
            }
            sellerOrderStatus {
              newOrder
              readyToShip
            }
          }
          notifcenter_unread(type_id: ${'$'}typeId) {
            notif_unread_int
          }
          status
        }
    """.trimIndent()

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String =QUERY

    override fun getTopOperationName(): String = TOP_OPERATION_NAME
}