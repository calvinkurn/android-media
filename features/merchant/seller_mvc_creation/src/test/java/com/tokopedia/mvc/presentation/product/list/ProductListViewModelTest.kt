package com.tokopedia.mvc.presentation.product.list

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductResult
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEvent
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListUiState
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*


@ExperimentalCoroutinesApi
class ProductListViewModelTest {

    @RelaxedMockK
    lateinit var getProductsUseCase: ProductListUseCase

    @RelaxedMockK
    lateinit var getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase

    private lateinit var viewModel: ProductListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ProductListViewModel(
            CoroutineTestDispatchersProvider,
            getInitiateVoucherPageUseCase,
            getProductsUseCase
        )
    }

    //region getProductsAndProductsMetadata
    @Test
    fun `when get products success, should set product to ui state`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE
            val maxProductSelection = 100
            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration()

            val selectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val selectedProductIds = listOf(selectedProduct.parentProductId)

            val product = populateProduct().copy(id = 1)
            val mockedGetProductResponse = listOf(product)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)


            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(selectedProduct),
                    showCtaUpdateProductOnToolbar = false,
                    isEntryPointFromVoucherSummaryPage = false,
                    selectedWarehouseId = selectedWarehouseId
                )
            )


            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(mockedGetProductResponse, actual.products)
            assertEquals(pageMode, actual.originalPageMode)
            assertEquals(pageMode, actual.currentPageMode)
            assertEquals(maxProductSelection, actual.maxProductSelection)
            assertEquals(voucherConfiguration, actual.voucherConfiguration)
            assertEquals(false, actual.showCtaChangeProductOnToolbar)
            assertEquals(false, actual.isEntryPointFromVoucherSummaryPage)
            assertEquals(selectedWarehouseId, actual.selectedWarehouseId)

            job.cancel()
        }
    }

    @Test
    fun `when get products on edit mode, product enableCheckbox and isDeletable properties should be false`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.EDIT
            val action = VoucherAction.UPDATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration()

            val selectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val selectedProductIds = listOf(selectedProduct.parentProductId)

            val product = populateProduct().copy(id = 1)
            val mockedGetProductResponse = listOf(product)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)


            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(selectedProduct),
                    showCtaUpdateProductOnToolbar = false,
                    isEntryPointFromVoucherSummaryPage = false,
                    selectedWarehouseId = selectedWarehouseId
                )
            )


            //Then
            val actual = emittedValues.last()
            assertEquals(listOf(product.copy(isDeletable = false, enableCheckbox = false)), actual.products)

            job.cancel()
        }
    }


    @Test
    fun `when no previous products selected, products should be an empty list`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.EDIT
            val action = VoucherAction.UPDATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration()

            val selectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val selectedProductIds = listOf(selectedProduct.parentProductId)

            val product = populateProduct().copy(id = 1)
            val mockedGetProductResponse = listOf(product)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)


            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(),
                    showCtaUpdateProductOnToolbar = false,
                    isEntryPointFromVoucherSummaryPage = false,
                    selectedWarehouseId = selectedWarehouseId
                )
            )


            //Then
            val actual = emittedValues.last()
            assertEquals(listOf<Product>(), actual.products)

            job.cancel()
        }
    }

    @Test
    fun `when showCtaChangeProductOnToolbar is true, showCtaChangeProductOnToolbar property on ui state should be true`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.EDIT
            val action = VoucherAction.UPDATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration()

            val selectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val selectedProductIds = listOf(selectedProduct.parentProductId)

            val product = populateProduct().copy(id = 1)
            val mockedGetProductResponse = listOf(product)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)


            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(),
                    showCtaUpdateProductOnToolbar = true,
                    isEntryPointFromVoucherSummaryPage = false,
                    selectedWarehouseId = selectedWarehouseId
                )
            )


            //Then
            val actual = emittedValues.last()
            assertEquals(true, actual.showCtaChangeProductOnToolbar)

            job.cancel()
        }
    }

    @Test
    fun `when isEntryPointFromVoucherSummaryPage is true, isEntryPointFromVoucherSummaryPage property on ui state should be true`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.EDIT
            val action = VoucherAction.UPDATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration()

            val selectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val selectedProductIds = listOf(selectedProduct.parentProductId)

            val product = populateProduct().copy(id = 1)
            val mockedGetProductResponse = listOf(product)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)


            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(),
                    showCtaUpdateProductOnToolbar = false,
                    isEntryPointFromVoucherSummaryPage = true,
                    selectedWarehouseId = selectedWarehouseId
                )
            )


            //Then
            val actual = emittedValues.last()
            assertEquals(true, actual.isEntryPointFromVoucherSummaryPage)

            job.cancel()
        }
    }

    @Test
    fun `when get products error, error property on ui state should be updated with the error message`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.EDIT
            val action = VoucherAction.UPDATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration()

            val selectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val selectedProductIds = listOf(selectedProduct.parentProductId)

            val product = populateProduct().copy(id = 1)
            val mockedGetProductResponse = listOf(product)
            val error = MessageErrorException("Server Error")

            val initiateVoucherPageParam = GetInitiateVoucherPageUseCase.Param(
                action = action,
                promoType = PromoType.FREE_SHIPPING,
                isVoucherProduct = true
            )
            coEvery { getInitiateVoucherPageUseCase.execute(initiateVoucherPageParam) } throws error

            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)

            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(),
                    showCtaUpdateProductOnToolbar = false,
                    isEntryPointFromVoucherSummaryPage = true,
                    selectedWarehouseId = selectedWarehouseId
                )
            )


            //Then
            val actual = emittedValues.last()
            assertEquals(error, actual.error)
            assertEquals(false, actual.isLoading)

            job.cancel()
        }
    }
    //endregion


    //region toOriginalVariant

    @Test
    fun `when finding product variants from a product, should return empty list of product variant if product is not found`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration()

            val selectedProduct = populateSelectedProduct(parentProductId = 3, variantIds = emptyList())
            val selectedProductIds = listOf(selectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)

            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)


            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(selectedProduct),
                    showCtaUpdateProductOnToolbar = false,
                    isEntryPointFromVoucherSummaryPage = false,
                    selectedWarehouseId = selectedWarehouseId
                )
            )


            //Then
            val actual = emittedValues.last()
            assertEquals(
                listOf(
                    firstProduct.copy(originalVariants = listOf()),
                    secondProduct.copy(originalVariants = listOf())
                ),
                actual.products
            )
            job.cancel()
        }
    }

    @Test
    fun `when finding a matched product, should return list its variant`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration()

            val selectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = listOf(111))
            val selectedProductIds = listOf(selectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = true
                    )
                )
            )

            val secondProduct = populateProduct().copy(
                id = 2,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 211,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                ),
            )

            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)


            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(selectedProduct),
                    showCtaUpdateProductOnToolbar = false,
                    isEntryPointFromVoucherSummaryPage = false,
                    selectedWarehouseId = selectedWarehouseId
                )
            )


            //Then
            val actual = emittedValues.last()
             assertEquals(listOf(
                 firstProduct.copy(
                     originalVariants = listOf(
                         Product.Variant(
                             variantProductId = 111,
                             isEligible = true,
                             reason = "",
                             isSelected = true
                         )
                     ),
                     selectedVariantsIds = setOf(111)
                 ),
                 secondProduct.copy(originalVariants = listOf())
             ), actual.products)

            job.cancel()
        }
    }

    //endregion


    //region handleCheckAllProduct
    @Test
    fun `when check all product toggled on, all products and its variant should be selected`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration()

            val selectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = listOf(111))
            val selectedProductIds = listOf(selectedProduct.parentProductId)

            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )

            val mockedGetProductResponse = listOf(product)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)


            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(selectedProduct),
                    showCtaUpdateProductOnToolbar = false,
                    isEntryPointFromVoucherSummaryPage = false,
                    selectedWarehouseId = selectedWarehouseId
                )
            )

            viewModel.processEvent(ProductListEvent.EnableSelectAllCheckbox)


            //Then
            val actual = emittedValues.last()

            assertEquals(true, actual.isSelectAllActive)
            assertEquals(setOf(product.id), actual.selectedProductsIdsToBeRemoved)
            assertEquals(
                listOf(
                    product.copy(
                        isSelected = true,
                        selectedVariantsIds = setOf(111),
                        originalVariants = listOf(
                            Product.Variant(
                                variantProductId = 111,
                                isEligible = true,
                                reason = "",
                                isSelected = true
                            )
                        )
                    )
                ),
                actual.products
            )

            job.cancel()
        }
    }

    //endregion


    //region handleUncheckAllProduct
    @Test
    fun `when uncheck all selected products, all product isSelected properties should be false`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration()

            val selectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val selectedProductIds = listOf(selectedProduct.parentProductId)

            val product = populateProduct().copy(id = 1)
            val mockedGetProductResponse = listOf(product)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)


            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(selectedProduct),
                    showCtaUpdateProductOnToolbar = false,
                    isEntryPointFromVoucherSummaryPage = false,
                    selectedWarehouseId = selectedWarehouseId
                )
            )

            viewModel.processEvent(ProductListEvent.DisableSelectAllCheckbox)

            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isSelectAllActive)
            assertEquals(emptySet<Long>(), actual.selectedProductsIdsToBeRemoved)
            assertEquals(listOf(product.copy(isSelected = false)), actual.products)

            job.cancel()
        }
    }
    //endregion

    //region handleMarkProductForDeletion
    @Test
    fun `when mark a product for deletion, its isSelected property should be true`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration()

            val firstSelectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val secondSelectedProduct = populateSelectedProduct(parentProductId = 2, variantIds = emptyList())

            val selectedProductIds = listOf(firstSelectedProduct.parentProductId, secondSelectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)


            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(firstSelectedProduct, secondSelectedProduct),
                    showCtaUpdateProductOnToolbar = false,
                    isEntryPointFromVoucherSummaryPage = false,
                    selectedWarehouseId = selectedWarehouseId
                )
            )

            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(productId = firstProduct.id))


            //Then
            val actual = emittedValues.last()

            assertEquals(setOf<Long>(1), actual.selectedProductsIdsToBeRemoved)
            assertEquals(
                listOf(firstProduct.copy(isSelected = true), secondProduct),
                actual.products
            )

            job.cancel()
        }
    }
    //endregion

    //region handleRemoveProductFromSelection
    @Test
    fun `when remove non variant product from selection, its isSelected property should be false`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration()

            val firstSelectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val secondSelectedProduct = populateSelectedProduct(parentProductId = 2, variantIds = emptyList())

            val selectedProductIds = listOf(firstSelectedProduct.parentProductId, secondSelectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)


            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(firstSelectedProduct, secondSelectedProduct),
                    showCtaUpdateProductOnToolbar = false,
                    isEntryPointFromVoucherSummaryPage = false,
                    selectedWarehouseId = selectedWarehouseId
                )
            )

            //Tick product id 1 and product 2 checkbox
            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(productId = firstProduct.id))
            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(productId = secondProduct.id))

            //Un-tick product id 1 checkbox
            viewModel.processEvent(ProductListEvent.RemoveProductFromSelection(productId = firstProduct.id))


            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isSelectAllActive)
            assertEquals(setOf<Long>(2), actual.selectedProductsIdsToBeRemoved)
            assertEquals(
                listOf(firstProduct.copy(isSelected = false), secondProduct.copy(isSelected = true)),
                actual.products
            )

            job.cancel()
        }
    }

    @Test
    fun `when remove product with variant from selection, its selectedVariantIds property should be restored to its original variant ids`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration()

            val selectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = listOf(111))
            val selectedProductIds = listOf(selectedProduct.parentProductId)

            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )

            val mockedGetProductResponse = listOf(product)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)


            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(selectedProduct),
                    showCtaUpdateProductOnToolbar = false,
                    isEntryPointFromVoucherSummaryPage = false,
                    selectedWarehouseId = selectedWarehouseId
                )
            )

            //Tick product id 1 checkbox
            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(productId = product.id))

            //Un-tick product id 1 checkbox
            viewModel.processEvent(ProductListEvent.RemoveProductFromSelection(productId = product.id))



            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isSelectAllActive)
            assertEquals(setOf<Long>(), actual.selectedProductsIdsToBeRemoved)
            assertEquals(
                listOf(
                    product.copy(
                        isSelected = false,
                        selectedVariantsIds = setOf(111),
                        originalVariants = listOf(
                            Product.Variant(
                                variantProductId = 111,
                                isEligible = true,
                                reason = "",
                                isSelected = true
                            )
                        )
                    )
                ),
                actual.products
            )

            job.cancel()
        }
    }
    //endregion


    //region handleRemoveProduct
    //endregion


    //region handleTapVariant
    //endregion


    //region handleVariantUpdated
    //endregion


    //region removeParentProduct
    //endregion


    //region updateVariants
    //endregion


    //region handleRedirection
    //endregion


    //region handleBulkDeleteProducts
    //endregion


    //region handleSwitchPageMode
    //endregion


    //region handleAddNewProductToSelection
    //endregion


    //region selectedProductsOnly
    //endregion


    //region handleCtaAddNewProduct
    //endregion


    //region emitRedirectToAddProductPageEvent
    //endregion


    //region handleTapToolbarBackIcon
    //endregion


    //region handleTapBackButton
    //endregion


    private fun buildVoucherConfiguration(): VoucherConfiguration {
        return VoucherConfiguration().copy(
            startPeriod = Date(2023, 1, 1, 0, 0, 0),
            endPeriod = Date(2023, 2, 1, 0, 0, 0)
        )
    }
    private fun populateSelectedProduct(parentProductId: Long, variantIds: List<Long>): SelectedProduct {
        return SelectedProduct(parentProductId = parentProductId, variantProductIds = variantIds)
    }


    private fun mockGetProductListGqlCall(selectedProductIds: List<Long>, response: List<Product>) {
        val getProductsParam = ProductListUseCase.Param(
            categoryIds = listOf(),
            page = 1,
            pageSize = 100,
            searchKeyword = "",
            showcaseIds = listOf(),
            sortDirection = "DESC",
            sortId = "DEFAULT",
            warehouseId = 0,
            productIdInclude = selectedProductIds
        )
        val getProductsResponse = ProductResult(total = 20, products = response)
        coEvery { getProductsUseCase.execute(getProductsParam) } returns getProductsResponse
    }

    private fun mockInitiateVoucherPageGqlCall(action : VoucherAction) {
        val initiateVoucherPageParam = GetInitiateVoucherPageUseCase.Param(
            action = action,
            promoType = PromoType.FREE_SHIPPING,
            isVoucherProduct = true
        )
        val initiateVoucherResponse = VoucherCreationMetadata(
            accessToken = "accessToken",
            isEligible = 1,
            maxProduct = 100,
            prefixVoucherCode = "OFC",
            shopId = 1,
            token = "token",
            userId = 1,
            discountActive = true,
            message = ""
        )
        coEvery { getInitiateVoucherPageUseCase.execute(initiateVoucherPageParam) } returns initiateVoucherResponse
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
            isDeletable = true,
        )
    }

}
