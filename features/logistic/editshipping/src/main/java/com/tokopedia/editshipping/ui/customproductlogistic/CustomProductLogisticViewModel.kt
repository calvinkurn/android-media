package com.tokopedia.editshipping.ui.customproductlogistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticCommon.data.mapper.CustomProductLogisticMapper
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.logisticCommon.data.repository.CustomProductLogisticRepository
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomProductLogisticViewModel @Inject constructor(
    private val repo: CustomProductLogisticRepository,
    private val mapper: CustomProductLogisticMapper
) :
    ViewModel() {

    private val _cplList = MutableLiveData<Result<CustomProductLogisticModel>>()
    val cplList: LiveData<Result<CustomProductLogisticModel>>
        get() = _cplList

    fun getCPLList(
        shopId: Long,
        productId: String,
        shipperServicesIds: List<Long>?,
        cplParam: List<Long>?
    ) {
        viewModelScope.launch {
            try {
                val cplList = repo.getCPLList(shopId, productId, cplParam ?: listOf())
                _cplList.value =
                    Success(mapper.mapCPLData(cplList.response.data, productId, shipperServicesIds))
            } catch (e: Throwable) {
                _cplList.value = Fail(e)
            }
        }
    }

    fun isShipperGroupAvailable(shipperGroupIndex: Int): Boolean {
        _cplList.value.let {
            if (it is Success) {
                return it.data.shipperList.getOrNull(shipperGroupIndex)?.shipper?.isNotEmpty()
                    ?: false
            }
        }
        return false
    }

    fun getActivatedProductIds(): List<Int> {
        val shipperProductIds = mutableListOf<Int>()
        _cplList.value.let {
            if (it is Success) {
                it.data.shipperList.forEach { shipperGroup ->
                    shipperGroup.shipper.forEach { s ->
                        s.shipperProduct.forEach { sp ->
                            if (sp.isActive) {
                                shipperProductIds.add(sp.shipperProductId.toInt())
                            }
                        }
                    }
                }
            }
        }
        return shipperProductIds
    }

    fun setAllShipperServiceState(active: Boolean, shipperId: Long) {
        _cplList.value.let {
            if (it is Success) {
                run loop@{
                    it.data.shipperList.forEach { shipperGroup ->
                        val selectedShipper =
                            shipperGroup.shipper.find { s -> s.shipperId == shipperId }
                        selectedShipper?.let { s ->
                            selectedShipper.isActive = active
                            s.shipperProduct.forEach { sp -> sp.isActive = active }
                            return@loop
                        }
                    }
                }
                _cplList.value = it
            }
        }
    }

    fun setShipperServiceState(active: Boolean, shipperProductId: Long) {
        _cplList.value.let {
            if (it is Success) {
                run loop@{
                    it.data.shipperList.forEach { shipperGroup ->
                        for (s in shipperGroup.shipper) {
                            for (sp in s.shipperProduct) {
                                if (sp.shipperProductId == shipperProductId) {
                                    sp.isActive = active
                                    s.isActive = s.shipperProduct.any { allSp -> allSp.isActive }
                                    return@loop
                                }
                            }
                        }
                    }
                }
                _cplList.value = it
            }
        }
    }
}
