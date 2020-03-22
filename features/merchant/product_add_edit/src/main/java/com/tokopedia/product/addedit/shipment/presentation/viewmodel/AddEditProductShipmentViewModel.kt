package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_GRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.UNIT_KILOGRAM
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddEditProductShipmentViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel(coroutineDispatcher) {
    private fun getWeight(weight: String) = weight.replace(".", "").toIntOrZero()

    fun isWeightValid(weight: String, unitWeight: Int, minWeight: Int): Boolean {
        var isValid = false
        val maxWeight = if (unitWeight == UNIT_KILOGRAM) MAX_WEIGHT_KILOGRAM else MAX_WEIGHT_GRAM
        if (getWeight(weight) in minWeight until maxWeight + 1) {
            isValid = true
        }

        return isValid
    }
}