package com.tokopedia.topads.common.domain.usecase

import android.os.Bundle
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.ACTION_ADD
import com.tokopedia.topads.common.data.internal.ParamObject.ACTION_CREATE
import com.tokopedia.topads.common.data.internal.ParamObject.ACTION_DELETE
import com.tokopedia.topads.common.data.internal.ParamObject.ACTION_EDIT
import com.tokopedia.topads.common.data.internal.ParamObject.ACTION_REMOVE
import com.tokopedia.topads.common.data.internal.ParamObject.ACTION_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.ACTIVE
import com.tokopedia.topads.common.data.internal.ParamObject.BID_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.BUDGET_LIMITED
import com.tokopedia.topads.common.data.internal.ParamObject.DAILY_BUDGET
import com.tokopedia.topads.common.data.internal.ParamObject.GROUPID
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_NAME
import com.tokopedia.topads.common.data.internal.ParamObject.INPUT
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD_TYPE_NEGATIVE_PHRASE
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD_TYPE_PHRASE
import com.tokopedia.topads.common.data.internal.ParamObject.NAME_EDIT
import com.tokopedia.topads.common.data.internal.ParamObject.NEGATIVE_KEYWORDS_ADDED
import com.tokopedia.topads.common.data.internal.ParamObject.NEGATIVE_KEYWORDS_DELETED
import com.tokopedia.topads.common.data.internal.ParamObject.NEGATIVE_PHRASE
import com.tokopedia.topads.common.data.internal.ParamObject.NEGATIVE_SPECIFIC
import com.tokopedia.topads.common.data.internal.ParamObject.POSITIVE_CREATE
import com.tokopedia.topads.common.data.internal.ParamObject.POSITIVE_DELETE
import com.tokopedia.topads.common.data.internal.ParamObject.POSITIVE_EDIT
import com.tokopedia.topads.common.data.internal.ParamObject.POSITIVE_PHRASE
import com.tokopedia.topads.common.data.internal.ParamObject.POSITIVE_SPECIFIC
import com.tokopedia.topads.common.data.internal.ParamObject.PUBLISHED
import com.tokopedia.topads.common.data.internal.ParamObject.STRATEGIES
import com.tokopedia.topads.common.data.raw.MANAGE_GROUP
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Pika on 24/5/20.
 */

