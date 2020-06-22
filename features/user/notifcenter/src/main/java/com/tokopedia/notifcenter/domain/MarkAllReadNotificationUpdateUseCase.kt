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
import rx.Subscriber
import javax.inject.Inject

/**
 * @author : Steven 11/04/19
 */
class MarkAllReadNotificationUpdateUseCase @Inject constructor(
        @NotificationContext val context: Context,
        private val graphqlUseCase: GraphqlUseCase
) {
    fun execute(requestParams: Map<String, Any>,
                subscriber: Subscriber<GraphqlResponse>) {
        val graphqlRequest = GraphqlRequest(QUERY,
                NotificationUpdateActionResponse::class.java, requestParams)

        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setSessionIncluded(true).build()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.setCacheStrategy(cacheStrategy)
        graphqlUseCase.execute(subscriber)
    }

    companion object {
        private const val QUERY = "mutation markAllReadStatusNotificationUpdate(\$typeOfNotif: Int, \$typeId: Int){\n" +
                "  notifcenter_markAllReadStatus(type_of_notif: \$typeOfNotif, type_id: \$typeId){\n" +
                "    data {\n" +
                "        is_success\n" +
                "    }\n" +
                "    message_error\n" +
                "  }\n" +
                "}"

        private const val PARAM_NOTIF_TYPE = "typeOfNotif"
        private const val PARAM_NOTIF_TYPE_ID = "typeId"
        private const val TYPE_NOTIF_UPDATE = 1 //update
        private const val TYPE_NOTIF_ID_DEFAULT = 0 //default
        private const val TYPE_NOTIF_ID_SELLER = 2 //seller

        fun params(typeOfNotif: Int = TYPE_NOTIF_UPDATE): HashMap<String, Any> {
            val params = hashMapOf<String, Any>()
            params[PARAM_NOTIF_TYPE] = typeOfNotif
            params[PARAM_NOTIF_TYPE_ID] = if (GlobalConfig.isSellerApp()) TYPE_NOTIF_ID_SELLER else TYPE_NOTIF_ID_DEFAULT
            return params
        }
    }

}