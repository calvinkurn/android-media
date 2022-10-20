package com.tokopedia.watch.notification.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.watch.notification.model.NotificationCenterDetailResponse
import com.tokopedia.watch.notification.model.NotificationListModel
import rx.functions.Func1

class NotificationListMapper : Func1<GraphqlResponse?, NotificationListModel> {
    override fun call(graphqlResponse: GraphqlResponse?): NotificationListModel {
        graphqlResponse ?: return NotificationListModel()
        return NotificationListModel(
            graphqlResponse.getData<NotificationCenterDetailResponse>(
                NotificationCenterDetailResponse::class.java
            ).notificationCenterDetail.newList
        )
    }
}