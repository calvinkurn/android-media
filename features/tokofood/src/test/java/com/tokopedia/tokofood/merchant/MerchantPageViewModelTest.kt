package com.tokopedia.tokofood.merchant

import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProduct
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductVariantParam
import com.tokopedia.tokofood.data.*
import com.tokopedia.tokofood.feature.merchant.domain.model.response.*
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CarouselDataType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.ProductListItemType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.tokofood.feature.merchant.presentation.model.*
import com.tokopedia.tokofood.utils.collectFromSharedFlow
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert.*
import org.junit.Test

class MerchantPageViewModelTest : MerchantPageViewModelTestFixture() {

    @Test
    fun `when view model is created expect initial properties to be false or empty`() {
        assertEquals(hashMapOf<String, Pair<Int, Int>>(), viewModel.productMap)
        assertEquals(mutableListOf<ProductListItem>(), viewModel.productListItems)
        assertEquals(listOf<CheckoutTokoFoodProduct>(), viewModel.selectedProducts)
        assertFalse(viewModel.isAddressManuallyUpdated)
        assertFalse(viewModel.isProductDetailBottomSheetVisible)
        assertEquals(listOf<TokoFoodCategoryFilter>(), viewModel.filterList)
        assertTrue(viewModel.filterNameSelected.isBlank())
        assertFalse(viewModel.isStickyBarVisible)
        assertEquals(null, viewModel.merchantData)
    }

    @Test
    fun `when variables values are being set, the value should reflect to the changes`() {
        val expectedProductListItems: MutableList<ProductListItem> = mutableListOf()
        val expectedSelectedProducts: List<CheckoutTokoFoodProduct> = listOf()
        val expectedIsAddressManuallyUpdated = false
        val expectedIsProductDetailBottomSheetVisible = false
        val expectedFilterList = listOf<TokoFoodCategoryFilter>()
        val expectedFilterNameSelected = ""
        val expectedIsStickyBarVisible = false
        val expectedMerchantData: TokoFoodGetMerchantData? = null

        viewModel.productListItems = expectedProductListItems
        viewModel.selectedProducts = expectedSelectedProducts
        viewModel.isAddressManuallyUpdated = expectedIsAddressManuallyUpdated
        viewModel.isProductDetailBottomSheetVisible = expectedIsProductDetailBottomSheetVisible
        viewModel.filterList = expectedFilterList
        viewModel.filterNameSelected = expectedFilterNameSelected
        viewModel.isStickyBarVisible = expectedIsStickyBarVisible
        viewModel.merchantData = expectedMerchantData

        assertEquals(expectedProductListItems, viewModel.productListItems)
        assertEquals(expectedSelectedProducts, viewModel.selectedProducts)
        assertEquals(expectedIsAddressManuallyUpdated, viewModel.isAddressManuallyUpdated)
        assertEquals(expectedIsProductDetailBottomSheetVisible, viewModel.isProductDetailBottomSheetVisible)
        assertEquals(expectedFilterList, viewModel.filterList)
        assertEquals(expectedFilterNameSelected, viewModel.filterNameSelected)
        assertEquals(expectedIsStickyBarVisible, viewModel.isStickyBarVisible)
        assertEquals(expectedMerchantData, viewModel.merchantData)
    }

