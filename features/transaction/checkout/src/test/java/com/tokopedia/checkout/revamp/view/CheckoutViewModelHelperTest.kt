package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckoutViewModelHelperTest : BaseCheckoutViewModelTest() {

    @Test
    fun `get_order_products`() {
        // given
        viewModel.listData.value = listOf(
            CheckoutProductModel(cartStringGroup = "1", productId = 1, productCatId = 1),
            CheckoutProductModel(cartStringGroup = "1", productId = 2, productCatId = 2),
            CheckoutOrderModel(cartStringGroup = "1"),
            CheckoutProductModel(cartStringGroup = "2", productId = 3, productCatId = 2),
            CheckoutProductModel(cartStringGroup = "2", productId = 4, productCatId = 3),
            CheckoutOrderModel(cartStringGroup = "2")
        )
        val cartStringGroup = "1"

        // when
        val products = viewModel.getOrderProducts(cartStringGroup)

        // then
        assertEquals(2, products.size)
        assertTrue(products.map { it.productId }.containsAll(listOf(1, 2)))
    }

    @Test
    fun `get_all_product_category_ids`() {
        // given
        viewModel.listData.value = listOf(
            CheckoutProductModel(cartStringGroup = "1", productId = 1, productCatId = 1),
            CheckoutProductModel(cartStringGroup = "1", productId = 2, productCatId = 2),
            CheckoutOrderModel(cartStringGroup = "1"),
            CheckoutProductModel(cartStringGroup = "2", productId = 3, productCatId = 2),
            CheckoutProductModel(cartStringGroup = "2", productId = 4, productCatId = 3),
            CheckoutProductModel(cartStringGroup = "2", productId = 5, productCatId = 4, isError = true),
            CheckoutOrderModel(cartStringGroup = "2")
        )

        // when
        val catIds = viewModel.getProductCatIds()

        // then
        assertEquals(4, catIds.size)
        assertTrue(catIds.containsAll(listOf(1, 2, 2, 3)))
    }
}
