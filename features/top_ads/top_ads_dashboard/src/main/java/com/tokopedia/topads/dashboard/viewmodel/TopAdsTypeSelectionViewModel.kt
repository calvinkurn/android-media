package com.tokopedia.topads.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.domain.model.GetVariantByIdResponse
import com.tokopedia.topads.common.domain.usecase.GetVariantByIdUseCase
import javax.inject.Inject

class TopAdsTypeSelectionViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getVariantByIdUseCase: GetVariantByIdUseCase,
) : BaseViewModel(dispatchers.io) {

    private val _shopVariant =
        MutableLiveData<List<GetVariantByIdResponse.GetVariantById.ExperimentVariant>>()
    val shopVariant: LiveData<List<GetVariantByIdResponse.GetVariantById.ExperimentVariant>>
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
