package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.draft.domain.usecase.InsertProductDraftUseCase
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
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
        private val insertProductDraftUseCase: InsertProductDraftUseCase
) : BaseViewModel(coroutineDispatcher) {

    var shipmentInputModel: ShipmentInputModel = ShipmentInputModel()
    var isEditMode:Boolean = false

    private val _insertProductDraftResult = MutableLiveData<Result<Long>>()
    val insertProductDraftResult: LiveData<Result<Long>>
        get() = _insertProductDraftResult

    private fun getWeight(weight: String) = weight.replace(".", "").toIntOrZero()

    fun isWeightValid(weight: String, unitWeight: Int): Boolean {
        var isValid = false
        val maxWeight = if (unitWeight == UNIT_KILOGRAM) MAX_WEIGHT_KILOGRAM else MAX_WEIGHT_GRAM
        if (getWeight(weight) in MIN_WEIGHT until maxWeight + 1) {
            isValid = true
        }

        return isValid
    }

    fun insertProductDraft(productInputModel: ProductInputModel, productId: Long, isUploading: Boolean) {
        launchCatchError(block = {
            insertProductDraftUseCase.params = InsertProductDraftUseCase.createRequestParams(productInputModel, productId, isUploading)
            _insertProductDraftResult.value = withContext(Dispatchers.IO) {
                insertProductDraftUseCase.executeOnBackground()
            }.let { Success(it) }
        }, onError = {
            _insertProductDraftResult.value = Fail(it)
        })
    }
}