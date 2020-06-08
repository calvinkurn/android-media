package com.tokopedia.notifcenter.domain

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.notifcenter.R
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
        val query = GraphqlHelper.loadRawString(context.resources
                , R.raw.mutation_mark_all_read_notification_update)
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
        private const val PARAM_NOTIF_TYPE = "typeOfNotif"
        private const val TYPE_NOTIF_UPDATE = 1 //update

        fun params(typeOfNotif: Int = TYPE_NOTIF_UPDATE): HashMap<String, Any> {
            val params = hashMapOf<String, Any>()
            params[PARAM_NOTIF_TYPE] = typeOfNotif
            return params
        }
    }

}