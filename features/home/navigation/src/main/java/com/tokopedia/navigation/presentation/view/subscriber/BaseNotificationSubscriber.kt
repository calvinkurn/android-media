package com.tokopedia.navigation.presentation.view.subscriber

import android.util.Log
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.navigation.domain.pojo.NotificationUpdateTotalUnread
import rx.Subscriber
import java.lang.reflect.Type

abstract class BaseNotificationSubscriber() : Subscriber<GraphqlResponse>() {

    fun handleError(graphqlResponse: GraphqlResponse,
                    type: Type, routingOnNext: (GraphqlResponse) -> Unit,
                    onError: ((Throwable) -> Unit)? = { Log.d("ERR", it.toString()) }) {
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
