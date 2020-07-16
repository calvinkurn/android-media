package com.tokopedia.topads.dashboard.domain.interactor

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject.INSIGHT_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_Id
import com.tokopedia.topads.dashboard.data.model.insight.Data
import com.tokopedia.topads.dashboard.data.model.insight.InsightResponse
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


/**
 * Created by Pika on 13/7/20.
 */

class TopAdsInsightUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<JsonObject>(graphqlRepository) {
    fun setParams() {
        setRequestParams(mapOf(SHOP_Id to userSession.shopId.toInt(), INSIGHT_TYPE to "all"))
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (InsightKeyResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(JsonObject::class.java)
        setCacheStrategy(cacheStrategy)
        execute({
//            val body = it.toString()
//            val jsonElement = it.getAsJsonObject("topAdsGetKeywordInsights").get("data")
            val data: InsightKeyResponse = Gson().fromJson(it.toString(), InsightKeyResponse::class.java)
//            val x = jsonElement.asJsonObject.get("data").toString()
//            val key = Gson().fromJson(it.getAsJsonObject("data").toString(),Data::class.java)
            onSuccess(data )
        }, onError)
    }
}