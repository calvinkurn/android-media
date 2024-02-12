package com.tokopedia.top_ads_headline.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.domain.model.GetVariantByIdResponse
import com.tokopedia.topads.common.domain.usecase.CreateHeadlineAdsUseCase
import com.tokopedia.topads.common.domain.model.createheadline.TopAdsManageHeadlineInput
import com.tokopedia.topads.common.domain.model.createheadline.TopadsManageHeadlineAdResponse
import com.tokopedia.topads.common.domain.usecase.GetVariantByIdUseCase
import javax.inject.Inject

class AdScheduleAndBudgetViewModel @Inject constructor(
    private val createHeadlineAdsUseCase: CreateHeadlineAdsUseCase,
    private val dispatcher: CoroutineDispatchers,
    private val getVariantByIdUseCase: GetVariantByIdUseCase,
) : ViewModel() {

    private val _shopVariant = MutableLiveData<List<GetVariantByIdResponse.GetVariantById.ExperimentVariant>>()
    val shopVariant: LiveData<List<GetVariantByIdResponse.GetVariantById.ExperimentVariant>>
        get() = _shopVariant

    fun createHeadlineAd(input: TopAdsManageHeadlineInput, onSuccess: ((id: String) -> Unit),
                         onError: ((String) -> Unit)) {
        viewModelScope.launchCatchError(
                block = {
                    createHeadlineAdsUseCase.setParams(input)
                    val response : TopadsManageHeadlineAdResponse.Data = createHeadlineAdsUseCase.executeOnBackground()
                    if (response.topadsManageHeadlineAd.success.id.isNotEmpty()) {
                        onSuccess(response.topadsManageHeadlineAd.success.id)
                    } else {
                        onError(response.topadsManageHeadlineAd.errors.first().detail)
                    }
                },
                onError = {
                    onError(it.message ?: "")
                    it.printStackTrace()
                }
        )
    }

    fun getVariantById() {
        viewModelScope.launchCatchError(dispatcher.io, {
            val data = getVariantByIdUseCase()
            _shopVariant.postValue(data.getVariantById.shopIdVariants)
        }, {
            _shopVariant.postValue(listOf())
        })
    }
}
