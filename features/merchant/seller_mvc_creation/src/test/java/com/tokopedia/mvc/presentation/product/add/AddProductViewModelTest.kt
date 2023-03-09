package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductMetadata
import com.tokopedia.mvc.domain.entity.ProductResult
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import com.tokopedia.mvc.domain.entity.Warehouse
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.domain.entity.enums.WarehouseType
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.GetShopWarehouseLocationUseCase
import com.tokopedia.mvc.domain.usecase.ProductListMetaUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.domain.usecase.ShopShowcasesByShopIDUseCase
import com.tokopedia.mvc.domain.usecase.VoucherValidationPartialUseCase
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductEvent
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductUiState
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class AddProductViewModelTest {

    @RelaxedMockK
    lateinit var getShopWarehouseLocationUseCase: GetShopWarehouseLocationUseCase

    @RelaxedMockK
    lateinit var getShopShowcasesByShopIDUseCase: ShopShowcasesByShopIDUseCase

    @RelaxedMockK
    lateinit var getProductListMetaUseCase: ProductListMetaUseCase

    @RelaxedMockK
    lateinit var getProductsUseCase: ProductListUseCase

    @RelaxedMockK
    lateinit var getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase

    @RelaxedMockK
    lateinit var voucherValidationPartialUseCase: VoucherValidationPartialUseCase

    companion object {
        private const val WAREHOUSE_ID : Long = 1
    }
    private lateinit var viewModel: AddProductViewModel
    private val mockedWarehouse = Warehouse(warehouseId = WAREHOUSE_ID, warehouseName = "Bekasi", warehouseType = WarehouseType.WAREHOUSE)
    private val mockedSortOptions = listOf(ProductSortOptions("price", "Harga Terendah", "ASC"))
    private val mockedCategoryOption = listOf(ProductCategoryOption("electronic", "TV", "Televisi"))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = AddProductViewModel(
            CoroutineTestDispatchersProvider,
            getShopWarehouseLocationUseCase,
            getShopShowcasesByShopIDUseCase,
            getProductListMetaUseCase,
            getProductsUseCase,
            getInitiateVoucherPageUseCase,
            voucherValidationPartialUseCase
        )
    }

    //region FetchRequiredData
    @Test
    fun `when FetchRequiredData function first called, should set voucher configuration and previously selected products fields correctly`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()
            val previouslySelectedProducts = populateProduct()

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall()
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall()

            val emittedValues = arrayListOf<AddProductUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(AddProductEvent.FetchRequiredData(pageMode, voucherConfiguration, listOf(previouslySelectedProducts)))


            //Then
            val actual = emittedValues.last()

            assertEquals(voucherConfiguration, actual.voucherConfiguration)
            assertEquals(listOf(previouslySelectedProducts), actual.previouslySelectedProducts)

            job.cancel()
        }
    }


    //getProductsAndProductsMetadata success page mode create

    @Test
    fun `When get voucher metadata in voucher creation mode, action should be VoucherAction CREATE`() = runBlockingTest{
        val pageMode = PageMode.CREATE
        val voucherConfiguration = buildVoucherConfiguration()
        val previouslySelectedProducts = populateProduct()

        mockShopWarehouseGqlCall()
        mockInitiateVoucherPageGqlCall()
        mockGetProductListMetaGqlCall()
        mockGetShopShowcasesGqlCall()
        mockGetProductListGqlCall()
        mockVoucherValidationPartialGqlCall()


        //When
        viewModel.processEvent(AddProductEvent.FetchRequiredData(pageMode, voucherConfiguration, listOf(previouslySelectedProducts)))


        //Then
        val expectedParam = GetInitiateVoucherPageUseCase.Param(VoucherAction.CREATE, PromoType.FREE_SHIPPING, isVoucherProduct = true)
        coVerify { getInitiateVoucherPageUseCase.execute(expectedParam) }
    }

    //getProductsAndProductsMetadata success page mode edit
    @Test
    fun `When get voucher metadata in edit voucher mode, action should be VoucherAction UPDATE`() {
        runBlockingTest {
            val pageMode = PageMode.EDIT
            val voucherConfiguration = buildVoucherConfiguration()
            val previouslySelectedProducts = populateProduct()

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall()
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall()


            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )


            //Then
            val expectedParam = GetInitiateVoucherPageUseCase.Param(
                VoucherAction.UPDATE,
                PromoType.FREE_SHIPPING,
                isVoucherProduct = true
            )
            coVerify { getInitiateVoucherPageUseCase.execute(expectedParam) }
        }
    }

    //getProductsAndProductsMetadata success page mode duplicate
    @Test
    fun `When get voucher metadata in duplicate voucher mode, action should be VoucherAction UPDATE`() {
        runBlockingTest {
            val pageMode = PageMode.EDIT
            val voucherConfiguration = buildVoucherConfiguration()
            val previouslySelectedProducts = populateProduct()

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall()
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall()


            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )


            //Then
            val expectedParam = GetInitiateVoucherPageUseCase.Param(
                VoucherAction.UPDATE,
                PromoType.FREE_SHIPPING,
                isVoucherProduct = true
            )
            coVerify { getInitiateVoucherPageUseCase.execute(expectedParam) }
        }
    }

    //getProductsAndProductsMetadata success default warehouse is null
    @Test
    fun `When get seller warehouse location response return empty warehouse, should not proceed to get product list metadata to server`() {
        runBlockingTest {
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()
            val previouslySelectedProducts = populateProduct()
            val emptyWarehouses = emptyList<Warehouse>()

            mockShopWarehouseGqlCall(warehousesResponse = emptyWarehouses)
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall()
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall()


            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )


            //Then
            val expectedParam = ProductListMetaUseCase.Param(warehouseId = WAREHOUSE_ID)
            coVerify(exactly = 0) { getProductListMetaUseCase.execute(expectedParam) }
        }
    }


    //getProductsAndProductsMetadata success, should
    @Test
    fun `When get products and its metadata return success, should set the response to ui state correctly`() {
        runBlockingTest {
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()
            val previouslySelectedProducts = populateProduct()
            val expectedMaxProductSubmission = 100


            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = expectedMaxProductSubmission)
            mockGetProductListMetaGqlCall(warehouseId = WAREHOUSE_ID, sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall()

            val emittedValues = arrayListOf<AddProductUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )


            //Then
            val actual = emittedValues.last()

            assertEquals(expectedMaxProductSubmission, actual.maxProductSelection)
            assertEquals(listOf(mockedWarehouse), actual.warehouses)
            assertEquals(mockedWarehouse.warehouseId, actual.defaultWarehouseLocationId)
            assertEquals(mockedWarehouse, actual.selectedWarehouseLocation)
            assertEquals(mockedSortOptions, actual.sortOptions)
            assertEquals(mockedCategoryOption, actual.categoryOptions)
            assertEquals(voucherConfiguration.productIds.toSet(), actual.voucherConfiguration.productIds.toSet())

            job.cancel()
        }
    }

    //getProductsAndProductsMetadata error, ui effect should emit Error, ui state error should contain throwable


    @Test
    fun `When get products success, should return all ready stock parent products only`() {
        runBlockingTest {
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()
            val expectedMaxProductSubmission = 100
            val totalProduct = 20
            val previouslySelectedProducts = populateProduct()
            val readyStockProduct = populateProduct().copy(id = 1, preorder = Product.Preorder(durationDays = 0))
            val preorderProducts = populateProduct().copy(id = 2, preorder = Product.Preorder(durationDays = 1))

            val mockedProductResponse = listOf(readyStockProduct, preorderProducts)

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = expectedMaxProductSubmission)
            mockGetProductListMetaGqlCall(warehouseId = WAREHOUSE_ID, sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(productIds = listOf(readyStockProduct.id))

            val emittedValues = arrayListOf<AddProductUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )


            //Then
            val actual = emittedValues.last()


          //  assertEquals(false, actual.isLoading)
            assertEquals(listOf(readyStockProduct), actual.products)

           /* assertEquals(previouslySelectedProducts, actual.products)
            assertEquals(mockedWarehouse.warehouseId, actual.selectedProductsIds)
            assertEquals(mockedWarehouse, actual.selectedProductCount)
            assertEquals(mockedSortOptions, actual.checkboxState)*/
            assertEquals(totalProduct, actual.totalProducts)

            job.cancel()
        }
    }

    //getProducts success category ids is not empty
    //getProducts success showcase ids is not empty
    //getProducts success: has pre order product
    //getProducts success: has no pre order product


    //getProducts success currentPageParentProductsIds is not empty
    //getProducts error ui effect should emit Error, ui state error should contain throwable

    //voucher validation partial success
    //voucher validation partial error

    //endregion


    private fun buildVoucherConfiguration(): VoucherConfiguration {
        return VoucherConfiguration().copy(
            startPeriod = Date(2023, 1, 1, 0, 0, 0),
            endPeriod = Date(2023, 2, 1, 0, 0, 0)
        )
    }

    private fun populateProduct(): Product {
        return Product(
            id = 1,
            isVariant = false,
            name = "",
            picture = "",
            preorder = Product.Preorder(durationDays = 0),
            price = Product.Price(min = 50_000, max = 50_000),
            sku = "sku-1",
            status = "",
            stock = 20,
            txStats = Product.TxStats(sold = 10),
            warehouseCount = 1,
            isEligible = true,
            ineligibleReason = "",
            originalVariants = emptyList(),
            selectedVariantsIds = emptySet(),
            isSelected = false,
            enableCheckbox = true,
            isDeletable = false,
        )
    }


    private fun mockShopWarehouseGqlCall(warehousesResponse: List<Warehouse> = listOf(mockedWarehouse)) {
        val shopWarehouseParam = GetShopWarehouseLocationUseCase.Param()
        coEvery { getShopWarehouseLocationUseCase.execute(shopWarehouseParam) } returns warehousesResponse
    }

    private fun mockInitiateVoucherPageGqlCall(maxProductSubmission : Int = 100) {
        val initiateVoucherPageParam = GetInitiateVoucherPageUseCase.Param(
            VoucherAction.CREATE,
            PromoType.FREE_SHIPPING,
            isVoucherProduct = true
        )
        val initiateVoucherResponse = VoucherCreationMetadata(
            accessToken = "accessToken",
            isEligible = 1,
            maxProduct = maxProductSubmission,
            prefixVoucherCode = "OFC",
            shopId = 1,
            token = "token",
            userId = 1,
            discountActive = true,
            message = ""
        )
        coEvery { getInitiateVoucherPageUseCase.execute(initiateVoucherPageParam) } returns initiateVoucherResponse
    }

    private fun mockGetProductListMetaGqlCall(
        warehouseId: Long = WAREHOUSE_ID,
        sortOptions: List<ProductSortOptions> = listOf(),
        categoryOptions: List<ProductCategoryOption> = listOf()
    ) {
        val getProductListMetaParam = ProductListMetaUseCase.Param(warehouseId)
        val getProductListMetaResponse = ProductMetadata(sortOptions = sortOptions, categoryOptions = categoryOptions)
        coEvery { getProductListMetaUseCase.execute(getProductListMetaParam) } returns getProductListMetaResponse
    }

    private fun mockGetShopShowcasesGqlCall(showcases: List<ShopShowcase> = listOf()) {
        coEvery { getShopShowcasesByShopIDUseCase.execute() } returns showcases
    }

    private fun mockGetProductListGqlCall(
        warehouseId: Long = WAREHOUSE_ID,
        totalProduct: Int = 20,
        products: List<Product> = emptyList()
    ) {
        val getProductsParam = ProductListUseCase.Param(
            categoryIds = listOf(),
            page = 1,
            pageSize = 20,
            searchKeyword = "",
            showcaseIds = listOf(),
            sortDirection = "DESC",
            sortId = "DEFAULT",
            warehouseId = warehouseId
        )
        val getProductsResponse = ProductResult(total = totalProduct, products = products)
        coEvery { getProductsUseCase.execute(getProductsParam) } returns getProductsResponse
    }

    private fun mockVoucherValidationPartialGqlCall(productIds: List<Long> = emptyList()) {
        val voucherValidationParam = VoucherValidationPartialUseCase.Param(
            benefitIdr = 0,
            benefitMax = 0,
            benefitPercent = 0,
            benefitType = BenefitType.NOMINAL,
            promoType = PromoType.FREE_SHIPPING,
            isVoucherProduct = false,
            minPurchase = 0,
            productIds = productIds,
            targetBuyer = VoucherTargetBuyer.ALL_BUYER,
            couponName = "",
            isPublic = true,
            code = "",
            isPeriod = false,
            periodType = 3,
            periodRepeat = 0,
            totalPeriod = 1,
            startDate = "3923-02-01",
            endDate = "3923-03-01",
            startHour = "00:00",
            endHour = "00:00",
            quota = 0
        )

        val voucherValidationResponse = VoucherValidationResult(
            totalAvailableQuota = 5,
            validationDate = emptyList(),
            validationError = VoucherValidationResult.ValidationError(
                benefitIdr = "",
                benefitMax = "",
                benefitPercent = "",
                benefitType = "",
                code = "",
                couponName = "",
                couponType = "",
                dateEnd = "",
                dateStart = "",
                hourEnd = "",
                hourStart = "",
                image = "",
                imageSquare = "",
                isPublic = "",
                minPurchase = "",
                minPurchaseType = "",
                minimumTierLevel = "",
                quota = "",
            ),
            validationProduct = emptyList()
        )
        coEvery { voucherValidationPartialUseCase.execute(voucherValidationParam) } returns voucherValidationResponse
    }


}
