package com.tokopedia.addongifting.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addongifting.data.response.GetAddOnByProductResponse
import com.tokopedia.addongifting.domain.usecase.GetAddOnByProductUseCase
import com.tokopedia.addongifting.domain.usecase.GetAddOnSavedStateUseCase
import com.tokopedia.addongifting.view.uimodel.AddOnUiModel
import com.tokopedia.addongifting.view.uimodel.ProductUiModel
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnProductData
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

class AddOnViewModel @Inject constructor(executorDispatchers: CoroutineDispatchers,
                                         private val getAddOnByProductUseCase: GetAddOnByProductUseCase,
                                         private val getAddOnSavedStateUseCase: GetAddOnSavedStateUseCase)
    : BaseViewModel(executorDispatchers.main) {

    // Global event
    private val _globalEvent = SingleLiveEvent<GlobalEvent>()
    val globalEvent: LiveData<GlobalEvent>
        get() = _globalEvent

    // Product Data
    private val _productData = MutableLiveData<ProductUiModel>()
    val productData: LiveData<ProductUiModel>
        get() = _productData

    // Add On Data
    private val _addOnData = MutableLiveData<AddOnUiModel>()
    val addOnData: LiveData<AddOnUiModel>
        get() = _addOnData

    fun loadAddOnData(addOnProductData: AddOnProductData, mockAddOnResponse: String = "", mockAddOnSavedStateResponse: String = "") {
        getAddOnByProductUseCase.mockResponse = mockAddOnResponse
        getAddOnByProductUseCase.execute(
                onSuccess = {
                    loadSavedStateData(addOnProductData, it, mockAddOnSavedStateResponse)
                },
                onError = {
                    Timber.d("Error")
                }
        )
    }

    private fun loadSavedStateData(addOnProductData: AddOnProductData, addOnByProductResponse: GetAddOnByProductResponse, mockAddOnSavedStateResponse: String = "") {
        getAddOnSavedStateUseCase.mockResponse = mockAddOnSavedStateResponse
        getAddOnSavedStateUseCase.execute(
                onSuccess = {
                    val productUiModel = UiModelMapper.mapProduct(addOnProductData, addOnByProductResponse)
                    _productData.value = productUiModel
                    val addOnUiModel = UiModelMapper.mapAddOn(addOnProductData, addOnByProductResponse, it)
                    _addOnData.value = addOnUiModel
                },
                onError = {
                    Timber.d("Error")
                }
        )
    }

}