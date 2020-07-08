package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.AD_IDS
import com.tokopedia.topads.common.data.internal.ParamObject.END_DATE
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_iDS
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.PAGE
import com.tokopedia.topads.common.data.internal.ParamObject.QUERY_INPUT
import com.tokopedia.topads.common.data.internal.ParamObject.SORT
import com.tokopedia.topads.common.data.internal.ParamObject.START_DATE
import com.tokopedia.topads.common.data.internal.ParamObject.STATUS
import com.tokopedia.topads.dashboard.data.model.groupitem.GroupStatisticsResponse
import com.tokopedia.topads.dashboard.data.model.nongroupItem.ProductStatisticsResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 29/5/20.
 */


class TopAdsGetProductStatisticsUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<ProductStatisticsResponse>(graphqlRepository) {

    fun setParams(startDate: String, endDate: String, adids: List<String>) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId.toInt()
        queryMap[START_DATE] = startDate
        queryMap[END_DATE] = endDate
        queryMap[AD_IDS] = adids.joinToString(",")
        setRequestParams(mapOf(QUERY_INPUT to queryMap))
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (ProductStatisticsResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(ProductStatisticsResponse::class.java)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)
        }, onError)
    }
}