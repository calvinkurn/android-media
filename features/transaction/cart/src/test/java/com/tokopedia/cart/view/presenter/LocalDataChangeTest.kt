package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.uimodel.CartItemHolderData
import io.mockk.every
import org.junit.Assert
import org.junit.Test

class LocalDataChangeTest : BaseCartTest() {

    @Test
    fun `WHEN quantity is changed THEN local data state should be changed`() {
        // GIVEN
        val cartDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData().apply {
                originalQty = 1
                quantity = 2
            })
        }

        every { view.getAllCartDataList() } returns cartDataList

        // WHEN
        val result = cartListPresenter.dataHasChanged()

        // THEN
        Assert.assertTrue(result)

    }

    @Test
    fun `WHEN notes is changed THEN local data state should be changed`() {
        // GIVEN
        val cartDataList = mutableListOf<CartItemHolderData>().apply {
            add(CartItemHolderData().apply {
                originalNotes = "nnn"
                notes = "n"
            })
        }

        every { view.getAllCartDataList() } returns cartDataList

        // WHEN
        val result = cartListPresenter.dataHasChanged()

        // THEN
        Assert.assertTrue(result)

    }

    @Test
    fun `WHEN quantity and notes are changed THEN local data state should be changed`() {
        // GIVEN
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
        val result = cartListPresenter.dataHasChanged()

        // THEN
        Assert.assertTrue(result)

    }

    @Test
    fun `WHEN quantity and notes did not changed THEN local data state should be changed`() {
        // GIVEN
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
        val result = cartListPresenter.dataHasChanged()

        // THEN
        Assert.assertFalse(result)
    }
}