package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.productlimitation.domain.model.ProductLimitationData
import com.tokopedia.product.addedit.productlimitation.domain.usecase.ProductLimitationUseCase
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_GRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MIN_WEIGHT
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.UNIT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductShipmentViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val productLimitationUseCase: ProductLimitationUseCase
) : BaseViewModel(dispatcher.main) {

    var shipmentInputModel: ShipmentInputModel = ShipmentInputModel()
    var isEditMode: Boolean = false
    var isAddMode: Boolean = false
    var isDraftMode: Boolean = false
    var isFirstMoved: Boolean = false

    private val mProductLimitationData = MutableLiveData<Result<ProductLimitationData>>()
    val productLimitationData: LiveData<Result<ProductLimitationData>> get() = mProductLimitationData

    private fun getWeight(weight: String) = weight.replace(".", "").toIntOrZero()

    fun isWeightValid(weight: String, unitWeight: Int): Boolean {
        var isValid = false
        val maxWeight = if (unitWeight == UNIT_KILOGRAM) MAX_WEIGHT_KILOGRAM else MAX_WEIGHT_GRAM
        if (getWeight(weight) in MIN_WEIGHT until maxWeight + 1) {
            isValid = true
        }

        return isValid
    }

    fun getProductLimitation() {
        launchCatchError(block = {
            val result = withContext(dispatcher.io) {
                productLimitationUseCase.executeOnBackground()
            }
            mProductLimitationData.value = Success(result.productAddRule.data)
        }, onError = {
            mProductLimitationData.value = Fail(it)
        })
    }
}