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
import com.tokopedia.navigation.domain.pojo.NotificationCenterDetail
import rx.Subscriber
import javax.inject.Inject

/**
 * @author : Steven 11/04/19
 */
class GetNotificationUpdateUseCase @Inject constructor(
        @ApplicationContext val context: Context,
        private val graphqlUseCase: GraphqlUseCase
){
    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(context.resources
                , R.raw.query_notification_update)
        val graphqlRequest = GraphqlRequest(query,
                NotificationCenterDetail::class.java, requestParams)

        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setSessionIncluded(true).build()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.setCacheStrategy(cacheStrategy)
        graphqlUseCase.execute(subscriber)
    }

    companion object {
        const val PARAM_PAGE = "page"
        const val PARAM_LAST_ID = "lastNotifId"

        fun getRequestParams(
                page: Int,
                variables: HashMap<String, Any>,
                lastNotifId: String
        ): HashMap<String, Any> {
            variables[PARAM_PAGE] = page
            variables[PARAM_LAST_ID] = lastNotifId
            return variables
        }
    }
}