package com.tokopedia.navigation.presentation.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.navigation.data.mapper.GetNotificationUpdateFilterMapper
import com.tokopedia.navigation.domain.pojo.NotificationUpdateFilter
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateContract
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel

class GetNotificationUpdateFilterSubscriber(
        val view: NotificationUpdateContract.View,
        val mapper: GetNotificationUpdateFilterMapper,
        private val onSuccessInitiateData: (ArrayList<NotificationUpdateFilterItemViewModel>) -> Unit
) : BaseNotificationSubscriber() {

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {

    }

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
