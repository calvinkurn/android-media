package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.topads.common.domain.usecase.GetAdKeywordUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditFormHeadlineViewModel @Inject constructor(
        private val topAdsGetPromoUseCase: TopAdsGetPromoUseCase,
        private val getAdKeywordUseCase: GetAdKeywordUseCase
) : ViewModel() {

    fun getHeadlineAdDetail(adId:Int, shopId:Int){
        viewModelScope.launch {
            topAdsGetPromoUseCase.setParams(adId, shopId)
            topAdsGetPromoUseCase.execute(
                    {

                    },
                    {

                    }
            )
        }
    }

    fun getAdKeyword(adId:Int, cursor:String, shopId: Int){
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