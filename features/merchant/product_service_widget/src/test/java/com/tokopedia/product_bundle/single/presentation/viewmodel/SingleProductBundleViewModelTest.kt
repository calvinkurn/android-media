package com.tokopedia.product_bundle.single.presentation.viewmodel

import com.tokopedia.atc_common.AtcConstant.ATC_ERROR_GLOBAL
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleDataModel
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.BundleStats
import com.tokopedia.product_bundle.common.data.model.response.ShopInformation
import com.tokopedia.product_bundle.common.util.AtcVariantMapper
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleErrorEnum
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleSelectedItem
import com.tokopedia.product_bundle.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class SingleProductBundleViewModelTest: SingleProductBundleViewModelTestFixture() {

    @Test
    fun `setBundleInfo using inactive bundle should invoke pageError`() = runBlocking {
        viewModel.setBundleInfo(context, singleBundleEmpty?.bundleInfo.orEmpty(), "", 0, emptyList())
        val pageErrorResult = viewModel.pageError.getOrAwaitValue()
        assertEquals(SingleProductBundleErrorEnum.ERROR_BUNDLE_IS_EMPTY, pageErrorResult)
    }

    @Test
    fun `setBundleInfo using active bundle should invoke UiModel`() = runBlocking {
        viewModel.setBundleInfo(context, singleBundleVariant?.bundleInfo.orEmpty(), "", 0, emptyList())
        val singleProductBundleUiModelResult = viewModel.singleProductBundleUiModel.getOrAwaitValue()
        assertEquals(true, singleProductBundleUiModelResult.items.isNotEmpty())
    }

    @Test
    fun `getVariantText using invalid child productID`() {
        val childProductId = "1111111111"
        val productVariantTest = generateProductVariant()
        val variantText = viewModel.getVariantText(productVariantTest, childProductId)
        assert(variantText.isEmpty())
    }

    @Test
    fun `getVariantText using valid child productID`() {
        val childProductId = "2147926784"
        val productVariantTest = generateProductVariant()
        val variantText = viewModel.getVariantText(productVariantTest, childProductId)
        assertEquals("Putih", variantText)
    }

    @Test
    fun `getVariantText using invalid optionIds`() {
        val optionIds = listOf("111111111")
        val productVariantTest = generateProductVariant()
        val variantText = viewModel.getVariantText(productVariantTest.variants, optionIds)
        assert(variantText.isEmpty())
    }

    @Test
    fun `getVariantText using valid optionIds`() {
        val productVariantTest = generateProductVariant()
        val optionIds = productVariantTest.children.first().optionIds
        val variantText = viewModel.getVariantText(productVariantTest.variants, optionIds)
        assertEquals("Merah", variantText)
    }

    @Test
    fun `updateTotalAmount should return correct calculations`() = runBlocking {
        viewModel.updateTotalAmount(1000.0, 500.0, 2)
        val totalAmountUiModel = viewModel.totalAmountUiModel.getOrAwaitValue()
        assertEquals("Rp1.000", totalAmountUiModel.price)
        assertEquals("Rp2.000", totalAmountUiModel.slashPrice)
        assertEquals("Rp1.000", totalAmountUiModel.priceGap)
        assertEquals(50, totalAmountUiModel.discount)
    }

    @Test
    fun `when user has login then isUserLoggedIn should be true`() = coroutineTestRule.runTest {
        // given
        coEvery {
            userSession.userId
        } returns "12345"

        // when
        val testHasLogin = viewModel.isUserLoggedIn()
        val userId = viewModel.getUserId()

        // then
        assert(testHasLogin)
        assert(userId.isNotEmpty())
    }

    @Test
    fun `when user has login then isUserLoggedIn should be false`() = coroutineTestRule.runTest {
        // given
        coEvery {
            userSession.userId
        } returns ""

        // when
        val testNotLogin = viewModel.isUserLoggedIn()
        val userId = viewModel.getUserId()

        // then
        assert(!testNotLogin)
        assert(userId.isEmpty())
    }

    @Test
    fun `validateAndAddToCart should return not selected toasterError`() = runBlocking {
        viewModel.validateAndAddToCart("", "", "", "", listOf(
            SingleProductBundleSelectedItem(
                shopId = "123",
                bundleId = "123",
                productId = "123",
                quantity = 12,
                isSelected = false,
                isVariantEmpty = false
            )
        ))
        val toasterError = viewModel.toasterError.getOrAwaitValue()
        assertEquals(SingleProductBundleErrorEnum.ERROR_BUNDLE_NOT_SELECTED, toasterError)
    }

    @Test
    fun `validateAndAddToCart should return variant not selected toasterError`() = runBlocking {
        viewModel.validateAndAddToCart("", "", "", "", listOf(
            SingleProductBundleSelectedItem(
                shopId = "123",
                bundleId = "123",
                productId = "",
                quantity = 12,
                isSelected = true,
                isVariantEmpty = false
            )
        ))
        val toasterError = viewModel.toasterError.getOrAwaitValue()
        assertEquals(SingleProductBundleErrorEnum.ERROR_VARIANT_NOT_SELECTED, toasterError)
    }

    @Test
    fun `validateAndAddToCart using same selectedBundleId should invoke success addToCartResult`() = runBlocking {
        val singleProductBundleSelectedItem = listOf(
            SingleProductBundleSelectedItem(
                shopId = "123",
                bundleId = "123",
                productId = "123",
                quantity = 12,
                isSelected = true,
                isVariantEmpty = false
            )
        )

        // if variant child changed
        viewModel.validateAndAddToCart(ProductBundleConstants.PAGE_SOURCE_CART, "", "123", "456", singleProductBundleSelectedItem)
        coVerify { addToCartBundleUseCase.executeOnBackground() }

        // if variant child not changed
        viewModel.validateAndAddToCart(ProductBundleConstants.PAGE_SOURCE_MINI_CART, "", "123", "123", singleProductBundleSelectedItem)
        val addToCartResult = viewModel.addToCartResult.getOrAwaitValue()
        assertEquals("123", addToCartResult.requestParams.bundleId)
    }

    @Test
    fun `validateAndAddToCart using different selectedBundleId should invoke success addToCartResult`() = runBlocking {
        val singleProductBundleSelectedItem = listOf(
            SingleProductBundleSelectedItem(
                shopId = "123",
                bundleId = "123",
                productId = "123",
                quantity = 12,
                isSelected = true,
                isVariantEmpty = false
            )
        )

        coEvery {
            addToCartBundleUseCase.executeOnBackground()
        } returns AddToCartBundleModel(
            status = "OK",
            addToCartBundleDataModel = AddToCartBundleDataModel(
                success = 1,
                message = emptyList()
            )
        )

        viewModel.validateAndAddToCart(ProductBundleConstants.PAGE_SOURCE_MINI_CART, "123", "1133", "123", singleProductBundleSelectedItem)

        coVerify { addToCartBundleUseCase.executeOnBackground() }
        val addToCartResult = viewModel.addToCartResult.getOrAwaitValue()
        assertEquals("123", addToCartResult.requestParams.shopId)
    }

    @Test
    fun `validateAndAddToCart should throw Exception`() = runBlocking {
        // Given
        val exceptionMessage = "an error"
        coEvery {
            addToCartBundleUseCase.executeOnBackground()
        } throws MessageErrorException(exceptionMessage)

        // When
        viewModel.validateAndAddToCart("", "", "", "",
            generateSingleProductBundleSelectedItem())

        // Then
        coVerify { addToCartBundleUseCase.executeOnBackground() }
        val throwableError = viewModel.throwableError.getOrAwaitValue()
        assertEquals(exceptionMessage, throwableError.message)
    }

    @Test
    fun `validateAndAddToCart should invoke DialogError`() = runBlocking {
        // Given
        val dialogTitle = "title error"
        val dialogMessage = "message error"
        coEvery {
            addToCartBundleUseCase.executeOnBackground()
        } returns AddToCartBundleModel(
            status = "OK",
            errorMessage = "Something Error",
            addToCartBundleDataModel = AddToCartBundleDataModel(
                success = 0,
                message = listOf(dialogTitle, dialogMessage)
            )
        )

        // When
        viewModel.validateAndAddToCart("", "", "", "",
            generateSingleProductBundleSelectedItem())

        // Then
        coVerify { addToCartBundleUseCase.executeOnBackground() }
        val dialogError = viewModel.dialogError.getOrAwaitValue()
        assertEquals("title error", dialogError.title)
        assertEquals("message error", dialogError.message)
    }

    @Test
    fun `validateAndAddToCart should invoke success addToCartResult`() = runBlocking {
        // Given
        coEvery {
            addToCartBundleUseCase.executeOnBackground()
        } returns AddToCartBundleModel(
            status = "OK",
            addToCartBundleDataModel = AddToCartBundleDataModel(
                success = 1,
                message = emptyList()
            )
        )

        // When
        viewModel.validateAndAddToCart("", "", "", "",
            generateSingleProductBundleSelectedItem())

        // Then
        coVerify { addToCartBundleUseCase.executeOnBackground() }
        val addToCartResult = viewModel.addToCartResult.getOrAwaitValue()
        assertEquals(1, addToCartResult.responseResult.success)
    }

    @Test
    fun `validateAndAddToCart should invoke throwable`() = runBlocking {
        // Given
        coEvery {
            addToCartBundleUseCase.executeOnBackground()
        } returns AddToCartBundleModel(status = "NOT_OK")

        // When
        viewModel.validateAndAddToCart("", "", "", "",
            generateSingleProductBundleSelectedItem())

        // Then
        coVerify { addToCartBundleUseCase.executeOnBackground() }
        val throwableError = viewModel.throwableError.getOrAwaitValue()
        assertEquals(ATC_ERROR_GLOBAL, throwableError.message)
    }

    @Test
    fun `when set shop info with name shoptest expect get shop info with name shoptest`() {
        viewModel.setShopInfo(ShopInformation(shopName = "shoptest"))
        assertEquals("shoptest", viewModel.getShopInfo()?.shopName.orEmpty())
    }

    @Test
    fun `when setting total bundle sold using bundle info expect sum of sold bundle`() {
        val bundleInfo = listOf(
            BundleInfo(bundleStats = BundleStats("2")),
            BundleInfo(bundleStats = BundleStats("2"))
        )
        viewModel.setBundleTotalSold(bundleInfo)
        assertEquals(4,viewModel.getBundleTotalSold())
    }
    private fun generateProductVariant(): ProductVariant {
        val bundleInfoTest = singleBundleVariant?.bundleInfo?.firstOrNull()
        val bundleItemTest = bundleInfoTest?.bundleItems?.firstOrNull()
        return bundleItemTest?.let {
            AtcVariantMapper.mapToProductVariant(it)
        } ?: ProductVariant()
    }

    private fun generateSingleProductBundleSelectedItem(): List<SingleProductBundleSelectedItem> {
        return listOf(
            SingleProductBundleSelectedItem(
                shopId = "123",
                bundleId = "123",
                productId = "123",
                quantity = 12,
                isSelected = true,
                isVariantEmpty = false
            )
        )
    }
}
