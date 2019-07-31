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
import com.tokopedia.navigation.domain.pojo.NotificationUpdateFilter
import rx.Subscriber
import javax.inject.Inject

/**
 * @author : Steven 11/04/19
 */
class GetNotificationUpdateFilterUseCase @Inject constructor(
        @ApplicationContext val context: Context,
        private val graphqlUseCase: GraphqlUseCase
){
    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(context.resources
                , R.raw.query_notification_update_filter)
        val graphqlRequest = GraphqlRequest(query,
                NotificationUpdateFilter::class.java)

        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setSessionIncluded(true).build()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.setCacheStrategy(cacheStrategy)
        graphqlUseCase.execute(subscriber)
    }

}