package com.tokopedia.navigation.presentation.view.subscriber

import android.util.Log
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.navigation.data.mapper.GetNotificationUpdateFilterMapper
import com.tokopedia.navigation.domain.pojo.NotificationUpdateFilter
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateContract
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import rx.Subscriber
import java.lang.reflect.Type

class GetNotificationUpdateFilterSubscriber(
        val view: NotificationUpdateContract.View,
        val mapper: GetNotificationUpdateFilterMapper,
        private val onSuccessInitiateData: (ArrayList<NotificationUpdateFilterItemViewModel>) -> Unit
) : Subscriber<GraphqlResponse>() {

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
