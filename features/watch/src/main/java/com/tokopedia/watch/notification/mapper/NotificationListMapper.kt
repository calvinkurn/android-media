package com.tokopedia.watch.notification.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.watch.notification.model.NotificationListModel
import com.tokopedia.watch.orderlist.model.OrderListModel
import rx.functions.Func1

class NotificationListMapper: Func1<GraphqlResponse?, NotificationListModel> {
    override fun call(graphqlResponse: GraphqlResponse?): NotificationListModel {
        graphqlResponse ?: return NotificationListModel()

        return NotificationListModel(

        )
    }
}