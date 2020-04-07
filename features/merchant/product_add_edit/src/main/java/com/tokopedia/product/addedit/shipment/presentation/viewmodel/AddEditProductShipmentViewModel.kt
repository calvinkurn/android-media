package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_GRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MIN_WEIGHT
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.UNIT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductShipmentViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher,
        private val saveProductDraftUseCase: SaveProductDraftUseCase
) : BaseViewModel(coroutineDispatcher) {

    var shipmentInputModel: ShipmentInputModel = ShipmentInputModel()
    var isEditMode:Boolean = false

    private val _saveProductDraftResult = MutableLiveData<Result<Long>>()
    val saveProductDraftResult: LiveData<Result<Long>>
        get() = _saveProductDraftResult

    private fun getWeight(weight: String) = weight.replace(".", "").toIntOrZero()

    fun isWeightValid(weight: String, unitWeight: Int): Boolean {
        var isValid = false
        val maxWeight = if (unitWeight == UNIT_KILOGRAM) MAX_WEIGHT_KILOGRAM else MAX_WEIGHT_GRAM
        if (getWeight(weight) in MIN_WEIGHT until maxWeight + 1) {
            isValid = true
        }

        return isValid
    }

    fun saveProductDraft(productDraft: ProductDraft, productId: Long, isUploading: Boolean) {
        launchCatchError(block = {
            saveProductDraftUseCase.params = SaveProductDraftUseCase.createRequestParams(productDraft, productId, isUploading)
            _saveProductDraftResult.value = withContext(Dispatchers.IO) {
                saveProductDraftUseCase.executeOnBackground()
            }.let { Success(it) }
        }, onError = {
            _saveProductDraftResult.value = Fail(it)
        })
    }
}