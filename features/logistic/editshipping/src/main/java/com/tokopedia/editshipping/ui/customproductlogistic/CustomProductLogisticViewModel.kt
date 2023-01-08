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

    fun getCPLList(shopId: Long, productId: String, shipperServicesIds: List<Long>?) {
        viewModelScope.launch {
            try {
                val cplList = repo.getCPLList(shopId, productId)
                _cplList.value =
                    Success(mapper.mapCPLData(cplList.response.data, productId, shipperServicesIds))
            } catch (e: Throwable) {
                _cplList.value = Fail(e)
            }
        }
    }
}