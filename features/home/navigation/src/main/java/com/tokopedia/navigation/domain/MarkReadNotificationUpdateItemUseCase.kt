package com.tokopedia.navigation.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.pojo.NotificationUpdateActionResponse
import rx.Subscriber
import javax.inject.Inject

/**
 * @author : Steven 11/04/19
 */
class MarkReadNotificationUpdateItemUseCase @Inject constructor(
        @ApplicationContext val context: Context,
        private val graphqlUseCase: GraphqlUseCase
){
    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(context.resources
                , R.raw.mutation_mark_read_notification_update_item)
        val graphqlRequest = GraphqlRequest(query,
                NotificationUpdateActionResponse::class.java, requestParams)

        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setSessionIncluded(true).build()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.setCacheStrategy(cacheStrategy)
        graphqlUseCase.execute(subscriber)
    }

    companion object {
        const val PARAM_NOTIF_ID = "notifId"

        fun getRequestParams(
                lastNotifId: String
        ): HashMap<String, Any> {
            var variables = HashMap<String, Any>()
            variables[PARAM_NOTIF_ID] = lastNotifId
            return variables
        }
    }
}