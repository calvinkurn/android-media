package com.tokopedia.product_bundle.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleDataModel
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleModel
import com.tokopedia.product.detail.common.data.model.variant.Picture
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.data.model.variant.VariantOption
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.PAGE_SOURCE_CART
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.PAGE_SOURCE_MINI_CART
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.PAGE_SOURCE_PDP
import com.tokopedia.product_bundle.common.data.model.request.Bundle
import com.tokopedia.product_bundle.common.data.model.response.*
import com.tokopedia.product_bundle.common.data.model.uimodel.ProductBundleState
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleDetail
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import com.tokopedia.product_bundle.util.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductBundleViewModelTest: ProductBundleViewModelTestFixture() {

    @Test
    fun `when BundleInfo type is invalid then isSingleProductBundle should be false`() = coroutineTestRule.runBlockingTest {
        // given, when
        val resultEmpty = viewModel.isSingleProductBundle(emptyList())
        val resultMultiple = viewModel.isSingleProductBundle(
            listOf(BundleInfo(
                bundleItems = List(2){ BundleItem() }
            ))
        )

        // then
        assertEquals(false, resultEmpty)
        assertEquals(false, resultMultiple)
    }

    @Test
    fun `when BundleInfo type is single then isSingleProductBundle should be true`() = coroutineTestRule.runBlockingTest {
        // given, when
        val resultSingle = viewModel.isSingleProductBundle(
            listOf(BundleInfo(
                bundleItems = List(1){ BundleItem() }
            ))
        )

        // then
        assertEquals(true, resultSingle)
    }

    @Test
    fun `when BundleInfo is active then isProductBundleAvailable should be true`() = coroutineTestRule.runBlockingTest {
        // given, when
        val resultAvailable = viewModel.isProductBundleAvailable(
            BundleInfo(
                status = "1",
                quota = 1
            )
        )
        val resultInactive = viewModel.isProductBundleAvailable(
            BundleInfo(
                status = "-1",
                quota = 1
            )
        )
        val resultInactive2 = viewModel.isProductBundleAvailable(
            BundleInfo(
                status = "1",
                quota = 0
            )
        )
        val resultInactive3 = viewModel.isProductBundleAvailable(
            BundleInfo(
                status = "-1",
                quota = 0
            )
        )

        // then
        assertEquals(true, resultAvailable)
        assertEquals(false, resultInactive)
        assertEquals(true, resultInactive2)
        assertEquals(false, resultInactive3)
    }

    @Test
    fun `when user has login then isUserLoggedIn should be true`() = coroutineTestRule.runBlockingTest {
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
    fun `when user has login then isUserLoggedIn should be false`() = coroutineTestRule.runBlockingTest {
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
    fun `when calculate total price should return correct value`() = coroutineTestRule.runBlockingTest {
        // given
        val originalPrice = 1000.toDouble()
        val bundlePrice = 500.toDouble()
        val productBundleDetails = listOf(
            ProductBundleDetail(
                bundlePrice = 500.toDouble(),
                originalPrice = 1000.toDouble(),
                productQuantity = 1
            ),
            ProductBundleDetail(
                bundlePrice = 500.toDouble(),
                originalPrice = 1000.toDouble(),
                productQuantity = 1
            )
        )

        // when
        val totalPrice = viewModel.calculateTotalPrice(productBundleDetails)
        val totalBundlePrice = viewModel.calculateTotalBundlePrice(productBundleDetails)
        val totalSaving = viewModel.calculateTotalSaving(originalPrice, bundlePrice)
        val discountPercentage = viewModel.calculateDiscountPercentage(originalPrice, bundlePrice)

        // then
        assertEquals(2000f.toString(), totalPrice.toString())
        assertEquals(1000f.toString(), totalBundlePrice.toString())
        assertEquals(500f.toString(), totalSaving.toString())
        assertEquals(50, discountPercentage)
    }

    @Test
    fun `when preorder data provided then should return correct value`() = coroutineTestRule.runBlockingTest {
        // given
        val activeStatus = "ACTIVE"
        val inactiveStatus = "INACTIVE"
        val preorderDay = 1
        val preorderWeek = 2
        val preorderMonth = 3
        val preorderInvalid = -1

        coEvery {
            resourceProvider.getPreOrderTimeUnitDay()
        } returns "hari"
        coEvery {
            resourceProvider.getPreOrderTimeUnitWeek()
        } returns "minggu"
        coEvery {
            resourceProvider.getPreOrderTimeUnitMonth()
        } returns "bulan"

        // when
        val preOrderActive = viewModel.isPreOrderActive(activeStatus)
        val preOrderInactive = viewModel.isPreOrderActive(inactiveStatus)
        val preOrderWordingInvalid = viewModel.getPreOrderTimeUnitWording(preorderInvalid)

        val preOrderWordingDay = viewModel.getPreOrderTimeUnitWording(preorderDay)
        val preOrderWordingWeek = viewModel.getPreOrderTimeUnitWording(preorderWeek)
        val preOrderWordingMonth = viewModel.getPreOrderTimeUnitWording(preorderMonth)

        // then
        assert(preOrderActive)
        assert(!preOrderInactive)
        assertEquals("", preOrderWordingInvalid)
        assertEquals("hari", preOrderWordingDay)
        assertEquals("minggu", preOrderWordingWeek)
        assertEquals("bulan", preOrderWordingMonth)
    }

    @Test
    fun `when ProductBundleMasters data provided then should return correct value`() = coroutineTestRule.runBlockingTest {
        val testProductBundleMasters = listOf(
            ProductBundleMaster(bundleId = 124),
            ProductBundleMaster(bundleId = 123)
        )

        val selectedBundleFound = viewModel.getSelectedBundle(123, testProductBundleMasters)
        val selectedBundleNullHandle = viewModel.getSelectedBundle(null, testProductBundleMasters)
        val selectedBundleNotFound = viewModel.getSelectedBundle(125, testProductBundleMasters)

        assertEquals(123L, selectedBundleFound?.bundleId)
        assertEquals(124L, selectedBundleNullHandle?.bundleId)
        assertEquals(124L, selectedBundleNotFound?.bundleId)
    }

    @Test
    fun `when getSelectedProductIds data provided then should return correct value`() = coroutineTestRule.runBlockingTest {
        val testProductBundleDetail = listOf(
            ProductBundleDetail(productId = 123)
        )

        val productId = viewModel.getSelectedProductIds(testProductBundleDetail)

        assertEquals(123.toString() , productId)
    }

    @Test
    fun `when getVariantLevel then should return expected size`() = coroutineTestRule.runBlockingTest {
        val expectedSize = 2
        val variantLevelSize = viewModel.getVariantLevel(ProductVariant(
            variants = List(expectedSize) { Variant() }
        ))

        assertEquals(expectedSize, variantLevelSize)
    }

    @Test
    fun `when getVariantTitle then should return expected title`() = coroutineTestRule.runBlockingTest {
        val expectedTitle = "merah, hitam, "
        val variantTitle = viewModel.getVariantTitle(ProductVariant(
            variants = listOf(
                Variant(name = "merah"),
                Variant(name = "hitam"),
                Variant(name = null)
            )
        ))

        assertEquals(expectedTitle, variantTitle)
    }

    @Test
    fun `when ProductBundleDetail data provided then should return correct value`() = coroutineTestRule.runBlockingTest {
        val productBundleDetail = ProductBundleDetail(
            selectedVariantId = "123"
        )
        val foundVariantId = viewModel.getSelectedProductIdFromBundleDetail(productBundleDetail)

        val productBundleDetail2 = ProductBundleDetail(
            productId = 123L,
            selectedVariantId = null
        )
        val foundProductId = viewModel.getSelectedProductIdFromBundleDetail(productBundleDetail2)

        assertEquals("123", foundVariantId)
        assertEquals("123", foundProductId)
    }

    @Test
    fun `when mapBundleDetailsToProductDetails data provided then should return correct value`() = coroutineTestRule.runBlockingTest {
        viewModel.parentProductID = 123
        val productDetailList1 = viewModel.mapBundleDetailsToProductDetails("2323", "2323", listOf(
            ProductBundleDetail(productId = 222)
        ))

        val productDetailList2 = viewModel.mapBundleDetailsToProductDetails("2323", "2323", listOf(
            ProductBundleDetail(selectedVariantId = "1234", productId = 123)
        ))

        assertEquals("222", productDetailList1.first().productId)
        assertEquals("1234", productDetailList2.first().productId)
    }

    @Test
    fun `when mapBundleItemsToBundleDetails data provided then should return correct value`() = coroutineTestRule.runBlockingTest {
        val nonVariant = viewModel.mapBundleItemsToBundleDetails(
            "",
            listOf(BundleItem())
        )

        val withVariant = viewModel.mapBundleItemsToBundleDetails(
            "",
            listOf(BundleItem(
                selections = listOf(Selection())
            ))
        )

        assertEquals(null, nonVariant.firstOrNull()?.productVariant)
        assertNotEquals(null, withVariant.firstOrNull()?.productVariant)
    }

    @Test
    fun `when setSelectedVariants data provided then should return correct value`() = coroutineTestRule.runBlockingTest {
        viewModel.setSelectedVariants(listOf(), ProductBundleMaster())
        viewModel.setSelectedVariants(listOf(""), ProductBundleMaster())

        coEvery {
            viewModel.getProductBundleDetails(any())
        } returns listOf(
            ProductBundleDetail(),
            ProductBundleDetail(productVariant = ProductVariant()),
            ProductBundleDetail(productVariant = ProductVariant(
                children = listOf(
                    VariantChild(productId = "1")
                )
            ))
        )

        viewModel.setSelectedVariants(listOf("1"), ProductBundleMaster())
        assert(viewModel.getProductBundleDetails(ProductBundleMaster())?.isNotEmpty() ?: false)
    }

    @Test
    fun `when validateAddToCartInput data provided then should return correct value`() = coroutineTestRule.runBlockingTest {
        // variantProductNotChanged test
        viewModel.pageSource = PAGE_SOURCE_CART
        viewModel.selectedBundleId = 1000L
        viewModel.selectedProductIds = listOf("1234")
        val result1 = viewModel.validateAddToCartInput(ProductBundleMaster(bundleId = 1000L), listOf())
        val result2 = viewModel.validateAddToCartInput(ProductBundleMaster(bundleId = 1000L), listOf( // not having variant
            ProductBundleDetail(
                selectedVariantId = "1234",
                productVariant = null
            )
        ))
        val result3 = viewModel.validateAddToCartInput(ProductBundleMaster(bundleId = 1000L), listOf(
            ProductBundleDetail(
                selectedVariantId = "1234",
                productVariant = ProductVariant(
                    variants = listOf(Variant())
                )
            )
        ))
        val result4 = viewModel.validateAddToCartInput(ProductBundleMaster(bundleId = 1000L), listOf(
            ProductBundleDetail(
                selectedVariantId = "5678",
                productVariant = ProductVariant(
                    variants = listOf(Variant())
                )
            )
        ))

        // isProductVariantSelectionComplete test
        val result5 = viewModel.validateAddToCartInput(ProductBundleMaster(), listOf(
            ProductBundleDetail(
                selectedVariantId = "123",
                productVariant = ProductVariant(
                    variants = listOf(Variant())
                )
            ),
            ProductBundleDetail(
                selectedVariantId = null,
                productVariant = null
            ),
            ProductBundleDetail(
                selectedVariantId = "123",
                productVariant = null
            ),
            ProductBundleDetail(
                selectedVariantId = null,
                productVariant = ProductVariant(
                    variants = listOf(Variant()),
                )
            )
        ))

        assertEquals(false, result1)
        assertEquals(false, result2)
        assertEquals(false, result3)
        assertEquals(true, result4)
        assertEquals(false, result5)
        assert(viewModel.selectedBundleId == 1000L)
        assert(viewModel.pageSource == PAGE_SOURCE_CART)

        // if pageSource is minicart should return false
        viewModel.pageSource = PAGE_SOURCE_MINI_CART
        val result6 = viewModel.validateAddToCartInput(ProductBundleMaster(bundleId = 1000), listOf())

        assertEquals(false, result6)
        assert(viewModel.pageSource == PAGE_SOURCE_MINI_CART)

        // if pageSource is psp should return true
        viewModel.pageSource = PAGE_SOURCE_PDP
        val result7 = viewModel.validateAddToCartInput(ProductBundleMaster(bundleId = 1000), listOf())

        assertEquals(true, result7)
        assert(viewModel.pageSource == PAGE_SOURCE_PDP)

        // if pageSource is minicart and selectedBundleId is 2000 but selectedBundleMasterId is 1000 then should return true
        viewModel.pageSource = PAGE_SOURCE_MINI_CART
        viewModel.selectedBundleId = 2000
        val result8 = viewModel.validateAddToCartInput(ProductBundleMaster(bundleId = 1000), listOf())

        assertEquals(true, result8)
        assert(viewModel.selectedBundleId == 2000L)
    }

    @Test
    fun `when getBundleInfo expect return correct value`() = coroutineTestRule.runBlockingTest {
        // negative case
        viewModel.getBundleInfo(123, "1")
        val pageStateError = viewModel.pageState.getOrAwaitValue()
        val getBundleInfoResultFail = viewModel.getBundleInfoResult.getOrAwaitValue()

        // positive case case
        coEvery {
            getBundleInfoUseCase.executeOnBackground()
        } returns GetBundleInfoResponse()

        viewModel.getBundleInfo(123, "1")
        coVerify { getBundleInfoUseCase.executeOnBackground() }
        val pageStateSuccess = viewModel.pageState.getOrAwaitValue()
        val getBundleInfoResultSuccess = viewModel.getBundleInfoResult.getOrAwaitValue()

        assertEquals(ProductBundleState.ERROR, pageStateError)
        assert(getBundleInfoResultFail is Fail)
        assertEquals(ProductBundleState.SUCCESS, pageStateSuccess)
        assert(getBundleInfoResultSuccess is Success)
    }

    @Test
    fun `when addProductBundleToCart expect return correct value`() = coroutineTestRule.runBlockingTest {
        // error local case
        coEvery {
            resourceProvider.getErrorMessage(any())
        } returns "error"

        viewModel.addProductBundleToCart(123, 123, 123, emptyList())
        val errorMessage = viewModel.errorMessage.getOrAwaitValue()

        // error server case
        coEvery {
            addToCartBundleUseCase.executeOnBackground()
        } returns AddToCartBundleModel(
            status = "OK",
            errorMessage = "Something Error",
            addToCartBundleDataModel = AddToCartBundleDataModel(
                success = 0,
                message = listOf("1", "2")
            )
        )

        viewModel.addProductBundleToCart(123, 123, 123, emptyList())
        coVerify { addToCartBundleUseCase.executeOnBackground() }
        val atcDialogMessagesError1 = viewModel.atcDialogMessages.getOrAwaitValue()

        // error server case
        coEvery {
            addToCartBundleUseCase.executeOnBackground()
        } returns AddToCartBundleModel(
            status = "OK",
            errorMessage = "Something Error",
            addToCartBundleDataModel = AddToCartBundleDataModel(
                success = 0,
                message = listOf()
            )
        )

        viewModel.addProductBundleToCart(123, 123, 123, emptyList())
        coVerify { addToCartBundleUseCase.executeOnBackground() }
        val atcDialogMessagesError2 = viewModel.atcDialogMessages.getOrAwaitValue()

        // error server case
        coEvery {
            addToCartBundleUseCase.executeOnBackground()
        } returns AddToCartBundleModel(
            status = "Error",
            errorMessage = "Something Error",
            addToCartBundleDataModel = AddToCartBundleDataModel()
        )

        viewModel.addProductBundleToCart(123, 123, 123, emptyList())
        coVerify { addToCartBundleUseCase.executeOnBackground() }
        val errorMessageServer = viewModel.errorMessage.getOrAwaitValue()

        // success server case
        coEvery {
            addToCartBundleUseCase.executeOnBackground()
        } returns AddToCartBundleModel(
            status = "OK",
            addToCartBundleDataModel = AddToCartBundleDataModel(
                success = 1,
                message = emptyList()
            )
        )

        viewModel.addProductBundleToCart(123, 123, 123, emptyList())
        coVerify { addToCartBundleUseCase.executeOnBackground() }
        val addToCartResultSuccess = viewModel.addToCartResult.getOrAwaitValue()

        assertEquals(1, addToCartResultSuccess.responseResult.success)
        assertEquals("error", errorMessage)
        assertEquals("1", atcDialogMessagesError1.first)
        assertEquals("2", atcDialogMessagesError1.second)
        assertEquals("", atcDialogMessagesError2.first)
        assertEquals("", atcDialogMessagesError2.second)
        assertEquals(AtcConstant.ATC_ERROR_GLOBAL, errorMessageServer)
    }

    @Test
    fun `when resetBundleMap expect empty getProductBundleMasters`() = coroutineTestRule.runBlockingTest {
        viewModel.resetBundleMap()
        assert(viewModel.getProductBundleMasters().isEmpty())
    }

    @Test
    fun `when updateProductBundleMap expect filled getProductBundleMasters`() = coroutineTestRule.runBlockingTest {
        viewModel.updateProductBundleMap(ProductBundleMaster(), listOf(ProductBundleDetail()))
        assert(viewModel.getProductBundleMasters().isNotEmpty())
    }

    @Test
    fun `when getBundleInfo using 0 product id expect setParams with list of Bundle`() = coroutineTestRule.runBlockingTest {
        viewModel.getBundleInfo(0, "1")
        coVerify { getBundleInfoUseCase.setParams(any(), any(), any(), any(), listOf(Bundle(ID = "0", WarehouseID = "1"))) }
    }

    @Test
    fun `when ProductBundleMaster data provided then should return correct value`() = coroutineTestRule.runBlockingTest {
        val observer = mockk<Observer<ProductBundleMaster>>(relaxed = true)
        nonSpykViewModel2.selectedProductBundleMaster.observeForever(observer)
        // given
        val bundleInfo = BundleInfo(
            bundleID = 123L,
            bundleStats = BundleStats("2")
        )
        val bundleMaster = nonSpykViewModel2.mapBundleInfoToBundleMaster(bundleInfo)

        // when
        val bundleMasterStartState = nonSpykViewModel2.getSelectedProductBundleMaster()
        val selectedBundleEmptyList = nonSpykViewModel2.getSelectedProductBundleDetails()
        nonSpykViewModel2.setSelectedProductBundleMaster(bundleMaster)
        nonSpykViewModel2.updateProductBundleMap(bundleMaster, listOf(ProductBundleDetail(productId=123)))
        val bundleMasterNew = nonSpykViewModel2.getSelectedProductBundleMaster()
        val selectedBundleList = nonSpykViewModel2.getSelectedProductBundleDetails()

        // then
        assertEquals(ProductBundleMaster(), bundleMasterStartState)
        assertEquals(listOf<ProductBundleDetail>(), selectedBundleEmptyList)
        assertEquals(123L, bundleMasterNew.bundleId)
        assertEquals(123, selectedBundleList.first().productId)
        assertEquals(2,bundleMaster.totalSold)
    }

    @Test
    fun `updateProductBundleDetail should return correct value`() = coroutineTestRule.runBlockingTest {
        // given
        viewModel.updateProductBundleMap(ProductBundleMaster(), listOf(
            ProductBundleDetail(productId = 123L),
            ProductBundleDetail(),
            ProductBundleDetail(productVariant = ProductVariant(
                children = listOf(
                    VariantChild(productId = "1111", optionIds = listOf("1"), optionName = listOf("merah"), picture = Picture(url100 = "")),
                    VariantChild(productId = "1111", optionIds = listOf("1"), optionName = listOf("merah"), picture = null)
                ),
                variants = listOf(Variant(pv = "1111", name = "Warna", options = listOf(
                    VariantOption(id = "1", value = "merah")
                )))
            ), productId = 456L)
        ))

        // when
        val resultNotMatch = viewModel.updateProductBundleDetail(ProductBundleMaster(), 111L, 0L)
        val resultMatch = viewModel.updateProductBundleDetail(ProductBundleMaster(), 123L, 0L)
        val resultMatchWithVariant = viewModel.updateProductBundleDetail(ProductBundleMaster(), 456L, 1111L)

        // then
        assert(resultMatch != null)
        assert(resultMatchWithVariant?.selectedVariantText.orEmpty().isNotEmpty())
        assert(resultNotMatch == null)
    }
}
