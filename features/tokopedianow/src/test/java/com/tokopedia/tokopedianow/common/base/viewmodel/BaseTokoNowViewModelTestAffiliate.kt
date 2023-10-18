package com.tokopedia.tokopedianow.common.base.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.universal_sharing.tracker.PageType
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.PageDetail
import com.tokopedia.universal_sharing.view.model.Product
import com.tokopedia.universal_sharing.view.model.Shop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class BaseTokoNowViewModelTestAffiliate : BaseTokoNowViewModelTestFixture() {

    companion object {
        private const val CHANGE_QUANTITY_DELAY = 700L
    }

    @Test
    fun `given mini cart item is null when add to cart should call init affiliate cookie`() {
        runTest {
            val affiliateUuid = "123e4567-e89b-12d3-a456-426614174000"
            val affiliateChannel = "channel"

            val productId = "1"
            val quantity = 1
            val shopId = "5"
            val stock = 150
            val isVariant = true
            val warehouseId = 9L

            val addToCartResponse = AddToCartDataModel()
            val miniCartItems = mapOf(
                MiniCartItemKey("5") to MiniCartItem.MiniCartItemProduct(
                    productId = "5",
                    quantity = 1
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetShopId_thenReturn(shopId.toLong())
            onGetWarehouseId_thenReturn(warehouseId)
            onGetUserLoggedIn_thenReturn(isLoggedIn = true)

            onAddToCart_thenReturn(addToCartResponse)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.initAffiliateCookie(affiliateUuid, affiliateChannel)
            viewModel.getMiniCart()
            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, isVariant)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyInitAffiliateCookieCalled(affiliateUuid, affiliateChannel)
        }
    }

    @Test
    fun `given mini cart item is not null when update cart item should call init affiliate cookie`() {
        runTest {
            val affiliateUuid = "123e4567-e89b-12d3-a456-426614174122"
            val affiliateChannel = "affiliate-channel"

            val productId = "1"
            val quantity = 5
            val currentQuantity = 1
            val shopId = "5"
            val stock = 0
            val isVariant = false
            val warehouseId = 1L

            val updateCartResponse = UpdateCartV2Data()
            val miniCartItems = mapOf(
                MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(
                    productId = productId,
                    quantity = currentQuantity
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetShopId_thenReturn(shopId.toLong())
            onGetWarehouseId_thenReturn(warehouseId)
            onGetUserLoggedIn_thenReturn(isLoggedIn = true)

            onUpdateItemCart_thenReturn(updateCartResponse)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.initAffiliateCookie(affiliateUuid, affiliateChannel)
            viewModel.getMiniCart()
            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, isVariant)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyInitAffiliateCookieCalled(affiliateUuid, affiliateChannel)
        }
    }

    @Test
    fun `given new quantity less than current quantity when update cart item should NOT call init affiliate`() {
        coroutineTestRule.runTest {
            val affiliateUuid = "123e4567-e89b-12d3-a456-426614174122"
            val affiliateChannel = "affiliate-channel"

            val productId = "1"
            val newQuantity = 2
            val currentQuantity = 5
            val shopId = "5"
            val stock = 0
            val isVariant = false
            val warehouseId = 1L

            val updateCartResponse = UpdateCartV2Data()
            val miniCartItems = mapOf(
                MiniCartItemKey(productId) to MiniCartItem.MiniCartItemProduct(
                    productId = productId,
                    quantity = currentQuantity
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetShopId_thenReturn(shopId.toLong())
            onGetWarehouseId_thenReturn(warehouseId)
            onGetUserLoggedIn_thenReturn(isLoggedIn = true)

            onUpdateItemCart_thenReturn(updateCartResponse)
            onGetMiniCart_thenReturn(miniCartResponse)

            viewModel.getMiniCart()
            viewModel.onCartQuantityChanged(productId, shopId, newQuantity, stock, isVariant)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyInitAffiliateCookieNotCalled(affiliateUuid, affiliateChannel)
        }
    }

    @Test
    fun `given add to cart when init affiliate cookie error should do nothing`() {
        coroutineTestRule.runTest {
            val affiliateUuid = "123e4567-e89b-12d3-a456-426614174000"
            val affiliateChannel = "channel"

            val productId = "1"
            val quantity = 1
            val shopId = "5"
            val stock = 150
            val isVariant = true
            val warehouseId = 9L

            val addToCartResponse = AddToCartDataModel()
            val miniCartItems = mapOf(
                MiniCartItemKey("5") to MiniCartItem.MiniCartItemProduct(
                    productId = "5",
                    quantity = 1
                )
            )
            val miniCartResponse = MiniCartSimplifiedData(miniCartItems = miniCartItems)

            onGetShopId_thenReturn(shopId.toLong())
            onGetWarehouseId_thenReturn(warehouseId)
            onGetUserLoggedIn_thenReturn(isLoggedIn = true)

            onAddToCart_thenReturn(addToCartResponse)
            onGetMiniCart_thenReturn(miniCartResponse)
            onInitAffiliateCookie_thenReturn(NullPointerException())

            viewModel.initAffiliateCookie(affiliateUuid, affiliateChannel)
            viewModel.getMiniCart()
            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, isVariant)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)
        }
    }

    @Test
    fun `given url when createAffiliateLink should call affiliate service createAffiliateLink`() {
        val url = "tokopedia://now"

        viewModel.createAffiliateLink(url)

        verifyCreateAffiliateLinkCalled(url)
    }

    @Test
    fun `when getAffiliateShareInput should create expected share input`() {
        val shopId = "11530573"
        val actualShareInput = viewModel.getAffiliateShareInput()

        val pageDetail = PageDetail(
            pageId = shopId,
            pageType = "shop",
            siteId = "1",
            verticalId = "1"
        )

        val shop = Shop(
            shopID = shopId,
            shopStatus = 1,
            isOS = true,
            isPM = false
        )

        val expectedShareInput = AffiliateInput(
            pageDetail = pageDetail,
            pageType = PageType.SHOP.value,
            product = Product(),
            shop = shop
        )

        assertEquals(expectedShareInput, actualShareInput)
    }
}
