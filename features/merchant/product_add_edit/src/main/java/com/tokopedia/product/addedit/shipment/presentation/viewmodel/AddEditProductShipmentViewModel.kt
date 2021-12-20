package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.logisticCommon.data.mapper.CustomProductLogisticMapper
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.logisticCommon.data.repository.CustomProductLogisticRepository
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_GRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MIN_WEIGHT
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.UNIT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductShipmentViewModel @Inject constructor(
        private val saveProductDraftUseCase: SaveProductDraftUseCase,
        private val customProductLogisticRepository: CustomProductLogisticRepository,
        private val customProductLogisticMapper: CustomProductLogisticMapper,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    var shipmentInputModel: ShipmentInputModel = ShipmentInputModel()
    var isEditMode: Boolean = false
    var isAddMode: Boolean = false
    var isDraftMode: Boolean = false
    var isFirstMoved: Boolean = false

    private val _cplList = MutableLiveData<Result<CustomProductLogisticModel>>()
    val cplList: LiveData<Result<CustomProductLogisticModel>>
        get() = _cplList

    private fun getWeight(weight: String) = weight.replace(".", "").toIntOrZero()

    fun isWeightValid(weight: String, unitWeight: Int): Boolean {
        var isValid = false
        val maxWeight = if (unitWeight == UNIT_KILOGRAM) MAX_WEIGHT_KILOGRAM else MAX_WEIGHT_GRAM
        if (getWeight(weight) in MIN_WEIGHT until maxWeight + 1) {
            isValid = true
        }

        return isValid
    }

    fun saveProductDraft(productInputModel: ProductInputModel) {
        val productId = productInputModel.productId
        val productDraft = AddEditProductMapper.mapProductInputModelDetailToDraft(productInputModel)
        launchCatchError(block = {
            saveProductDraftUseCase.params = SaveProductDraftUseCase.createRequestParams(productDraft, productId, false)
            withContext(dispatcher.io) {
                saveProductDraftUseCase.executeOnBackground()
            }
        }, onError = {
            AddEditProductErrorHandler.logExceptionToCrashlytics(it)
        })
    }

    fun getCPLList(shopId: Long, productId: String) {
        viewModelScope.launch {
            try {
                val cplList = customProductLogisticRepository.getCPLList(shopId, productId)
                _cplList.value = Success(customProductLogisticMapper.mapCPLData(cplList.response.data))
            } catch (e: Throwable) {
                _cplList.value = Fail(e)
            }
        }
    }
}