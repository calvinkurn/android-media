package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.topads.common.data.response.SingleAd
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.domain.usecase.GetAdKeywordUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TYPE_BANNER = "banner"

class SharedEditHeadlineViewModel @Inject constructor(
        private val topAdsGetGroupProductUseCase: TopAdsGetGroupProductDataUseCase,
        private val topAdsGetPromoUseCase: TopAdsGetPromoUseCase,
        private val getAdKeywordUseCase: GetAdKeywordUseCase
) : ViewModel() {

    private var groupItemLiveData: MutableLiveData<WithoutGroupDataItem> = MutableLiveData()
    private var headlineAdDetailLiveData: MutableLiveData<SingleAd> = MutableLiveData()

    fun getGroupItemLiveData(): LiveData<WithoutGroupDataItem> = groupItemLiveData
    fun getHeadlineAdDetailLiveData(): LiveData<SingleAd> = headlineAdDetailLiveData

    fun getHeadlineAdId(groupId: Int, shopId: Int) {
        viewModelScope.launch {
            topAdsGetGroupProductUseCase.setParams(groupId, 0, "", "", null, "", "", shopId, TYPE_BANNER)
            topAdsGetGroupProductUseCase.executeQuerySafeMode(
                    {
                        if (it.data.isNotEmpty()) {
                            groupItemLiveData.value = it.data.first()
                        }
                    },
                    {
                        it.printStackTrace()
                    }
            )

        }
    }

    fun getHeadlineAdDetail(adId: Int, shopId: Int, onError: (message: String) -> Unit) {
        viewModelScope.launch {
            topAdsGetPromoUseCase.setParams(adId, shopId)
            topAdsGetPromoUseCase.execute(
                    {
                        if (it.topAdsGetPromo.data.isNotEmpty()) {

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

    fun getAdKeyword(adId: Int, cursor: String, shopId: Int) {
        viewModelScope.launch {
            getAdKeywordUseCase.setParams(adId, cursor, shopId)
            getAdKeywordUseCase.execute(
                    {

                    },
                    {

                    }
            )
        }
    }
}