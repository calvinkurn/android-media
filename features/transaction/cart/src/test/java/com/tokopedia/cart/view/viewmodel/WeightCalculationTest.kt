package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cartrevamp.view.helper.CartDataHelper
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import io.mockk.every
import org.junit.Test

class WeightCalculationTest : BaseCartViewModelTest() {

    private lateinit var firstProductFirstShop: CartItemHolderData
    private lateinit var firstProductSecondShop: CartItemHolderData
    private lateinit var secondProductFirstShop: CartItemHolderData
    private lateinit var secondProductSecondShop: CartItemHolderData

    private lateinit var firstShop: CartGroupHolderData
    private lateinit var secondShop: CartGroupHolderData

    private lateinit var cartShops: ArrayList<CartGroupHolderData>

    private fun initializeData() {
        //region First Item In First Shop
        firstProductFirstShop = CartItemHolderData().apply {
            productPrice = 1000.0
            parentId = "0"
            productId = "1"
            productCashBack = "10%"
            quantity = 1
            productWeight = 1
        }
        //endregion

        //region First Item In Second Shop
        firstProductSecondShop = CartItemHolderData().apply {
            productPrice = 40.0
            parentId = "0"
            productId = "2"
            quantity = 2
            productWeight = 2
        }
        //endregion

        //region Second Item In First Shop
        secondProductFirstShop = CartItemHolderData().apply {
            productPrice = 200.0
            parentId = "0"
            productId = "3"
            quantity = 3
            productWeight = 3
        }
        //endregion

        //region Second Item In Second Shop
        secondProductSecondShop = CartItemHolderData().apply {
            productPrice = 1.0
            parentId = "0"
            productId = "4"
            quantity = 4
            productWeight = 4
        }
        //endregion

        //region First Shop
        firstShop = CartGroupHolderData().apply {
            productUiModelList = arrayListOf(firstProductFirstShop, secondProductFirstShop)
        }
        //endregion

        //region Second Shop
        secondShop = CartGroupHolderData().apply {
            productUiModelList = arrayListOf(firstProductSecondShop, secondProductSecondShop)
        }
        //endregion

        cartShops = arrayListOf(firstShop, secondShop)
    }

    override fun setUp() {
        super.setUp()
        every { CartDataHelper.getAllAvailableCartItemData(any()) } answers {
            cartShops.flatMap {
                it.productUiModelList
            }
        }
        every { CartDataHelper.getAllAvailableShopGroupDataList(any()) } answers { cartShops }
    }

    @Test
    fun `WHEN no item selected THEN should have 0 total weight`() {
        // GIVEN
        initializeData()

        // WHEN
        cartViewModel.reCalculateSubTotal()

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

        // WHEN
        cartViewModel.reCalculateSubTotal()

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

        // WHEN
        cartViewModel.reCalculateSubTotal()

        // THEN
        assert(cartShops[0].totalWeight == 10.0)
        assert(cartShops[1].totalWeight == 20.0)
    }
}
