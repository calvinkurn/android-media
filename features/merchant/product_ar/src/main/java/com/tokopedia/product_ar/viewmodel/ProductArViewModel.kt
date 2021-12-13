package com.tokopedia.product_ar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.product_ar.di.PRODUCT_ID_PROVIDED
import com.tokopedia.product_ar.di.SHOP_ID_PROVIDED
import com.tokopedia.product_ar.model.ProductAr
import com.tokopedia.product_ar.usecase.GetProductArUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject
import javax.inject.Named


class ProductArViewModel @Inject constructor(dispatchers: CoroutineDispatchers,
                                             @Named(PRODUCT_ID_PROVIDED) private val productId: String,
                                             @Named(SHOP_ID_PROVIDED) private val shopId: String,
                                             private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
                                             private val getProductArUseCase: GetProductArUseCase)
    : BaseViewModel(dispatchers.io) {

    init {
        getArData()
    }

    private val _selectedProductArData = MutableLiveData<Result<ProductAr>>()
    val selectedProductArData: LiveData<Result<ProductAr>>
        get() = _selectedProductArData


    private fun getArData() {
        viewModelScope.launchCatchError(block = {
            val result = getProductArUseCase.executeOnBackground(
                    GetProductArUseCase.createParams(productId, shopId, chosenAddressRequestHelper)
            )
            _selectedProductArData.postValue(Success(result.options[productId] ?: ProductAr()))
        }, onError = {
            _selectedProductArData.postValue(Fail(it))
        })
    }
}