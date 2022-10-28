package com.tokopedia.vouchercreation.product.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.common.utils.ResourceProvider
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
import com.tokopedia.vouchercreation.product.create.domain.entity.MinimumPurchaseType
import com.tokopedia.vouchercreation.product.list.domain.model.response.*
import com.tokopedia.vouchercreation.product.list.domain.model.response.ProductListMetaResponse.*
import com.tokopedia.vouchercreation.product.list.domain.usecase.*
import com.tokopedia.vouchercreation.product.list.view.model.*
import com.tokopedia.vouchercreation.product.list.view.viewmodel.AddProductViewModel.Companion.ACTIVE
import com.tokopedia.vouchercreation.product.list.view.viewmodel.AddProductViewModel.Companion.BENEFIT_TYPE_IDR
import com.tokopedia.vouchercreation.product.list.view.viewmodel.AddProductViewModel.Companion.BENEFIT_TYPE_PERCENT
import com.tokopedia.vouchercreation.product.list.view.viewmodel.AddProductViewModel.Companion.COUPON_TYPE_CASHBACK
import com.tokopedia.vouchercreation.product.list.view.viewmodel.AddProductViewModel.Companion.COUPON_TYPE_SHIPPING
import com.tokopedia.vouchercreation.product.list.view.viewmodel.AddProductViewModel.Companion.EMPTY_STRING
import com.tokopedia.vouchercreation.product.list.view.viewmodel.AddProductViewModel.Companion.SORT_DEFAULT
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.lang.reflect.Method

class AddProductViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private lateinit var mViewModel: AddProductViewModel

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @RelaxedMockK
    lateinit var getProductListUseCase: GetProductListUseCase

    @RelaxedMockK
    lateinit var validateVoucherUseCase: ValidateVoucherUseCase

    @RelaxedMockK
    lateinit var getWarehouseLocationsUseCase: GetWarehouseLocationsUseCase

    @RelaxedMockK
    lateinit var getShowCasesByIdUseCase: GetShowCasesByIdUseCase

    @RelaxedMockK
    lateinit var getProductListMetaDataUseCase: GetProductListMetaDataUseCase

    @RelaxedMockK
    lateinit var productListMetaUiModel: ProductListMetaResponse

    @RelaxedMockK
    lateinit var voucherValidationPartialUiModel: VoucherValidationPartialResponse

    @RelaxedMockK
    lateinit var productListUiModel: ProductListResponse

    @RelaxedMockK
    lateinit var shopLocGetWarehouseByShopIdsUiModel: ShopLocGetWarehouseByShopIdsResponse

    @RelaxedMockK
    lateinit var shopShowcasesByShopIdUiModel: ShopShowcasesByShopIdResponse

    private lateinit var getFormattedSku: Method
    private lateinit var getFormattedStatisticText: Method
    private lateinit var getFormattedProductPrice: Method

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel = AddProductViewModel(
            resourceProvider,
            testCoroutineDispatcherProvider,
            getProductListUseCase,
            validateVoucherUseCase,
            getWarehouseLocationsUseCase,
            getShowCasesByIdUseCase,
            getProductListMetaDataUseCase
        )

        getFormattedSku = mViewModel::class.java.getDeclaredMethod("getFormattedSku", String::class.java).apply {
            isAccessible = true
        }
        getFormattedStatisticText = mViewModel::class.java.getDeclaredMethod("getFormattedStatisticText", Int::class.java, Int::class.java).apply {
            isAccessible = true
        }
        getFormattedProductPrice = mViewModel::class.java.getDeclaredMethod("getFormattedProductPrice", Long::class.java).apply {
            isAccessible = true
        }
    }

    @After
    fun cleanup() {

    }

    @Test
    fun `success get product list`() {
        with(mViewModel) {
            val dummyResult = productListUiModel

            coEvery {
                getProductListUseCase.executeOnBackground()
            } returns dummyResult

            getProductList(
                1,
                "test",
                "1",
                warehouseLocationId = 0,
                shopShowCaseIds = listOf(),
                categoryList = listOf(),
                sort = null
            )

            coVerify {
                getProductListUseCase.executeOnBackground()
            }

            assert(productListResult.value == Success(dummyResult))
        }
    }

    @Test
    fun `fail get product list`() {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                getProductListUseCase.executeOnBackground()
            } throws dummyThrowable

            getProductList()

            coVerify {
                getProductListUseCase.executeOnBackground()
            }

            assert(productListResult.value is Fail)
        }
    }

    @Test
    fun `success validate product list`() {
        with(mViewModel) {
            val dummyResult = voucherValidationPartialUiModel

            coEvery {
                validateVoucherUseCase.executeOnBackground()
            } returns dummyResult

            validateProductList(
                benefitType = "",
                couponType = "",
                benefitIdr = 10,
                benefitMax = 10,
                benefitPercent = 10,
                minPurchase = 10,
                productIds = listOf()
            )

            coVerify {
                validateVoucherUseCase.executeOnBackground()
            }

            assert(validateVoucherResult.value == Success(dummyResult))
        }
    }

    @Test
    fun `fail validate product list`() {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                validateVoucherUseCase.executeOnBackground()
            } throws dummyThrowable

            validateProductList(
                benefitType = "",
                couponType = "",
                benefitIdr = 10,
                benefitMax = 10,
                benefitPercent = 10,
                minPurchase = 10,
                productIds = listOf()
            )

            coVerify {
                validateVoucherUseCase.executeOnBackground()
            }

            assert(validateVoucherResult.value is Fail)
        }
    }

    @Test
    fun `success get warehouse location`() {
        with(mViewModel) {
            val dummyResult = shopLocGetWarehouseByShopIdsUiModel

            coEvery {
                getWarehouseLocationsUseCase.executeOnBackground()
            } returns dummyResult

            getWarehouseLocations(0)

            coVerify {
                getWarehouseLocationsUseCase.executeOnBackground()
            }

            assert(getWarehouseLocationsResult.value == Success(dummyResult))
        }
    }

    @Test
    fun `fail get warehouse location`() {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                getWarehouseLocationsUseCase.executeOnBackground()
            } throws  dummyThrowable

            getWarehouseLocations(0)

            coVerify {
                getWarehouseLocationsUseCase.executeOnBackground()
            }

            assert(getWarehouseLocationsResult.value is Fail)
        }
    }

    @Test
    fun `success get product list meta data`() {
        with(mViewModel) {
            val dummyResult = productListMetaUiModel

            coEvery {
                getProductListMetaDataUseCase.executeOnBackground()
            } returns dummyResult

            getProductListMetaData(
                "",
                0
            )

            coVerify {
                getProductListMetaDataUseCase.executeOnBackground()
            }

            assert(getProductListMetaDataResult.value == Success(dummyResult))
        }
    }

    @Test
    fun `fail to get product list meta data`() {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                getProductListMetaDataUseCase.executeOnBackground()
            } throws dummyThrowable

            getProductListMetaData(
                "",
                0
            )

            coVerify {
                getProductListMetaDataUseCase.executeOnBackground()
            }

            assert(getProductListMetaDataResult.value is Fail)
        }
    }

    @Test
    fun `success get show cases by id`() {
        with(mViewModel) {
            val dummyResult = shopShowcasesByShopIdUiModel

            coEvery {
                getShowCasesByIdUseCase.executeOnBackground()
            } returns dummyResult

            getShopShowCases("")

            coVerify {
                getShowCasesByIdUseCase.executeOnBackground()
            }

            assert(getShowCasesByIdResult.value == Success(shopShowcasesByShopIdUiModel))
        }
    }

    @Test
    fun `fail to get show cases by id`() {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                getShowCasesByIdUseCase.executeOnBackground()
            } throws dummyThrowable

            getShopShowCases("")

            coVerify {
                getShowCasesByIdUseCase.executeOnBackground()
            }

            assert(getShowCasesByIdResult.value is Fail)
        }
    }

    @Test
    fun `success map product data to ui model`() {
        with(mViewModel) {
            val productDataList = listOf<ProductData>()

            assert(mapProductDataToProductUiModel(productDataList) == listOf<ProductUiModel>())
        }
    }

    @Test
    fun `when product data are mapped expect view model return product ui models`() {
        val dummyFormattedSku = "sku : ABC"
        coEvery {
            getFormattedSku.invoke(mViewModel, "abc")
        } returns dummyFormattedSku
        val dummyFormattedStatistic = "Stock 1000 | 100 terjual"
        coEvery {
            getFormattedStatisticText.invoke(mViewModel, 1000, 100)
        } returns dummyFormattedStatistic
        val dummyFormattedProductPrice = "Rp. 100.000"
        coEvery {
            getFormattedProductPrice.invoke(mViewModel,100000L)
        } returns dummyFormattedProductPrice
        val dummyProductData = listOf(
                ProductData(
                        pictures = listOf(GoodsPicture("urlThumbNail")),
                        id = "id",
                        name = "productName",
                        sku = "sku",
                        price = GoodsPriceRange(90000L, 100000L),
                        txStats = GoodsTxStats(100),
                        stock = 1000,
                        isVariant = true,
                        bundleCount = 0,
                        cashback = 0,
                        category = listOf(),
                        condition = "",
                        featured = 0,
                        hasInbound = true,
                        hasStockReserved = true,
                        isCOD = true,
                        isCampaign = true,
                        isEmptyStock = true,
                        isKreasiLokal = true,
                        isMustInsurance = true,
                        maxOrder = 0,
                        menu = listOf(),
                        minOrder = 0,
                        position = GoodsPosition(0),
                        score = GoodsScore(0.0),
                        status = "",
                        stockReserved = 0,
                        suspendLevel = 0,
                        url = "",
                        warehouse = listOf(),
                        warehouseCount = 0,
                        weight = 0,
                        weightUnit = ""
                )
        )
        val expectedProductUiModel = listOf(ProductUiModel(
                imageUrl = "urlThumbNail",
                id = "id",
                productName = "productName",
                sku = "sku : ABC",
                price = "Rp. 100.000",
                sold = 100,
                soldNStock = "Stock 1000 | 100 terjual",
                hasVariant = true
        ))
        val actualProductUiModel = mViewModel.mapProductDataToProductUiModel(dummyProductData)
        assertEquals(expectedProductUiModel, actualProductUiModel)
    }

    @Test
    fun `when selected warehouse id match and the type is seller warehouse expect new list with selected warehouse`() {
        val selectedWarehouseId = 1
        val warehouse1 = Warehouses(warehouseId = 1, warehouseType = 1, warehouseName = "abc")
        val warehouse2 = Warehouses(warehouseId = 2, warehouseType = 2, warehouseName = "def")
        val warehouses = listOf(warehouse1, warehouse2)
        val selection1 = WarehouseLocationSelection(warehouseId = 1, warehouseType = 1, isSelected = true, warehouseName = "abc")
        val selection2 = WarehouseLocationSelection(warehouseId = 2, warehouseType = 2, isSelected = false, warehouseName = "def")
        val expectedResult = listOf(selection1, selection2)
        val actualResult = mViewModel.mapWarehouseLocationToSelections(warehouses, selectedWarehouseId)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when seller warehouse id contained in the list return the warehouse id`() {
        val warehouse1 = Warehouses(warehouseId = 1, warehouseType = 1, warehouseName = "abc")
        val warehouse2 = Warehouses(warehouseId = 2, warehouseType = 2, warehouseName = "def")
        val warehouses = listOf(warehouse1, warehouse2)
        val expectedResult = 1
        val actualResult = mViewModel.getSellerWarehouseId(warehouses)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when showcases are mapped to view model expect show case selections`() {
        val showcase1 = ShopShowcase(id = "1", name = "etalase1")
        val showcase2 = ShopShowcase(id = "2", name = "etalase2")
        val showcases = listOf(showcase1, showcase2)
        val showcaseSelection1 = ShowCaseSelection(id = "1", name = "etalase1")
        val showcaseSelection2 = ShowCaseSelection(id = "2", name = "etalase2")
        val expectedResult = listOf(showcaseSelection1, showcaseSelection2)
        val actualResult = mViewModel.mapShopShowCasesToSelections(showcases)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when sort list are mapped to view model expect sort selections`() {
        val sort1 = Sort(id = "1", value = "sortA", name = "sortA")
        val sort2 = Sort(id = "2", value = "sortB", name = "sortB")
        val sorts = listOf(sort1, sort2)
        val sortSelection1 = SortSelection(id = "1", value = "sortA", name = "sortA")
        val sortSelection2 = SortSelection(id = "2", value = "sortB", name = "sortB")
        val expectedResult = listOf(sortSelection1, sortSelection2)
        val actualResult = mViewModel.mapSortListToSortSelections(sorts)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when exclude default sort expect no sort with sort default id`() {
        val sort1 = Sort(id = "1", value = "sortA", name = "sortA")
        val sort2 = Sort(id = "2", value = "sortB", name = "sortB")
        val sort3 = Sort(id = SORT_DEFAULT, value = "sortC", name = "sortC")
        val sorts = listOf(sort1, sort2, sort3)
        val expectedResult = listOf(sort1, sort2)
        val actualResult = mViewModel.excludeDefaultSortSelection(sorts)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when category list are mapped to view model category selections`() {
        val category1 = Category(id = "1", value = "value1", name = "name1")
        val category2 = Category(id = "2", value = "value2", name = "name2")
        val categories = listOf(category1, category2)
        val selection1 = CategorySelection(id = "1", value = "value1", name = "name1")
        val selection2 = CategorySelection(id = "2", value = "value2", name = "name2")
        val expectedResult = listOf(selection1, selection2)
        val actualResult = mViewModel.mapCategoriesToCategorySelections(categories)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when selected sort is empty set expect null selectedSort and selectedSortName`(){
        mViewModel.setSelectedSort(listOf())
        val expectedResult = null
        val actualResult1 = mViewModel.getSelectedSort()
        val actualResult2 = mViewModel.getSelectedSortName()
        assertTrue(expectedResult == actualResult1 && expectedResult == actualResult2)
    }

    @Test
    fun `when getSelectedProductIds function is called expect list of selected product ids`() {
        val productUiModel1 = ProductUiModel(id = "id1")
        val productUiModel2 = ProductUiModel(id = "id2")
        val selectedProductUiModels = arrayListOf(productUiModel1, productUiModel2)
        val expectedResult = listOf("id1", "id2")
        val actualResult = mViewModel.getSelectedProductIds(selectedProductUiModels)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getIdsFromProductList function is called expect list of product ids`() {
        val productUiModel1 = ProductUiModel(id = "id1")
        val productUiModel2 = ProductUiModel(id = "id2")
        val productUiModels = listOf(productUiModel1, productUiModel2)
        val expectedResult = listOf("id1", "id2")
        val actualResult = mViewModel.getIdsFromProductList(productUiModels)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when coupon type is free shipping expect benefit type idr`() {
        val couponSettings = CouponSettings(
                type = CouponType.FREE_SHIPPING,
                discountType = DiscountType.NONE,
                minimumPurchaseType = MinimumPurchaseType.NONE,
                discountAmount = 1000,
                discountPercentage = 10,
                maxDiscount = 10,
                quota = 10,
                minimumPurchase = 10,
                estimatedMaxExpense = 10
        )
        val expectedResult = BENEFIT_TYPE_IDR
        val actualResult = mViewModel.getBenefitType(couponSettings)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when coupon type is cashback and discount type is nominal expect benefit type idr`() {
        val couponSettings = CouponSettings(
                type = CouponType.CASHBACK,
                discountType = DiscountType.NOMINAL,
                minimumPurchaseType = MinimumPurchaseType.NONE,
                discountAmount = 1000,
                discountPercentage = 10,
                maxDiscount = 10,
                quota = 10,
                minimumPurchase = 10,
                estimatedMaxExpense = 10
        )
        val expectedResult = BENEFIT_TYPE_IDR
        val actualResult = mViewModel.getBenefitType(couponSettings)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when coupon type is cashback and discount type is percentage expect benefit type percent`() {
        val couponSettings = CouponSettings(
                type = CouponType.CASHBACK,
                discountType = DiscountType.PERCENTAGE,
                minimumPurchaseType = MinimumPurchaseType.NONE,
                discountAmount = 1000,
                discountPercentage = 10,
                maxDiscount = 10,
                quota = 10,
                minimumPurchase = 10,
                estimatedMaxExpense = 10
        )
        val expectedResult = BENEFIT_TYPE_PERCENT
        val actualResult = mViewModel.getBenefitType(couponSettings)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when coupon type is none expect benefit type idr`() {
        val couponSettings = CouponSettings(
                type = CouponType.NONE,
                discountType = DiscountType.NONE,
                minimumPurchaseType = MinimumPurchaseType.NONE,
                discountAmount = 1000,
                discountPercentage = 10,
                maxDiscount = 10,
                quota = 10,
                minimumPurchase = 10,
                estimatedMaxExpense = 10
        )
        val expectedResult = BENEFIT_TYPE_IDR
        val actualResult = mViewModel.getBenefitType(couponSettings)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when coupon settings type is none expect empty string`() {
        val couponSettings = CouponSettings(
                type = CouponType.NONE,
                discountType = DiscountType.NONE,
                minimumPurchaseType = MinimumPurchaseType.NONE,
                discountAmount = 1000,
                discountPercentage = 10,
                maxDiscount = 10,
                quota = 10,
                minimumPurchase = 10,
                estimatedMaxExpense = 10
        )
        val expectedResult = EMPTY_STRING
        val actualResult = mViewModel.getCouponType(couponSettings)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when coupon settings type is cashback expect empty string`() {
        val couponSettings = CouponSettings(
                type = CouponType.CASHBACK,
                discountType = DiscountType.NONE,
                minimumPurchaseType = MinimumPurchaseType.NONE,
                discountAmount = 1000,
                discountPercentage = 10,
                maxDiscount = 10,
                quota = 10,
                minimumPurchase = 10,
                estimatedMaxExpense = 10
        )
        val expectedResult = COUPON_TYPE_CASHBACK
        val actualResult = mViewModel.getCouponType(couponSettings)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when coupon settings type is free shipping expect empty string`() {
        val couponSettings = CouponSettings(
                type = CouponType.FREE_SHIPPING,
                discountType = DiscountType.NONE,
                minimumPurchaseType = MinimumPurchaseType.NONE,
                discountAmount = 1000,
                discountPercentage = 10,
                maxDiscount = 10,
                quota = 10,
                minimumPurchase = 10,
                estimatedMaxExpense = 10
        )
        val expectedResult = COUPON_TYPE_SHIPPING
        val actualResult = mViewModel.getCouponType(couponSettings)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getBenefitIdr function is called expect discount amount from coupon settings`() {
        val couponSettings = CouponSettings(
                type = CouponType.NONE,
                discountType = DiscountType.NONE,
                minimumPurchaseType = MinimumPurchaseType.NONE,
                discountAmount = 1000,
                discountPercentage = 10,
                maxDiscount = 10,
                quota = 10,
                minimumPurchase = 10,
                estimatedMaxExpense = 10
        )
        val expectedResult = 1000
        val actualResult = mViewModel.getBenefitIdr(couponSettings)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getBenefitMax function is called expect max discount from coupon settings`() {
        val couponSettings = CouponSettings(
                type = CouponType.NONE,
                discountType = DiscountType.NONE,
                minimumPurchaseType = MinimumPurchaseType.NONE,
                discountAmount = 1000,
                discountPercentage = 10,
                maxDiscount = 10,
                quota = 10,
                minimumPurchase = 10,
                estimatedMaxExpense = 10
        )
        val expectedResult = 10
        val actualResult = mViewModel.getBenefitMax(couponSettings)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getBenefitPercent function is called expect discount percentage from coupon settings`() {
        val couponSettings = CouponSettings(
                type = CouponType.NONE,
                discountType = DiscountType.NONE,
                minimumPurchaseType = MinimumPurchaseType.NONE,
                discountAmount = 1000,
                discountPercentage = 10,
                maxDiscount = 10,
                quota = 10,
                minimumPurchase = 10,
                estimatedMaxExpense = 10
        )
        val expectedResult = 10
        val actualResult = mViewModel.getBenefitPercent(couponSettings)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getMinimumPurchase function is called expect minimum purchase from coupon settings`() {
        val couponSettings = CouponSettings(
                type = CouponType.NONE,
                discountType = DiscountType.NONE,
                minimumPurchaseType = MinimumPurchaseType.NONE,
                discountAmount = 1000,
                discountPercentage = 10,
                maxDiscount = 10,
                quota = 10,
                minimumPurchase = 10,
                estimatedMaxExpense = 10
        )
        val expectedResult = 10
        val actualResult = mViewModel.getMinimumPurchase(couponSettings)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when selected products size is bigger than max limit expect isMaxProductLimitReached to be true`() {
        val maxProductLimit = 50
        val selectedProductsSize = 100
        mViewModel.setMaxProductLimit(maxProductLimit)
        assertTrue(mViewModel.isMaxProductLimitReached(selectedProductsSize))
    }

    @Test
    fun `when the page index is one expect isInitialLoad to be true`() {
        assertTrue(mViewModel.isInitialLoad(Int.ONE))
    }

    @Test
    fun `when exclude selected products expect new product list without the selected product`() {
        val productUiModel1 = ProductUiModel(id = "1")
        val productUiModel2 = ProductUiModel(id = "2")
        val productUiModel3 = ProductUiModel(id = "3")
        val dummyProductList = listOf(productUiModel1,productUiModel2,productUiModel3)
        val expectedProductList = listOf(productUiModel1,productUiModel2)
        val selectedProductId = "3"
        val actualProductList = mViewModel.excludeSelectedProducts(dummyProductList, listOf(selectedProductId))
        assertEquals(expectedProductList,actualProductList)
    }

    @Test
    fun `when warehouse selection is different with origin expect location selection is changed`() {
        val dummyOriginId = 0
        val dummyWarehouseSelectionId = 1
        mViewModel.isLocationSelectionChanged(dummyOriginId,dummyWarehouseSelectionId)
    }

    @Test
    fun `when active tab is missing expect zero active product count`() {
        val tabs: List<Tab> = listOf()
        val expectedResult = Int.ZERO
        val actualResult = mViewModel.getTotalActiveProductCount(tabs)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when active tabs exist expect legit active product count`() {
        val tabs = listOf(
            Tab(
                id = ACTIVE,
                value = 100,
                name = "Test"
            )
        )
        val expectedResult = 100
        val actualResult = mViewModel.getTotalActiveProductCount(tabs)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when there is no active tabs expect zero active product count`() {
        val tabs = listOf(
            Tab(
                id = "Test",
                value = 100,
                name = "Test"
            )
        )
        val expectedResult = Int.ZERO
        val actualResult = mViewModel.getTotalActiveProductCount(tabs)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when product list size is less than total active product count expect load more to be available`() {
        val productListSize = 90
        val totalActiveProductCount = 100
        val actualResult = mViewModel.isLoadMoreAvailable(productListSize, totalActiveProductCount)
        assertTrue(actualResult)
    }

    @Test
    fun `when product list size is more than total active product count expect load more to be unavailable`() {
        val productListSize = 100
        val totalActiveProductCount = 100
        val actualResult = mViewModel.isLoadMoreAvailable(productListSize, totalActiveProductCount)
        assertFalse(actualResult)
    }

    @Test
    fun `product counter and total active product count should have zero default value`() {
        assertEquals(Int.ZERO, mViewModel.productCounter)
        assertEquals(Int.ZERO, mViewModel.totalActiveProductCount)
    }

    @Test
    fun `paging index setter and getter should perform correctly`() {
        mViewModel.setPagingIndex(10)
        assertEquals(10, mViewModel.getPagingIndex())
    }
}
