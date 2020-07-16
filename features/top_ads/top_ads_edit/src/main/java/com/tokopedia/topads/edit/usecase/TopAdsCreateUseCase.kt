package com.tokopedia.topads.edit.usecase

import android.content.Context
import android.os.Bundle
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.di.ActivityContext
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.param.GroupEditInput
import com.tokopedia.topads.edit.data.param.KeywordEditInput
import com.tokopedia.topads.edit.data.param.TopadsManageGroupAdsInput
import com.tokopedia.topads.edit.data.response.FinalAdResponse
import com.tokopedia.topads.edit.data.response.GetAdProductResponse
import com.tokopedia.topads.edit.data.response.GetKeywordResponse
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
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_SPECIFIC
import com.tokopedia.topads.edit.utils.Constants.POSTIVE_PHRASE
import com.tokopedia.topads.edit.utils.Constants.PRODUCT_ID
import com.tokopedia.topads.edit.utils.Constants.PUBLISHED
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 24/5/20.
 */

class TopAdsCreateUseCase @Inject constructor(@ActivityContext
                                              val context: Context?, graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<FinalAdResponse>(graphqlRepository) {

    private fun getQuery(): String {
        return GraphqlHelper.loadRawString(context?.resources, R.raw.query_ads_edit_activated_ads)
    }

    fun setParam(dataProduct: Bundle, dataKeyword: HashMap<String, Any?>, dataGroup: HashMap<String, Any?>) {

        val variable: HashMap<String, Any> = HashMap()
        variable[INPUT] = convertToParam(dataProduct, dataKeyword, dataGroup)
        setRequestParams(variable)

    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (FinalAdResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(FinalAdResponse::class.java)
        setGraphqlQuery(getQuery())
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }

    fun convertToParam(dataProduct: Bundle, dataKeyword: HashMap<String, Any?>, dataGroup: HashMap<String, Any?>): TopadsManageGroupAdsInput {
//
        val groupName: String = dataGroup[GROUP_NAME] as String
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
        if (isBudgetLimited == true) {
            group?.dailyBudget = 0
        } else
            group?.dailyBudget = dailyBudgetGroup
        group?.priceBid = priceBidGroup
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
            keyword.price_bid = posKey.priceBid
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
            keyword.price_bid = keyPos.priceBid
            keyword.status = ACTIVE
            keyword.tag = keyPos.tag
            if (keyPos.type == KEYWORD_TYPE_PHRASE) {
                keyword.type = POSTIVE_PHRASE
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
            keyword.price_bid = 0
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
            keyword.price_bid = 0
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
}