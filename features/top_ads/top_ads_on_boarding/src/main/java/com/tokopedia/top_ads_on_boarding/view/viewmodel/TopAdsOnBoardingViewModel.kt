package com.tokopedia.top_ads_on_boarding.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.domain.model.GetVariantByIdResponse.GetVariantById.ExperimentVariant
import com.tokopedia.topads.common.domain.usecase.GetVariantByIdUseCase
import javax.inject.Inject

class TopAdsOnBoardingViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getVariantByIdUseCase: GetVariantByIdUseCase,
) : BaseViewModel(dispatchers.io) {

    private val _shopVariant = MutableLiveData<List<ExperimentVariant>>()
    val shopVariant: LiveData<List<ExperimentVariant>>
        get() = _shopVariant

    fun getVariantById() {
        launchCatchError(dispatchers.io, {
            val data = getVariantByIdUseCase()
            _shopVariant.postValue(data.getVariantById.shopIdVariants)
        }, {
            _shopVariant.postValue(listOf())
        })
    }
}
