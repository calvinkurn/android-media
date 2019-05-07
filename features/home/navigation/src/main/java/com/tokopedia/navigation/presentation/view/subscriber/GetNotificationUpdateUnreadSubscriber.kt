package com.tokopedia.navigation.presentation.view.subscriber

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.navigation.domain.pojo.NotificationUpdateUnread
import rx.Subscriber
import java.lang.reflect.Type

class GetNotificationUpdateUnreadSubscriber(
        private val onSuccessInitiateData: (NotificationUpdateUnread) -> Unit,
        private val onErrorInitiateData: ((Throwable) -> Unit)?
) : Subscriber<GraphqlResponse>() {

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


    fun handleError(graphqlResponse: GraphqlResponse,
                    type: Type, routingOnNext: (GraphqlResponse) -> Unit) {
        val graphqlErrorList = graphqlResponse.getError(type)
        if (graphqlErrorList == null || graphqlErrorList.isEmpty()) {
            routingOnNext(graphqlResponse)
        } else if (!graphqlErrorList.isEmpty()
                && graphqlErrorList[0] != null
                && graphqlErrorList[0].message != null) {
            onError(MessageErrorException(graphqlErrorList[0].message))
        }
    }
}
