package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.GROUPID
import com.tokopedia.topads.common.data.internal.ParamObject.PRICE_BID
import com.tokopedia.topads.common.data.internal.ParamObject.PRICE_DAILY
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUPS
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.SOURCE_DASH
import com.tokopedia.topads.dashboard.data.model.GroupActionResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 29/5/20.
 */


class TopAdsGroupActionUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<GroupActionResponse>(graphqlRepository) {


    fun setParams(action: String, groupIds: List<String>) {

        val group: ArrayList<Map<String, String?>> = arrayListOf()
        groupIds.forEach {
            val map = mapOf(GROUPID to it, PRICE_BID to null, PRICE_DAILY to null)
            group.add(map)
        }
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_ID] = userSession.shopId
        queryMap[ACTION] = action
        queryMap[SOURCE] = SOURCE_DASH
        queryMap[GROUPS] = group
        setRequestParams(queryMap)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (GroupActionResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(GroupActionResponse::class.java)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}