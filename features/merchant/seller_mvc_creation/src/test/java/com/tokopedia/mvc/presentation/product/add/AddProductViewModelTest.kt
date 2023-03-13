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
        private const val WAREHOUSE_ID : Long = 0
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
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
            )

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

    //getProducts success category ids is not empty
    @Test
    fun `when apply category filter, should call get products with correct categories ids`() {
        runBlockingTest {
            //Given

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall()
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
            )

            val emittedValues = arrayListOf<AddProductUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }
            val categories = listOf(
                ProductCategoryOption("1", "TV", "Televisi"),
                ProductCategoryOption("2", "Gaming", "PS 5")
            )

            //When
            viewModel.processEvent(AddProductEvent.ApplyCategoryFilter(categories))


            //Then
            val getProductsParam = ProductListUseCase.Param(
                categoryIds = listOf(1, 2),
                page = 1,
                pageSize = 20,
                searchKeyword = "",
                showcaseIds = listOf(),
                sortDirection = "DESC",
                sortId = "DEFAULT",
                warehouseId = WAREHOUSE_ID
            )

            coVerify { getProductsUseCase.execute(getProductsParam) }

            val actual = emittedValues.last()

            assertEquals(true, actual.isLoading)
            assertEquals(1, actual.page)
            assertEquals(emptyList<Product>(), actual.products)
            assertEquals(categories, actual.selectedCategories)
            assertEquals(true, actual.isFilterActive)
            job.cancel()
        }
    }
    //getProducts success showcase ids is not empty

    @Test
    fun `when apply showcase filter, should call get products with correct showcase ids`() {
        runBlockingTest {
            //Given
            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall()
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
            )

            val emittedValues = arrayListOf<AddProductUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }
            val showcases = listOf(
                ShopShowcase(1, "TV", "Televisi", 1, isSelected = false),
                ShopShowcase(2, "Gaming", "PS 5", 2, isSelected = false)
            )

            //When
            viewModel.processEvent(AddProductEvent.ApplyShowCaseFilter(showcases))


            //Then
            val getProductsParam = ProductListUseCase.Param(
                categoryIds = listOf(),
                page = 1,
                pageSize = 20,
                searchKeyword = "",
                showcaseIds = listOf(1, 2),
                sortDirection = "DESC",
                sortId = "DEFAULT",
                warehouseId = WAREHOUSE_ID
            )

            coVerify { getProductsUseCase.execute(getProductsParam) }

            val actual = emittedValues.last()

            assertEquals(true, actual.isLoading)
            assertEquals(1, actual.page)
            assertEquals(emptyList<Product>(), actual.products)
            assertEquals(showcases, actual.selectedShopShowcase)
            assertEquals(true, actual.isFilterActive)
            job.cancel()
        }
    }

    //getProductsAndProductsMetadata success page mode create

    @Test
    fun `When get voucher metadata in voucher creation mode, action should be VoucherAction CREATE`() = runBlockingTest {
        //Given
        val pageMode = PageMode.CREATE
        val voucherConfiguration = buildVoucherConfiguration()
        val previouslySelectedProducts = populateProduct()

        mockShopWarehouseGqlCall()
        mockInitiateVoucherPageGqlCall()
        mockGetProductListMetaGqlCall()
        mockGetShopShowcasesGqlCall()
        mockGetProductListGqlCall()
        mockVoucherValidationPartialGqlCall(
            "3923-02-01",
            "3923-03-01",
        )


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
            //Given
            val pageMode = PageMode.EDIT
            val voucherConfiguration = buildVoucherConfiguration()
            val previouslySelectedProducts = populateProduct()

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall()
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
            )


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
            //Given
            val pageMode = PageMode.EDIT
            val voucherConfiguration = buildVoucherConfiguration()
            val previouslySelectedProducts = populateProduct()

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall()
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
            )


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
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()
            val previouslySelectedProducts = populateProduct()
            val emptyWarehouses = emptyList<Warehouse>()

            mockShopWarehouseGqlCall(warehousesResponse = emptyWarehouses)
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall()
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
            )


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
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()
            val previouslySelectedProducts = populateProduct()
            val expectedMaxProductSubmission = 100


            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = expectedMaxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
            )

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
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()
            val expectedMaxProductSubmission = 100
            val previouslySelectedProducts = populateProduct()
            val readyStockProduct = populateProduct().copy(id = 1, preorder = Product.Preorder(durationDays = 0))
            val preorderProducts = populateProduct().copy(id = 2, preorder = Product.Preorder(durationDays = 1))

            val mockedProductResponse = listOf(readyStockProduct, preorderProducts)

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = expectedMaxProductSubmission)
            mockGetProductListMetaGqlCall(warehouseId = WAREHOUSE_ID, sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01", productIds = listOf(readyStockProduct.id)
            )

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

            assertEquals(false, actual.isLoading)
            assertEquals(listOf(readyStockProduct), actual.products)

            job.cancel()
        }
    }

    //region UT for shouldSelectProduct function to test isEligible property
    //getProducts success isEligible = false
    @Test
    fun `When validating two product eligibility to servers, and second product is ineligible then second product isEligible should be false`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)


            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = false, //Second product isEligible should be false
                    isVariant = false,
                    parentProductId = secondProduct.id,
                    reason = "",
                    variant = emptyList()
                )
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

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

            assertEquals(false, actual.isLoading)
            assertEquals(listOf(firstProduct, secondProduct.copy(isEligible = false, enableCheckbox = false)), actual.products)

            job.cancel()
        }
    }
    //endregion

    //region UT for shouldSelectProduct function to test isSelected property
    //getProducts success: isSelected should be false -> if product not in selection, isSelected should be false
    @Test
    fun `When product not exist on current selection then isSelected should be false`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = true)
            val secondProduct = populateProduct().copy(id = 2, isSelected = true)

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = secondProduct.id,
                    reason = "",
                    variant = emptyList()
                )
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

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

            assertEquals(false, actual.isLoading)
            assertEquals(
                listOf(
                    firstProduct.copy(isSelected = false),
                    secondProduct.copy(isSelected = false)
                ), actual.products
            )

            job.cancel()
        }
    }

    @Test
    fun `When product is exist on current selection then isSelected should be true`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = secondProduct.id,
                    reason = "",
                    variant = emptyList()
                )
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

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
            viewModel.processEvent(AddProductEvent.AddProductToSelection(firstProduct.id))

            viewModel.processEvent(AddProductEvent.LoadPage(page = 1))



            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(
                listOf(
                    firstProduct.copy(isSelected = true),
                    secondProduct,
                    firstProduct.copy(isSelected = true),
                    secondProduct
                ), actual.products
            )

            job.cancel()
        }
    }
    //getProducts success: isSelected should be false -> if checkbox "select all" is unchecked, isSelected should be false
    @Test
    fun `When has remaining product selection while checkbox select all is checked, then isSelected should be true`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = secondProduct.id,
                    reason = "",
                    variant = emptyList()
                )
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

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

            viewModel.processEvent(AddProductEvent.EnableSelectAllCheckbox)
            viewModel.processEvent(AddProductEvent.LoadPage(page = 1))


            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(
                listOf(
                    firstProduct.copy(isSelected = true),
                    secondProduct.copy(isSelected = true),
                    firstProduct.copy(isSelected = true),
                    secondProduct.copy(isSelected = true)
                ), actual.products
            )

            job.cancel()
        }
    }

    @Test
    fun `When has no remaining product selection while checkbox select all is checked, then isSelected should be false`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val maxProductSubmission = 1

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = secondProduct.id,
                    reason = "",
                    variant = emptyList()
                )
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

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
            viewModel.processEvent(AddProductEvent.EnableSelectAllCheckbox)

            viewModel.processEvent(AddProductEvent.LoadPage(page = 1))


            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(
                listOf(
                    firstProduct.copy(isSelected = true, enableCheckbox = true),
                    secondProduct.copy(isSelected = false, enableCheckbox = false),
                    firstProduct.copy(isSelected = true, enableCheckbox = true),
                    secondProduct.copy(isSelected = false, enableCheckbox = false)
                ), actual.products
            )

            job.cancel()
        }
    }

    @Test
    fun `When load second page of product while checkbox select all is checked and has remaining product selection, then isSelected should be true`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = secondProduct.id,
                    reason = "",
                    variant = emptyList()
                )
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

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
            viewModel.processEvent(AddProductEvent.EnableSelectAllCheckbox)


            val thirdProduct = populateProduct().copy(id = 3)
            val fourthProduct = populateProduct().copy(id = 4)
            mockGetProductListGqlCall(page = 2, products = listOf(thirdProduct, fourthProduct))

            val mockedSecondPageProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = thirdProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = fourthProduct.id,
                    reason = "",
                    variant = emptyList()
                )
            )
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(thirdProduct.id, fourthProduct.id),
                productValidationResponse = mockedSecondPageProductValidationResponse
            )



            viewModel.processEvent(AddProductEvent.LoadPage(page = 2))

            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(
                listOf(
                    firstProduct.copy(isSelected = true, enableCheckbox = true),
                    secondProduct.copy(isSelected = true, enableCheckbox = true),
                    thirdProduct.copy(isSelected = true, enableCheckbox = true),
                    fourthProduct.copy(isSelected = true, enableCheckbox = true)
                ), actual.products
            )

            job.cancel()
        }
    }

    //endregion


    //region getProducts
    //getProducts error ui effect should emit Error, ui state error should contain throwable

    //voucher validation partial success
    //voucher validation partial error

    //endregion

    //region handleCheckAllProduct
    @Test
    fun `When tick select all products checkbox and the product is not eligible, then make the product non selectable`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = false,
                    isVariant = false,
                    parentProductId = secondProduct.id,
                    reason = "",
                    variant = emptyList()
                )
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

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
            viewModel.processEvent(AddProductEvent.EnableSelectAllCheckbox)

            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(
                listOf(
                    firstProduct.copy(isSelected = true, enableCheckbox = true, isEligible = true),
                    secondProduct.copy(isSelected = false, enableCheckbox = false, isEligible = false)
                ),
                actual.products
            )

            job.cancel()
        }
    }

    @Test
    fun `When tick select all products checkbox while searching product name, then only select product from the search result`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = secondProduct.id,
                    reason = "",
                    variant = emptyList()
                )
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(searchKeyword = "", warehouseId = WAREHOUSE_ID, products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedValues = arrayListOf<AddProductUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            mockGetProductListGqlCall(searchKeyword = "some keyword..", warehouseId = WAREHOUSE_ID, products = mockedProductResponse)

            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )
            viewModel.processEvent(AddProductEvent.SearchProduct("some keyword.."))
            viewModel.processEvent(AddProductEvent.EnableSelectAllCheckbox)

            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(
                listOf(
                    firstProduct.copy(isSelected = true, enableCheckbox = true, isEligible = true),
                    secondProduct.copy(isSelected = true, enableCheckbox = true, isEligible = true)
                ),
                actual.products
            )

            job.cancel()
        }
    }
    //endregion

    //region handleUncheckAllProduct
    @Test
    fun `When uncheck all products, all products should be updated to non selected`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = secondProduct.id,
                    reason = "",
                    variant = emptyList()
                )
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

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
            viewModel.processEvent(AddProductEvent.AddProductToSelection(firstProduct.id))
            viewModel.processEvent(AddProductEvent.DisableSelectAllCheckbox)

            //Then
            val actual = emittedValues.last()


            assertEquals(AddProductUiState.CheckboxState.UNCHECKED, actual.checkboxState)
            assertEquals(emptySet<Long>(), actual.selectedProductsIds)
            assertEquals(0, actual.selectedProductCount)
            assertEquals(
                listOf(
                    firstProduct.copy(isSelected = false, enableCheckbox = true),
                    secondProduct.copy(isSelected = false, enableCheckbox = true)
                ),
                actual.products
            )

            job.cancel()
        }
    }
    //endregion

    //region handleAddProductToSelection
    @Test
    fun `When newly selected product count is equal to max allowed selection count, should disable other unselected products`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val thirdProduct = populateProduct().copy(id = 3, isSelected = false)
            val maxProductSubmission = 2

            val mockedProductResponse = listOf(firstProduct, secondProduct, thirdProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = secondProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = thirdProduct.id,
                    reason = "",
                    variant = emptyList()
                )
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse, totalProduct = mockedProductResponse.size)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id, thirdProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

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
            viewModel.processEvent(AddProductEvent.AddProductToSelection(firstProduct.id))
            viewModel.processEvent(AddProductEvent.AddProductToSelection(secondProduct.id))

            //Then
            val actual = emittedValues.last()


            assertEquals(AddProductUiState.CheckboxState.INDETERMINATE, actual.checkboxState)
            assertEquals(setOf<Long>(1, 2), actual.selectedProductsIds)
            assertEquals(2, actual.selectedProductCount)
            assertEquals(
                listOf(
                    firstProduct.copy(isSelected = true, enableCheckbox = true),
                    secondProduct.copy(isSelected = true, enableCheckbox = true),
                    thirdProduct.copy(isSelected = false, enableCheckbox = false)
                ),
                actual.products
            )

            job.cancel()
        }
    }

    @Test
    fun `When selected product count is same with total product count, checkbox should have CHECKED state`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val thirdProduct = populateProduct().copy(id = 3, isSelected = false)
            val maxProductSubmission = 2

            val mockedProductResponse = listOf(firstProduct, secondProduct, thirdProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = secondProduct.id,
                    reason = "",
                    variant = emptyList()
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = thirdProduct.id,
                    reason = "",
                    variant = emptyList()
                )
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse, totalProduct = 2)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id, thirdProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

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
            viewModel.processEvent(AddProductEvent.AddProductToSelection(firstProduct.id))
            viewModel.processEvent(AddProductEvent.AddProductToSelection(secondProduct.id))

            //Then
            val actual = emittedValues.last()


            assertEquals(AddProductUiState.CheckboxState.CHECKED, actual.checkboxState)
            assertEquals(setOf<Long>(1, 2), actual.selectedProductsIds)
            assertEquals(2, actual.selectedProductCount)
            assertEquals(
                listOf(
                    firstProduct.copy(isSelected = true, enableCheckbox = true),
                    secondProduct.copy(isSelected = true, enableCheckbox = true),
                    thirdProduct.copy(isSelected = false, enableCheckbox = false)
                ),
                actual.products
            )

            job.cancel()
        }
    }
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
        page : Int = 1,
        searchKeyword: String = "",
        warehouseId: Long = WAREHOUSE_ID,
        totalProduct: Int = 20,
        products: List<Product> = emptyList()
    ) {
        val getProductsParam = ProductListUseCase.Param(
            categoryIds = listOf(),
            page = page,
            pageSize = 20,
            searchKeyword = searchKeyword,
            showcaseIds = listOf(),
            sortDirection = "DESC",
            sortId = "DEFAULT",
            warehouseId = warehouseId
        )
        val getProductsResponse = ProductResult(total = totalProduct, products = products)
        coEvery { getProductsUseCase.execute(getProductsParam) } returns getProductsResponse
    }

    private fun mockVoucherValidationPartialGqlCall(
        startDate: String,
        endDate: String,
        productIds: List<Long> = emptyList(),
        productValidationResponse: List<VoucherValidationResult.ValidationProduct> = emptyList()
    ) {
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
            startDate = startDate,
            endDate = endDate,
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
            validationProduct = productValidationResponse
        )
        coEvery { voucherValidationPartialUseCase.execute(voucherValidationParam) } returns voucherValidationResponse
    }


}
