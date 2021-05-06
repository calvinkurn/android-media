package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.top_ads_headline.Constants.EDIT_HEADLINE_PAGE
import com.tokopedia.top_ads_headline.Constants.HEADLINE
import com.tokopedia.top_ads_headline.Constants.STATUS_INACTIVE
import com.tokopedia.top_ads_headline.Constants.STATUS_PUBLISHED
import com.tokopedia.top_ads_headline.Constants.TYPE_HEADLINE_KEYWORD
import com.tokopedia.top_ads_headline.data.Category
import com.tokopedia.top_ads_headline.data.HeadlineAdStepperModel
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.SingleAd
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import kotlinx.coroutines.launch
import rx.Subscriber
import java.lang.reflect.Type
import javax.inject.Inject

private const val TYPE_BANNER = "banner"

class SharedEditHeadlineViewModel @Inject constructor(
        private val topAdsGetGroupProductUseCase: TopAdsGetGroupProductDataUseCase,
        private val topAdsGetPromoUseCase: TopAdsGetPromoUseCase,
        private val bidInfoUseCase: BidInfoUseCase) : ViewModel() {

    private var adId: Int = 0
    private var status: String = ""
    private val editHeadlineAdLiveData: MutableLiveData<HeadlineAdStepperModel> = MutableLiveData()

    fun getEditHeadlineAdLiveData(): LiveData<HeadlineAdStepperModel> = editHeadlineAdLiveData

    private fun getBidInfoDetail(headlineAdStepperModel: HeadlineAdStepperModel) {
        viewModelScope.launch {
            val selectedProductIds: List<Long>? = editHeadlineAdLiveData.value?.selectedProductIds?.map {
                it.toLong()
            }
            val suggestions = DataSuggestions(TYPE_HEADLINE_KEYWORD, ids = selectedProductIds)
            bidInfoUseCase.setParams(listOf(suggestions), HEADLINE, EDIT_HEADLINE_PAGE)
            bidInfoUseCase.executeQuerySafeMode({ result ->
                result.topadsBidInfo.data.firstOrNull()?.let {
                    setBiddingValueIntoStepperModel(headlineAdStepperModel, it)
                }
            }, {
                it.printStackTrace()
            })
        }
    }

    private fun setBiddingValueIntoStepperModel(stepperModel: HeadlineAdStepperModel, bidData: TopadsBidInfo.DataItem) {
        stepperModel.maxBid = bidData.maxBid
        stepperModel.minBid = bidData.minBid
        editHeadlineAdLiveData.value = stepperModel
    }

    fun getHeadlineAdId(groupId: Int, shopId: Int, onError: (message: String) -> Unit) {
        viewModelScope.launch {
            val requestParams = topAdsGetGroupProductUseCase.setParams(groupId, 0, "", "", null, "", "", TYPE_BANNER)
            topAdsGetGroupProductUseCase.execute(requestParams, object : Subscriber<Map<Type, RestResponse>>() {
                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    e?.printStackTrace()
                }

                override fun onNext(typeResponse: Map<Type, RestResponse>) {
                    val token = object : TypeToken<DataResponse<NonGroupResponse?>>() {}.type
                    val restResponse: RestResponse? = typeResponse[token]
                    val response = restResponse?.getData() as DataResponse<NonGroupResponse>
                    val nonGroupResponse = response.data.topadsDashboardGroupProducts
                    if (nonGroupResponse.data.isNotEmpty()) {
                        adId = nonGroupResponse.data.first().adId
                        getHeadlineAdDetail(nonGroupResponse.data.first().adId.toString(), shopId.toString(), onError)
                    }
                }
            })

        }
    }

    private fun getHeadlineAdDetail(adId: String, shopId: String, onError: (message: String) -> Unit) {
        viewModelScope.launch {
            topAdsGetPromoUseCase.setParams(adId, shopId)
            topAdsGetPromoUseCase.execute(
                    {
                        if (it.topAdsGetPromo.data.isNotEmpty()) {
                            status = it.topAdsGetPromo.data.first().status
                            val headlineAdStepperModel = setEditAdHeadlineData(it.topAdsGetPromo.data.first())
                            getBidInfoDetail(headlineAdStepperModel)
                        } else if (it.topAdsGetPromo.errors.isNotEmpty()) {
                            onError(it.topAdsGetPromo.errors.first().detail)
                        }
                    },
                    {
                        onError(it.message ?: "")
                        it.printStackTrace()
                    }
            )
        }
    }

    private fun setEditAdHeadlineData(ad: SingleAd): HeadlineAdStepperModel {
        val editHeadlineAdModel = HeadlineAdStepperModel()
        editHeadlineAdModel.adBidPrice = ad.priceBid.toDouble()
        editHeadlineAdModel.dailyBudget = ad.priceDaily.toFloat()
        editHeadlineAdModel.startDate = "${ad.adStartDate} ${ad.adStartTime}"
        editHeadlineAdModel.endDate = "${ad.adEndDate} ${ad.adEndTime}"
        editHeadlineAdModel.stateRestoreKeyword = true
        ad.cpmDetails.firstOrNull()?.let {
            editHeadlineAdModel.groupName = it.title
            editHeadlineAdModel.slogan = it.description.slogan
            val selectedProductMap = HashMap<Category, ArrayList<TopAdsProductModel>>()
            it.product.forEach { product ->
                editHeadlineAdModel.selectedProductIds.add(product.productID)
                val category = Category(product.departmentID.toString(), product.departmentName)
                if (selectedProductMap[category] != null) {
                    selectedProductMap[category]?.add(product)
                } else {
                    val arrayList: ArrayList<TopAdsProductModel> = ArrayList()
                    arrayList.add(product)
                    selectedProductMap[category] = arrayList
                }
                editHeadlineAdModel.selectedTopAdsProductMap = selectedProductMap
            }
            editHeadlineAdModel.selectedTopAdsProducts = it.product as ArrayList<TopAdsProductModel>
        }
        return editHeadlineAdModel
    }

    fun saveProductData(stepperModel: HeadlineAdStepperModel) {
        stepperModel.adOperations.firstOrNull()?.ad?.id = adId.toString()
        val tempHeadlineAdStepperModel = editHeadlineAdLiveData.value
        tempHeadlineAdStepperModel?.let {
            it.slogan = stepperModel.slogan
            it.selectedProductIds = stepperModel.selectedProductIds
            it.adOperations = stepperModel.adOperations
            getBidInfoDetail(it)
        }
    }

    fun saveKeywordOperation(stepperModel: HeadlineAdStepperModel) {
        editHeadlineAdLiveData.value?.let {
            it.adBidPrice = stepperModel.adBidPrice
            it.keywordOperations = stepperModel.keywordOperations
        }
    }

    fun saveOtherDetails(stepperModel: HeadlineAdStepperModel) {
        editHeadlineAdLiveData.value?.let {
            it.startDate = stepperModel.startDate
            it.endDate = stepperModel.endDate
            it.groupName = stepperModel.groupName
            it.dailyBudget = stepperModel.dailyBudget
            it.adOperations.firstOrNull()?.ad?.title = stepperModel.groupName
        }
    }

    fun getStatus(): String {
        return if (status == "1") {
            STATUS_PUBLISHED
        } else {
            STATUS_INACTIVE
        }
    }
}