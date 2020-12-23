package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.top_ads_headline.data.Category
import com.tokopedia.top_ads_headline.data.HeadlineAdStepperModel
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.SingleAd
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TYPE_BANNER = "banner"

class SharedEditHeadlineViewModel @Inject constructor(
        private val topAdsGetGroupProductUseCase: TopAdsGetGroupProductDataUseCase,
        private val topAdsGetPromoUseCase: TopAdsGetPromoUseCase,
        private val bidInfoUseCase: BidInfoUseCase) : ViewModel() {

    private val editHeadlineAdLiveData: MutableLiveData<HeadlineAdStepperModel> = MutableLiveData()
    private val bidInfoData:MutableLiveData<TopadsBidInfo.DataItem> = MutableLiveData()


    fun getEditHeadlineAdLiveData(): LiveData<HeadlineAdStepperModel> = editHeadlineAdLiveData

    fun getBidInfoData():LiveData<TopadsBidInfo.DataItem> = bidInfoData

    private fun getBidInfoDetail() {
        viewModelScope.launch {
            val dummyId: MutableList<Int> = mutableListOf()
            val suggestionsDefault = java.util.ArrayList<DataSuggestions>()
            suggestionsDefault.add(DataSuggestions(ParamObject.PRODUCT, dummyId))
            bidInfoUseCase.setParams(suggestionsDefault, ParamObject.HEADLINE, ParamObject.EDIT_HEADLINE_PAGE)
            bidInfoUseCase.executeQuerySafeMode({ result ->
                result.topadsBidInfo.data.firstOrNull()?.let {
                    bidInfoData.value = it
                }
            }, {
                it.printStackTrace()
            })
        }
    }

    fun getHeadlineAdId(groupId: Int, shopId: Int, onError: (message: String) -> Unit) {
        viewModelScope.launch {
            topAdsGetGroupProductUseCase.setParams(groupId, 0, "", "", null, "", "", shopId, TYPE_BANNER)
            topAdsGetGroupProductUseCase.executeQuerySafeMode(
                    {
                        if (it.data.isNotEmpty()) {
                            getHeadlineAdDetail(it.data.first().adId.toString(), shopId.toString(), onError)
                        }
                    },
                    {
                        onError(it.message ?: "")
                        it.printStackTrace()
                    }
            )

        }
    }

    private fun getHeadlineAdDetail(adId: String, shopId: String, onError: (message: String) -> Unit) {
        viewModelScope.launch {
            topAdsGetPromoUseCase.setParams(adId, shopId)
            topAdsGetPromoUseCase.execute(
                    {
                        if (it.topAdsGetPromo.data.isNotEmpty()) {
                            setEditAdHeadlineData(it.topAdsGetPromo.data.first())
                        } else if (it.topAdsGetPromo.errors.isNotEmpty()) {
                            onError(it.topAdsGetPromo.errors.first().detail)
                        }
                        getBidInfoDetail()
                    },
                    {
                        onError(it.message ?: "")
                        it.printStackTrace()
                    }
            )
        }
    }

    private fun setEditAdHeadlineData(ad: SingleAd) {
        val editHeadlineAdModel = HeadlineAdStepperModel()
        editHeadlineAdModel.minBid = ad.priceBid
        editHeadlineAdModel.startDate = "${ad.adStartDate} ${ad.adStartTime}"
        editHeadlineAdModel.endDate = "${ad.adEndDate} ${ad.adEndTime}"
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
        editHeadlineAdLiveData.value = editHeadlineAdModel
    }

}