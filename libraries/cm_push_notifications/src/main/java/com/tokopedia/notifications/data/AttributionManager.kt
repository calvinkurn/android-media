package com.tokopedia.notifications.data

import android.util.Log
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifications.data.model.AttributionNotifier
import com.tokopedia.notifications.domain.AttributionUseCase
import com.tokopedia.notifications.model.BaseNotificationModel

class AttributionManager(
        private val useCase: AttributionUseCase,
        private val notification: BaseNotificationModel?
) {

    fun postAttribution() {
        val params = useCase.params(
                transactionId = notification?.title,
                receipientId = notification?.message
        )
        useCase.execute(params, ::onSuccess, ::onError)
    }

    private fun onSuccess(data: AttributionNotifier) {
        Log.d("TAG", data.webhookAttributionNotifier.isSuccess.toString())
    }

    private fun onError(throwable: Throwable) {
        Log.d("TAG", throwable.localizedMessage)
    }

    companion object {
        fun post(notification: BaseNotificationModel?) {
            val graphqlUseCase = GraphqlUseCase<AttributionNotifier>(
                    GraphqlInteractor.getInstance().graphqlRepository
            )
            val useCase = AttributionUseCase(graphqlUseCase)
            val manager = AttributionManager(useCase, notification)
            manager.postAttribution()
        }
    }

}