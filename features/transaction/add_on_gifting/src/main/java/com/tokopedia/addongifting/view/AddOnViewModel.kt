package com.tokopedia.addongifting.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addongifting.domain.usecase.GetAddOnByProductUseCase
import com.tokopedia.addongifting.domain.usecase.GetAddOnSavedStateUseCase
import com.tokopedia.addongifting.view.uimodel.AddOnUiModel
import com.tokopedia.addongifting.view.uimodel.ProductUiModel
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnProductData
import javax.inject.Inject

class AddOnViewModel @Inject constructor(executorDispatchers: CoroutineDispatchers,
                                         private val getAddOnByProductUseCase: GetAddOnByProductUseCase,
                                         private val getAddOnSavedStateUseCase: GetAddOnSavedStateUseCase)
    : BaseViewModel(executorDispatchers.main) {

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
                    loadSavedStateData()
                },
                onError = {

                }
        )
    }

    fun loadSavedStateData() {
        getAddOnSavedStateUseCase.execute(
                onSuccess = {

                },
                onError = {

                }
        )
    }

}