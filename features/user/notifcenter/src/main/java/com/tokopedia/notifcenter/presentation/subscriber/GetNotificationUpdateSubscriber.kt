package com.tokopedia.notifcenter.presentation.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.notifcenter.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.notifcenter.data.entity.NotificationCenterDetail
import com.tokopedia.notifcenter.data.model.NotificationViewData

class GetNotificationUpdateSubscriber(
        val mapper: GetNotificationUpdateMapper,
        private val onSuccessInitiateData: (NotificationViewData) -> Unit,
        private val onErrorInitiateData: (Throwable) -> Unit
) : BaseNotificationSubscriber() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        onErrorInitiateData(e)
    }

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse,
                NotificationCenterDetail::class.java,
                routingOnNext(graphqlResponse),
                onErrorInitiateData)
    }


    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<NotificationCenterDetail>(NotificationCenterDetail::class.java)
            val viewModel = mapper.map(pojo)
            onSuccessInitiateData(viewModel)
        }
    }
}
