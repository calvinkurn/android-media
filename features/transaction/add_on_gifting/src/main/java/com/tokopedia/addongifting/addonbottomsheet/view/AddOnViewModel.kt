package com.tokopedia.addongifting.addonbottomsheet.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.GetAddOnByProductResponse
import com.tokopedia.addongifting.addonbottomsheet.domain.usecase.GetAddOnByProductUseCase
import com.tokopedia.addongifting.addonbottomsheet.domain.usecase.GetAddOnSavedStateUseCase
import com.tokopedia.addongifting.addonbottomsheet.domain.usecase.SaveAddOnStateUseCase
import com.tokopedia.addongifting.addonbottomsheet.view.mapper.AddOnUiModelMapper
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.AddOnUiModel
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.FragmentUiModel
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.ProductUiModel
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnProductData
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class AddOnViewModel @Inject constructor(executorDispatchers: CoroutineDispatchers,
                                         private val getAddOnByProductUseCase: GetAddOnByProductUseCase,
                                         private val getAddOnSavedStateUseCase: GetAddOnSavedStateUseCase,
                                         private val saveAddOnStateUseCase: SaveAddOnStateUseCase)
    : BaseViewModel(executorDispatchers.main) {

    // Global Event
    private val _globalEvent = SingleLiveEvent<GlobalEvent>()
    val globalEvent: LiveData<GlobalEvent>
        get() = _globalEvent

    // Fragment / Global Data
    private val _fragmentUiModel = MutableLiveData<FragmentUiModel>()
    val fragmentUiModel: LiveData<FragmentUiModel>
        get() = _fragmentUiModel

    // Product Data
    private val _productUiModel = MutableLiveData<ProductUiModel>()
    val productUiModel: LiveData<ProductUiModel>
        get() = _productUiModel

    // Add On Data
    private val _addOnUiModel = MutableLiveData<AddOnUiModel>()
    val addOnUiModel: LiveData<AddOnUiModel>
        get() = _addOnUiModel

    fun loadAddOnData(addOnProductData: AddOnProductData, mockAddOnResponse: String? = "", mockAddOnSavedStateResponse: String? = "") {
        getAddOnByProductUseCase.mockResponse = mockAddOnResponse ?: ""
        getAddOnByProductUseCase.execute(
                onSuccess = {
                    // Todo : adjust error validation
                    if (it.dataResponse.error.message.isBlank()) {
                        loadSavedStateData(addOnProductData, it, mockAddOnSavedStateResponse)
                    } else {
                        throw ResponseErrorException(it.dataResponse.error.message)
                    }
                },
                onError = {
                    _globalEvent.value = GlobalEvent().apply {
                        state = GlobalEvent.STATE_FAILED_LOAD_ADD_ON_DATA
                        throwable = it
                    }
                }
        )
    }

    private fun loadSavedStateData(addOnProductData: AddOnProductData, addOnByProductResponse: GetAddOnByProductResponse, mockAddOnSavedStateResponse: String? = "") {
        getAddOnSavedStateUseCase.mockResponse = mockAddOnSavedStateResponse ?: ""
        getAddOnSavedStateUseCase.execute(
                onSuccess = {
                    if (it.getAddOns.errorMessage.firstOrNull()?.isNotBlank() == true) {
                        throw ResponseErrorException(it.getAddOns.errorMessage.joinToString(". "))
                    } else {
                        _globalEvent.value = GlobalEvent().apply {
                            state = GlobalEvent.STATE_SUCCESS_LOAD_ADD_ON_DATA
                        }
                        val productUiModel = AddOnUiModelMapper.mapProduct(addOnProductData, addOnByProductResponse)
                        _productUiModel.value = productUiModel
                        val addOnUiModel = AddOnUiModelMapper.mapAddOn(addOnProductData, addOnByProductResponse, it)
                        _addOnUiModel.value = addOnUiModel
                    }
                },
                onError = {
                    _globalEvent.value = GlobalEvent().apply {
                        state = GlobalEvent.STATE_SUCCESS_LOAD_ADD_ON_DATA
                    }
                    val productUiModel = AddOnUiModelMapper.mapProduct(addOnProductData, addOnByProductResponse)
                    _productUiModel.value = productUiModel
                    val addOnUiModel = AddOnUiModelMapper.mapAddOn(addOnProductData, addOnByProductResponse)
                    _addOnUiModel.value = addOnUiModel
                }
        )
    }

    fun saveAddOnState(mockSaveStateResponse: String? = null) {
        saveAddOnStateUseCase.mockResponse = mockSaveStateResponse ?: ""
        saveAddOnStateUseCase.execute(
                onSuccess = {
                    if (it.getAddOns.errorMessage.firstOrNull()?.isNotBlank() == true) {
                        throw ResponseErrorException(it.getAddOns.errorMessage.joinToString(". "))
                    } else {
                        _globalEvent.value = GlobalEvent().apply {
                            state = GlobalEvent.STATE_SUCCESS_SAVE_ADD_ON
                            data = it
                        }
                    }
                },
                onError = {
                    _globalEvent.value = GlobalEvent().apply {
                        state = GlobalEvent.STATE_FAILED_SAVE_ADD_ON
                    }
                }
        )
    }

    fun updateFragmentUiModel(addOnUiModel: AddOnUiModel) {
        _fragmentUiModel.value = FragmentUiModel().apply {
            if (addOnUiModel.isAddOnSelected) {
                addOnTotalPrice = addOnUiModel.addOnPrice
                addOnTotalQuantity = 1
            } else {
                addOnTotalPrice = 0
                addOnTotalQuantity = 0
            }
        }
    }

    fun validateCloseAction() {
        addOnUiModel.value?.let {
            if (it.initialAddOnNote != it.addOnNote || it.initialAddOnNoteFrom != it.addOnNoteFrom || it.initialAddOnNoteTo != it.addOnNoteTo) {
                _globalEvent.value = GlobalEvent().apply {
                    state = GlobalEvent.STATE_SHOW_CLOSE_DIALOG_CONFIRMATION
                }
            } else {
                _globalEvent.value = GlobalEvent().apply {
                    state = GlobalEvent.STATE_DISMISS_BOTTOM_SHEET
                }
            }
        }
    }
}