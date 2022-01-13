package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class DigitalPDPDataPlanViewModel @Inject constructor(
    val repo: DigitalPDPRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    val observableDenomData: LiveData<Result<DenomWidgetModel>>
    get() = _observableDenomData

    private val _observableDenomData = MutableLiveData<Result<DenomWidgetModel>>()


    fun getRechargeCatalogInput(menuId: Int, operator: String){
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val denomGrid = repo.getCatalogRepository(menuId, operator)
            _observableDenomData.postValue(Success(denomGrid))
        }){
            _observableDenomData.postValue(Fail(it))
        }
    }
}