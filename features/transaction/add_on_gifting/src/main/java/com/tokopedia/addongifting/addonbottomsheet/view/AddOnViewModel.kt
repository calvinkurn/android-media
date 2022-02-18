package com.tokopedia.addongifting.addonbottomsheet.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.*
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonsavedstate.GetAddOnSavedStateRequest
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonsavedstate.GetAddOnSavedStateResponse
import com.tokopedia.addongifting.addonbottomsheet.data.saveaddonstate.*
import com.tokopedia.addongifting.addonbottomsheet.domain.usecase.GetAddOnByProductUseCase
import com.tokopedia.addongifting.addonbottomsheet.domain.usecase.GetAddOnSavedStateUseCase
import com.tokopedia.addongifting.addonbottomsheet.domain.usecase.SaveAddOnStateUseCase
import com.tokopedia.addongifting.addonbottomsheet.view.mapper.AddOnUiModelMapper
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.AddOnUiModel
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.FragmentUiModel
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.ProductUiModel
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.TotalAmountUiModel
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnProductData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddOnViewModel @Inject constructor(val executorDispatchers: CoroutineDispatchers,
                                         private val getAddOnByProductUseCase: GetAddOnByProductUseCase,
                                         private val getAddOnSavedStateUseCase: GetAddOnSavedStateUseCase,
                                         private val saveAddOnStateUseCase: SaveAddOnStateUseCase)
    : BaseViewModel(executorDispatchers.main) {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var hasLoadData: Boolean = false

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

    fun loadAddOnData(addOnProductData: AddOnProductData, mockAddOnResponse: String? = "", mockAddOnSavedStateResponse: String? = "") {
        getAddOnByProductUseCase.mockResponse = mockAddOnResponse ?: ""

        val params = generateGetAddOnByProductRequestParams(addOnProductData)
        getAddOnByProductUseCase.setParams(params)
        getAddOnByProductUseCase.execute(
                onSuccess = {
                    handleOnSuccessGetAddOnByProduct(it, addOnProductData, mockAddOnSavedStateResponse)
                },
                onError = {
                    handleOnErrorGetAddOnProduct(it)
                }
        )
    }

    private fun generateGetAddOnByProductRequestParams(addOnProductData: AddOnProductData): GetAddOnByProductRequest {
        return GetAddOnByProductRequest().apply {
            addOnRequest = addOnProductData.availableBottomSheetData.products.map {
                AddOnByProductRequest().apply {
                    productId = it.productId
                    warehouseId = addOnProductData.availableBottomSheetData.warehouseId
                    addOnLevel = if (addOnProductData.availableBottomSheetData.isTokoCabang) {
                        AddOnByProductRequest.ADD_ON_LEVEL_ORDER
                    } else {
                        AddOnByProductRequest.ADD_ON_LEVEL_PRODUCT
                    }
                }
            }
            sourceRequest = SourceRequest().apply {
                squad = SourceRequest.SQUAD_VALUE
                useCase = SourceRequest.USE_CASE_VALUE
            }
            dataRequest = DataRequest().apply {
                inventory = true
            }
        }
    }

    private fun handleOnSuccessGetAddOnByProduct(getAddOnByProductResponse: GetAddOnByProductResponse,
                                                 addOnProductData: AddOnProductData,
                                                 mockAddOnSavedStateResponse: String?) {
        // Todo : adjust error validation, should be based on error code not error message
        if (getAddOnByProductResponse.dataResponse.error.message.isBlank()) {
            loadSavedStateData(addOnProductData, getAddOnByProductResponse, mockAddOnSavedStateResponse)
        } else {
            throw ResponseErrorException(getAddOnByProductResponse.dataResponse.error.message)
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

    private fun loadSavedStateData(addOnProductData: AddOnProductData,
                                   addOnByProductResponse: GetAddOnByProductResponse,
                                   mockAddOnSavedStateResponse: String? = "") {
        getAddOnSavedStateUseCase.mockResponse = mockAddOnSavedStateResponse ?: ""

        val params = generateGetAddOnSavedStateRequestParams(addOnProductData)
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

    private fun generateGetAddOnSavedStateRequestParams(addOnProductData: AddOnProductData): GetAddOnSavedStateRequest {
        return GetAddOnSavedStateRequest().apply {
            source = addOnProductData.source
            addOnKeys = listOf(
                    if (addOnProductData.availableBottomSheetData.isTokoCabang) {
                        "${addOnProductData.availableBottomSheetData.cartString}-0"
                    } else {
                        "${addOnProductData.availableBottomSheetData.cartString}-${addOnProductData.availableBottomSheetData.products.firstOrNull()?.cartId ?: ""}"
                    }
            )
        }
    }

    private fun handleOnSuccessGetAddOnSavedState(getAddOnSavedStateResponse: GetAddOnSavedStateResponse,
                                                  addOnProductData: AddOnProductData,
                                                  addOnByProductResponse: GetAddOnByProductResponse) {
        hasLoadData = true
        // Todo : adjust error validation, should be based on error code not error message
        if (getAddOnSavedStateResponse.getAddOns.errorMessage.firstOrNull()?.isNotBlank() == true) {
            throw ResponseErrorException(getAddOnSavedStateResponse.getAddOns.errorMessage.joinToString(". "))
        } else {
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
    }

    private fun handleOnErrorGetAddOnSavedState(addOnProductData: AddOnProductData,
                                                addOnByProductResponse: GetAddOnByProductResponse) {
        hasLoadData = true
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

    fun saveAddOnState(addOnProductData: AddOnProductData, mockSaveStateResponse: String? = null) {
        saveAddOnStateUseCase.mockResponse = mockSaveStateResponse ?: ""

        val params = generateSaveAddOnStateRequestParams(addOnProductData)
        saveAddOnStateUseCase.setParams(params)
        saveAddOnStateUseCase.execute(
                onSuccess = {
                    handleOnSuccessSaveAddOnState(it)
                },
                onError = {
                    handleOnErrorSaveAddOnState()
                }
        )
    }

    private fun generateSaveAddOnStateRequestParams(addOnProductData: AddOnProductData): SaveAddOnStateRequest {
        return SaveAddOnStateRequest().apply {
            source = addOnProductData.source
            addOns = listOf(
                    AddOnRequest().apply {
                        if (addOnProductData.availableBottomSheetData.isTokoCabang) {
                            addOnKey = "${addOnProductData.availableBottomSheetData.cartString}-0"
                            addOnLevel = AddOnConstant.ADD_ON_LEVEL_ORDER
                        } else {
                            addOnKey = "${addOnProductData.availableBottomSheetData.cartString}-${addOnProductData.availableBottomSheetData.products.firstOrNull()?.cartId}"
                            addOnLevel = AddOnConstant.ADD_ON_LEVEL_PRODUCT
                        }
                        cartProducts = addOnProductData.availableBottomSheetData.products.map {
                            CartProduct().apply {
                                cartId = it.cartId
                                productId = it.productId
                                warehouseId = addOnProductData.availableBottomSheetData.warehouseId
                                productName = it.productName
                                productImageUrl = it.productImageUrl
                            }
                        }
                        _addOnUiModel.value.let {
                            addOnData = listOf(
                                    AddOnDataRequest().apply {
                                        addOnId = it.addOnId
                                        addOnQty = it.addOnQty
                                        addOnMetadata = AddOnMetadataRequest().apply {
                                            addOnNote = AddOnNoteRequest().apply {
                                                isCustomNote = it.isCustomNote
                                                to = it.addOnNoteTo
                                                from = it.addOnNoteFrom
                                                notes = if (isCustomNote) {
                                                    it.addOnNote
                                                } else {
                                                    ""
                                                }
                                            }
                                        }
                                    }
                            )
                        }
                    }
            )
        }
    }

    private fun handleOnSuccessSaveAddOnState(saveAddOnStateResponse: SaveAddOnStateResponse) {
        // Todo : adjust error validation, should be based on error code not error message
        if (saveAddOnStateResponse.saveAddOns.errorMessage.firstOrNull()?.isNotBlank() == true) {
            throw ResponseErrorException(saveAddOnStateResponse.saveAddOns.errorMessage.joinToString(". "))
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

    private fun handleOnErrorSaveAddOnState() {
        launch {
            _uiEvent.emit(
                    UiEvent().apply {
                        state = UiEvent.STATE_FAILED_SAVE_ADD_ON
                    }
            )
        }
    }

    fun updateFragmentUiModel(addOnUiModel: AddOnUiModel) {
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

    fun hasChangedState(): Boolean {
        _addOnUiModel.value.let {
            return it.initialSelectedState != it.isAddOnSelected ||
                    it.initialAddOnNote != it.addOnNote ||
                    it.initialAddOnNoteFrom != it.addOnNoteFrom ||
                    it.initialAddOnNoteTo != it.addOnNoteTo
        }
    }
}