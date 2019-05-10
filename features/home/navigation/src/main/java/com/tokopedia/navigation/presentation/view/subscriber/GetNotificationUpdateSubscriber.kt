package com.tokopedia.navigation.presentation.view.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.navigation.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.navigation.domain.pojo.NotificationCenterDetail
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateContract
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateViewModel

class GetNotificationUpdateSubscriber(
        val view: NotificationUpdateContract.View,
        val mapper: GetNotificationUpdateMapper,
        private val onSuccessInitiateData: (NotificationUpdateViewModel) -> Unit,
        private val onErrorInitiateData: (Throwable) -> Unit
) : BaseNotificationSubscriber() {

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        onErrorInitiateData(e)
    }

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, NotificationCenterDetail::class.java,
                routingOnNext(graphqlResponse), onErrorInitiateData)
    }


    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<NotificationCenterDetail>(NotificationCenterDetail::class.java)
            val viewModel = mapper.map(pojo)
            onSuccessInitiateData(viewModel)
        }
    }
}
