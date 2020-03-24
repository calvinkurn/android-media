package com.tokopedia.notifcenter.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.NotificationCenterDetail
import rx.Subscriber
import javax.inject.Inject
import com.tokopedia.abstraction.common.utils.GraphqlHelper.loadRawString as raw
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy.Builder as cacheBuilder

class SingleNotificationUpdateUseCase @Inject constructor(
        @ApplicationContext val context: Context,
        private val useCase: GraphqlUseCase
){

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = raw(context.resources, R.raw.query_notif_center_single)
        val cacheStrategy = cacheBuilder(CacheType.CLOUD_THEN_CACHE)
                .setSessionIncluded(true)
                .build()
        val request = GraphqlRequest(
                query,
                NotificationCenterDetail::class.java,
                requestParams
        )

        useCase.apply {
            clearRequest()
            addRequest(request)
            setCacheStrategy(cacheStrategy)
            execute(subscriber)
        }
    }

    companion object {
        private const val PARAM_NOTIF_ID = "notifId"
        private const val PARAM_NOTIF_LANG = "notifLang"

        fun params(notifId: Int, notifLang: String): Map<String, Any> {
            val variables = hashMapOf<String, Any>()
            variables[PARAM_NOTIF_ID] = notifId
            variables[PARAM_NOTIF_LANG] = notifLang
            return variables
        }
    }

}