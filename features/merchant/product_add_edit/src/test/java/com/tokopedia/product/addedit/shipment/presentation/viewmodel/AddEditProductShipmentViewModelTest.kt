package com.tokopedia.product.addedit.shipment.presentation.viewmodel

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
    }

    @Test
    fun `isWeightValid should valid when unit is gram and weight is in allowed range`() {
        val isValid = viewModel.isWeightValid(AddEditProductShipmentConstants.MIN_WEIGHT.toString(), AddEditProductShipmentConstants.MAX_WEIGHT_GRAM)
        Assert.assertTrue(isValid)
    }

    @Test
    fun `isWeightValid should valid when unit is kg and weight is in allowed range`() {
        val isValid = viewModel.isWeightValid(AddEditProductShipmentConstants.MIN_WEIGHT.toString(), AddEditProductShipmentConstants.UNIT_KILOGRAM)
        Assert.assertTrue(isValid)
    }

    @Test
    fun `isWeightValid should invalid when unit is gram and weight isn't in allowed range`() {
        val isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MIN_WEIGHT - 1}", AddEditProductShipmentConstants.MAX_WEIGHT_GRAM)
        Assert.assertFalse(isValid)
    }

    @Test
    fun `isWeightValid should valid when unit is kg and weight isn't in allowed range`() {
        val isValid = viewModel.isWeightValid("${AddEditProductShipmentConstants.MIN_WEIGHT - 1}", AddEditProductShipmentConstants.UNIT_KILOGRAM)
        Assert.assertFalse(isValid)
    }
}