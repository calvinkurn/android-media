package com.tokopedia.tokopedianow.productrecommendation

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.NowAffiliateAtcData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowProductRecommendationViewModelTestAffiliate : TokoNowProductRecommendationViewModelTestFixture() {

    companion object {
        private const val CHANGE_QUANTITY_DELAY = 700L
    }

    @Test
    fun `given product when add to cart should call check atc affiliate cookie`() {
        runTest {
            val quantity = 2
            val isVariant = true

            val product = ProductCardCompactCarouselItemUiModel(
                productCardModel = ProductCardCompactUiModel(
                    productId = "12345",
                    name = "product a",
                    price = "RP. 10.000",
                    minOrder = 1,
                    orderQuantity = 2,
                    availableStock = 100,
                    maxOrder = 6,
                    isVariant = isVariant
                ),
                shopId = "122212",
                parentId = "0"
            )
            productModels.add(product)

            val shopId = product.shopId
            val productId = product.getProductId()
            val stock = product.productCardModel.availableStock

            mockProductModels()

            val response = AddToCartDataModel()

            onAddToCart_thenReturn(response)

            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, isVariant)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val expectedAffiliateData = NowAffiliateAtcData(
                productId = productId,
                shopId = shopId,
                stock = stock,
                isVariant = isVariant,
                newQuantity = quantity,
                currentQuantity = 0
            )

            verifyCheckAtcAffiliateCookieCalled(expectedAffiliateData)
        }
    }

    @Test
    fun `given product when update cart should call check atc affiliate cookie`() {
        runTest {
            val quantity = 2
            val currentQuantity = 3
            val isVariant = true

            val product = ProductCardCompactCarouselItemUiModel(
                productCardModel = ProductCardCompactUiModel(
                    productId = "12345",
                    name = "product a",
                    price = "RP. 10.000",
                    minOrder = 1,
                    orderQuantity = 2,
                    availableStock = 100,
                    maxOrder = 6,
                    isVariant = isVariant
                ),
                shopId = "122212",
                parentId = "0"
            )
            productModels.add(product)

            val shopId = product.shopId
            val productId = product.getProductId()
            val stock = product.productCardModel.availableStock

            mockProductModels()

            mockMiniCartSimplifiedData(
                productId = productId,
                quantity = currentQuantity
            )

            val response = UpdateCartV2Data()

            onUpdateItemCart_thenReturn(response)

            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, isVariant)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val expectedAffiliateData = NowAffiliateAtcData(
                productId = productId,
                shopId = shopId,
                stock = stock,
                isVariant = isVariant,
                newQuantity = quantity,
                currentQuantity = currentQuantity
            )

            verifyCheckAtcAffiliateCookieCalled(expectedAffiliateData)
        }
    }

    @Test
    fun `given check atc affiliate cookie throws error when add to cart should do nothing`() {
        coroutineTestRule.runTest {
            val quantity = 2
            val isVariant = true

            val product = ProductCardCompactCarouselItemUiModel(
                productCardModel = ProductCardCompactUiModel(
                    productId = "12345",
                    name = "product a",
                    price = "RP. 10.000",
                    minOrder = 1,
                    orderQuantity = 2,
                    availableStock = 100,
                    maxOrder = 6,
                    isVariant = isVariant
                ),
                shopId = "122212",
                parentId = "0"
            )
            productModels.add(product)

            val productId = product.getProductId()
            val shopId = product.shopId
            val stock = product.productCardModel.availableStock
            val response = AddToCartDataModel()

            mockProductModels()
            onAddToCart_thenReturn(response)
            onCheckAtcAffiliateCookie_thenReturn(error = NullPointerException())

            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, isVariant)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)
        }
    }
}
