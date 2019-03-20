package com.tokopedia.notifcenter.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.domain.pojo.NotifCenterPojo
import rx.Subscriber
import javax.inject.Inject



/**
 * @author by alvinatin on 21/08/18.
 */

class NotifCenterUseCase @Inject constructor(@ApplicationContext val context: Context,
                                             val graphqlUseCase: GraphqlUseCase) {

    fun execute(variables: HashMap<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.HOUR.`val`())
                .setSessionIncluded(true)
                .build()
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_notif_center)
        val graphqlRequest = GraphqlRequest(query, NotifCenterPojo::class.java, variables, false)

        graphqlUseCase.setCacheStrategy(graphqlCacheStrategy)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun executeNoCache(variables: HashMap<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD)
                .setExpiryTime(MINUTE_5)
                .setSessionIncluded(true)
                .build()
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_notif_center)
        val graphqlRequest = GraphqlRequest(query, NotifCenterPojo::class.java, variables, false)

        graphqlUseCase.setCacheStrategy(graphqlCacheStrategy)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() = graphqlUseCase.unsubscribe()

    companion object {
        const val PARAM_FILTER_ID = "filterId"
        const val PARAM_NOTIF_LANG = "notifLang"
        const val NOTIF_LANG_DEFAULT = "id"
        const val PARAM_LAST_ID = "lastNotifId"
        const val MINUTE_5 = 5 * GraphqlConstant.MINUTE_MS

        fun getRequestParams(filterId: Int, lastNotifId: String): HashMap<String, Any> {
            val variables: HashMap<String, Any> = HashMap()
            variables.put(PARAM_FILTER_ID, filterId)
            variables.put(PARAM_NOTIF_LANG, NOTIF_LANG_DEFAULT)
            variables.put(PARAM_LAST_ID, lastNotifId)
            return variables
        }
    }
}
