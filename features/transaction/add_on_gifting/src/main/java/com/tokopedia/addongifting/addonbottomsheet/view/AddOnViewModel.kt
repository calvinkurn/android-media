package com.tokopedia.addongifting.addonbottomsheet.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.*
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonsavedstate.GetAddOnSavedStateResponse
import com.tokopedia.addongifting.addonbottomsheet.domain.mapper.AddOnRequestMapper
import com.tokopedia.addongifting.addonbottomsheet.domain.usecase.GetAddOnByProductUseCase
import com.tokopedia.addongifting.addonbottomsheet.domain.usecase.GetAddOnSavedStateUseCase
import com.tokopedia.addongifting.addonbottomsheet.view.mapper.AddOnUiModelMapper
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.AddOnUiModel
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.FragmentUiModel
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.ProductUiModel
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.TotalAmountUiModel
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnStateResponse
import com.tokopedia.purchase_platform.common.feature.addons.domain.SaveAddOnStateUseCase
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnProductData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddOnViewModel @Inject constructor(
    val executorDispatchers: CoroutineDispatchers,
    private val getAddOnByProductUseCase: GetAddOnByProductUseCase,
    private val getAddOnSavedStateUseCase: GetAddOnSavedStateUseCase,
    private val saveAddOnStateUseCase: SaveAddOnStateUseCase
) :
    BaseViewModel(executorDispatchers.main) {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var hasLoadData: Boolean = false
    var hasSavedState: Boolean = false

    private val _productUiModel = MutableStateFlow(ProductUiModel())
    private val _addOnUiModel = MutableStateFlow(AddOnUiModel())
    private val _totalAmountUiModel = MutableStateFlow(TotalAmountUiModel())

    val fragmentUiModel: Flow<FragmentUiModel> = combine(
        _productUiModel,
        _addOnUiModel,
        _totalAmountUiModel
    ) { productUiModel, addonUiModel, totalAmountUiModel ->
        FragmentUiModel().apply {
            hasLoadedData = hasLoadData
            recyclerViewItems = listOf(productUiModel, addonUiModel)
            totalAmount = totalAmountUiModel
        }
    }.flowOn(executorDispatchers.immediate)

    fun loadAddOnData(addOnProductData: AddOnProductData) {
        val params = AddOnRequestMapper.generateGetAddOnByProductRequestParams(addOnProductData)
        getAddOnByProductUseCase.setParams(params)
        getAddOnByProductUseCase.execute(
            onSuccess = {
                handleOnSuccessGetAddOnByProduct(it, addOnProductData)
            },
            onError = {
                handleOnErrorGetAddOnProduct(it)
            }
        )
    }

    private fun handleOnSuccessGetAddOnByProduct(
        getAddOnByProductResponse: GetAddOnByProductResponse,
        addOnProductData: AddOnProductData
    ) {
        if (getAddOnByProductResponse.dataResponse.error.errorCode.isBlank()) {
            loadSavedStateData(addOnProductData, getAddOnByProductResponse)
        } else {
            handleOnErrorGetAddOnProduct(ResponseErrorException(getAddOnByProductResponse.dataResponse.error.message))
        }
    }

    private fun handleOnErrorGetAddOnProduct(throwable: Throwable) {
        launch {
            _uiEvent.emit(
                UiEvent().apply {
                    state = UiEvent.STATE_FAILED_LOAD_ADD_ON_DATA
                    this.throwable = throwable
                }
            )
        }
    }

    private fun loadSavedStateData(
        addOnProductData: AddOnProductData,
        addOnByProductResponse: GetAddOnByProductResponse
    ) {
        val params = AddOnRequestMapper.generateGetAddOnSavedStateRequestParams(addOnProductData)
        getAddOnSavedStateUseCase.setParams(params)
        getAddOnSavedStateUseCase.execute(
            onSuccess = {
                handleOnSuccessGetAddOnSavedState(it, addOnProductData, addOnByProductResponse)
            },
            onError = {
                handleOnErrorGetAddOnSavedState(addOnProductData, addOnByProductResponse)
            }
        )
    }

    private fun handleOnSuccessGetAddOnSavedState(
        getAddOnSavedStateResponse: GetAddOnSavedStateResponse,
        addOnProductData: AddOnProductData,
        addOnByProductResponse: GetAddOnByProductResponse
    ) {
        if (getAddOnSavedStateResponse.getAddOns.errorMessage.firstOrNull()?.isNotBlank() == true) {
            handleOnErrorGetAddOnSavedState(addOnProductData, addOnByProductResponse)
        } else {
            prepareAddOnData(addOnProductData, addOnByProductResponse, getAddOnSavedStateResponse)
        }
    }

    private fun prepareAddOnData(
        addOnProductData: AddOnProductData,
        addOnByProductResponse: GetAddOnByProductResponse,
        getAddOnSavedStateResponse: GetAddOnSavedStateResponse? = null
    ) {
        hasLoadData = true
        if (AddOnUiModelMapper.hasSavedAddOn(addOnProductData, addOnByProductResponse, getAddOnSavedStateResponse)) {
            hasSavedState = true
        }
        launch {
            _uiEvent.emit(
                UiEvent().apply {
                    state = UiEvent.STATE_SUCCESS_LOAD_ADD_ON_DATA
                }
            )
            val productUiModel = AddOnUiModelMapper.mapProduct(addOnProductData, addOnByProductResponse)
            _productUiModel.emit(productUiModel)
            val addOnUiModel = AddOnUiModelMapper.mapAddOn(addOnProductData, addOnByProductResponse, getAddOnSavedStateResponse)
            _addOnUiModel.emit(addOnUiModel)
        }
    }

    private fun handleOnErrorGetAddOnSavedState(
        addOnProductData: AddOnProductData,
        addOnByProductResponse: GetAddOnByProductResponse
    ) {
        hasLoadData = true
        hasSavedState = false
        launch {
            _uiEvent.emit(
                UiEvent().apply {
                    state = UiEvent.STATE_SUCCESS_LOAD_ADD_ON_DATA
                }
            )
            val productUiModel = AddOnUiModelMapper.mapProduct(addOnProductData, addOnByProductResponse)
            _productUiModel.emit(productUiModel)
            val addOnUiModel = AddOnUiModelMapper.mapAddOn(addOnProductData, addOnByProductResponse)
            _addOnUiModel.emit(addOnUiModel)
        }
    }

    fun saveAddOnState(addOnProductData: AddOnProductData) {
        val addOnUiModel = _addOnUiModel.value
        val params = AddOnRequestMapper.generateSaveAddOnStateRequestParams(addOnProductData, addOnUiModel)
        saveAddOnStateUseCase.setParams(params, false)
        saveAddOnStateUseCase.execute(
            onSuccess = {
                handleOnSuccessSaveAddOnState(it)
            },
            onError = {
                handleOnErrorSaveAddOnState(it)
            }
        )
    }

    private fun handleOnSuccessSaveAddOnState(saveAddOnStateResponse: SaveAddOnStateResponse) {
        if (saveAddOnStateResponse.saveAddOns.errorMessage.firstOrNull()?.isNotBlank() == true) {
            handleOnErrorSaveAddOnState(ResponseErrorException(saveAddOnStateResponse.saveAddOns.errorMessage.first()))
        } else {
            launch {
                _uiEvent.emit(
                    UiEvent().apply {
                        state = UiEvent.STATE_SUCCESS_SAVE_ADD_ON
                        data = saveAddOnStateResponse
                    }
                )
            }
        }
    }

    private fun handleOnErrorSaveAddOnState(throwable: Throwable) {
        launch {
            _uiEvent.emit(
                UiEvent().apply {
                    state = UiEvent.STATE_FAILED_SAVE_ADD_ON
                    this.throwable = throwable
                }
            )
        }
    }

    fun updateFragmentUiModel(addOnUiModel: AddOnUiModel) {
        _addOnUiModel.value = addOnUiModel
        _totalAmountUiModel.value = TotalAmountUiModel().apply {
            if (addOnUiModel.isAddOnSelected) {
                addOnTotalPrice = addOnUiModel.addOnQty * addOnUiModel.addOnPrice
                addOnTotalQuantity = addOnUiModel.addOnQty
            } else {
                addOnTotalPrice = 0
                addOnTotalQuantity = 0
            }
        }
    }

    fun validateCloseAction() {
        launch {
            if (hasChangedState()) {
                _uiEvent.emit(
                    UiEvent().apply {
                        state = UiEvent.STATE_SHOW_CLOSE_DIALOG_CONFIRMATION
                    }
                )
            } else {
                _uiEvent.emit(
                    UiEvent().apply {
                        state = UiEvent.STATE_DISMISS_BOTTOM_SHEET
                    }
                )
            }
        }
    }

    private fun hasChangedState(): Boolean {
        _addOnUiModel.value.let {
            return if (hasSavedState) {
                it.initialSelectedState != it.isAddOnSelected ||
                    it.initialAddOnNote != it.addOnNote ||
                    it.initialAddOnNoteTo != it.addOnNoteTo ||
                    it.initialAddOnNoteFrom != it.addOnNoteFrom
            } else {
                it.initialAddOnNote != it.addOnNote ||
                    it.initialAddOnNoteTo != it.addOnNoteTo ||
                    it.initialAddOnNoteFrom != it.addOnNoteFrom
            }
        }
    }
}
