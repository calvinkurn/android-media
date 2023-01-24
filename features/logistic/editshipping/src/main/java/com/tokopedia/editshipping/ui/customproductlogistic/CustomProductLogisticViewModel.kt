package com.tokopedia.editshipping.ui.customproductlogistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticCommon.data.mapper.CustomProductLogisticMapper
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.logisticCommon.data.repository.CustomProductLogisticUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomProductLogisticViewModel @Inject constructor(
    private val getCplList: CustomProductLogisticUseCase,
    private val mapper: CustomProductLogisticMapper
) :
    ViewModel() {

    var cplData: CustomProductLogisticModel = CustomProductLogisticModel()
    private val _cplState = MutableLiveData<CPLState>()
    val cplState: LiveData<CPLState>
        get() = _cplState

    fun getCPLList(
        shopId: Long,
        productId: Long?,
        shipperServicesIds: List<Long>?,
        cplParam: List<Long>?,
        shouldShowOnBoarding: Boolean = false
    ) {
        viewModelScope.launch {
            try {
                _cplState.value = CPLState.Loading
                val param = getCplList.getParam(shopId, productId, cplParam)
                val cplList = getCplList(param)
                cplData = mapper.mapCPLData(
                    cplList.response.data,
                    shipperServicesIds,
                    shouldShowOnBoarding
                )
                _cplState.value =
                    CPLState.FirstLoad(cplData)
            } catch (e: Throwable) {
                _cplState.value = CPLState.Failed(e)
            }
        }
    }

    fun setAllShipperServiceState(active: Boolean, shipperId: Long) {
        cplData.shipperList.forEach { shipperGroup ->
            val selectedShipper =
                shipperGroup.shipper.find { s -> s.shipperId == shipperId }
            selectedShipper?.let { s ->
                selectedShipper.isActive = active
                s.shipperProduct.forEach { sp -> sp.isActive = active }
                _cplState.value = CPLState.Update(selectedShipper, shipperGroup.header)
                return
            }
        }
    }

    fun setShipperServiceState(active: Boolean, shipperProductId: Long) {
        cplData.shipperList.forEach { shipperGroup ->
            for (s in shipperGroup.shipper) {
                for (sp in s.shipperProduct) {
                    if (sp.shipperProductId == shipperProductId) {
                        sp.isActive = active
                        val lastActiveState = s.isActive
                        s.isActive = s.shipperProduct.any { allSp -> allSp.isActive }
                        if (lastActiveState != s.isActive) {
                            _cplState.value = CPLState.Update(s, shipperGroup.header)
                        }
                        return
                    }
                }
            }
        }

    }

    fun setWhitelabelServiceState(spIds: List<Long>, check: Boolean) {
        cplData.shipperList.forEach { shipperGroup ->
            val whitelabelShippers =
                shipperGroup.shipper.filter { shipper -> shipper.isWhitelabel }
            for (service in whitelabelShippers) {
                val shipperProductIds = service.shipperProduct.map { sp -> sp.shipperProductId }
                if (spIds.containsAll(shipperProductIds)) {
                    service.isActive = check
                    service.shipperProduct.forEach { sp -> sp.isActive = check }
                    return
                }
            }
        }
    }

    fun setAlreadyShowOnBoarding() {
        cplData.shouldShowOnBoarding = false
    }

}
