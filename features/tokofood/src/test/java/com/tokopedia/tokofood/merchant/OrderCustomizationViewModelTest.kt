package com.tokopedia.tokofood.merchant

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam
import com.tokopedia.tokofood.data.generateCustomListItemsWithError
import com.tokopedia.tokofood.data.generateCustomListItemsWithoutError
import com.tokopedia.tokofood.data.generateTestDataGetCustomListItems
import com.tokopedia.tokofood.data.generateTestProductUiModel
import com.tokopedia.tokofood.data.generateTestUiAddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.OptionUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.OrderCustomizationViewModel
import io.mockk.MockKAnnotations
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderCustomizationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: OrderCustomizationViewModel


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = OrderCustomizationViewModel()
    }

    @Test
    fun `when calculate subtotal price using addOnUiModels expect legit subtotal`() {
        val testData = generateTestUiAddOnUiModel()
        val expectedResult = 30000.0
        val actualResult = viewModel.calculateSubtotalPrice(15000.0, 2, listOf(testData))
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when calculate subtotal price but no addOns applied expect simple calculated subtotal `() {
        val baseProductPrice = 15000.0
        val quantity = 2
        val expectedResult = baseProductPrice * quantity
        viewModel.baseProductPrice = baseProductPrice
        val actualResult = viewModel.calculateSubtotalPrice(baseProductPrice, quantity, listOf(null))
        assertEquals(expectedResult, actualResult)
        assertEquals(viewModel.baseProductPrice, baseProductPrice)
    }

    @Test
    fun `when formatting subtotal price expect rupiah format`() {
        val testData = 10000.0
        val expectedResult = "Rp10.000"
        val actualResult = viewModel.formatSubtotalPrice(testData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when cart id is empty expect master data when getting custom list items`() {
        val testData = generateTestDataGetCustomListItems("")
        val expectedResult = testData.customListItems
        val actualResult = viewModel.getCustomListItems("", testData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when cart id is not empty but custom order details also empty expect same custom list items`() {
        val testData = generateTestDataGetCustomListItems("cartId-garlicKnots", isCustomOrderDetailEmpty = true)
        val expectedResult = testData.customListItems
        val actualResult = viewModel.getCustomListItems("cartId-garlicKnots", testData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when cart id is empty but items don't have addOnUiModel expect same custom list items`() {
        val testData = generateTestDataGetCustomListItems("", isAddOnUiModelNull = true, isCustomOrderDetailEmpty = true)
        val expectedResult = testData.customListItems
        val actualResult = viewModel.getCustomListItems("", testData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when cart id is empty but addOnUiModel option is empty expect same custom list items`() {
        val testData = generateTestDataGetCustomListItems("", isAddOnUiModelNull = false, isAddOnOptionEmpty = true, isCustomOrderDetailEmpty = true)
        val expectedResult = testData.customListItems
        val actualResult = viewModel.getCustomListItems("", testData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when cart id is not empty expect custom list items from custom order details when getting custom list items`() {
        val testData = generateTestDataGetCustomListItems("cartId-garlicKnots")
        val expectedResult = testData.customOrderDetails.first().customListItems
        val actualResult = viewModel.getCustomListItems("cartId-garlicKnots", testData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when cart id is not empty but no order details matched that cartId, expect custom list items to be empty`() {
        val testData = generateTestDataGetCustomListItems("cartId-garlicKnots", customCartId = "cartId-anotherGarlicKnots")
        val expectedResult = listOf<CustomListItem>()
        val actualResult = viewModel.getCustomListItems("cartId-garlicKnots", testData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when cartId is not empty expect isEditingCustomOrder to be true`() {
        val cartId = "cartId"
        val actualResult = viewModel.isEditingCustomOrder(cartId)
        assertTrue(actualResult)
    }

    @Test
    fun `when cartId is empty expect isEditingCustomOrder to be false`() {
        val cartId = ""
        val actualResult = viewModel.isEditingCustomOrder(cartId)
        assertFalse(actualResult)
    }

    @Test
    fun `when selected option is less that mandatory minimum selection expect is error true and custom list items with error`() {
        val testData = generateCustomListItemsWithError()
        val actualResult = viewModel.validateCustomOrderInput(listOf(testData))
        assertTrue(actualResult.first)
    }

    @Test
    fun `when selected option matched the mandatory minimum selection expect is error false and custom list items without error`() {
        val testData = generateCustomListItemsWithoutError()
        val actualResult = viewModel.validateCustomOrderInput(listOf(testData))
        assertFalse(actualResult.first)
    }

    @Test
    fun `when validateCustomOrderInput, should not apply error checking on items without addOns`() {
        val testData = generateCustomListItemsWithError()
        val listItemWithoutAddOns = CustomListItem(
            listItemType = CustomListItemType.PRODUCT_ADD_ON,
            addOnUiModel = null
        )
        val actualResult = viewModel.validateCustomOrderInput(listOf(testData, listItemWithoutAddOns))
        assertEquals(listItemWithoutAddOns.addOnUiModel?.isError, actualResult.second[1].addOnUiModel?.isError)
    }

    @Test
    fun `when generating atc request param expect legit atc request param `() {
        val testData = generateTestProductUiModel()
        val expectedResult = UpdateParam(
                productList = listOf(UpdateProductParam(
                        cartId = "cartId-garlicKnots",
                        productId = "bf3eba99-534d-4344-9cf8-6a46326feae0",
                        notes = "orderNote",
                        quantity = 1)
                ), shopId = "shopId"
        )
        val actualResult = viewModel.generateRequestParam(
                shopId = "shopId",
                productUiModel = testData,
                cartId = "cartId-garlicKnots",
                orderNote = "orderNote",
                orderQty = 1,
                addOnUiModels = listOf(generateTestUiAddOnUiModel())
        )
        assertEquals(expectedResult.productList.first().cartId, actualResult.productList.first().cartId)
        assertEquals(expectedResult.productList.first().productId, actualResult.productList.first().productId)
        assertEquals(expectedResult.productList.first().notes, actualResult.productList.first().notes)
        assertEquals(expectedResult.productList.first().quantity, actualResult.productList.first().quantity)
        assertEquals(expectedResult.shopId, actualResult.shopId)
    }
}
