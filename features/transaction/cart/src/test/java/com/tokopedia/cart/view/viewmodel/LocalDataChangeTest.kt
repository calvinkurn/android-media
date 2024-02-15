package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cart.view.helper.CartDataHelper
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import io.mockk.every
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class LocalDataChangeTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN quantity is changed THEN local data state should be changed`() {
        // GIVEN
        val cartDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    originalQty = 1
                    quantity = 2
                }
            )
        }

        every { CartDataHelper.getAllCartItemData(any(), any()) } returns cartDataList

        // WHEN
        val result = cartViewModel.dataHasChanged()

        // THEN
        assertTrue(result)
    }

    @Test
    fun `WHEN notes is changed THEN local data state should be changed`() {
        // GIVEN
        val cartDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    originalNotes = "nnn"
                    notes = "n"
                }
            )
        }

        every { CartDataHelper.getAllCartItemData(any(), any()) } returns cartDataList

        // WHEN
        val result = cartViewModel.dataHasChanged()

        // THEN
        assertTrue(result)
    }

    @Test
    fun `WHEN quantity and notes are changed THEN local data state should be changed`() {
        // GIVEN
        val cartDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    originalNotes = "nnn"
                    notes = "n"
                    originalQty = 1
                    quantity = 2
                }
            )
        }

        every { CartDataHelper.getAllCartItemData(any(), any()) } returns cartDataList

        // WHEN
        val result = cartViewModel.dataHasChanged()

        // THEN
        assertTrue(result)
    }

    @Test
    fun `WHEN quantity and notes did not changed THEN local data state should be changed`() {
        // GIVEN
        val cartDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    originalNotes = "nnn"
                    notes = "nnn"
                    originalQty = 1
                    quantity = 1
                }
            )
        }

        every { CartDataHelper.getAllCartItemData(any(), any()) } returns cartDataList

        // WHEN
        val result = cartViewModel.dataHasChanged()

        // THEN
        assertFalse(result)
    }
}
