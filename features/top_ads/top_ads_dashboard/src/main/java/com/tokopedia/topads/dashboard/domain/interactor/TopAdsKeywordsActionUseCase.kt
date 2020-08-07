package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.GROUPID
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD_ID
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD_PRICE_BID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KEYWORDS
import com.tokopedia.topads.dashboard.data.model.KeywordActionResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 5/6/20.
 */


class TopAdsKeywordsActionUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<KeywordActionResponse>(graphqlRepository) {

    fun setParams(action: String, keywords: List<String>) {

        val keyword: ArrayList<Map<String, String?>> = arrayListOf()
        keywords.forEach {
            val map = mapOf(KEYWORD_ID to it, GROUPID to null, KEYWORD_PRICE_BID to null)
            keyword.add(map)
        }
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_ID] = userSession.shopId
        queryMap[ACTION] = action
        queryMap[KEYWORDS] = keyword
        setRequestParams(queryMap)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (KeywordActionResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(KeywordActionResponse::class.java)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}