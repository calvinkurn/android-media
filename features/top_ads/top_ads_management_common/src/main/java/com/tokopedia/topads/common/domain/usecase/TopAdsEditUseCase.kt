package com.tokopedia.topads.common.domain.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject.INPUT
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_DAILY_BUDGET
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_EDIT_OPTION
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_GROUP_Id
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_PRICE_BID
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_RECOM_EDIT_SOURCE
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT
import com.tokopedia.topads.common.data.raw.EDIT_GROUP_QUERY
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.GroupEditInput
import com.tokopedia.topads.common.data.response.TopadsManageGroupAdsInput
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 24/5/20.
 */

@GqlQuery("EditGroupQuery", EDIT_GROUP_QUERY)

class TopAdsEditUseCase @Inject constructor(val userSession: UserSessionInterface) : RestRequestUseCase() {

    fun setParam(dataProduct: MutableList<GroupEditInput.Group.AdOperationsItem>?, dataGroup: HashMap<String, Any?>?) : RequestParams {

        var requestParams = RequestParams.create()

        val variable: HashMap<String, Any> = HashMap()
        variable[INPUT] = convertToParam(dataProduct, dataGroup)
        requestParams.putAll(variable)
        return requestParams

    }

    private val cacheStrategy: RestCacheStrategy = RestCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build()


    override fun buildRequest(requestParams: RequestParams?): MutableList<RestRequest> {
        val tempRequest = java.util.ArrayList<RestRequest>()
        val token = object : TypeToken<DataResponse<FinalAdResponse>>() {}.type

        var request: GraphqlRequest = GraphqlRequest(EditGroupQuery.GQL_QUERY, FinalAdResponse::class.java, requestParams?.parameters)
        val restReferralRequest = RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
                .setBody(request)
                .setCacheStrategy(cacheStrategy)
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restReferralRequest)
        return tempRequest
    }

    private fun convertToParam(dataProduct: MutableList<GroupEditInput.Group.AdOperationsItem>?, dataGroup: HashMap<String, Any?>?): TopadsManageGroupAdsInput {
        val priceBidGroup = dataGroup?.get(PARAM_PRICE_BID) as? Int
        val dailyBudgetGroup = dataGroup?.get(PARAM_DAILY_BUDGET) as? Int
        val groupId = dataGroup?.get(PARAM_GROUP_Id) as String
        return TopadsManageGroupAdsInput().apply {
            shopID = userSession.shopId
            keywordOperation = null
            groupID = groupId
            source = PARAM_RECOM_EDIT_SOURCE
            groupInput = GroupEditInput(
                    action = PARAM_EDIT_OPTION,
                    group = GroupEditInput.Group(
                            adOperations = dataProduct,
                            name = null,
                            type = PRODUCT,
                            dailyBudget = dailyBudgetGroup?.toDouble(),
                            priceBid = priceBidGroup?.toDouble()
                    )
            )
        }

    }
}