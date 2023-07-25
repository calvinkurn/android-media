package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject.DATA
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_ID
import com.tokopedia.topads.common.data.internal.ParamObject.INSIGHT_SOURCE
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_id
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.dashboard.data.model.insightkey.MutationData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 22/7/20.
 */

class TopAdsEditKeywordUseCase @Inject constructor(
    val userSession: UserSessionInterface,
    graphqlRepository: GraphqlRepository
) {

    private val graphql by lazy { GraphqlUseCase<FinalAdResponse>(graphqlRepository) }

    suspend fun execute(query: String, requestParams: RequestParams): FinalAdResponse {
        graphql.apply {
            setGraphqlQuery(query)
            setTypeClass(FinalAdResponse::class.java)
        }

        return graphql.run {
            setRequestParams(requestParams.parameters)
            executeOnBackground()
        }
    }

    fun setParam(groupId: String, data: List<MutationData>): RequestParams {
        val variable: HashMap<String, Any> = HashMap()
        val requestParams = RequestParams.create()
        variable[SHOP_id] = userSession.shopId.toString()
        variable[GROUP_ID] = groupId
        variable[SOURCE] = INSIGHT_SOURCE
        variable[DATA] = data
        requestParams.putAll(variable)
        return requestParams
    }
}
