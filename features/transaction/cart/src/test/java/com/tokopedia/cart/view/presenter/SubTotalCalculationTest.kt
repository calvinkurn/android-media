package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.data.model.response.shopgroupsimplified.WholesalePrice
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

class SubTotalCalculationTest : BaseCartTest() {

    private lateinit var firstProductFirstShop: CartItemHolderData
    private lateinit var firstProductSecondShop: CartItemHolderData
    private lateinit var secondProductFirstShop: CartItemHolderData
    private lateinit var secondProductSecondShop: CartItemHolderData
    private lateinit var firstProductThirdShop: CartItemHolderData

    private lateinit var firstShop: CartGroupHolderData
    private lateinit var secondShop: CartGroupHolderData
    private lateinit var thirdShop: CartGroupHolderData

    private lateinit var cartShops: ArrayList<CartGroupHolderData>
    private lateinit var cartShopsIncludingBundleItems: ArrayList<CartGroupHolderData>

    private fun initializeData() {
        //region First Item In First Shop
        firstProductFirstShop = CartItemHolderData().apply {
            productPrice = 1000.0
            parentId = "0"
            productId = "1"
            productCashBack = "10%"
            quantity = 1
        }
        //endregion

        //region First Item In Second Shop
        firstProductSecondShop = CartItemHolderData().apply {
            productPrice = 40.0
            parentId = "0"
            productId = "2"
            quantity = 2
        }
        //endregion

        //region Second Item In First Shop
        secondProductFirstShop = CartItemHolderData().apply {
            productPrice = 200.0
            parentId = "0"
            productId = "3"
            quantity = 3
        }
        //endregion

        //region Second Item In Second Shop
        secondProductSecondShop = CartItemHolderData().apply {
            productPrice = 1.0
            parentId = "0"
            productId = "4"
            quantity = 4
        }
        //endregion

        //region First Item In Third Shop, bundling product
        firstProductThirdShop = CartItemHolderData().apply {
            isBundlingItem = true
            bundleId = "123"
            bundleGroupId = "123-abc"
            bundlePrice = 50000.0
            bundleQuantity = 2
            quantity = 1
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

        //region Third Shop
        thirdShop = CartGroupHolderData().apply {
            productUiModelList = arrayListOf(firstProductThirdShop)
        }
        //endregion

        cartShops = arrayListOf(firstShop, secondShop)
        cartShopsIncludingBundleItems = arrayListOf(firstShop, secondShop, thirdShop)
    }

    @Test
    fun `WHEN no item selected THEN should have 0 subtotal and 0 cashback`() {
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
        verifyOrder {
            view.updateCashback(0.0)
            view.renderDetailInfoSubTotal("0", 0.0, false)
        }
    }

    @Test
    fun `WHEN some item selected THEN should have '1004' on subtotal and '100' on cashback from '5' items`() {
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

        cartListPresenter.reCalculateSubTotal(cartShops)

        verifyOrder {
            view.updateCashback(100.0)
            view.renderDetailInfoSubTotal("5", 1004.0, false)
        }
    }

    @Test
    fun `WHEN some item selected THEN should have '1684' on subtotal and '100' on cashback from '10' items`() {
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
        verifyOrder {
            view.updateCashback(100.0)
            view.renderDetailInfoSubTotal("10", 1684.0, false)
        }
    }

    @Test
    fun `WHEN all item selected with wholesale price THEN should have '1684' on subtotal and '100' on cashback from '19' items`() {
        // GIVEN
        initializeData()
        firstProductFirstShop.isSelected = true
        secondProductFirstShop.isSelected = true
        firstShop.isAllSelected = true

        firstProductSecondShop.isSelected = true
        secondProductSecondShop.isSelected = true
        secondShop.isAllSelected = true

        val wholesalePriceData = WholesalePrice(qtyMin = 5, prdPrc = 100.0)
        firstProductFirstShop.wholesalePriceData = arrayListOf(wholesalePriceData)
        firstProductFirstShop.quantity = 10

        every { view.getAllAvailableCartDataList() } answers {
            cartShops.flatMap {
                it.productUiModelList
            }
        }

        // WHEN
        cartListPresenter.reCalculateSubTotal(cartShops)

        // THEN
        verifyOrder {
            view.updateCashback(100.0)
            view.renderDetailInfoSubTotal("19", 1684.0, false)
        }
    }

    @Test
    fun `WHEN all item selected with invalid wholesale price THEN should have '1684' on subtotal and '100' on cashback from '10' items`() {
        // GIVEN
        initializeData()
        firstProductFirstShop.isSelected = true
        secondProductFirstShop.isSelected = true
        firstShop.isAllSelected = true

        firstProductSecondShop.isSelected = true
        secondProductSecondShop.isSelected = true
        secondShop.isAllSelected = true

        val wholesalePriceData = WholesalePrice(qtyMin = 10, prdPrc = 100.0)
        firstProductFirstShop.wholesalePriceData = arrayListOf(wholesalePriceData)

        every { view.getAllAvailableCartDataList() } answers {
            cartShops.flatMap {
                it.productUiModelList
            }
        }

        // WHEN
        cartListPresenter.reCalculateSubTotal(cartShops)

        // THEN
        verifyOrder {
            view.updateCashback(100.0)
            view.renderDetailInfoSubTotal("10", 1684.0, false)
        }
    }

    @Test
    fun `WHEN all item selected with product variant THEN should have '1684' on subtotal and '160' on cashback from '10' items`() {
        // GIVEN
        initializeData()
        firstProductFirstShop.isSelected = true
        secondProductFirstShop.isSelected = true
        firstShop.isAllSelected = true

        firstProductSecondShop.isSelected = true
        secondProductSecondShop.isSelected = true
        secondShop.isAllSelected = true

        firstProductFirstShop.parentId = "9"
        secondProductFirstShop.parentId = "9"
        secondProductFirstShop.productCashBack = firstProductFirstShop.productCashBack

        every { view.getAllAvailableCartDataList() } answers {
            cartShops.flatMap {
                it.productUiModelList
            }
        }

        // WHEN
        cartListPresenter.reCalculateSubTotal(cartShops)

        // THEN
        verifyOrder {
            view.updateCashback(160.0)
            view.renderDetailInfoSubTotal("10", 1684.0, false)
        }
    }

    @Test
    fun `WHEN all item selected with same priced product variant THEN should have '4300' on subtotal and '400' on cashback from '6' items`() {
        // GIVEN
        initializeData()
        firstProductFirstShop.isSelected = true
        secondProductFirstShop.isSelected = true
        firstShop.isAllSelected = true

        firstProductSecondShop.isSelected = true
        secondProductSecondShop.isSelected = true
        secondShop.isAllSelected = true

        firstProductSecondShop.quantity = 1
        firstProductSecondShop.productPrice = 100.0
        secondProductSecondShop.quantity = 1
        secondProductSecondShop.productPrice = 200.0

        firstProductFirstShop.parentId = "9"
        firstProductFirstShop.productPrice = 1000.0
        firstProductFirstShop.productCashBack = "10%"
        firstProductFirstShop.quantity = 2

        secondProductFirstShop.parentId = "9"
        secondProductFirstShop.productPrice = 1000.0
        secondProductFirstShop.productCashBack = "10%"
        secondProductFirstShop.quantity = 2

        every { view.getAllAvailableCartDataList() } answers {
            cartShops.flatMap {
                it.productUiModelList
            }
        }

        // WHEN
        cartListPresenter.reCalculateSubTotal(cartShops)

        // THEN
        verifyOrder {
            view.updateCashback(400.0)
            view.renderDetailInfoSubTotal("6", 4300.0, false)
        }
    }

    @Test
    fun `WHEN all item selected with same priced product variant different quantity THEN should have '6300' on subtotal and '600' on cashback from '8' items`() {
        // GIVEN
        initializeData()
        firstProductFirstShop.isSelected = true
        secondProductFirstShop.isSelected = true
        firstShop.isAllSelected = true

        firstProductSecondShop.isSelected = true
        secondProductSecondShop.isSelected = true
        secondShop.isAllSelected = true

        firstProductSecondShop.quantity = 1
        firstProductSecondShop.productPrice = 100.0
        secondProductSecondShop.quantity = 1
        secondProductSecondShop.productPrice = 200.0

        firstProductFirstShop.parentId = "9"
        firstProductFirstShop.productPrice = 1000.0
        firstProductFirstShop.productCashBack = "10%"
        firstProductFirstShop.quantity = 2

        secondProductFirstShop.parentId = "9"
        secondProductFirstShop.productPrice = 1000.0
        secondProductFirstShop.productCashBack = "10%"
        secondProductFirstShop.quantity = 4

        every { view.getAllAvailableCartDataList() } answers {
            cartShops.flatMap {
                it.productUiModelList
            }
        }

        // WHEN
        cartListPresenter.reCalculateSubTotal(cartShops)

        // THEN
        verifyOrder {
            view.updateCashback(600.0)
            view.renderDetailInfoSubTotal("8", 6300.0, false)
        }
    }

    @Test
    fun `WHEN bundling item selected THEN should have '101684' on subtotal from '12' items`() {
        // GIVEN
        initializeData()
        firstProductFirstShop.isSelected = true
        secondProductFirstShop.isSelected = true
        firstShop.isAllSelected = true

        firstProductSecondShop.isSelected = true
        secondProductSecondShop.isSelected = true
        secondShop.isAllSelected = true

        firstProductThirdShop.isSelected = true
        thirdShop.isAllSelected = true

        every { view.getAllAvailableCartDataList() } answers {
            cartShopsIncludingBundleItems.flatMap {
                it.productUiModelList
            }
        }

        // WHEN
        cartListPresenter.reCalculateSubTotal(cartShopsIncludingBundleItems)

        // THEN
        verify {
            view.renderDetailInfoSubTotal("12", 101684.0, false)
        }
    }

    @Test
    fun `WHEN some item selected with slash price THEN should have '1004' on subtotal and '100' on cashback from '5' items`() {
        // GIVEN
        initializeData()
        firstProductFirstShop.isSelected = true
        firstProductFirstShop.productOriginalPrice = 2000.0
        firstShop.isPartialSelected = true

        secondProductSecondShop.isSelected = true
        secondShop.isPartialSelected = true

        every { view.getAllAvailableCartDataList() } answers {
            cartShops.flatMap {
                it.productUiModelList
            }
        }

        cartListPresenter.reCalculateSubTotal(cartShops)

        verifyOrder {
            view.updateCashback(100.0)
            view.renderDetailInfoSubTotal("5", 1004.0, false)
        }
    }
}
