package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditProductShipmentViewModelTest {

    private val coroutineDispatcher = TestCoroutineDispatcher()

    private val viewModel: AddEditProductShipmentViewModel by lazy {
        AddEditProductShipmentViewModel(coroutineDispatcher)
    }

    @Test
    fun `isWeightValid should valid when unit is gram and weight is in allowed range`() {
        val isValid = viewModel.isWeightValid(AddEditProductShipmentConstants.MIN_WEIGHT.toString(), AddEditProductShipmentConstants.UNIT_GRAM)
        Assert.assertTrue(isValid)
    }

    @Test
    fun `isWeightValid should valid when unit is kg and weight is in allowed range`() {
        val isValid = viewModel.isWeightValid(AddEditProductShipmentConstants.MIN_WEIGHT.toString(), AddEditProductShipmentConstants.UNIT_KILOGRAM)
        Assert.assertTrue(isValid)
    }

    @Test
    fun `isWeightValid should invalid when unit is gram and weight isn't in allowed range`() {
        var isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MIN_WEIGHT - 1}", AddEditProductShipmentConstants.MAX_WEIGHT_GRAM)
        Assert.assertFalse(isValid)

        isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MAX_WEIGHT_GRAM + 1}", AddEditProductShipmentConstants.MAX_WEIGHT_GRAM)
        Assert.assertFalse(isValid)
    }

    @Test
    fun `isWeightValid should valid when unit is kg and weight isn't in allowed range`() {
        var isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MIN_WEIGHT - 1}", AddEditProductShipmentConstants.UNIT_KILOGRAM)
        Assert.assertFalse(isValid)

        isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MAX_WEIGHT_KILOGRAM + 1}", AddEditProductShipmentConstants.UNIT_KILOGRAM)
        Assert.assertFalse(isValid)
    }

    @Test
    fun `when all boolean variables should return true and object should return the same object`() {
        val shipmentInputModel = ShipmentInputModel(
                weight = 10,
                weightUnit = 12,
                isMustInsurance = true
        )
        viewModel.isAddMode = true
        viewModel.isEditMode = true
        viewModel.isDraftMode = true
        viewModel.isFirstMoved = true
        viewModel.shipmentInputModel = shipmentInputModel

        Assert.assertTrue(viewModel.isAddMode)
        Assert.assertTrue(viewModel.isEditMode)
        Assert.assertTrue(viewModel.isDraftMode)
        Assert.assertTrue(viewModel.isFirstMoved)
        Assert.assertTrue(viewModel.shipmentInputModel == shipmentInputModel)
    }
}