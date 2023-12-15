package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductMetadata
import com.tokopedia.mvc.domain.entity.ProductResult
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.SelectedProduct
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
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductEffect
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductEvent
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductUiState
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    //endregion

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

    @Test
    fun `When product added to selection, should add eligible variants to the selection as well`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val expectedFirstProductVariant = listOf(
                Product.Variant(
                    variantProductId = 111,
                    isEligible = true,
                    reason = "",
                    isSelected = true
                ),
                Product.Variant(
                    variantProductId = 112,
                    isEligible = false,
                    reason = "Has been registered on another voucher",
                    isSelected = false
                ),
            )
            val firstProductVariants = listOf(
                VoucherValidationResult.ValidationProduct.ProductVariant(
                    productId = 111,
                    productName = "First Product - Red Variant",
                    price = 55_000,
                    stock = 10,
                    isEligible = true,
                    reason = ""
                ),
                VoucherValidationResult.ValidationProduct.ProductVariant(
                    productId = 112,
                    productName = "First Product - Blue Variant",
                    price = 55_000,
                    stock = 10,
                    isEligible = false,
                    reason = "Has been registered on another voucher"
                )
            )

            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = firstProductVariants
                )
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse, totalProduct = 50)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id),
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

            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstProduct.copy(
                        isSelected = true,
                        enableCheckbox = true,
                        originalVariants = expectedFirstProductVariant,
                        selectedVariantsIds = setOf<Long>(111)
                    ),
                ),
                actual.products
            )

            job.cancel()
        }
    }
    //endregion

    //region handleRemoveProductFromSelection

    @Test
    fun `When removing product with no variant, should mark the deleted product as non selected`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val expectedFirstProductVariant = listOf(
                Product.Variant(
                    variantProductId = 111,
                    isEligible = true,
                    reason = "",
                    isSelected = true
                ),
                Product.Variant(
                    variantProductId = 112,
                    isEligible = false,
                    reason = "",
                    isSelected = false
                ),
            )
            val firstProductVariants = listOf(
                VoucherValidationResult.ValidationProduct.ProductVariant(
                    productId = 111,
                    productName = "First Product - Red Variant",
                    price = 55_000,
                    stock = 10,
                    isEligible = true,
                    reason = ""
                ),
                VoucherValidationResult.ValidationProduct.ProductVariant(
                    productId = 112,
                    productName = "First Product - Blue Variant",
                    price = 55_000,
                    stock = 10,
                    isEligible = false,
                    reason = ""
                )
            )

            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = firstProductVariants
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
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse, totalProduct = 50)
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
            viewModel.processEvent(AddProductEvent.AddProductToSelection(secondProduct.id))

            viewModel.processEvent(AddProductEvent.RemoveProductFromSelection(secondProduct.id))

            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstProduct.copy(
                        isSelected = true,
                        enableCheckbox = true,
                        originalVariants = expectedFirstProductVariant,
                        selectedVariantsIds = setOf<Long>(111)
                    ),
                    secondProduct.copy(
                        isSelected = false,
                        enableCheckbox = true
                    )
                ),
                actual.products
            )
            assertEquals(AddProductUiState.CheckboxState.INDETERMINATE, actual.checkboxState)
            assertEquals(setOf<Long>(1), actual.selectedProductsIds)
            assertEquals(1, actual.selectedProductCount)

            job.cancel()
        }
    }

    @Test
    fun `When removing product with variant, should mark the deleted product as non selected`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val expectedFirstProductVariant = listOf(
                Product.Variant(
                    variantProductId = 111,
                    isEligible = true,
                    reason = "",
                    isSelected = true
                ),
                Product.Variant(
                    variantProductId = 112,
                    isEligible = false,
                    reason = "",
                    isSelected = false
                ),
            )
            val firstProductVariants = listOf(
                VoucherValidationResult.ValidationProduct.ProductVariant(
                    productId = 111,
                    productName = "First Product - Red Variant",
                    price = 55_000,
                    stock = 10,
                    isEligible = true,
                    reason = ""
                ),
                VoucherValidationResult.ValidationProduct.ProductVariant(
                    productId = 112,
                    productName = "First Product - Blue Variant",
                    price = 55_000,
                    stock = 10,
                    isEligible = false,
                    reason = ""
                )
            )

            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = firstProductVariants
                )
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse, totalProduct = 50)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id),
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
            viewModel.processEvent(AddProductEvent.RemoveProductFromSelection(firstProduct.id))

            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstProduct.copy(
                        isSelected = false,
                        enableCheckbox = true,
                        originalVariants = expectedFirstProductVariant,
                        selectedVariantsIds = setOf<Long>(111, 112)
                    )
                ),
                actual.products
            )
            assertEquals(AddProductUiState.CheckboxState.UNCHECKED, actual.checkboxState)
            assertEquals(emptySet<Long>(), actual.selectedProductsIds)
            assertEquals(0, actual.selectedProductCount)

            job.cancel()
        }
    }
    //endregion

    //region TapCategoryFilter
    @Test
    fun `When change category filter, should emit ShowProductCategoryBottomSheet effect`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf<VoucherValidationResult.ValidationProduct>()

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse, totalProduct = 50)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedEffects = arrayListOf<AddProductEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }


            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            viewModel.processEvent(AddProductEvent.TapCategoryFilter)

            //Then
            val actual = emittedEffects.last()

            assertEquals(
                AddProductEffect.ShowProductCategoryBottomSheet(mockedCategoryOption, listOf()),
                actual
            )


            job.cancel()
        }
    }
    //endregion

    //region TapWarehouseLocationFilter
    @Test
    fun `When change warehouse filter, should emit ShowWarehouseLocationBottomSheet effect`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val maxProductSubmission = 100
            val currentlySelectedWarehouse = mockedWarehouse

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf<VoucherValidationResult.ValidationProduct>()

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse, totalProduct = 50)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedEffects = arrayListOf<AddProductEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }


            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            viewModel.processEvent(AddProductEvent.TapWarehouseLocationFilter)

            //Then
            val actual = emittedEffects.last()

            assertEquals(
                AddProductEffect.ShowWarehouseLocationBottomSheet(listOf(mockedWarehouse) , currentlySelectedWarehouse),
                actual
            )


            job.cancel()
        }
    }
    //endregion

    //region TapShowCaseFilter
    @Test
    fun `When change shop showcase filter with no showcase id supplied, should emit ShowShowcasesBottomSheet effect with empty showcase id`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val maxProductSubmission = 100
            val selectedShowcaseIds = listOf<Long>()

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf<VoucherValidationResult.ValidationProduct>()
            val mockedShowcases = listOf(
                ShopShowcase(id = 2, alias = "hp", name = "Handphone", type = 1, isSelected = false)
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall(showcases = mockedShowcases)
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse, totalProduct = 50)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedEffects = arrayListOf<AddProductEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }


            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            viewModel.processEvent(AddProductEvent.TapShowCaseFilter)

            //Then
            val actual = emittedEffects.last()

            assertEquals(
                AddProductEffect.ShowShowcasesBottomSheet(mockedShowcases, selectedShowcaseIds),
                actual
            )

            job.cancel()
        }
    }

    @Test
    fun `When change shop showcase filter with showcase id supplied, should emit ShowShowcasesBottomSheet effect with the selected showcase ids`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val maxProductSubmission = 100


            val firstShopShowcase = ShopShowcase(id = 2, alias = "hp", name = "Handphone", type = 1, isSelected = false)
            val secondShopShowcase =  ShopShowcase(id = 3, alias = "ac", name = "Air Conditioner", type = 1, isSelected = false)
            val selectedShowcases = listOf(firstShopShowcase)
            val expectedSelectedShowcaseIds = listOf(firstShopShowcase.id)


            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf<VoucherValidationResult.ValidationProduct>()
            val mockedShowcases = listOf(firstShopShowcase, secondShopShowcase)

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall(showcases = mockedShowcases)
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse, totalProduct = 50)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedEffects = arrayListOf<AddProductEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }


            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            viewModel.processEvent(AddProductEvent.ApplyShowCaseFilter(selectedShowcases))
            viewModel.processEvent(AddProductEvent.TapShowCaseFilter)

            //Then
            val actual = emittedEffects.last()

            assertEquals(
                AddProductEffect.ShowShowcasesBottomSheet(
                    mockedShowcases,
                    expectedSelectedShowcaseIds
                ),
                actual
            )

            job.cancel()
        }
    }
    //endregion

    //region TapSortFilter
    @Test
    fun `When change product sort, should emit ShowSortBottomSheet effect`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val mockedProductValidationResponse = listOf<VoucherValidationResult.ValidationProduct>()

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall(maxProductSubmission = maxProductSubmission)
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse, totalProduct = 50)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedEffects = arrayListOf<AddProductEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }


            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            viewModel.processEvent(AddProductEvent.TapSortFilter)

            //Then
            val actual = emittedEffects.last()

            assertEquals(
                AddProductEffect.ShowSortBottomSheet(
                    mockedSortOptions,
                    ProductSortOptions("DEFAULT", "", "DESC")
                ),
                actual
            )

            job.cancel()
        }
    }
    //endregion

    //region handleClearSearchbar
    @Test
    fun `When clear searchbar, should reset the state before re-fetch the products`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
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
                productIds = listOf(firstProduct.id),
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
            viewModel.processEvent(AddProductEvent.ClearSearchBar)

            //Then
            val actual = emittedValues.last()

            assertEquals("", actual.searchKeyword)
            assertEquals(1, actual.page)
            assertEquals(AddProductUiState.CheckboxState.UNCHECKED, actual.checkboxState)

            job.cancel()
        }
    }
    //endregion

    //region handleClearFilter
    @Test
    fun `When clear filter, should reset the state before re-fetch the products`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
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
                productIds = listOf(firstProduct.id),
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
            viewModel.processEvent(AddProductEvent.ClearFilter)

            //Then
            val actual = emittedValues.last()

            assertEquals("", actual.searchKeyword)
            assertEquals(1, actual.page)
            assertEquals(emptyList<ProductCategoryOption>(), actual.selectedCategories)
            assertEquals(Warehouse(WAREHOUSE_ID, "", WarehouseType.DEFAULT_WAREHOUSE_LOCATION), actual.selectedWarehouseLocation)
            assertEquals(emptyList<ShopShowcase>(), actual.selectedShopShowcase)
            assertEquals(ProductSortOptions(id = "DEFAULT", name = "", value = "DESC"), actual.selectedSort)
            assertEquals(false, actual.isFilterActive)

            job.cancel()
        }
    }
    //endregion

    //region handleApplySortFilter
    @Test
    fun `When apply new sort with id other than DEFAULT, isFilterActive should be true`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
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
                productIds = listOf(firstProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedValues = arrayListOf<AddProductUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }
            val newSort = ProductSortOptions(id = "PRICE", name = "Harga", value = "ASC")

            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            mockGetProductListGqlCall(sortDirection = "ASC", sortId = "PRICE")

            viewModel.processEvent(AddProductEvent.ApplySortFilter(newSort))

            //Then
            val actual = emittedValues.last()

            assertEquals(1, actual.page)
            assertEquals(newSort, actual.selectedSort)
            assertEquals(true, actual.isFilterActive)



            job.cancel()
        }
    }

    @Test
    fun `When apply new sort with id equals to DEFAULT, isFilterActive should be false`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
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
                productIds = listOf(firstProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedValues = arrayListOf<AddProductUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }
            val newSort = ProductSortOptions(id = "DEFAULT", name = "", value = "ASC")

            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            mockGetProductListGqlCall(sortDirection = "ASC", sortId = "DEFAULT")

            viewModel.processEvent(AddProductEvent.ApplySortFilter(newSort))

            //Then
            val actual = emittedValues.last()

            assertEquals(1, actual.page)
            assertEquals(newSort, actual.selectedSort)
            assertEquals(false, actual.isFilterActive)



            job.cancel()
        }
    }
    //endregion

    //region handleApplyWarehouseLocationFilter
    @Test
    fun `When change warehouse filter to new different warehouse, should emit dialog confirmation event`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
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
                productIds = listOf(firstProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedEvent = arrayListOf<AddProductEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEvent)
            }
            val newWarehouse = Warehouse(warehouseId = 100, warehouseName = "Depok", warehouseType = WarehouseType.WAREHOUSE)

            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            viewModel.processEvent(AddProductEvent.ApplyWarehouseLocationFilter(newWarehouse))

            //Then
            val actual = emittedEvent.last()

            assertEquals(AddProductEffect.ShowChangeWarehouseDialogConfirmation(newWarehouse), actual)

            job.cancel()
        }
    }


    @Test
    fun `When change warehouse to the same warehouse, should fetch products from the selected warehouse`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
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
                productIds = listOf(firstProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedValues = arrayListOf<AddProductUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }
            val newWarehouse = Warehouse(warehouseId = WAREHOUSE_ID, warehouseName = "", warehouseType = WarehouseType.DEFAULT_WAREHOUSE_LOCATION)

            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            viewModel.processEvent(AddProductEvent.ApplyWarehouseLocationFilter(newWarehouse))

            //Then
            val actual = emittedValues.last()


            assertEquals(1, actual.page)
            assertEquals(AddProductUiState.CheckboxState.UNCHECKED, actual.checkboxState)
            assertEquals(emptySet<Long>(), actual.selectedProductsIds)
            assertEquals(0, actual.selectedProductCount)
            assertEquals(newWarehouse, actual.selectedWarehouseLocation)
            assertEquals(false, actual.isFilterActive)

            job.cancel()
        }
    }

    //endregion

    //region handleConfirmChangeWarehouseLocationFilter
    @Test
    fun `When change warehouse to the same warehouse, isFilterActive should be false and should fetch products from the selected warehouse`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
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
                productIds = listOf(firstProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedValues = arrayListOf<AddProductUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }
            val newWarehouse = Warehouse(warehouseId = WAREHOUSE_ID, warehouseName = "", warehouseType = WarehouseType.DEFAULT_WAREHOUSE_LOCATION)

            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            viewModel.processEvent(AddProductEvent.ConfirmChangeWarehouseLocationFilter(newWarehouse))

            //Then
            val actual = emittedValues.last()


             assertEquals(1, actual.page)
             assertEquals(AddProductUiState.CheckboxState.UNCHECKED, actual.checkboxState)
             assertEquals(emptySet<Long>(), actual.selectedProductsIds)
             assertEquals(0, actual.selectedProductCount)
             assertEquals(newWarehouse, actual.selectedWarehouseLocation)
             assertEquals(false, actual.isFilterActive)

            job.cancel()
        }
    }

    /**
    @Test
    fun `When change warehouse to different warehouse, isFilterActive should be true and should fetch products from the selected warehouse`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
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
                productIds = listOf(firstProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedValues = arrayListOf<AddProductUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }
            val newWarehouse = Warehouse(warehouseId = 100, warehouseName = "Depok", warehouseType = WarehouseType.WAREHOUSE)

            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            viewModel.processEvent(AddProductEvent.ConfirmChangeWarehouseLocationFilter(newWarehouse))

            //Then
            val actual = emittedValues.last()


            assertEquals(1, actual.page)
            assertEquals(AddProductUiState.CheckboxState.UNCHECKED, actual.checkboxState)
            assertEquals(emptySet<Long>(), actual.selectedProductsIds)
            assertEquals(0, actual.selectedProductCount)
            assertEquals(newWarehouse, actual.selectedWarehouseLocation)
            assertEquals(true, actual.isFilterActive)

            job.cancel()
        }
    }
    //endregion
    */

    /**
    //region handleApplyShopShowcasesFilter
    @Test
    fun `when apply showcase filter with showcase selected, isFilterActive should be true`() {
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
    */

    @Test
    fun `when apply showcase filter with no showcase selected, isFilterActive should be false`() {
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
            val showcases = listOf<ShopShowcase>()

            //When
            viewModel.processEvent(AddProductEvent.ApplyShowCaseFilter(showcases))


            //Then
            val getProductsParam = ProductListUseCase.Param(
                categoryIds = listOf(),
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

            assertEquals(1, actual.page)
            assertEquals(emptyList<Product>(), actual.products)
            assertEquals(showcases, actual.selectedShopShowcase)
            assertEquals(false, actual.isFilterActive)
            job.cancel()
        }
    }
    //endregion

    //region handleApplyCategoryFilter
    @Test
    fun `when apply category filter with category selected, isFilterActive should be true`() {
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

            assertEquals(1, actual.page)
            assertEquals(emptyList<Product>(), actual.products)
            assertEquals(categories, actual.selectedCategories)
            assertEquals(true, actual.isFilterActive)
            job.cancel()
        }
    }

    @Test
    fun `when apply category filter with no category selected, isFilterActive should be false`() {
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
            val categories = listOf<ProductCategoryOption>()

            //When
            viewModel.processEvent(AddProductEvent.ApplyCategoryFilter(categories))


            //Then
            val getProductsParam = ProductListUseCase.Param(
                categoryIds = listOf(),
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

            assertEquals(1, actual.page)
            assertEquals(emptyList<Product>(), actual.products)
            assertEquals(categories, actual.selectedCategories)
            assertEquals(false, actual.isFilterActive)
            job.cancel()
        }
    }
    //endregion

    //region handleTapVariant
    @Test
    fun `When tap variant function called, should emit ShowVariantBottomSheet effect` () {
        runBlockingTest {
            //Given
            val emittedEffects = arrayListOf<AddProductEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            val product = populateProduct()

            //When
            viewModel.processEvent(AddProductEvent.TapVariant(product))


            //Then
            val actual = emittedEffects.last()
            assertEquals(AddProductEffect.ShowVariantBottomSheet(product), actual)

            job.cancel()
        }
    }
    //endregion

    //region handleVariantUpdated
    @Test
    fun `When select variants from a product, parent product should be selected and selected variants ids should be stored`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val firstProductVariants = listOf(
                VoucherValidationResult.ValidationProduct.ProductVariant(
                    productId = 111,
                    productName = "First Product - Red Variant",
                    price = 55_000,
                    stock = 10,
                    isEligible = true,
                    reason = ""
                ),
                VoucherValidationResult.ValidationProduct.ProductVariant(
                    productId = 112,
                    productName = "First Product - Blue Variant",
                    price = 55_000,
                    stock = 10,
                    isEligible = false,
                    reason = "Has been registered on another voucher"
                ),
                VoucherValidationResult.ValidationProduct.ProductVariant(
                    productId = 113,
                    productName = "First Product - Green Variant",
                    price = 55_000,
                    stock = 10,
                    isEligible = false,
                    reason = "Has been registered on another voucher"
                )
            )
            val expectedFirstProductVariant = listOf(
                Product.Variant(
                    variantProductId = 111,
                    isEligible = true,
                    reason = "",
                    isSelected = true
                ),
                Product.Variant(
                    variantProductId = 112,
                    isEligible = false,
                    reason = "Has been registered on another voucher",
                    isSelected = false
                ),
                Product.Variant(
                    variantProductId = 113,
                    isEligible = false,
                    reason = "Has been registered on another voucher",
                    isSelected = false
                )
            )

            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
                    reason = "",
                    variant = firstProductVariants
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
                productIds = listOf(firstProduct.id),
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

            viewModel.processEvent(
                AddProductEvent.VariantUpdated(
                    modifiedParentProductId = 1,
                    selectedVariantIds = setOf(111)
                )
            )

            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstProduct.copy(
                        isSelected = true,
                        selectedVariantsIds = setOf(111),
                        originalVariants = expectedFirstProductVariant
                    )
                ),
                actual.products
            )
            assertEquals(setOf<Long>(1), actual.selectedProductsIds)
            assertEquals(1, actual.selectedProductCount)
            assertEquals(AddProductUiState.CheckboxState.INDETERMINATE, actual.checkboxState)

            job.cancel()
        }
    }

    @Test
    fun `When select variants from a non exist parent product id, is selected should be false`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)

            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct)
            val mockedProductValidationResponse = listOf(
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = firstProduct.id,
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
                productIds = listOf(firstProduct.id),
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

            val unknownParentProduct : Long = 2
            viewModel.processEvent(
                AddProductEvent.VariantUpdated(
                    modifiedParentProductId = unknownParentProduct,
                    selectedVariantIds = setOf()
                )
            )

            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstProduct.copy(
                        isSelected = false,
                        selectedVariantsIds = setOf()
                    )
                ),
                actual.products
            )
            assertEquals(setOf<Long>(), actual.selectedProductsIds)
            assertEquals(0, actual.selectedProductCount)
            assertEquals(AddProductUiState.CheckboxState.UNCHECKED, actual.checkboxState)

            job.cancel()
        }
    }
    //endregion

    //region handleConfirmAddProduct
    @Test
    fun `When confirmed add product, should emit ProductConfirmed effect`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false, txStats = Product.TxStats(sold = 10), picture = "product1.img")
            val secondProduct = populateProduct().copy(id = 2, isSelected = false, txStats = Product.TxStats(sold = 20), picture = "product2.img")
            val thirdProduct = populateProduct().copy(id = 3, isSelected = false, txStats = Product.TxStats(sold = 5), picture = "product3.img")

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
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedEffects = arrayListOf<AddProductEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
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

            viewModel.processEvent(AddProductEvent.ConfirmAddProduct)


            //Then
            val actual = emittedEffects.last()
            val expectedProducts = listOf(
                SelectedProduct(parentProductId = firstProduct.id, variantProductIds = emptyList()),
                SelectedProduct(parentProductId = secondProduct.id, variantProductIds = emptyList()),
            )
            val expectedImageUrls = listOf("product2.img", "product1.img")

            assertEquals(
                AddProductEffect.ProductConfirmed(
                    selectedProducts = expectedProducts,
                    selectedParentProductImageUrls = expectedImageUrls,
                    voucherConfiguration = voucherConfiguration,
                    selectedWarehouseId = WAREHOUSE_ID
                ),
                actual
            )

            job.cancel()
        }
    }

    @Test
    fun `When confirmed add product but no products selected, should return empty list of products`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct()
            val firstProduct = populateProduct().copy(id = 1, isSelected = false, txStats = Product.TxStats(sold = 10), picture = "product1.img")
            val secondProduct = populateProduct().copy(id = 2, isSelected = false, txStats = Product.TxStats(sold = 20), picture = "product2.img")
            val thirdProduct = populateProduct().copy(id = 3, isSelected = false, txStats = Product.TxStats(sold = 5), picture = "product3.img")

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
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedEffects = arrayListOf<AddProductEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            viewModel.processEvent(AddProductEvent.ConfirmAddProduct)


            //Then
            val actual = emittedEffects.last()
            val expectedProducts = listOf<SelectedProduct>()
            val expectedImageUrls = listOf<String>()

            assertEquals(
                AddProductEffect.ProductConfirmed(
                    selectedProducts = expectedProducts,
                    selectedParentProductImageUrls = expectedImageUrls,
                    voucherConfiguration = voucherConfiguration,
                    selectedWarehouseId = WAREHOUSE_ID
                ),
                actual
            )

            job.cancel()
        }
    }
    //endregion

    //region handleAddNewProducts
    @Test
    fun `When add product, should append newly selected products with the previously selected products`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct().copy(id = 200, isSelected = true)
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val thirdProduct = populateProduct().copy(id = 3, isSelected = false)

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
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedEffects = arrayListOf<AddProductEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
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

            viewModel.processEvent(AddProductEvent.AddNewProducts)


            //Then
            val actual = emittedEffects.last()
            val expectedProducts = listOf(
                previouslySelectedProducts,
                firstProduct.copy(isSelected = true),
                secondProduct.copy(isSelected = true)
            )
            assertEquals(AddProductEffect.AddNewProducts(selectedProducts = expectedProducts), actual)

            job.cancel()
        }
    }

    @Test
    fun `When add product with no selected product, return previously selected products only`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct().copy(id = 200, isSelected = true)
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val thirdProduct = populateProduct().copy(id = 3, isSelected = false)

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
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(firstProduct.id, secondProduct.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedEffects = arrayListOf<AddProductEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            viewModel.processEvent(AddProductEvent.AddNewProducts)


            //Then
            val actual = emittedEffects.last()
            val expectedProducts = listOf(previouslySelectedProducts)
            assertEquals(AddProductEffect.AddNewProducts(selectedProducts = expectedProducts), actual)

            job.cancel()
        }
    }

    @Test
    fun `When add product with previous selected product id exist within newly selected product id, should return both previous and newly selected products`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val previouslySelectedProducts = populateProduct().copy(id = 200, isSelected = true)
            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val thirdProduct = populateProduct().copy(id = 3, isSelected = false)

            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct, secondProduct, thirdProduct, previouslySelectedProducts)
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
                ),
                VoucherValidationResult.ValidationProduct(
                    isEligible = true,
                    isVariant = false,
                    parentProductId = previouslySelectedProducts.id,
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
                productIds = listOf(firstProduct.id, secondProduct.id, thirdProduct.id, previouslySelectedProducts.id),
                productValidationResponse = mockedProductValidationResponse
            )

            val emittedEffects = arrayListOf<AddProductEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    listOf(previouslySelectedProducts)
                )
            )

            viewModel.processEvent(AddProductEvent.AddProductToSelection(previouslySelectedProducts.id))
            viewModel.processEvent(AddProductEvent.AddProductToSelection(firstProduct.id))
            viewModel.processEvent(AddProductEvent.AddProductToSelection(secondProduct.id))
            viewModel.processEvent(AddProductEvent.AddNewProducts)


            //Then
            val actual = emittedEffects.last()
            val expectedProducts = listOf(
                previouslySelectedProducts.copy(isSelected = true),
                firstProduct.copy(isSelected = true),
                secondProduct.copy(isSelected = true)
            )
            assertEquals(AddProductEffect.AddNewProducts(selectedProducts = expectedProducts), actual)

            job.cancel()
        }
    }

    //endregion


    //region findSelectedProductCount
    @Test
    fun `When searching products, if search result matched with the current selected products, the should return the correct selectedProductCount`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val thirdProduct = populateProduct().copy(id = 3, isSelected = false)
            val fourthProduct = populateProduct().copy(id = 4, isSelected = false)

            val maxProductSubmission = 100

            val mockedProductResponse = listOf(firstProduct, secondProduct)
            val secondPageOfMockedProductResponse = listOf(thirdProduct, fourthProduct)

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
            val secondPageOfMockedProductValidationResponse = listOf(
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
                    listOf()
                )
            )
            viewModel.processEvent(AddProductEvent.AddProductToSelection(thirdProduct.id))
            viewModel.processEvent(AddProductEvent.AddProductToSelection(fourthProduct.id))

            mockGetProductListGqlCall(searchKeyword = "some keyword..", warehouseId = WAREHOUSE_ID, products = secondPageOfMockedProductResponse)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(thirdProduct.id, fourthProduct.id),
                productValidationResponse = secondPageOfMockedProductValidationResponse
            )

            viewModel.processEvent(AddProductEvent.SearchProduct("some keyword.."))


            //Then
            val actual = emittedValues.last()

            assertEquals(2, actual.selectedProductCount)


            job.cancel()
        }
    }


    //endregion


    //region determineCheckboxState
    @Test
    fun `When selected product count is bigger than total product count, checkbox state should be UNCHECKED`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)
            val thirdProduct = populateProduct().copy(id = 3, isSelected = false)

            val maxProductSubmission = 100

            val mockedTotalProductCount = 2
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
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse, totalProduct = mockedTotalProductCount)
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
                    listOf()
                )
            )
            viewModel.processEvent(AddProductEvent.AddProductToSelection(firstProduct.id))
            viewModel.processEvent(AddProductEvent.AddProductToSelection(secondProduct.id))
            viewModel.processEvent(AddProductEvent.AddProductToSelection(thirdProduct.id))


            viewModel.processEvent(AddProductEvent.LoadPage(page = 1))


            //Then
            val actual = emittedValues.last()

            assertEquals(AddProductUiState.CheckboxState.UNCHECKED, actual.checkboxState)


            job.cancel()
        }
    }

    @Test
    fun `When selected product count is equal to total product count, checkbox state should be CHECKED`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val firstProduct = populateProduct().copy(id = 1, isSelected = false)
            val secondProduct = populateProduct().copy(id = 2, isSelected = false)

            val maxProductSubmission = 100

            val mockedTotalProductCount = 2
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
            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID, products = mockedProductResponse, totalProduct = mockedTotalProductCount)
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
                    listOf()
                )
            )
            viewModel.processEvent(AddProductEvent.AddProductToSelection(firstProduct.id))
            viewModel.processEvent(AddProductEvent.AddProductToSelection(secondProduct.id))


            viewModel.processEvent(AddProductEvent.LoadPage(page = 1))


            //Then
            val actual = emittedValues.last()

            assertEquals(AddProductUiState.CheckboxState.CHECKED, actual.checkboxState)


            job.cancel()
        }
    }
    //endregion

    //region getShopShowcases
    @Test
    fun `When get shop showcases and got showcase created by Tokopedia, should remove it from the list`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val mockedShowcases = listOf(
                ShopShowcase(id = 1, alias = "book", name = "Buku", type = NumberConstant.ID_TOKOPEDIA_CREATED_SHOWCASE_TYPE, isSelected = false),
                ShopShowcase(id = 1, alias = "hp", name = "Handphone", type = 1, isSelected = false)
            )

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)

            coEvery { getShopShowcasesByShopIDUseCase.execute() } returns mockedShowcases

            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(),
                productValidationResponse = listOf()
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
                    listOf()
                )
            )

            //Then
            val actual = emittedValues.last()
            assertEquals(
                listOf(ShopShowcase(id = 1, alias = "hp", name = "Handphone", type = 1, isSelected = false)),
                actual.shopShowcases
            )

            job.cancel()
        }
    }

    @Test
    fun `When get shop showcase error, should set error`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()

            val error = MessageErrorException("Server error")

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall(sortOptions = mockedSortOptions, categoryOptions = mockedCategoryOption)

            coEvery { getShopShowcasesByShopIDUseCase.execute() } throws error

            mockGetProductListGqlCall(warehouseId = WAREHOUSE_ID)
            mockVoucherValidationPartialGqlCall(
                "3923-02-01",
                "3923-03-01",
                productIds = listOf(),
                productValidationResponse = listOf()
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
                    listOf()
                )
            )

            //Then
            val actual = emittedValues.last()
            assertEquals(error, actual.error)


            job.cancel()
        }
    }
    //endregion

    //region processEvent
    @Test
    fun `When unlisted event is triggered, should not emit any effect`() {
        runBlockingTest {
            //When
            viewModel.processEvent(mockk(relaxed = true))

            val emittedEffects = arrayListOf<AddProductEffect>()

            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            //Then
            val actualEffect = emittedEffects.lastOrNull()

            assertEquals(null, actualEffect)

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
        sortDirection: String = "DESC",
        sortId: String = "DEFAULT",
        totalProduct: Int = 20,
        products: List<Product> = emptyList()
    ) {
        val getProductsParam = ProductListUseCase.Param(
            categoryIds = listOf(),
            page = page,
            pageSize = 20,
            searchKeyword = searchKeyword,
            showcaseIds = listOf(),
            sortDirection = sortDirection,
            sortId = sortId,
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
