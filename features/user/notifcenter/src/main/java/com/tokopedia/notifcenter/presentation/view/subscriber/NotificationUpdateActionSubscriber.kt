package com.tokopedia.notifcenter.presentation.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.notifcenter.domain.pojo.NotificationUpdateActionResponse

class NotificationUpdateActionSubscriber(
        private val onSuccessDoAction: (() -> Unit)? = {},
        private val onErrorDoAction: ((String) -> Unit)? ={}
) : BaseNotificationSubscriber() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {}

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, NotificationUpdateActionResponse::class.java, routingOnNext(graphqlResponse))
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val dataBean = graphqlResponse.getData<NotificationUpdateActionResponse>(NotificationUpdateActionResponse::class.java)
            if(dataBean.messageError.isNullOrBlank()){
                onSuccessDoAction?.invoke()
            } else {
                onErrorDoAction?.invoke(dataBean.messageError)
            }
        }
    }

}
