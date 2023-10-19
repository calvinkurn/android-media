package com.tokopedia.topads.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.QUERY_INPUT
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.domain.query.GetTopadsDashboardProductsV4
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.set

/**
 * Created by Pika on 7/6/20.
 */

class TopAdsGetGroupProductDataUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
    graphqlRepository: GraphqlRepository
) {

    private val graphql by lazy { GraphqlUseCase<NonGroupResponse>(graphqlRepository) }

    suspend fun execute(requestParams: RequestParams): NonGroupResponse {
        graphql.apply {
            setGraphqlQuery(GetTopadsDashboardProductsV4)
            setTypeClass(NonGroupResponse::class.java)
        }

        return graphql.run {
            setRequestParams(requestParams.parameters)
            executeOnBackground()
        }
    }

    fun setParams(
        groupId: String?,
        page: Int,
        search: String,
        sort: String,
        status: Int?,
        startDate: String,
        endDate: String,
        type: String = "",
        goalId: Int = 0
    ): RequestParams {
        val requestParams = RequestParams.create()
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId
        queryMap[ParamObject.GROUP] = groupId
        queryMap[ParamObject.SORT] = sort
        queryMap[ParamObject.PAGE] = page
        queryMap[ParamObject.START_DATE] = startDate
        queryMap[ParamObject.END_DATE] = endDate
        queryMap[ParamObject.KEYWORD] = search
        queryMap[ParamObject.STATUS] = status
        queryMap[ParamObject.TYPE] = type
        queryMap[ParamObject.GOAL_ID] = goalId.toString()
        requestParams.putAll(mapOf(QUERY_INPUT to queryMap))
        return requestParams
    }
}
