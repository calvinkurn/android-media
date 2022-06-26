package com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.WarehouseUiModelMapper
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel
import javax.inject.Inject

class EditProductInfoViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private var _warehouseList = MutableLiveData<List<WarehouseUiModel>>()
    val warehouseList: LiveData<List<WarehouseUiModel>>
        get() = _warehouseList

    private var _errorThrowable = MutableLiveData<Throwable>()
    val errorThrowable: LiveData<Throwable>
        get() = _errorThrowable

    val hasWarehouse = Transformations.map(warehouseList) {
        it.size > 1
    }

    fun setWarehouseList(warehouseList: List<SellerCampaignProductList.WarehouseData>) {
        _warehouseList.postValue(WarehouseUiModelMapper.map(warehouseList))
    }

}