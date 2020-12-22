package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import javax.inject.Inject

class EditAdOthersViewModel @Inject constructor(
        private val topAdsGroupValidateNameUseCase: TopAdsGroupValidateNameUseCase,
        private val bidInfoUseCase: BidInfoUseCase
) : ViewModel() {

    private val bidInfoData: MutableLiveData<TopadsBidInfo.DataItem> = MutableLiveData()

    fun validateGroup(adName: String, shopId: Int, onSuccess: (() -> Unit), onError: ((String) -> Unit)) {
        viewModelScope.launchCatchError(
                block = {
                    topAdsGroupValidateNameUseCase.setParams(shopId, adName)
                    val response = topAdsGroupValidateNameUseCase.executeOnBackground()
                    if (response.topAdsGroupValidateName.errors.isEmpty()) {
                        onSuccess()
                    } else {
                        onError(response.topAdsGroupValidateName.errors[0].detail)
                    }
                },
                onError = {
                    it.printStackTrace()
                    onError(it.message.toString())
                }
        )
    }
}