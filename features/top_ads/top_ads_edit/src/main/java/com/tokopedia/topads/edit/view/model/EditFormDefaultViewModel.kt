package com.tokopedia.topads.edit.view.model

import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.SingleAd
import com.tokopedia.topads.common.data.response.SingleAdInFo
import com.tokopedia.topads.edit.data.param.DataSuggestions
import com.tokopedia.topads.edit.data.response.*
import com.tokopedia.topads.edit.usecase.*
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 6/4/20.
 */

class EditFormDefaultViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val validGroupUseCase: ValidGroupUseCase,
        private val bidInfoUseCase: BidInfoUseCase,
        private val getAdsUseCase: GetAdsUseCase,
        private val getAdKeywordUseCase: GetAdKeywordUseCase,
        private val groupInfoUseCase: GroupInfoUseCase,
        private val editSingleAdUseCase: EditSingleAdUseCase,
        private val singleAdInfoUseCase: GraphqlUseCase<SingleAdInFo>,
        private val userSession: UserSessionInterface,
        private val topAdsCreateUseCase: TopAdsCreateUseCase) : BaseViewModel(dispatcher) {

    companion object {
        private const val QUERY_PRODUCT = """query topAdsGetPromo(${'$'}shopID: String!, ${'$'}adID: String!) {
            topAdsGetPromo(shopID:${'$'}shopID, adID: ${'$'}adID) {
                data {
                    adType
                    itemID
                    status
                    priceBid
                    priceDaily
                }
                errors {
                    code
                    detail
                    title
                }
            }
        }"""
    }

    fun validateGroup(groupName: String, onSuccess: ((ResponseGroupValidateName.TopAdsGroupValidateName) -> Unit)) {
        validGroupUseCase.setParams(groupName)
        validGroupUseCase.executeQuerySafeMode(
                {
                    onSuccess(it)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun getBidInfoDefault(suggestions: List<DataSuggestions>, onSuccess: (List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) -> Unit) {
        bidInfoUseCase.setParams(suggestions, ParamObject.PRODUCT)
        bidInfoUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topadsBidInfo.data)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun editSingleAd(adId: String, priceBid: Float, priceDaily: Float) {
        editSingleAdUseCase.setParams(adId, priceBid, priceDaily)
        editSingleAdUseCase.executeQuerySafeMode(
                {
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun getAds(groupId: Int?, onSuccess: (List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>) -> Unit) {

        getAdsUseCase.setParams(groupId)
        getAdsUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topadsGetListProductsOfGroup.data)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun getGroupInfo(groupId: String, onSuccess: (GroupInfoResponse.TopAdsGetPromoGroup.Data) -> Unit) {

        groupInfoUseCase.setParams(groupId)
        groupInfoUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topAdsGetPromoGroup?.data!!)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun getAdKeyword(groupId: Int, cursor: String, onSuccess: (List<GetKeywordResponse.KeywordsItem>, cursor: String) -> Unit) {
        getAdKeywordUseCase.setParams(groupId, cursor)
        getAdKeywordUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topAdsListKeyword.data.keywords, it.topAdsListKeyword.data.pagination.cursor)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun getBidInfo(suggestions: List<DataSuggestions>, onSuccess: (List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) -> Unit) {
        bidInfoUseCase.setParams(suggestions, KEYWORD)
        bidInfoUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topadsBidInfo.data)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun topAdsCreated(dataProduct: Bundle, dataKeyword: HashMap<String, Any?>,
                      dataGroup: HashMap<String, Any?>, onSuccess: (() -> Unit), onError: (() -> Unit)) {
        topAdsCreateUseCase.setParam(dataProduct, dataKeyword, dataGroup)
        topAdsCreateUseCase.executeQuerySafeMode(
                { onSuccess() },
                { throwable ->
                    onError()
                    throwable.printStackTrace()
                })
    }

    @GqlQuery("CategoryList", QUERY_PRODUCT)
    fun getSingleAdInfo(adId: Int, onSuccess: ((List<SingleAd>) -> Unit)) {
        val params = mapOf(ParamObject.SHOP_ID to userSession.shopId,
                ParamObject.AD_ID to adId.toString())
        singleAdInfoUseCase.setTypeClass(SingleAdInFo::class.java)
        singleAdInfoUseCase.setRequestParams(params)
        singleAdInfoUseCase.setGraphqlQuery(CategoryList.GQL_QUERY)
        singleAdInfoUseCase.execute(
                onSuccessGroup(onSuccess),
                {
                    it.printStackTrace()
                }
        )
    }

    private fun onSuccessGroup(onSuccess: (List<SingleAd>) -> Unit): (SingleAdInFo) -> Unit {
        return {
            onSuccess(it.topAdsGetPromo.data)
        }
    }

    public override fun onCleared() {
        super.onCleared()
        validGroupUseCase.cancelJobs()
        bidInfoUseCase.cancelJobs()
        getAdsUseCase.cancelJobs()
        getAdKeywordUseCase.cancelJobs()
        groupInfoUseCase.cancelJobs()
        topAdsCreateUseCase.cancelJobs()
        editSingleAdUseCase.cancelJobs()
        singleAdInfoUseCase.cancelJobs()
    }
}

