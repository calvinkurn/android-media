package com.tokopedia.notifcenter.domain

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.NotificationUpdateTotalUnread
import com.tokopedia.notifcenter.di.scope.NotificationContext
import rx.Subscriber
import javax.inject.Inject

/**
 * @author : Steven 11/04/19
 */
class GetNotificationTotalUnreadUseCase @Inject constructor(
        @NotificationContext val context: Context,
        private val graphqlUseCase: GraphqlUseCase
) {
    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(context.resources
                , R.raw.query_notification_update_total_unread)
        val graphqlRequest = GraphqlRequest(query,
                NotificationUpdateTotalUnread::class.java, requestParams)

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
        private const val PARAM_TYPE_OF_NOTIF = "typeOfNotif"
        private const val TYPE_NOTIF_UPDATE = 1 //update

        fun getRequestParams(typeOfNotif: Int = TYPE_NOTIF_UPDATE): HashMap<String, Any> {
            val variables = HashMap<String, Any>()
            variables[PARAM_TYPE_OF_NOTIF] = typeOfNotif
            return variables
        }
    }

}