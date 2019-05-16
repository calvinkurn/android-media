package com.tokopedia.navigation.presentation.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.navigation.domain.pojo.NotificationUpdateTotalUnread

class GetNotificationTotalUnreadSubscriber(
        private val onSuccessInitiateData: (NotificationUpdateTotalUnread) -> Unit={},
        private val onErrorInitiateData: ((Throwable) -> Unit)? ={}
) : BaseNotificationSubscriber() {

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        onErrorInitiateData?.invoke(e)
    }

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, NotificationUpdateTotalUnread::class.java,
                routingOnNext(graphqlResponse))
    }


    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<NotificationUpdateTotalUnread>(NotificationUpdateTotalUnread::class.java)
            onSuccessInitiateData(pojo)
        }
    }
}
