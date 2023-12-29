package com.tokopedia.topads.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
import com.tokopedia.topads.common.domain.query.GetTopadsDashboardGroupsV3
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.set

/**
 * Created by Pika on 6/9/20.
 */

class TopAdsGetGroupDataUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
    graphqlRepository: GraphqlRepository
) {

    private val graphql by lazy { GraphqlUseCase<GroupItemResponse>(graphqlRepository) }

    suspend fun execute(requestParams: RequestParams): GroupItemResponse {
        graphql.apply {
            setGraphqlQuery(GetTopadsDashboardGroupsV3)
            setTypeClass(GroupItemResponse::class.java)
        }

        return graphql.run {
            setRequestParams(requestParams.parameters)
            executeOnBackground()
        }
    }

    fun setParams(
        search: String,
        page: Int,
        sort: String,
        status: Int?,
        startDate: String,
        endDate: String,
        groupType: Int
    ): RequestParams {
        val queryMap = HashMap<String, Any?>()
        val requestParams = RequestParams.create()
        queryMap[ParamObject.SHOP_id] = userSession.shopId
        queryMap[SORT] = sort
        queryMap[KEYWORD] = search
        queryMap[PAGE] = page
        queryMap[START_DATE] = startDate
        queryMap[END_DATE] = endDate
        queryMap[STATUS] = status
        queryMap[GROUP_TYPE] = groupType
        requestParams.putAll(mapOf(QUERY_INPUT to queryMap))
        return requestParams
    }
}
