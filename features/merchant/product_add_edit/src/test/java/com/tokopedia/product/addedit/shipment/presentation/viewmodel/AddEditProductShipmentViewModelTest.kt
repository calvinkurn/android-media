package com.tokopedia.product.addedit.shipment.presentation.viewmodel

import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_GRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.UNIT_GRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.UNIT_KILOGRAM
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

/**
 * Created by faisalramd on 2020-05-08.
 */
class AddEditProductShipmentViewModelTest: AddEditProductShipmentViewModelTestFixture() {

    @Test
    fun `When the weight is below zero Expect invalid weight`() {
        val weight = "-1"
        val unit = UNIT_GRAM

        val result = shipmentViewModel.isWeightValid(weight, unit)
        assertFalse(result)
    }

    @Test
    fun `When the weight is gram and weight is below maximum Expect valid weight`() {
        val weight = MAX_WEIGHT_GRAM.toString()
        val unit = UNIT_GRAM

        val result = shipmentViewModel.isWeightValid(weight, unit)
        assertTrue(result)
    }

    @Test
    fun `When the weight is gram and weight is exceed maximum Expect invalid weight`() {
        val weight = (MAX_WEIGHT_GRAM + 1).toString()
        val unit = UNIT_GRAM

        val result = shipmentViewModel.isWeightValid(weight, unit)
        assertFalse(result)
    }

    @Test
    fun `When the weight is kilogram and weight is below maximum Expect valid weight`() {
        val weight = MAX_WEIGHT_KILOGRAM.toString()
        val unit = UNIT_KILOGRAM

        val result = shipmentViewModel.isWeightValid(weight, unit)
        assertTrue(result)
    }

    @Test
    fun `When the weight is kilogram and weight is exceed maximum Expect invalid weight`() {
        val weight = (MAX_WEIGHT_KILOGRAM + 1).toString()
        val unit = UNIT_KILOGRAM

        val result = shipmentViewModel.isWeightValid(weight, unit)
        assertFalse(result)
    }
}