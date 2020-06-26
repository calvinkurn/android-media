package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.QUERY_INPUT
import com.tokopedia.topads.dashboard.data.model.nongroupItem.NonGroupResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 7/6/20.
 */

class TopAdsGetGroupProductDataUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<NonGroupResponse>(graphqlRepository) {


    fun setParams(groupId: Int?, page: Int,  search: String, sort: String, status: Int?, startDate: String, endDate: String) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId.toInt()
        queryMap[ParamObject.GROUP] = groupId
        queryMap[ParamObject.SORT] = sort
        queryMap[ParamObject.PAGE] = page
        queryMap[ParamObject.START_DATE] = startDate
        queryMap[ParamObject.END_DATE] = endDate
        queryMap[ParamObject.KEYWORD] = search
        queryMap[ParamObject.STATUS] = status
        setRequestParams(mapOf(QUERY_INPUT to queryMap))
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (NonGroupResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(NonGroupResponse::class.java)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}