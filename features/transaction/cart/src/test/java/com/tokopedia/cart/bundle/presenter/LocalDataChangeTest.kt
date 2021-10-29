package com.tokopedia.cart.bundle.presenter

import com.tokopedia.cart.bundle.view.uimodel.CartItemHolderData
import io.mockk.every
import org.junit.Assert
import org.junit.Test

class LocalDataChangeTest : BaseCartTest() {

    @Test
    fun `WHEN quantity is changed THEN local data state should be changed`() {
        // GIVEN
        var result = false

        val cartDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData().apply {
                originalQty = 1
                quantity = 2
            })
        }

        every { view.getAllCartDataList() } returns cartDataList

        // WHEN
        result = cartListPresenter?.dataHasChanged() ?: false

        // THEN
        Assert.assertTrue(result)

    }

    @Test
    fun `WHEN notes is changed THEN local data state should be changed`() {
        // GIVEN
        var result = false

        val cartDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData().apply {
                originalNotes = "nnn"
                notes = "n"
            })
        }

        every { view.getAllCartDataList() } returns cartDataList

        // WHEN
        result = cartListPresenter?.dataHasChanged() ?: false

        // THEN
        Assert.assertTrue(result)

    }

    @Test
    fun `WHEN quantity and notes are changed THEN local data state should be changed`() {
        // GIVEN
        var result = false

        val cartDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData().apply {
                originalNotes = "nnn"
                notes = "n"
                originalQty = 1
                quantity = 2
            })
        }

        every { view.getAllCartDataList() } returns cartDataList

        // WHEN
        result = cartListPresenter?.dataHasChanged() ?: false

        // THEN
        Assert.assertTrue(result)

    }

    @Test
    fun `WHEN quantity and notes did not changed THEN local data state should be changed`() {
        // GIVEN
        var result = false

        val cartDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData().apply {
                originalNotes = "nnn"
                notes = "nnn"
                originalQty = 1
                quantity = 1
            })
        }

        every { view.getAllCartDataList() } returns cartDataList

        // WHEN
        result = cartListPresenter?.dataHasChanged() ?: false

        // THEN
        Assert.assertFalse(result)
    }
}