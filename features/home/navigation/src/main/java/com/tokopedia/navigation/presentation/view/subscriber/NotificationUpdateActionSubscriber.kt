package com.tokopedia.navigation.presentation.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.navigation.domain.pojo.NotificationUpdateActionResponse

class NotificationUpdateActionSubscriber(
        private val onSuccessDoAction: (() -> Unit)? = {},
        private val onErrorDoAction: ((String) -> Unit)? ={}
) : BaseNotificationSubscriber() {

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

}
