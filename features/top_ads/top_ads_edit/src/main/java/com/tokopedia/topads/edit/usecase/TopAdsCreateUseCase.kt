package com.tokopedia.topads.edit.usecase

import android.os.Bundle
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.edit.data.raw.MANAGE_GROUP
import com.tokopedia.topads.edit.data.response.GetAdProductResponse
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.utils.Constants.ACTION_ADD
import com.tokopedia.topads.edit.utils.Constants.ACTION_CREATE
import com.tokopedia.topads.edit.utils.Constants.ACTION_DELETE
import com.tokopedia.topads.edit.utils.Constants.ACTION_EDIT
import com.tokopedia.topads.edit.utils.Constants.ACTION_REMOVE
import com.tokopedia.topads.edit.utils.Constants.ACTIVE
import com.tokopedia.topads.edit.utils.Constants.EDIT_SOURCE
import com.tokopedia.topads.edit.utils.Constants.GROUP_NAME
import com.tokopedia.topads.edit.utils.Constants.INPUT
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_TYPE_NEGATIVE_PHRASE
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_TYPE_PHRASE
import com.tokopedia.topads.edit.utils.Constants.NAME_EDIT
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_KEYWORDS_ADDED
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_KEYWORDS_DELETED
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_PHRASE
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_SPECIFIC
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_CREATE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_DELETE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_EDIT
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_PHRASE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_SPECIFIC
import com.tokopedia.topads.edit.utils.Constants.PRODUCT_ID
import com.tokopedia.topads.edit.utils.Constants.PUBLISHED
import com.tokopedia.topads.edit.utils.Constants.STRATEGIES
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Pika on 24/5/20.
 */

@GqlQuery("ManageGroupAdsQuery", MANAGE_GROUP)
class TopAdsCreateUseCase @Inject constructor(val userSession: UserSessionInterface) : RestRequestUseCase() {


    fun setParam(dataProduct: Bundle, dataKeyword: HashMap<String, Any?>, dataGroup: HashMap<String, Any?>): RequestParams {

        val param = RequestParams.create()
        val variable: HashMap<String, Any> = HashMap()
        variable[INPUT] = convertToParam(dataProduct, dataKeyword, dataGroup)
        param.putAll(variable)
        return param
    }

    private fun convertToParam(dataProduct: Bundle, dataKeyword: HashMap<String, Any?>, dataGroup: HashMap<String, Any?>): TopadsManageGroupAdsInput {
//
        val strategy = dataKeyword[STRATEGIES] as ArrayList<String>?
        val groupName = dataGroup[GROUP_NAME] as? String
        val priceBidGroup = dataGroup[Constants.PRICE_BID] as? Int
        val dailyBudgetGroup = dataGroup[Constants.DAILY_BUDGET] as? Int
        val groupId = dataGroup[Constants.GROUP_ID] as? Int
        val isNameEdited = dataGroup[NAME_EDIT] as? Boolean
        val isBudgetLimited = dataGroup[Constants.BUDGET_LIMITED] as? Boolean
        val dataAddProduct = dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>("addedProducts")
        val dataDeleteProduct = dataProduct.getParcelableArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>("deletedProducts")


        val keywordsPositiveCreate = dataKeyword[POSITIVE_CREATE] as? MutableList<GetKeywordResponse.KeywordsItem>
        val keywordsPositiveDelete = dataKeyword[POSITIVE_DELETE] as? MutableList<GetKeywordResponse.KeywordsItem>
        val keywordsNegCreate = dataKeyword[NEGATIVE_KEYWORDS_ADDED] as? MutableList<GetKeywordResponse.KeywordsItem>
        val keywordsNegDelete = dataKeyword[NEGATIVE_KEYWORDS_DELETED] as? MutableList<GetKeywordResponse.KeywordsItem>
        val keywordsPostiveEdit = dataKeyword[POSITIVE_EDIT] as? MutableList<GetKeywordResponse.KeywordsItem>

        //always
        val input = TopadsManageGroupAdsInput()
        val groupInput = input.groupInput
        val group = groupInput.group
        input.groupID = groupId.toString()
        input.shopID = userSession.shopId
        input.source = EDIT_SOURCE
        groupInput.action = ACTION_EDIT
        // if only group name is edited
        if (isNameEdited == true) {
            group?.type = PRODUCT_ID
            group?.name = groupName
        } else {
            group?.name = null
        }
        group?.type = PRODUCT_ID
        group?.status = PUBLISHED
        group?.scheduleStart = ""
        group?.scheduleEnd = ""
        group?.strategies = strategy
        if (isBudgetLimited == true) {
            group?.dailyBudget = 0.0
        } else
            group?.dailyBudget = dailyBudgetGroup?.toDouble()
        group?.priceBid = priceBidGroup?.toDouble()
        val productList: MutableList<GroupEditInput.Group.AdOperationsItem> = mutableListOf()
        val keywordList: MutableList<KeywordEditInput> = mutableListOf()
        dataAddProduct?.forEach { x ->
            val adOperation = GroupEditInput.Group.AdOperationsItem()
            val ad = GroupEditInput.Group.AdOperationsItem.Ad()
            ad.productId = x.itemID.toString()
            adOperation.ad = ad
            adOperation.action = ACTION_ADD
            productList.add(adOperation)
        }
        dataDeleteProduct?.forEach { productDeleted ->
            val adOperation = GroupEditInput.Group.AdOperationsItem()
            val ad = GroupEditInput.Group.AdOperationsItem.Ad()
            ad.productId = productDeleted.itemID.toString()
            adOperation.ad = ad
            adOperation.action = ACTION_REMOVE
            productList.add(adOperation)
        }

        keywordsPostiveEdit?.forEach { posKey ->
            val keywordEditInput = KeywordEditInput()
            val keyword = KeywordEditInput.Keyword()
            keyword.price_bid = posKey.priceBid.toDouble()
            keyword.id = posKey.keywordId
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
            keyword.id = posKey.keywordId
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
            keyword.id = keyPos.keywordId
            keyword.price_bid = keyPos.priceBid.toDouble()
            keyword.status = ACTIVE
            keyword.tag = keyPos.tag
            if (keyPos.type == KEYWORD_TYPE_PHRASE) {
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
        val restReferralRequest = RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
                .setBody(request)
                .setHeaders(headers)
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restReferralRequest)
        return tempRequest
    }
}