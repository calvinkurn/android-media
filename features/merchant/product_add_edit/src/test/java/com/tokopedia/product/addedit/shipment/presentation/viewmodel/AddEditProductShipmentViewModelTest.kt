package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditProductShipmentViewModelTest {

    private val coroutineDispatcher = TestCoroutineDispatcher()

    private val viewModel: AddEditProductShipmentViewModel by lazy {
        AddEditProductShipmentViewModel(coroutineDispatcher)
    }

    @Before
    fun setup() {
        viewModel.isAddMode = true
        viewModel.isEditMode = true
        viewModel.isDraftMode = true
        viewModel.isFirstMoved = true
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
    fun `isWeightValid should invalid when unit is gram and weight is below in allowed range`() {
        val isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MIN_WEIGHT - 1}", AddEditProductShipmentConstants.UNIT_GRAM)
        Assert.assertFalse(isValid)
    }

    @Test
    fun `isWeightValid should invalid when unit is kg and weight is below in allowed range`() {
        val isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MIN_WEIGHT - 1}", AddEditProductShipmentConstants.UNIT_KILOGRAM)
        Assert.assertFalse(isValid)
    }

    @Test
    fun `isWeightValid should invalid when unit is gram and weight is above in allowed range`() {
        val isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MAX_WEIGHT_GRAM + 1}", AddEditProductShipmentConstants.UNIT_GRAM)
        Assert.assertFalse(isValid)
    }

    @Test
    fun `isWeightValid should invalid when unit is kg and weight is above in allowed range`() {
        val isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MAX_WEIGHT_KILOGRAM + 1}", AddEditProductShipmentConstants.UNIT_KILOGRAM)
        Assert.assertFalse(isValid)
    }

    @Test
    fun `constant variables should valid when it's assigned`() {
        // test add mode
        viewModel.isAddMode = true
        viewModel.isDraftMode = true
        Assert.assertFalse(viewModel.getIsAddMode())
        Assert.assertTrue(viewModel.isAddMode)
        Assert.assertTrue(viewModel.isDraftMode)

        viewModel.isAddMode = true
        viewModel.isDraftMode = false
        Assert.assertTrue(viewModel.getIsAddMode())
        Assert.assertTrue(viewModel.isAddMode)
        Assert.assertFalse(viewModel.isDraftMode)

        viewModel.isAddMode = false
        viewModel.isDraftMode = true
        Assert.assertFalse(viewModel.getIsAddMode())
        Assert.assertFalse(viewModel.isAddMode)
        Assert.assertTrue(viewModel.isDraftMode)

        // test edit mode
        viewModel.isEditMode = true
        viewModel.isDraftMode = true
        Assert.assertTrue(viewModel.isEditMode)

        viewModel.isEditMode = true
        viewModel.isDraftMode = false
        Assert.assertTrue(viewModel.isEditMode)

        viewModel.isEditMode = false
        viewModel.isDraftMode = true
        Assert.assertFalse(viewModel.isEditMode)

        // test input model default value
        viewModel.setShipmentInputModel(ProductInputModel(), true)
        Assert.assertTrue(viewModel.shipmentInputModel.weight == AddEditProductShipmentConstants.DEFAULT_WEIGHT_VALUE)
        Assert.assertTrue(viewModel.shipmentInputModel.weightUnit == AddEditProductShipmentConstants.DEFAULT_WEIGHT_UNIT)
        Assert.assertTrue(viewModel.isFirstMoved)

        viewModel.setShipmentInputModel(ProductInputModel(), false)
        Assert.assertTrue(viewModel.shipmentInputModel.weight == AddEditProductShipmentConstants.DEFAULT_WEIGHT_VALUE)
        Assert.assertTrue(viewModel.shipmentInputModel.weightUnit == AddEditProductShipmentConstants.DEFAULT_WEIGHT_UNIT)
        Assert.assertFalse(viewModel.isFirstMoved)

        viewModel.setShipmentInputModel(null, false)
        Assert.assertTrue(viewModel.shipmentInputModel.weight == AddEditProductShipmentConstants.DEFAULT_WEIGHT_VALUE)
        Assert.assertTrue(viewModel.shipmentInputModel.weightUnit == AddEditProductShipmentConstants.DEFAULT_WEIGHT_UNIT)
        Assert.assertFalse(viewModel.isFirstMoved)
    }
}