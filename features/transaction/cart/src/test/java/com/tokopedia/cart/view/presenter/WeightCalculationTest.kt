package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import io.mockk.every
import org.junit.Test

class WeightCalculationTest : BaseCartTest() {

    private lateinit var firstProductFirstShop: CartItemHolderData
    private lateinit var firstProductSecondShop: CartItemHolderData
    private lateinit var secondProductFirstShop: CartItemHolderData
    private lateinit var secondProductSecondShop: CartItemHolderData

    private lateinit var firstShop: CartShopHolderData
    private lateinit var secondShop: CartShopHolderData

    private lateinit var cartShops: ArrayList<CartShopHolderData>

    private fun initializeData() {
        //region First Item In First Shop
        firstProductFirstShop = CartItemHolderData().apply {
            productPrice = 1000
            parentId = "0"
            productId = "1"
            productCashBack = "10%"
            quantity = 1
            productWeight = 1
        }
        //endregion

        //region First Item In Second Shop
        firstProductSecondShop = CartItemHolderData().apply {
            productPrice = 40
            parentId = "0"
            productId = "2"
            quantity = 2
            productWeight = 2
        }
        //endregion

        //region Second Item In First Shop
        secondProductFirstShop = CartItemHolderData().apply {
            productPrice = 200
            parentId = "0"
            productId = "3"
            quantity = 3
            productWeight = 3
        }
        //endregion

        //region Second Item In Second Shop
        secondProductSecondShop = CartItemHolderData().apply {
            productPrice = 1
            parentId = "0"
            productId = "4"
            quantity = 4
            productWeight = 4
        }
        //endregion

        //region First Shop
        firstShop = CartShopHolderData().apply {
            productUiModelList = arrayListOf(firstProductFirstShop, secondProductFirstShop)
        }
        //endregion

        //region Second Shop
        secondShop = CartShopHolderData().apply {
            productUiModelList = arrayListOf(firstProductSecondShop, secondProductSecondShop)
        }
        //endregion

        cartShops = arrayListOf(firstShop, secondShop)
    }

    @Test
    fun `WHEN no item selected THEN should have 0 total weight`() {
        // GIVEN
        initializeData()
        every { view.getAllAvailableCartDataList() } answers {
            cartShops.flatMap {
                it.productUiModelList
            }
        }

        // WHEN
        cartListPresenter.reCalculateSubTotal(cartShops)

        // THEN
        assert(cartShops[0].totalWeight == 0.0)
        assert(cartShops[1].totalWeight == 0.0)
    }

    @Test
    fun `WHEN some item selected THEN should have '1' gram weight in first shop and '16' gram weight in second shop`() {
        // GIVEN
        initializeData()
        firstProductFirstShop.isSelected = true
        firstShop.isPartialSelected = true

        secondProductSecondShop.isSelected = true
        secondShop.isPartialSelected = true

        every { view.getAllAvailableCartDataList() } answers {
            cartShops.flatMap {
                it.productUiModelList
            }
        }

        // WHEN
        cartListPresenter.reCalculateSubTotal(cartShops)

        // THEN
        assert(cartShops[0].totalWeight == 1.0)
        assert(cartShops[1].totalWeight == 16.0)
    }

    @Test
    fun `WHEN all item selected THEN should have 10 gram in first shop and 20 gram in second shop`() {
        // GIVEN
        initializeData()
        firstProductFirstShop.isSelected = true
        secondProductFirstShop.isSelected = true
        firstShop.isAllSelected = true

        firstProductSecondShop.isSelected = true
        secondProductSecondShop.isSelected = true
        secondShop.isAllSelected = true

        every { view.getAllAvailableCartDataList() } answers {
            cartShops.flatMap {
                it.productUiModelList
            }
        }

        // WHEN
        cartListPresenter.reCalculateSubTotal(cartShops)

        // THEN
        assert(cartShops[0].totalWeight == 10.0)
        assert(cartShops[1].totalWeight == 20.0)
    }
}