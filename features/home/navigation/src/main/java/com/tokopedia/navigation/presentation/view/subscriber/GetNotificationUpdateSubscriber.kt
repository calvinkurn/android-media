package com.tokopedia.navigation.presentation.view.subscriber

import android.util.Log
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.navigation.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.navigation.domain.pojo.NotificationCenterDetail
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateContract
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateViewModel
import rx.Subscriber
import java.lang.reflect.Type

class GetNotificationUpdateSubscriber(
        val view: NotificationUpdateContract.View,
        val mapper: GetNotificationUpdateMapper,
        private val onSuccessInitiateData: (NotificationUpdateViewModel) -> Unit,
        private val onErrorInitiateData: (Throwable) -> Unit
) : Subscriber<GraphqlResponse>() {

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
