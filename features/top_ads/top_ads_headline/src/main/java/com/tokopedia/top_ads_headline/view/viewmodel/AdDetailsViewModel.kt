package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import javax.inject.Inject

class AdDetailsViewModel @Inject constructor(
        private val topAdsGroupValidateNameUseCase: TopAdsGroupValidateNameUseCase
) : ViewModel(){

    fun validateGroup(adName: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launchCatchError(
                block = {
                    topAdsGroupValidateNameUseCase.setParams(adName)
                    val response = topAdsGroupValidateNameUseCase.executeOnBackground()
                    if (response.topAdsGroupValidateName.errors.isEmpty()){
                        onSuccess()
                    } else{
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