@GqlQuery("ManageGroupAdsQuery", MANAGE_GROUP)
class TopAdsCreateUseCase @Inject constructor(val userSession: UserSessionInterface) :
    RestRequestUseCase() {

    private val restRepository: RestRepository by lazy { RestRequestInteractor.getInstance().restRepository }

    suspend fun execute(requestParams: RequestParams?): FinalAdResponse {
        val token = object : TypeToken<DataResponse<FinalAdResponse>>() {}.type
        val query = ManageGroupAdsQuery.GQL_QUERY
        val request = GraphqlRequest(query, FinalAdResponse::class.java, requestParams?.parameters)
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        val restRequest =
            RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
                .setBody(request)
                .setHeaders(headers)
                .setRequestType(RequestType.POST)
                .build()
        return restRepository.getResponse(restRequest).getData<DataResponse<FinalAdResponse>>().data
    }

    fun setParam(
        source: String?, dataProduct: Bundle,
        dataKeyword: HashMap<String, Any?>, dataGroup: HashMap<String, Any?>,
    ): RequestParams {

        val param = RequestParams.create()
        val variable: HashMap<String, Any> = HashMap()
        variable[INPUT] =
            convertToParam(source, dataProduct, dataKeyword, dataGroup)
        param.putAll(variable)
        return param
    }

    private fun convertToParam(
        source: String?, dataProduct: Bundle,
        dataKeyword: HashMap<String, Any?>, dataGroup: HashMap<String, Any?>,
    ): TopadsManagePromoGroupProductInput {
        var strategy = dataKeyword[STRATEGIES] as ArrayList<String>?
        val groupName = dataGroup[GROUP_NAME] as? String
        val groupAction = dataGroup[ACTION_TYPE] as? String
        var bidtypeData = dataKeyword[BID_TYPE] as MutableList<TopAdsBidSettingsModel>?
//        val priceBidGroup = dataKeyword[Constants.PRICE_BID] as? Int
        if (bidtypeData == null) {
            bidtypeData = dataGroup[BID_TYPE] as MutableList<TopAdsBidSettingsModel>?
        }
        if (strategy == null) {
            strategy = dataGroup[STRATEGIES] as ArrayList<String>?
        }
        val dailyBudgetGroup = dataGroup[DAILY_BUDGET]?.toString()?.toDouble()
        val groupId = dataGroup[GROUPID] as? Int
        val isNameEdited = dataGroup[NAME_EDIT] as? Boolean
        val isBudgetLimited = dataGroup[BUDGET_LIMITED] as? Boolean
        val dataAddProduct =
            dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>(
                "addedProducts"
            )
        val dataDeleteProduct =
            dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>(
                "deletedProducts"
            )


        val keywordsPositiveCreate = dataKeyword[POSITIVE_CREATE] as? MutableList<KeySharedModel>
        val keywordsPositiveDelete = dataKeyword[POSITIVE_DELETE] as? MutableList<KeySharedModel>
        val keywordsNegCreate =
            dataKeyword[NEGATIVE_KEYWORDS_ADDED] as? MutableList<GetKeywordResponse.KeywordsItem>
        val keywordsNegDelete =
            dataKeyword[NEGATIVE_KEYWORDS_DELETED] as? MutableList<GetKeywordResponse.KeywordsItem>
        val keywordsPostiveEdit = dataKeyword[POSITIVE_EDIT] as? MutableList<KeySharedModel>

        //always
        val input = TopadsManagePromoGroupProductInput()
        val groupInput = input.groupInput
        val group = groupInput.group
        if (groupId != null)
            input.groupID = groupId.toString()
        input.shopID = userSession.shopId
        if (source != null) {
            input.source = source
        }
        if (groupAction != null) {
            groupInput.action = groupAction
        }
        // if only group name is edited
        if (isNameEdited == true) {
            group?.name = groupName
        } else {
            group?.name = null
        }
        group?.status = PUBLISHED
        group?.strategies = strategy
        group?.suggestionBidSettings = dataKeyword[ParamObject.SUGGESTION_BID_SETTINGS] as? List<GroupEditInput.Group.TopadsSuggestionBidSetting>?
        if (isBudgetLimited == false) {
            group?.dailyBudget = 0.0
        } else
            group?.dailyBudget = dailyBudgetGroup
        val bidSettingsList: MutableList<GroupEditInput.Group.TopadsGroupBidSetting> = mutableListOf()
        bidtypeData?.forEach {
            val bidType = GroupEditInput.Group.TopadsGroupBidSetting()
            bidType.bidType = it.bidType
            bidType.priceBid = it.priceBid
            bidSettingsList.add(bidType)
        }
        group?.bidSettings = bidSettingsList
        val productList: MutableList<GroupEditInput.Group.AdOperationsItem> = mutableListOf()
        val keywordList: MutableList<KeywordEditInput> = mutableListOf()
        dataAddProduct?.forEach { x ->
            val adOperation = GroupEditInput.Group.AdOperationsItem()
            val ad = GroupEditInput.Group.AdOperationsItem.Ad()
            ad.productId = x.itemID
            adOperation.ad = ad
            adOperation.action = ACTION_ADD
            productList.add(adOperation)
        }
        dataDeleteProduct?.forEach { productDeleted ->
            val adOperation = GroupEditInput.Group.AdOperationsItem()
            val ad = GroupEditInput.Group.AdOperationsItem.Ad()
            ad.productId = productDeleted.itemID
            adOperation.ad = ad
            adOperation.action = ACTION_REMOVE
            productList.add(adOperation)
        }

        keywordsPostiveEdit?.forEach { posKey ->
            val keywordEditInput = KeywordEditInput()
            val keyword = KeywordEditInput.Keyword()
            keyword.price_bid = posKey.priceBid.toDouble()
            keyword.id = posKey.id
            keyword.status = null
            keyword.tag = null
            keyword.type = null
            keyword.source = null
            keywordEditInput.keyword = keyword
            keywordEditInput.action = ACTION_EDIT
            keywordList.add(keywordEditInput)

        }
        keywordsPositiveDelete?.forEach { posKey ->
            val keywordEditInput = KeywordEditInput()
            val keyword = KeywordEditInput.Keyword()
            keyword.price_bid = null
            keyword.id = posKey.id
            keyword.tag = null
            keyword.status = null
            keyword.type = null
            keyword.source = null
            keywordEditInput.keyword = keyword
            keywordEditInput.action = ACTION_DELETE
            keywordList.add(keywordEditInput)
        }
        keywordsPositiveCreate?.forEach { keyPos ->
            val keywordEditInput = KeywordEditInput()
            val keyword = KeywordEditInput.Keyword()
            keyword.source = keyPos.source
            keyword.id = keyPos.id
            keyword.price_bid = keyPos.priceBid.toDouble()
            keyword.status = ACTIVE
            keyword.tag = keyPos.name
            if (keyPos.typeInt == KEYWORD_TYPE_PHRASE) {
                keyword.type = POSITIVE_PHRASE
            } else {
                keyword.type = POSITIVE_SPECIFIC
            }
            keywordEditInput.keyword = keyword
            keywordEditInput.action = ACTION_CREATE
            keywordList.add(keywordEditInput)
        }

        keywordsNegDelete?.forEach { negKey ->
            val keywordEditInput = KeywordEditInput()
            val keyword = KeywordEditInput.Keyword()
            keyword.price_bid = 0.0
            keyword.id = negKey.keywordId
            keyword.tag = null
            keyword.status = null
            keyword.type = null
            keyword.source = null
            keywordEditInput.keyword = keyword
            keywordEditInput.action = ACTION_DELETE
            keywordList.add(keywordEditInput)
        }

        keywordsNegCreate?.forEach { negKey ->
            val keywordEditInput = KeywordEditInput()
            val keyword = KeywordEditInput.Keyword()
            keyword.id = "0"
            keyword.price_bid = 0.0
            if (negKey.type == KEYWORD_TYPE_NEGATIVE_PHRASE) {
                keyword.type = NEGATIVE_PHRASE
            } else {
                keyword.type = NEGATIVE_SPECIFIC
            }
            keyword.status = ACTIVE
            keyword.tag = negKey.tag
            keyword.source = negKey.source
            keywordEditInput.keyword = keyword
            keywordEditInput.action = ACTION_CREATE
            keywordList.add(keywordEditInput)
        }

        input.groupInput = groupInput
        input.groupInput.group = group
        if (productList.isNullOrEmpty())
            input.groupInput.group?.adOperations = null
        else
            input.groupInput.group?.adOperations = productList
        if (keywordList.isNullOrEmpty())
            input.keywordOperation = null
        else
            input.keywordOperation = keywordList
        return input
    }

    override fun buildRequest(requestParams: RequestParams?): MutableList<RestRequest> {
        val tempRequest = ArrayList<RestRequest>()
        val token = object : TypeToken<DataResponse<FinalAdResponse>>() {}.type
        val query = ManageGroupAdsQuery.GQL_QUERY
        val request = GraphqlRequest(query, FinalAdResponse::class.java, requestParams?.parameters)
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        val restReferralRequest =
            RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
                .setBody(request)
                .setHeaders(headers)
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restReferralRequest)
        return tempRequest
    }
}