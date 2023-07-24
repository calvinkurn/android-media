package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.GROUPID
import com.tokopedia.topads.common.data.internal.ParamObject.PRICE_BID
import com.tokopedia.topads.common.data.internal.ParamObject.PRICE_DAILY
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUPS
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.SOURCE_DASH
import com.tokopedia.topads.dashboard.data.model.GroupActionResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 29/5/20.
 */

class TopAdsGroupActionUseCase @Inject constructor(private val userSession: UserSessionInterface, graphqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<GroupActionResponse>(graphqlRepository) }

    suspend fun execute(query: String, requestParams: RequestParams): GroupActionResponse {
        graphql.apply {
            setGraphqlQuery(query)
            setTypeClass(GroupActionResponse::class.java)
        }

        return graphql.run {
            setRequestParams(requestParams.parameters)
            executeOnBackground()
        }
    }

    fun setParams(action: String, groupIds: List<String>): RequestParams {
        val requestParams = RequestParams.create()
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
        requestParams.putAll(queryMap)
        return requestParams
    }
}
