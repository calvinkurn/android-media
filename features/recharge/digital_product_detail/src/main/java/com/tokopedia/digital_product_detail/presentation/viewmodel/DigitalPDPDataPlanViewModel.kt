package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import javax.inject.Inject

class DigitalPDPDataPlanViewModel @Inject constructor(
    val repo: DigitalPDPRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    val observableDenomData: LiveData<RechargeNetworkResult<DenomWidgetModel>>
    get() = _observableDenomData

    private val _observableDenomData = MutableLiveData<RechargeNetworkResult<DenomWidgetModel>>()

    fun setInital(){
        _observableDenomData.postValue(RechargeNetworkResult.Loading)
    }

    fun getRechargeCatalogInput(menuId: Int, operator: String){
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val denomGrid = repo.getDenomGridList(menuId, operator)
            _observableDenomData.postValue(RechargeNetworkResult.Success(denomGrid.denomWidgetModel))
        }){
            _observableDenomData.postValue(RechargeNetworkResult.Fail(it))
        }
    }
}