    @Test
    fun `when getting position from card positions pair expect the first value to be dataset position`() {
        val datasetPosition = 1
        val adapterPosition = 0
        val testData = datasetPosition to adapterPosition
        val expectedResult = 1
        val actualResult = viewModel.getDataSetPosition(testData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getting position from card positions pair expect the second value to be adapter position`() {
        val datasetPosition = 0
        val adapterPosition = 1
        val testData = datasetPosition to adapterPosition
        val expectedResult = 1
        val actualResult = viewModel.getAdapterPosition(testData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when GetMerchantDataUseCase is success expect merchant data`() {
        coEvery {
            getMerchantDataUseCase.executeOnBackground()
        } returns generateTestMerchantData()
        viewModel.getMerchantData("merchantId", "latlong", "timezone")
        coVerify { getMerchantDataUseCase.executeOnBackground() }
        val expectedResponse = generateTestMerchantData()
        val actualResponse = viewModel.getMerchantDataResult.value
        assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when GetMerchantDataUseCase is failed expect fail response`() {
        coEvery {
            getMerchantDataUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.getMerchantData("merchantId", "latlong", "timezone")
        coVerify { getMerchantDataUseCase.executeOnBackground() }
        val actualResponse = viewModel.getMerchantDataResult.value
        assert(actualResponse is Fail)
    }

    @Test
    fun `when GetChosenAddressWarehouseLocUseCase is success expect chosen address`() {
        coEvery {
            getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any())
        } answers {
            firstArg<(GetStateChosenAddressResponse) -> Unit>().invoke(createChooseAddress().response)
        }
        viewModel.getChooseAddress("source")
        val expectedResponse = createChooseAddress().response
        val actualResponse = viewModel.chooseAddress.value
        assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    @Test
    fun `when GetChosenAddressWarehouseLocUseCase is failed expect fail response`() {
        coEvery {
            getChooseAddressWarehouseLocUseCase.getStateChosenAddress(any(), any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }
        viewModel.getChooseAddress("source")
        val actualResponse = viewModel.chooseAddress.value
        assert(actualResponse is Fail)
    }

    @Test
    fun `when mapping TokoFoodMerchantProfile to CarouselData expect legit CarouselData`() {
        val testData = generateTestTokoFoodMerchantProfile()
        coEvery { resourceProvider.getDistanceTitle() } returns "Jarak"
        coEvery { resourceProvider.getEstimationTitle() } returns "Tiba"
        coEvery { resourceProvider.getOpsHoursTitle() } returns "Jam Operasional"
        // rating
        val ratingData = CarouselData(
                carouselDataType = CarouselDataType.RATING,
                title = "totalRatingFmt",
                information = "ratingFmt"
        )
        // distance
        val distanceData = CarouselData(
                carouselDataType = CarouselDataType.DISTANCE,
                title = "Jarak",
                information = "distanceInformation",
                isWarning = false
        )
        // estimation
        val estimationData = CarouselData(
                carouselDataType = CarouselDataType.ETA,
                title = "Tiba",
                information = "etaInformation",
                isWarning = false
        )
        // ops hours
        val opsHoursData = CarouselData(
                carouselDataType = CarouselDataType.OPS_HOUR,
                title = "Jam Operasional",
                information = "opsHourInformation",
                isWarning = false
        )
        val expectedCarouselData = listOf(ratingData, distanceData, estimationData, opsHoursData)
        val actualCarouselData = viewModel.mapMerchantProfileToCarouselData(testData)
        assertEquals(expectedCarouselData, actualCarouselData)
    }

    @Test
    fun `when resource provider return null expect the titles to be empty`() {
        val testData = generateTestTokoFoodMerchantProfile()
        coEvery { resourceProvider.getDistanceTitle() } returns null
        coEvery { resourceProvider.getEstimationTitle() } returns null
        coEvery { resourceProvider.getOpsHoursTitle() } returns null
        val actualCarouselData = viewModel.mapMerchantProfileToCarouselData(testData)
        actualCarouselData.forEachIndexed { index, carouselData ->
            if (index != 0) {
                assertTrue(carouselData.title.isBlank())
            }
        }
    }

    @Test
    fun `when today is sunday expect isToday is true in MerchantOpsHour with day equal to sunday`() {
        val monday = MerchantOpsHour(
                initial = 'S',
                day = "Senin",
                time = "11:00 - 21:45",
                isWarning = false,
                isToday = false
        )
        val tuesday = MerchantOpsHour(
                initial = 'S',
                day = "Selasa",
                time = "11:00 - 21:45",
                isWarning = false,
                isToday = false
        )
        val wednesday = MerchantOpsHour(
                initial = 'R',
                day = "Rabu",
                time = "11:00 - 21:45",
                isWarning = false,
                isToday = false
        )
        val thursday = MerchantOpsHour(
                initial = 'K',
                day = "Kamis",
                time = "11:00 - 21:45",
                isWarning = false,
                isToday = false
        )
        val friday = MerchantOpsHour(
                initial = 'J',
                day = "Jumat",
                time = "11:00 - 21:45",
                isWarning = false,
                isToday = false
        )
        val saturday = MerchantOpsHour(
                initial = 'S',
                day = "Sabtu",
                time = "11:00 - 21:45",
                isWarning = false,
                isToday = false
        )
        val sunday = MerchantOpsHour(
                initial = 'M',
                day = "Minggu",
                time = "11:00 - 21:45",
                isWarning = false,
                isToday = true
        )
        val today = 1 // Calendar.SUNDAY = 1
        val testData = generateTestMerchantOpsHour()
        val expectedResult = listOf(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
        val actualResult = viewModel.mapOpsHourDetailsToMerchantOpsHours(today, testData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when TokoFoodCategoryCatalog is mapped to ProductListItems expect legit ProductListItems`() {
        coEvery { resourceProvider.getOutOfStockWording() } returns "Stok habis"
        val isShopClosed = false
        val expectedResult = generateExpectedProductListItems(isShopClosed)
        val testData = generateTestFoodCategories()
        val actualResult = viewModel.mapFoodCategoriesToProductListItems(isShopClosed, testData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when applying product selection expect ProductListItems with selected items`() {
        coEvery { resourceProvider.getOutOfStockWording() } returns "Stok habis"
        val isShopClosed = false
        val expectedResult = generateExpectedSelectedProductListItems(isShopClosed)
        val testFoodCategories = generateTestFoodCategories()
        val productListItems = viewModel.mapFoodCategoriesToProductListItems(isShopClosed, testFoodCategories)
        val testData = generateTestCheckoutTokoFoodProduct()
        val actualResult = viewModel.applyProductSelection(productListItems, testData)
        // index 0 is reserved for the header
        // non variant test
        assertEquals(expectedResult[1].productUiModel.isAtc, actualResult[1].productUiModel.isAtc)
        // with variant test
        assertEquals(expectedResult[3].productUiModel.isAtc, actualResult[3].productUiModel.isAtc)
        val expectedCustomOrderDetail = expectedResult[3].productUiModel.customOrderDetails.first()
        val expectedSelectedAddons = expectedCustomOrderDetail.customListItems.first().addOnUiModel?.selectedAddOns
        val actualCustomOrderDetail = actualResult[3].productUiModel.customOrderDetails.first()
        val actualSelectedAddons = actualCustomOrderDetail.customListItems.first().addOnUiModel?.selectedAddOns
        assertEquals(expectedSelectedAddons, actualSelectedAddons)
    }

    @Test
    fun `when applying product selection which empty products should return empty`() {
        coEvery { resourceProvider.getOutOfStockWording() } returns "Stok habis"
        val testData = generateTestCheckoutTokoFoodProduct()
        val actualResult = viewModel.applyProductSelection(listOf(), testData)
        assert(actualResult.isEmpty())
    }

    @Test
    fun `when getting product list items from view model expect ProductListItems with selected items`() {
        coEvery {
            getMerchantDataUseCase.executeOnBackground()
        } returns GetMerchantDataResponse(
                TokoFoodGetMerchantData(
                        ticker = TokoFoodTickerDetail(),
                        topBanner = TokoFoodTopBanner(),
                        merchantProfile = TokoFoodMerchantProfile(),
                        filters = listOf(),
                        categories = generateTestFoodCategories()
                )
        )
        viewModel.getMerchantData("merchantId", "latlong", "timezone")
        viewModel.selectedProducts = generateTestCheckoutTokoFoodProduct()
        val expectedResult = generateExpectedSelectedProductListItems(false)
        val actualResult = viewModel.getAppliedProductSelection()?: listOf()
        // index 0 is reserved for the header
        // non variant test
        assertEquals(expectedResult[1].productUiModel.isAtc, actualResult[1].productUiModel.isAtc)
        // with variant test
        assertEquals(expectedResult[3].productUiModel.isAtc, actualResult[3].productUiModel.isAtc)
        val expectedCustomOrderDetail = expectedResult[3].productUiModel.customOrderDetails.first()
        val expectedSelectedAddons = expectedCustomOrderDetail.customListItems.first().addOnUiModel?.selectedAddOns
        val actualCustomOrderDetail = actualResult[3].productUiModel.customOrderDetails.first()
        val actualSelectedAddons = actualCustomOrderDetail.customListItems.first().addOnUiModel?.selectedAddOns
        assertEquals(expectedSelectedAddons, actualSelectedAddons)
    }

    @Test
    fun `when getting product list items with promo is not shown expect mvc data is null`() {
        coEvery {
            getMerchantDataUseCase.executeOnBackground()
        } returns GetMerchantDataResponse(
            TokoFoodGetMerchantData(
                ticker = TokoFoodTickerDetail(),
                topBanner = TokoFoodTopBanner(isShown = false),
                merchantProfile = TokoFoodMerchantProfile(),
                filters = listOf(),
                categories = generateTestFoodCategories()
            )
        )
        viewModel.getMerchantData("merchantId", "latlong", "timezone")

        val actualResult = viewModel.mvcLiveData.value
        assert(actualResult == null)
    }

    @Test
    fun `when getting product list items with promo is shown expect mvc data is not null`() {
        coEvery {
            getMerchantDataUseCase.executeOnBackground()
        } returns GetMerchantDataResponse(
            TokoFoodGetMerchantData(
                ticker = TokoFoodTickerDetail(),
                topBanner = TokoFoodTopBanner(isShown = true),
                merchantProfile = TokoFoodMerchantProfile(),
                filters = listOf(),
                categories = generateTestFoodCategories()
            )
        )
        viewModel.getMerchantData("merchantId", "latlong", "timezone")

        val actualResult = viewModel.mvcLiveData.value
        assert(actualResult != null)
    }

    @Test
    fun `when getting applied selection product but no merchant data yet, expect return null`() {
        viewModel.getMerchantData("merchantId", "latlong", "timezone")
        val actualResult = viewModel.getAppliedProductSelection()
        assert(actualResult == null)
    }

    @Test
    fun `when getting applied selection product and merchant data failed, expect return null`() {
        coEvery {
            getMerchantDataUseCase.executeOnBackground()
        } throws Throwable()
        viewModel.getMerchantData("merchantId", "latlong", "timezone")
        val actualResult = viewModel.getAppliedProductSelection()
        assert(actualResult == null)
    }

    @Test
    fun `when mapping ProductUiModel to AtcRequestParam expect legit AtcRequestParam`() {
        val expectedResult = UpdateParam(
                productList = listOf(
                        UpdateProductParam(
                                productId = "bf3eba99-534d-4344-9cf8-6a46326feae0",
                                cartId = "cartId-garlicKnots",
                                notes = "",
                                quantity = 1,
                                variants = listOf()
                        )
                ),
                shopId = "shopId",
        )
        val testData = generateTestProductUiModel()
        val actualResult = viewModel.mapProductUiModelToAtcRequestParam("shopId", testData)
        assertEquals(expectedResult.productList.first().productId, actualResult.productList.first().productId)
        assertEquals(expectedResult.productList.first().cartId, actualResult.productList.first().cartId)
        assertEquals(expectedResult.productList.first().notes, actualResult.productList.first().notes)
        assertEquals(expectedResult.productList.first().quantity, actualResult.productList.first().quantity)
        assertEquals(expectedResult.productList.first().variants, actualResult.productList.first().variants)
        assertEquals(expectedResult.shopId, actualResult.shopId)
    }

    @Test
    fun `when mapping CustomOrderDetail to AtcRequestParam expect legit AtcRequestParam`() {
        val expectedResult = UpdateParam(
                productList = listOf(
                        UpdateProductParam(
                                productId = "bf3eba99-534d-4344-9cf8-6a46326feae0",
                                cartId = "cartId-garlicKnots",
                                notes = "",
                                quantity = 1,
                                variants = listOf(
                                        UpdateProductVariantParam(
                                                variantId = "d105b801-75de-4306-93a6-cc7124193042",
                                                optionId = "379913bf-e89e-4a26-a2e6-a650ebe77aef"
                                        )
                                )
                        )
                ),
                shopId = "shopId",
        )
        val testData = generateTestCustomOrderDetail()
        val actualResult = viewModel.mapCustomOrderDetailToAtcRequestParam(
                shopId = "shopId",
                productId = "bf3eba99-534d-4344-9cf8-6a46326feae0",
                customOrderDetail = testData
        )
        assertEquals(expectedResult.productList.first().productId, actualResult.productList.first().productId)
        assertEquals(expectedResult.productList.first().cartId, actualResult.productList.first().cartId)
        assertEquals(expectedResult.productList.first().notes, actualResult.productList.first().notes)
        assertEquals(expectedResult.productList.first().quantity, actualResult.productList.first().quantity)
        assertEquals(expectedResult.productList.first().variants.first().variantId, actualResult.productList.first().variants.first().variantId)
        assertEquals(expectedResult.productList.first().variants.first().optionId, actualResult.productList.first().variants.first().optionId)
        assertEquals(expectedResult.shopId, actualResult.shopId)
    }

    @Test
    fun `when mapping CartTokoFood to CustomOrderDetail using non variant product expect null`() {
        val expectedResult = null
        val testData = generateTestCartTokoFood()
        val productUiModel = ProductUiModel()
        val actualResult = viewModel.mapCartTokoFoodToCustomOrderDetail(testData, productUiModel)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when mapping CartTokoFood to CustomOrderDetail expect legit CustomOrderDetail`() {
        val expectedResult = generateExpectedCustomOrderDetail()
        val testData = generateTestCartTokoFood()
        val productUiModel = generateTestProductUiModelWithVariant()
        val actualResult = viewModel.mapCartTokoFoodToCustomOrderDetail(testData, productUiModel)
        assertEquals(expectedResult.cartId, actualResult?.cartId)
        assertEquals(expectedResult.subTotal, actualResult?.subTotal ?: 0.0, 0.0)
        assertEquals(expectedResult.subTotalFmt, actualResult?.subTotalFmt)
        assertEquals(expectedResult.qty, actualResult?.qty)
        assertEquals(expectedResult.customListItems.first(), actualResult?.customListItems?.first())
    }

    @Test
    fun `when mapping CartTokoFood should not reset addOnUiModel if the value is null`() {
        val expectedResult = generateExpectedCustomOrderDetail()
        val testData = generateTestCartTokoFood()
        val productUiModel = generateTestProductUiModelWithVariant(true)
        val actualResult = viewModel.mapCartTokoFoodToCustomOrderDetail(testData, productUiModel)
        assertEquals(expectedResult.cartId, actualResult?.cartId)
        assertEquals(expectedResult.subTotal, actualResult?.subTotal ?: 0.0, 0.0)
        assertEquals(expectedResult.subTotalFmt, actualResult?.subTotalFmt)
        assertEquals(expectedResult.qty, actualResult?.qty)
        assertTrue(actualResult?.customListItems?.all { it.addOnUiModel == null } == true)
    }

    @Test
    fun `when mapping CartTokoFood should not reset option isSelected if the value is empty`() {
        val expectedResult = generateExpectedCustomOrderDetail()
        val testData = generateTestCartTokoFood()
        val productUiModel = generateTestProductUiModelWithVariant(
            isAddOnUiModelNull = false,
            isOptionEmpty = true
        )
        val actualResult = viewModel.mapCartTokoFoodToCustomOrderDetail(testData, productUiModel)
        assertEquals(expectedResult.cartId, actualResult?.cartId)
        assertEquals(expectedResult.subTotal, actualResult?.subTotal ?: 0.0, 0.0)
        assertEquals(expectedResult.subTotalFmt, actualResult?.subTotalFmt)
        assertEquals(expectedResult.qty, actualResult?.qty)
        assertTrue(actualResult?.customListItems?.all { it.addOnUiModel?.options?.isEmpty() == true } == true)
    }

    @Test
    fun `when updateQuantity with productUiModel should emit notice to hit update API after debounce time`() {
        coroutineTestRule.runBlockingTest {
            viewModel.updateQuantityParam.collectFromSharedFlow(
                whenAction = {
                    viewModel.updateQuantity("123", ProductUiModel())
                    advanceTimeBy(500L)
                },
                then = {
                    assert(it != null)
                }
            )
        }
    }

    @Test
    fun `when updateQuantity to replace with productUiModel should emit notice to hit update API after debounce time`() {
        coroutineTestRule.runBlockingTest {
            val expectedOrderQty = 5
            viewModel.updateQuantityParam.collectFromSharedFlow(
                whenAction = {
                    viewModel.updateQuantity("123", ProductUiModel())
                    advanceTimeBy(100L)
                    viewModel.updateQuantity("123", ProductUiModel(orderQty = expectedOrderQty))
                    advanceTimeBy(500L)
                },
                then = {
                    assertEquals(expectedOrderQty, it?.productList?.firstOrNull()?.quantity)
                }
            )
        }
    }

    @Test
    fun `when updateQuantity with productUiModel should not emit notice to hit update API before debounce time`() {
        coroutineTestRule.runBlockingTest {
            viewModel.updateQuantityParam.collectFromSharedFlow(
                whenAction = {
                    viewModel.updateQuantity("123", ProductUiModel())
                    advanceTimeBy(100L)
                },
                then = {
                    assert(it == null)
                }
            )
        }
    }

    @Test
    fun `when updateQuantity with customOrderDetail should emit notice to hit update API after debounce time`() {
        coroutineTestRule.runBlockingTest {
            viewModel.updateQuantityParam.collectFromSharedFlow(
                whenAction = {
                    viewModel.updateQuantity("123", "123", CustomOrderDetail())
                    advanceTimeBy(500L)
                },
                then = {
                    assert(it != null)
                }
            )
        }
    }

    @Test
    fun `when updateQuantity with customOrderDetail should not emit notice to hit update API before debounce time`() {
        coroutineTestRule.runBlockingTest {
            viewModel.updateQuantityParam.collectFromSharedFlow(
                whenAction = {
                    viewModel.updateQuantity("123", "123", CustomOrderDetail())
                    advanceTimeBy(100L)
                },
                then = {
                    assert(it == null)
                }
            )
        }
    }

    @Test
    fun `when updateQuantity to replace with customOrderDetail should emit notice to hit update API after debounce time`() {
        coroutineTestRule.runBlockingTest {
            val cartId = "123"
            val expectedQty = 10
            viewModel.updateQuantityParam.collectFromSharedFlow(
                whenAction = {
                    viewModel.updateQuantity("123", "123", CustomOrderDetail(cartId = cartId, qty = 0))
                    advanceTimeBy(100L)
                    viewModel.updateQuantity("123", "123", CustomOrderDetail(cartId = cartId, qty = expectedQty))
                    advanceTimeBy(500L)
                },
                then = {
                    assertEquals(expectedQty, it?.productList?.firstOrNull()?.quantity)
                }
            )
        }
    }

    @Test
    fun `when updateQuantity same productId with different cartId should emit different product params`() {
        coroutineTestRule.runBlockingTest {
            viewModel.updateQuantityParam.collectFromSharedFlow(
                whenAction = {
                    viewModel.updateQuantity("123", ProductUiModel(cartId = "123", id = "123"))
                    advanceTimeBy(100L)
                    viewModel.updateQuantity("123", ProductUiModel(cartId = "1234", id = "123"))
                    advanceTimeBy(500L)
                },
                then = {
                    assertEquals(2, it?.productList?.size)
                }
            )
        }
    }

    @Test
    fun `when updateQuantity same cartId with different productId should emit different product params`() {
        coroutineTestRule.runBlockingTest {
            viewModel.updateQuantityParam.collectFromSharedFlow(
                whenAction = {
                    viewModel.updateQuantity("123", ProductUiModel(cartId = "123", id = "123"))
                    advanceTimeBy(100L)
                    viewModel.updateQuantity("123", ProductUiModel(cartId = "123", id = "1234"))
                    advanceTimeBy(500L)
                },
                then = {
                    assertEquals(2, it?.productList?.size)
                }
            )
        }
    }

    @Test
    fun `when title and subtitle are empty expect isTickerDetailEmpty to be true`() {
        val emptyTickerData = TokoFoodTickerDetail()
        val expectedResult = true
        val actualResult = viewModel.isTickerDetailEmpty(emptyTickerData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when title or subtitle is not empty expect isTickerDetailEmpty to be false`() {
        val tickerData1 = TokoFoodTickerDetail(title = "title")
        val actualResult1 = viewModel.isTickerDetailEmpty(tickerData1)
        assertFalse(actualResult1)
        val tickerData2 = TokoFoodTickerDetail(subtitle = "subtitle")
        val actualResult2 = viewModel.isTickerDetailEmpty(tickerData2)
        assertFalse(actualResult2)
    }

    @Test
    fun `when title and subtitle are not empty expect isTickerDetailEmpty to be false`() {
        val tickerData = TokoFoodTickerDetail(title = "title", subtitle = "subtitle")
        val actualResult = viewModel.isTickerDetailEmpty(tickerData)
        assertFalse(actualResult)
    }

    private fun generateExpectedProductListItems(isShopClosed: Boolean): List<ProductListItem> {
        val categoryHeader = ProductListItem(
                listItemType = ProductListItemType.CATEGORY_HEADER,
                productCategory = CategoryUiModel(
                        id = "244f0661-515b-45ed-8969-c9f41fad2979",
                        key = "",
                        title = "Kick Off"
                )
        )
        val garlicKnots = ProductListItem(
                listItemType = ProductListItemType.PRODUCT_CARD,
                productUiModel = ProductUiModel(
                        id = "bf3eba99-534d-4344-9cf8-6a46326feae0",
                        name = "Home-plate Garlic Knots",
                        description = "What\u0027s better than garlic bread? 5 pcs of Garlic tangled in knots!",
                        imageURL = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e561a5c-55ef-47f6-8b6a-967ca35d229e_master-menu-item-image_1612931947827.jpg",
                        price = 38000.0,
                        priceFmt = "Rp38.000",
                        slashPrice = 0.0,
                        slashPriceFmt = "",
                        isOutOfStock = false,
                        isShopClosed = isShopClosed,
                        customListItems = listOf()
                )
        )
        val umamiEdamame = ProductListItem(
            listItemType = ProductListItemType.PRODUCT_CARD,
            productUiModel = ProductUiModel(
                id = "bf3eba99-534d-4344-9cf8-6a46326feae1",
                name = "Home-plate Garlic Knots",
                description = "What\u0027s better than garlic bread? 5 pcs of Garlic tangled in knots!",
                imageURL = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e561a5c-55ef-47f6-8b6a-967ca35d229e_master-menu-item-image_1612931947827.jpg",
                price = 38000.0,
                priceFmt = "Rp38.000",
                slashPrice = 0.0,
                slashPriceFmt = "",
                isOutOfStock = false,
                isShopClosed = isShopClosed,
                customListItems = listOf()
            )
        )
        val singleOriginal = OptionUiModel(
                isSelected = false,
                id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
                status = 1,
                name = "Original",
                price = 0.0,
                priceFmt = "Gratis",
                selectionControlType = SelectionControlType.SINGLE_SELECTION
        )
        val singleHot = OptionUiModel(
                isSelected = false,
                id = "8af415a2-3406-4536-b2b6-0561f7b68148",
                status = 1,
                name = "Hot",
                price = 0.0,
                priceFmt = "Gratis",
                selectionControlType = SelectionControlType.SINGLE_SELECTION
        )
        val multiOriginal = OptionUiModel(
                isSelected = false,
                id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
                status = 1,
                name = "Original",
                price = 0.0,
                priceFmt = "Gratis",
                selectionControlType = SelectionControlType.MULTIPLE_SELECTION
        )
        val multiHot = OptionUiModel(
                isSelected = false,
                id = "8af415a2-3406-4536-b2b6-0561f7b68148",
                status = 1,
                name = "Hot",
                price = 0.0,
                priceFmt = "Gratis",
                selectionControlType = SelectionControlType.MULTIPLE_SELECTION
        )
        val singleRequiredAddOnUiModel = AddOnUiModel(
                id = "d105b801-75de-4306-93a6-cc7124193042",
                name = "Spicy",
                isRequired = true,
                maxQty = 1,
                minQty = 1,
                options = listOf(singleOriginal, singleHot),
                outOfStockWording = "Stok habis"
        )
        val singleOptionalAddOnUiModel = AddOnUiModel(
                id = "d105b801-75de-4306-93a6-cc7124193042",
                name = "Spicy",
                isRequired = false,
                maxQty = 1,
                minQty = 0,
                options = listOf(multiOriginal, multiHot),
                outOfStockWording = "Stok habis"
        )
        val multipleOptionalAddOnUiModel = AddOnUiModel(
                id = "d105b801-75de-4306-93a6-cc7124193042",
                name = "Spicy",
                isRequired = false,
                maxQty = 2,
                minQty = 0,
                options = listOf(multiOriginal, multiHot),
                outOfStockWording = "Stok habis"
        )
        val singleToggleAddOnUiModel = AddOnUiModel(
            id = "d105b801-75de-4306-93a6-cc7124193042",
            name = "Spicy",
            isRequired = false,
            maxQty = 0,
            minQty = 0,
            options = listOf(singleOriginal, singleHot),
            outOfStockWording = "Stok habis"
        )
        val singleReqCustomListItem = CustomListItem(
                listItemType = CustomListItemType.PRODUCT_ADD_ON,
                addOnUiModel = singleRequiredAddOnUiModel
        )
        val singleOpCustomListItem = CustomListItem(
                listItemType = CustomListItemType.PRODUCT_ADD_ON,
                addOnUiModel = singleOptionalAddOnUiModel
        )
        val multiOpCustomListItem = CustomListItem(
                listItemType = CustomListItemType.PRODUCT_ADD_ON,
                addOnUiModel = multipleOptionalAddOnUiModel
        )
        val singleToggleCustomListItem = CustomListItem(
            listItemType = CustomListItemType.PRODUCT_ADD_ON,
            addOnUiModel = singleToggleAddOnUiModel
        )
        val noteCustomListItem = CustomListItem(
                listItemType = CustomListItemType.ORDER_NOTE_INPUT,
                addOnUiModel = null
        )
        val battingUpChicken = ProductListItem(
                listItemType = ProductListItemType.PRODUCT_CARD,
                productUiModel = ProductUiModel(
                        id = "8829ce4a-6f00-4406-8112-721f569f0d4b",
                        name = "Batting up chicken",
                        description = "4 pcs of deep-fried breaded chicken tenders served with sauce on the side",
                        imageURL = "https =//i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/27cb56de-1c92-4ca6-b797-f98777351524_master-menu-item-image_1612932163220.jpg",
                        price = 55000.0,
                        priceFmt = "Rp55.000",
                        slashPrice = 0.0,
                        slashPriceFmt = "",
                        isOutOfStock = false,
                        isShopClosed = false,
                        customListItems = listOf(singleReqCustomListItem, singleOpCustomListItem, multiOpCustomListItem, singleToggleCustomListItem, noteCustomListItem)
                )
        )
        val crispyFrenchFries = ProductListItem(
            listItemType = ProductListItemType.PRODUCT_CARD,
            productUiModel = ProductUiModel(
                id = "8829ce4a-6f00-4406-8112-721f569f0d4c",
                name = "Crispy French Fries",
                description = "4 pcs of deep-fried breaded chicken tenders served with sauce on the side",
                imageURL = "https =//i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/27cb56de-1c92-4ca6-b797-f98777351524_master-menu-item-image_1612932163220.jpg",
                price = 55000.0,
                priceFmt = "Rp55.000",
                slashPrice = 0.0,
                slashPriceFmt = "",
                isOutOfStock = false,
                isShopClosed = false,
                customListItems = listOf(singleReqCustomListItem, singleOpCustomListItem, multiOpCustomListItem, singleToggleCustomListItem, noteCustomListItem)
            )
        )
        return listOf(categoryHeader, garlicKnots, umamiEdamame, battingUpChicken, crispyFrenchFries)
    }

    private fun generateExpectedSelectedProductListItems(isShopClosed: Boolean): List<ProductListItem> {
        val categoryHeader = ProductListItem(
                listItemType = ProductListItemType.CATEGORY_HEADER,
                productCategory = CategoryUiModel(
                        id = "244f0661-515b-45ed-8969-c9f41fad2979",
                        key = "",
                        title = "Kick Off"
                )
        )
        val garlicKnots = ProductListItem(
                listItemType = ProductListItemType.PRODUCT_CARD,
                productUiModel = ProductUiModel(
                        cartId = "cartId-garlicKnots",
                        isAtc = true,
                        id = "bf3eba99-534d-4344-9cf8-6a46326feae0",
                        name = "Home-plate Garlic Knots",
                        description = "What\u0027s better than garlic bread? 5 pcs of Garlic tangled in knots!",
                        imageURL = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e561a5c-55ef-47f6-8b6a-967ca35d229e_master-menu-item-image_1612931947827.jpg",
                        price = 38000.0,
                        priceFmt = "Rp38.000",
                        slashPrice = 0.0,
                        slashPriceFmt = "",
                        isOutOfStock = false,
                        isShopClosed = isShopClosed,
                        customListItems = listOf()
                )
        )
        val umamiEdamame = ProductListItem(
            listItemType = ProductListItemType.PRODUCT_CARD,
            productUiModel = ProductUiModel(
                cartId = "cartId-garlicKnots",
                isAtc = true,
                id = "bf3eba99-534d-4344-9cf8-6a46326feae1",
                name = "Home-plate Garlic Knots",
                description = "What\u0027s better than garlic bread? 5 pcs of Garlic tangled in knots!",
                imageURL = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e561a5c-55ef-47f6-8b6a-967ca35d229e_master-menu-item-image_1612931947827.jpg",
                price = 38000.0,
                priceFmt = "Rp38.000",
                slashPrice = 0.0,
                slashPriceFmt = "",
                isOutOfStock = false,
                isShopClosed = isShopClosed,
                customListItems = listOf()
            )
        )
        val singleOriginal = OptionUiModel(
                isSelected = false,
                id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
                status = 1,
                name = "Original",
                price = 0.0,
                priceFmt = "Gratis",
                selectionControlType = SelectionControlType.SINGLE_SELECTION
        )
        val singleHot = OptionUiModel(
                isSelected = false,
                id = "8af415a2-3406-4536-b2b6-0561f7b68148",
                status = 1,
                name = "Hot",
                price = 0.0,
                priceFmt = "Gratis",
                selectionControlType = SelectionControlType.SINGLE_SELECTION
        )
        val multiOriginal = OptionUiModel(
                isSelected = false,
                id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
                status = 1,
                name = "Original",
                price = 0.0,
                priceFmt = "Gratis",
                selectionControlType = SelectionControlType.MULTIPLE_SELECTION
        )
        val multiHot = OptionUiModel(
                isSelected = false,
                id = "8af415a2-3406-4536-b2b6-0561f7b68148",
                status = 1,
                name = "Hot",
                price = 0.0,
                priceFmt = "Gratis",
                selectionControlType = SelectionControlType.MULTIPLE_SELECTION
        )
        val singleRequiredAddOnUiModel = AddOnUiModel(
                id = "d105b801-75de-4306-93a6-cc7124193042",
                name = "Spicy",
                isRequired = true,
                maxQty = 1,
                minQty = 1,
                options = listOf(singleOriginal, singleHot),
                outOfStockWording = "Stok habis"
        )
        val singleOptionalAddOnUiModel = AddOnUiModel(
                id = "d105b801-75de-4306-93a6-cc7124193042",
                name = "Spicy",
                isRequired = false,
                maxQty = 1,
                minQty = 0,
                options = listOf(multiOriginal, multiHot),
                outOfStockWording = "Stok habis"
        )
        val multipleOptionalAddOnUiModel = AddOnUiModel(
                id = "d105b801-75de-4306-93a6-cc7124193042",
                name = "Spicy",
                isRequired = false,
                maxQty = 2,
                minQty = 0,
                options = listOf(multiOriginal, multiHot),
                outOfStockWording = "Stok habis"
        )
        val singleToggleAddOnUiModel = AddOnUiModel(
            id = "d105b801-75de-4306-93a6-cc7124193042",
            name = "Spicy",
            isRequired = false,
            maxQty = 0,
            minQty = 0,
            options = listOf(singleOriginal, singleHot),
            outOfStockWording = "Stok habis"
        )
        val singleReqCustomListItem = CustomListItem(
                listItemType = CustomListItemType.PRODUCT_ADD_ON,
                addOnUiModel = singleRequiredAddOnUiModel
        )
        val singleOpCustomListItem = CustomListItem(
                listItemType = CustomListItemType.PRODUCT_ADD_ON,
                addOnUiModel = singleOptionalAddOnUiModel
        )
        val multiOpCustomListItem = CustomListItem(
                listItemType = CustomListItemType.PRODUCT_ADD_ON,
                addOnUiModel = multipleOptionalAddOnUiModel
        )
        val singleToggleCustomListItem = CustomListItem(
            listItemType = CustomListItemType.PRODUCT_ADD_ON,
            addOnUiModel = singleToggleAddOnUiModel
        )
        val noteCustomListItem = CustomListItem(
                listItemType = CustomListItemType.ORDER_NOTE_INPUT,
                addOnUiModel = null
        )
        val customOrderDetail = CustomOrderDetail(
                cartId = "cartId-battingUpChicken",
                subTotal = 55000.0,
                subTotalFmt = "Rp55.000",
                qty = 1,
                customListItems = listOf(
                        CustomListItem(
                                listItemType = CustomListItemType.PRODUCT_ADD_ON,
                                addOnUiModel = AddOnUiModel(
                                        id = "d105b801-75de-4306-93a6-cc7124193042",
                                        name = "Spicy",
                                        isError = false,
                                        isRequired = false,
                                        isSelected = true,
                                        selectedAddOns = listOf("Original"),
                                        maxQty = 0,
                                        minQty = 0,
                                        options = listOf(
                                                OptionUiModel(
                                                        isSelected = true,
                                                        id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
                                                        status = 0,
                                                        name = "Original",
                                                        price = 0.0,
                                                        priceFmt = "Gratis", selectionControlType = SelectionControlType.SINGLE_SELECTION)
                                        )
                                ),
                        ),
                        CustomListItem(listItemType = CustomListItemType.ORDER_NOTE_INPUT, addOnUiModel = null, orderNote = "")
                )
        )
        val battingUpChicken = ProductListItem(
                listItemType = ProductListItemType.PRODUCT_CARD,
                productUiModel = ProductUiModel(
                        cartId = "",
                        isAtc = true,
                        id = "8829ce4a-6f00-4406-8112-721f569f0d4b",
                        name = "Batting up chicken",
                        description = "4 pcs of deep-fried breaded chicken tenders served with sauce on the side",
                        imageURL = "https =//i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/27cb56de-1c92-4ca6-b797-f98777351524_master-menu-item-image_1612932163220.jpg",
                        price = 55000.0,
                        priceFmt = "Rp55.000",
                        slashPrice = 0.0,
                        slashPriceFmt = "",
                        isOutOfStock = false,
                        isShopClosed = false,
                        customListItems = listOf(singleReqCustomListItem, singleOpCustomListItem, multiOpCustomListItem, singleToggleCustomListItem, noteCustomListItem),
                        customOrderDetails = mutableListOf(customOrderDetail)
                )
        )
        val crispyFrenchFries = ProductListItem(
            listItemType = ProductListItemType.PRODUCT_CARD,
            productUiModel = ProductUiModel(
                cartId = "",
                isAtc = true,
                id = "8829ce4a-6f00-4406-8112-721f569f0d4c",
                name = "Crispy French Fries",
                description = "4 pcs of deep-fried breaded chicken tenders served with sauce on the side",
                imageURL = "https =//i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/27cb56de-1c92-4ca6-b797-f98777351524_master-menu-item-image_1612932163220.jpg",
                price = 55000.0,
                priceFmt = "Rp55.000",
                slashPrice = 0.0,
                slashPriceFmt = "",
                isOutOfStock = false,
                isShopClosed = false,
                customListItems = listOf(singleReqCustomListItem, singleOpCustomListItem, multiOpCustomListItem, singleToggleCustomListItem, noteCustomListItem),
                customOrderDetails = mutableListOf(customOrderDetail)
            )
        )
        return listOf(categoryHeader, garlicKnots, umamiEdamame, battingUpChicken, crispyFrenchFries)
    }

    private fun generateUnexpectedSelectedProductListItems(isShopClosed: Boolean): List<ProductListItem> {
        val categoryHeader = ProductListItem(
            listItemType = ProductListItemType.CATEGORY_HEADER,
            productCategory = CategoryUiModel(
                id = "244f0661-515b-45ed-8969-c9f41fad2979",
                key = "",
                title = "Kick Off"
            )
        )
        val garlicKnots = ProductListItem(
            listItemType = ProductListItemType.PRODUCT_CARD,
            productUiModel = ProductUiModel(
                cartId = "cartId-garlicKnots",
                isAtc = true,
                id = "bf3eba99-534d-4344-9cf8-6a46326feae9",
                name = "Home-plate Garlic Knots",
                description = "What\u0027s better than garlic bread? 5 pcs of Garlic tangled in knots!",
                imageURL = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e561a5c-55ef-47f6-8b6a-967ca35d229e_master-menu-item-image_1612931947827.jpg",
                price = 38000.0,
                priceFmt = "Rp38.000",
                slashPrice = 0.0,
                slashPriceFmt = "",
                isOutOfStock = false,
                isShopClosed = isShopClosed,
                customListItems = listOf()
            )
        )
        val umamiEdamame = ProductListItem(
            listItemType = ProductListItemType.PRODUCT_CARD,
            productUiModel = ProductUiModel(
                cartId = "cartId-garlicKnots",
                isAtc = true,
                id = "bf3eba99-534d-4344-9cf8-6a46326feae1",
                name = "Home-plate Garlic Knots",
                description = "What\u0027s better than garlic bread? 5 pcs of Garlic tangled in knots!",
                imageURL = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e561a5c-55ef-47f6-8b6a-967ca35d229e_master-menu-item-image_1612931947827.jpg",
                price = 38000.0,
                priceFmt = "Rp38.000",
                slashPrice = 0.0,
                slashPriceFmt = "",
                isOutOfStock = false,
                isShopClosed = isShopClosed,
                customListItems = listOf()
            )
        )
        val singleOriginal = OptionUiModel(
            isSelected = false,
            id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
            status = 1,
            name = "Original",
            price = 0.0,
            priceFmt = "Gratis",
            selectionControlType = SelectionControlType.SINGLE_SELECTION
        )
        val singleHot = OptionUiModel(
            isSelected = false,
            id = "8af415a2-3406-4536-b2b6-0561f7b68148",
            status = 1,
            name = "Hot",
            price = 0.0,
            priceFmt = "Gratis",
            selectionControlType = SelectionControlType.SINGLE_SELECTION
        )
        val multiOriginal = OptionUiModel(
            isSelected = false,
            id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
            status = 1,
            name = "Original",
            price = 0.0,
            priceFmt = "Gratis",
            selectionControlType = SelectionControlType.MULTIPLE_SELECTION
        )
        val multiHot = OptionUiModel(
            isSelected = false,
            id = "8af415a2-3406-4536-b2b6-0561f7b68148",
            status = 1,
            name = "Hot",
            price = 0.0,
            priceFmt = "Gratis",
            selectionControlType = SelectionControlType.MULTIPLE_SELECTION
        )
        val singleRequiredAddOnUiModel = AddOnUiModel(
            id = "d105b801-75de-4306-93a6-cc7124193042",
            name = "Spicy",
            isRequired = true,
            maxQty = 1,
            minQty = 1,
            options = listOf(singleOriginal, singleHot),
            outOfStockWording = "Stok habis"
        )
        val singleOptionalAddOnUiModel = AddOnUiModel(
            id = "d105b801-75de-4306-93a6-cc7124193042",
            name = "Spicy",
            isRequired = false,
            maxQty = 1,
            minQty = 0,
            options = listOf(multiOriginal, multiHot),
            outOfStockWording = "Stok habis"
        )
        val multipleOptionalAddOnUiModel = AddOnUiModel(
            id = "d105b801-75de-4306-93a6-cc7124193042",
            name = "Spicy",
            isRequired = false,
            maxQty = 2,
            minQty = 0,
            options = listOf(multiOriginal, multiHot),
            outOfStockWording = "Stok habis"
        )
        val singleToggleAddOnUiModel = AddOnUiModel(
            id = "d105b801-75de-4306-93a6-cc7124193042",
            name = "Spicy",
            isRequired = false,
            maxQty = 0,
            minQty = 0,
            options = listOf(singleOriginal, singleHot),
            outOfStockWording = "Stok habis"
        )
        val singleReqCustomListItem = CustomListItem(
            listItemType = CustomListItemType.PRODUCT_ADD_ON,
            addOnUiModel = singleRequiredAddOnUiModel
        )
        val singleOpCustomListItem = CustomListItem(
            listItemType = CustomListItemType.PRODUCT_ADD_ON,
            addOnUiModel = singleOptionalAddOnUiModel
        )
        val multiOpCustomListItem = CustomListItem(
            listItemType = CustomListItemType.PRODUCT_ADD_ON,
            addOnUiModel = multipleOptionalAddOnUiModel
        )
        val singleToggleCustomListItem = CustomListItem(
            listItemType = CustomListItemType.PRODUCT_ADD_ON,
            addOnUiModel = singleToggleAddOnUiModel
        )
        val noteCustomListItem = CustomListItem(
            listItemType = CustomListItemType.ORDER_NOTE_INPUT,
            addOnUiModel = null
        )
        val customOrderDetail = CustomOrderDetail(
            cartId = "cartId-battingUpChicken",
            subTotal = 55000.0,
            subTotalFmt = "Rp55.000",
            qty = 1,
            customListItems = listOf(
                CustomListItem(
                    listItemType = CustomListItemType.PRODUCT_ADD_ON,
                    addOnUiModel = AddOnUiModel(
                        id = "d105b801-75de-4306-93a6-cc7124193042",
                        name = "Spicy",
                        isError = false,
                        isRequired = false,
                        isSelected = true,
                        selectedAddOns = listOf("Original"),
                        maxQty = 0,
                        minQty = 0,
                        options = listOf(
                            OptionUiModel(
                                isSelected = true,
                                id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
                                status = 0,
                                name = "Original",
                                price = 0.0,
                                priceFmt = "Gratis", selectionControlType = SelectionControlType.SINGLE_SELECTION)
                        )
                    ),
                ),
                CustomListItem(listItemType = CustomListItemType.ORDER_NOTE_INPUT, addOnUiModel = null, orderNote = "")
            )
        )
        val battingUpChicken = ProductListItem(
            listItemType = ProductListItemType.PRODUCT_CARD,
            productUiModel = ProductUiModel(
                cartId = "",
                isAtc = true,
                id = "8829ce4a-6f00-4406-8112-721f569f0d4y",
                name = "Batting up chicken",
                description = "4 pcs of deep-fried breaded chicken tenders served with sauce on the side",
                imageURL = "https =//i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/27cb56de-1c92-4ca6-b797-f98777351524_master-menu-item-image_1612932163220.jpg",
                price = 55000.0,
                priceFmt = "Rp55.000",
                slashPrice = 0.0,
                slashPriceFmt = "",
                isOutOfStock = false,
                isShopClosed = false,
                customListItems = listOf(singleReqCustomListItem, singleOpCustomListItem, multiOpCustomListItem, singleToggleCustomListItem, noteCustomListItem),
                customOrderDetails = mutableListOf(customOrderDetail)
            )
        )
        val crispyFrenchFries = ProductListItem(
            listItemType = ProductListItemType.PRODUCT_CARD,
            productUiModel = ProductUiModel(
                cartId = "",
                isAtc = true,
                id = "8829ce4a-6f00-4406-8112-721f569f0d4c",
                name = "Crispy French Fries",
                description = "4 pcs of deep-fried breaded chicken tenders served with sauce on the side",
                imageURL = "https =//i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/27cb56de-1c92-4ca6-b797-f98777351524_master-menu-item-image_1612932163220.jpg",
                price = 55000.0,
                priceFmt = "Rp55.000",
                slashPrice = 0.0,
                slashPriceFmt = "",
                isOutOfStock = false,
                isShopClosed = false,
                customListItems = listOf(singleReqCustomListItem, singleOpCustomListItem, multiOpCustomListItem, singleToggleCustomListItem, noteCustomListItem),
                customOrderDetails = mutableListOf(customOrderDetail)
            )
        )
        return listOf(categoryHeader, garlicKnots, umamiEdamame, battingUpChicken, crispyFrenchFries)
    }

    private fun generateExpectedCustomOrderDetail() : CustomOrderDetail {
        val original = OptionUiModel(
                isSelected = true,
                id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
                status = 1,
                name = "Original",
                price = 0.0,
                priceFmt = "Gratis",
                selectionControlType = SelectionControlType.SINGLE_SELECTION
        )
        val hot = OptionUiModel(
                isSelected = false,
                id = "8af415a2-3406-4536-b2b6-0561f7b68148",
                status = 1,
                name = "Hot",
                price = 0.0,
                priceFmt = "Gratis",
                selectionControlType = SelectionControlType.SINGLE_SELECTION
        )
        val spicyAddOnUiModel = AddOnUiModel(
                id = "d105b801-75de-4306-93a6-cc7124193042",
                name = "Spicy",
                isRequired = true,
                isSelected = true,
                maxQty = 1,
                minQty = 1,
                options = listOf(hot, original),
                outOfStockWording = "Stok habis",
                selectedAddOns = listOf("Original")
        )
        val customListItem = CustomListItem(
                listItemType = CustomListItemType.PRODUCT_ADD_ON,
                addOnUiModel = spicyAddOnUiModel
        )
        return CustomOrderDetail(
                cartId = "cartId-garlicKnots",
                subTotal = 38000.0,
                subTotalFmt = "Rp38.000",
                qty = 1,
                customListItems = listOf(customListItem)
        )
    }
}
