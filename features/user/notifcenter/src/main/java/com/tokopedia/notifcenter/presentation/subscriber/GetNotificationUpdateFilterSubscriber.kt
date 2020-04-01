package com.tokopedia.notifcenter.presentation.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.notifcenter.data.mapper.GetNotificationUpdateFilterMapper
import com.tokopedia.notifcenter.data.entity.NotificationUpdateFilter
import com.tokopedia.notifcenter.data.viewbean.NotificationUpdateFilterViewBean

class GetNotificationUpdateFilterSubscriber(
        val mapper: GetNotificationUpdateFilterMapper,
        private val onSuccessInitiateData: (ArrayList<NotificationUpdateFilterViewBean>) -> Unit
) : BaseNotificationSubscriber() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {}

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, NotificationUpdateFilter::class.java,
                routingOnNext(graphqlResponse))
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<NotificationUpdateFilter>(NotificationUpdateFilter::class.java)
            val viewModel = mapper.map(pojo)
            onSuccessInitiateData(viewModel)
        }
    }

}
