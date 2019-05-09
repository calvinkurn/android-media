package com.tokopedia.navigation.presentation.view.subscriber

import android.util.Log
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.navigation.domain.pojo.NotificationUpdateActionResponse
import rx.Subscriber
import java.lang.reflect.Type

class NotificationUpdateActionSubscriber(
        private val onSuccessDoAction: (() -> Unit)? = {},
        private val onErrorDoAction: ((String) -> Unit)? ={}
) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {

    }

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, NotificationUpdateActionResponse::class.java,
                routingOnNext(graphqlResponse))
    }


    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<NotificationUpdateActionResponse>(NotificationUpdateActionResponse::class.java)

            if(pojo.messageError.isNullOrBlank()){
                onSuccessDoAction?.invoke()
            } else {
                onErrorDoAction?.invoke(pojo.messageError)
            }
        }
    }


    fun handleError(graphqlResponse: GraphqlResponse,
                    type: Type, routingOnNext: (GraphqlResponse) -> Unit,
                    onError: (Throwable) -> Unit = { Log.d("ERR", it.toString()) }) {
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
