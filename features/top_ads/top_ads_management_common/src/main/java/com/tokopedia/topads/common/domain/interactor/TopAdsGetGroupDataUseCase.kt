package com.tokopedia.topads.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.END_DATE
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.PAGE
import com.tokopedia.topads.common.data.internal.ParamObject.QUERY_INPUT
import com.tokopedia.topads.common.data.internal.ParamObject.SORT
import com.tokopedia.topads.common.data.internal.ParamObject.START_DATE
import com.tokopedia.topads.common.data.internal.ParamObject.STATUS
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 6/9/20.
 */


class TopAdsGetGroupDataUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<GroupItemResponse>(graphqlRepository) {


    fun setParams(search: String, page: Int, sort: String, status: Int?, startDate: String, endDate: String) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId.toInt()
        queryMap[SORT] = sort
        queryMap[KEYWORD] = search
        queryMap[PAGE] = page
        queryMap[START_DATE] = startDate
        queryMap[END_DATE] = endDate
        queryMap[STATUS] = status
        queryMap[GROUP_TYPE] = 1
        setRequestParams(mapOf(QUERY_INPUT to queryMap))
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (GroupItemResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(GroupItemResponse::class.java)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}