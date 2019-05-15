package com.tokopedia.navigation.presentation.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.navigation.domain.pojo.NotificationUpdateUnread

class GetNotificationUpdateUnreadSubscriber(
        private val onSuccessInitiateData: (NotificationUpdateUnread) -> Unit ={},
        private val onErrorInitiateData: ((Throwable) -> Unit)? ={}
) : BaseNotificationSubscriber() {

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        onErrorInitiateData?.invoke(e)
    }

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, NotificationUpdateUnread::class.java,
                routingOnNext(graphqlResponse))
    }


    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<NotificationUpdateUnread>(NotificationUpdateUnread::class.java)
            onSuccessInitiateData(pojo)
        }
    }
}
