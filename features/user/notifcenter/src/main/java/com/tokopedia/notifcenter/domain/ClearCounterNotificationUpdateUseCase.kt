package com.tokopedia.notifcenter.domain

import android.content.Context
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.notifcenter.data.entity.NotificationUpdateActionResponse
import com.tokopedia.notifcenter.di.scope.NotificationContext
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * @author : Steven 11/04/19
 */
class ClearCounterNotificationUpdateUseCase @Inject constructor(
        @NotificationContext val context: Context,
        private val graphqlUseCase: GraphqlUseCase
) {
    var params: RequestParams = RequestParams.EMPTY

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        val graphqlRequest = GraphqlRequest(QUERY,
                NotificationUpdateActionResponse::class.java, params.parameters)

        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setSessionIncluded(true).build()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.setCacheStrategy(cacheStrategy)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    companion object {
        private const val QUERY = "mutation clearNotifCounter(\$typeId: Int){\n" +
                "  notifcenter_clearNotifCounter(type_id: \$typeId) {\n" +
                "    data {\n" +
                "      is_success\n" +
                "    }\n" +
                "    message_error\n" +
                "  }\n" +
                "}"

        private const val TYPE_ID = "typeId"
        private const val TYPE_ID_DEFAULT = 0
        private const val TYPE_ID_SELLER = 2

        fun getRequestParams(): RequestParams = RequestParams.create().apply {
            if (GlobalConfig.isSellerApp()) {
                putInt(TYPE_ID, TYPE_ID_SELLER)
            } else {
                putInt(TYPE_ID, TYPE_ID_DEFAULT)
            }
        }
    }
}