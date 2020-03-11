package com.tokopedia.notifications.data

import android.content.Context
import android.util.Log
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifications.R
import com.tokopedia.notifications.data.model.AttributionNotifier
import com.tokopedia.notifications.domain.AttributionUseCase
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper.loadRawString as raw

class AttributionManager(
        private val useCase: AttributionUseCase,
        private val notification: BaseNotificationModel?
) {

    fun postAttribution() {
        val params = useCase.params(
                transactionId = notification?.transactionId,
                userTransId = notification?.userTransactionId,
                shopId = notification?.shopId,
                blastId = notification?.blastId
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
        fun post(context: Context, notification: BaseNotificationModel?) {
            //post notification attribution only has shopId
            if (notification?.shopId == null) return

            val query = raw(context.resources, R.raw.query_notification_attribution)
            val useCase = GraphqlUseCase<AttributionNotifier>(
                    GraphqlInteractor.getInstance().graphqlRepository
            )
            val attributionUseCase = AttributionUseCase(useCase, query)
            val manager = AttributionManager(attributionUseCase, notification)
            manager.postAttribution()
        }
    }

}