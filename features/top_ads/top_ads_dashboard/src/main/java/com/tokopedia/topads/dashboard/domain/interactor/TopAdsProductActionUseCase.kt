package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.PRICE_BID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.AD_ID
import com.tokopedia.topads.dashboard.data.model.ProductActionResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 5/6/20.
 */

class TopAdsProductActionUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<ProductActionResponse>(graphqlRepository) {

    fun setParams(action: String, adIds: List<String>, selectedFilter: String?) {

        val product: ArrayList<Map<String, String?>> = arrayListOf()

        adIds.forEach {
            val map = mapOf(ParamObject.AD_ID to it, ParamObject.GROUPID to (selectedFilter
                    ?: ""), PRICE_BID to null)
            product.add(map)
        }
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_ID] = userSession.shopId
        queryMap[ACTION] = action
        queryMap[AD_ID] = product
        setRequestParams(queryMap)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (ProductActionResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(ProductActionResponse::class.java)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}