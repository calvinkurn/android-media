package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPPaketDataRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recharge_component.model.denom.DenomMCCMModel
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import javax.inject.Inject

class DigitalPDPDataPlanViewModel @Inject constructor(
    val repo: DigitalPDPPaketDataRepository,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val _filterData = ArrayList<HashMap<String, Any>>()

    private val _observableDenomMCCMData = MutableLiveData<RechargeNetworkResult<InputMultiTabDenomModel>>()
    val observableDenomMCCMData: LiveData<RechargeNetworkResult<InputMultiTabDenomModel>>
        get() = _observableDenomMCCMData

    private val _menuDetailData = MutableLiveData<RechargeNetworkResult<MenuDetailModel>>()
    val menuDetailData: LiveData<RechargeNetworkResult<MenuDetailModel>>
        get() = _menuDetailData


    fun addFilter(paramName: String, listKey: ArrayList<String>){
        val valueItem = HashMap<String, Any>()
        valueItem[FILTER_PARAM_NAME] = paramName
        valueItem[FILTER_VALUE] = listKey
        _filterData.add(valueItem)
    }

    fun getRechargeCatalogInputMultiTab(menuId: Int, operator: String, clientNumber: String){
        _observableDenomMCCMData.postValue(RechargeNetworkResult.Loading)
        launchCatchError(block = {
            val denomGrid = repo.getProductInputMultiTabDenom(menuId, operator, clientNumber, _filterData)
            _observableDenomMCCMData.postValue(RechargeNetworkResult.Success(denomGrid))
        }){
            _observableDenomMCCMData.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun getMenuDetail(menuId: Int, isLoadFromCloud: Boolean = false) {
        _menuDetailData.postValue(RechargeNetworkResult.Loading)
        viewModelScope.launchCatchError(dispatchers.io, block = {
            val menuDetail = repo.getMenuDetail(menuId, isLoadFromCloud, true)
            _menuDetailData.postValue(RechargeNetworkResult.Success(menuDetail))
        }) {
            _menuDetailData.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    private fun removeFilter(paramName: String) {
        val iterator = _filterData.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.containsValue(paramName)) {
                iterator.remove()
            }
        }
    }

    companion object {
        const val FILTER_PARAM_NAME = "param_name"
        const val FILTER_VALUE = "value"
    }
}