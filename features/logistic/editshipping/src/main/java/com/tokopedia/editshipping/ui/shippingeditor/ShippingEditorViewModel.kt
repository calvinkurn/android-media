package com.tokopedia.editshipping.ui.shippingeditor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.editshipping.data.repository.ShippingEditorRepository
import com.tokopedia.editshipping.data.usecase.GetShipperDetailUseCase
import com.tokopedia.editshipping.data.usecase.GetShipperInfoUseCase
import com.tokopedia.editshipping.data.usecase.SaveShippingUseCase
import com.tokopedia.editshipping.data.usecase.ValidateShippingEditorUseCase
import com.tokopedia.editshipping.domain.mapper.ShipperDetailMapper
import com.tokopedia.editshipping.domain.mapper.ValidateShippingNewMapper
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperDetailModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperListModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperTickerModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShippingEditorState
import com.tokopedia.editshipping.domain.model.shippingEditor.ValidateShippingEditorModel
import com.tokopedia.editshipping.domain.param.OngkirShippingEditorPopupInput
import com.tokopedia.editshipping.domain.param.OngkirShippingEditorSaveInput
import com.tokopedia.logisticCommon.data.response.shippingeditor.SaveShippingResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShippingEditorViewModel @Inject constructor(
    private val shippingEditorRepo: ShippingEditorRepository,
    private val validateShippingMapper: ValidateShippingNewMapper,
    private val detailMapper: ShipperDetailMapper,
    private val getShipperInfoUseCase: GetShipperInfoUseCase,
    private val getShipperDetailUseCase: GetShipperDetailUseCase,
    private val validateShippingEditorUseCase: ValidateShippingEditorUseCase,
    private val saveShippingUseCase: SaveShippingUseCase
) : ViewModel() {

    private val _shipperList = MutableLiveData<ShippingEditorState<ShipperListModel>>()
    val shipperList: LiveData<ShippingEditorState<ShipperListModel>>
        get() = _shipperList

    private val _shipperTickerList = MutableLiveData<ShippingEditorState<ShipperTickerModel>>()
    val shipperTickerList: LiveData<ShippingEditorState<ShipperTickerModel>>
        get() = _shipperTickerList

    private val _shipperDetail = MutableLiveData<ShippingEditorState<ShipperDetailModel>>()
    val shipperDetail: LiveData<ShippingEditorState<ShipperDetailModel>>
        get() = _shipperDetail

    private val _validateDataShipper =
        MutableLiveData<ShippingEditorState<ValidateShippingEditorModel>>()
    val validateDataShipper: LiveData<ShippingEditorState<ValidateShippingEditorModel>>
        get() = _validateDataShipper

    private val _saveShippingData = MutableLiveData<ShippingEditorState<SaveShippingResponse>>()
    val saveShippingData: LiveData<ShippingEditorState<SaveShippingResponse>>
        get() = _saveShippingData

    fun getShipperList(shopId: Long) {
        viewModelScope.launch(onErrorGetShipperData) {
            _shipperList.value = ShippingEditorState.Loading
            val getShipperListData = getShipperInfoUseCase(shopId)
            _shipperList.value = ShippingEditorState.Success(getShipperListData)
        }
    }

    fun getShipperDetail() {
        _shipperDetail.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorGetShipperDetails) {
            val getShipperDetail = getShipperDetailUseCase(Unit)
            val data =
                detailMapper.mapShipperDetails(getShipperDetail.ongkirShippingEditorGetShipperDetail.data)
            _shipperDetail.value = ShippingEditorState.Success(data)
        }
    }

    fun validateShippingEditor(shopId: Long, activatedSpIds: String) {
        _validateDataShipper.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorValidateShippingEditor) {
            val getValidateData = validateShippingEditorUseCase(OngkirShippingEditorPopupInput(shopId, activatedSpIds))
            _validateDataShipper.value = ShippingEditorState.Success(
                validateShippingMapper.mapShippingEditorData(getValidateData.ongkirShippingEditorPopup.data)
            )
        }
    }

    fun saveShippingData(shopId: Long, activatedSpIds: String, featuresId: String?) {
        _saveShippingData.value = ShippingEditorState.Loading
        viewModelScope.launch(onErrorSaveShippingEditor) {
            val saveShippingEditor =
                saveShippingUseCase(
                    OngkirShippingEditorSaveInput(
                        shopId,
                        activatedSpIds,
                        featuresId.orEmpty()
                    )
                )
            _saveShippingData.value =
                ShippingEditorState.Success(saveShippingEditor.saveShippingEditor)
        }
    }

    private val onErrorGetShipperData = CoroutineExceptionHandler { _, e ->
        _shipperList.value = ShippingEditorState.Fail(e, "")
    }

    private fun getOnErrorGetShipperTicker(shipperList: ShipperListModel) =
        CoroutineExceptionHandler { _, e ->
            _shipperList.value = ShippingEditorState.Success(shipperList)
            _shipperTickerList.value = ShippingEditorState.Fail(e, "")
        }

    private val onErrorGetShipperDetails = CoroutineExceptionHandler { _, e ->
        _shipperDetail.value = ShippingEditorState.Fail(e, "")
    }

    private val onErrorValidateShippingEditor = CoroutineExceptionHandler { _, e ->
        _validateDataShipper.value = ShippingEditorState.Fail(e, "")
    }

    private val onErrorSaveShippingEditor = CoroutineExceptionHandler { _, e ->
        _saveShippingData.value = ShippingEditorState.Fail(e, "")
    }
}
