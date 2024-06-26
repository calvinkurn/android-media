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
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEffect
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListEvent
import com.tokopedia.mvc.presentation.product.list.uimodel.ProductListUiState
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

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
            // Given
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

            // When
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

            // Then
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
            // Given
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

            // When
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

            // Then
            val actual = emittedValues.last()
            assertEquals(listOf(product.copy(isDeletable = false, enableCheckbox = false)), actual.products)

            job.cancel()
        }
    }

    @Test
    fun `when no previous products selected, products should be an empty list`() {
        runBlockingTest {
            // Given
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

            // When
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

            // Then
            val actual = emittedValues.last()
            assertEquals(listOf<Product>(), actual.products)

            job.cancel()
        }
    }

    @Test
    fun `when showCtaChangeProductOnToolbar is true, showCtaChangeProductOnToolbar property on ui state should be true`() {
        runBlockingTest {
            // Given
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

            // When
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

            // Then
            val actual = emittedValues.last()
            assertEquals(true, actual.showCtaChangeProductOnToolbar)

            job.cancel()
        }
    }

    @Test
    fun `when isEntryPointFromVoucherSummaryPage is true, isEntryPointFromVoucherSummaryPage property on ui state should be true`() {
        runBlockingTest {
            // Given
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

            // When
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

            // Then
            val actual = emittedValues.last()
            assertEquals(true, actual.isEntryPointFromVoucherSummaryPage)

            job.cancel()
        }
    }

    @Test
    fun `when get products error, error property on ui state should be updated with the error message`() {
        runBlockingTest {
            // Given
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

            // When
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

            // Then
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
            // Given
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

            // When
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

            // Then
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
            // Given
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
                )
            )

            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)

            val emittedValues = arrayListOf<ProductListUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            // When
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

            // Then
            val actual = emittedValues.last()
            assertEquals(
                listOf(
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
                ),
                actual.products
            )

            job.cancel()
        }
    }

    //endregion

    //region handleCheckAllProduct
    @Test
    fun `when check all product toggled on, all products and its variant should be selected`() {
        runBlockingTest {
            // Given
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

            // When
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

            // Then
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
            // Given
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

            // When
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

            // Then
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
            // Given
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

            // When
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

            // Then
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
            // Given
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

            // When
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

            // Tick product id 1 and product 2 checkbox
            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(productId = firstProduct.id))
            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(productId = secondProduct.id))

            // Un-tick product id 1 checkbox
            viewModel.processEvent(ProductListEvent.RemoveProductFromSelection(productId = firstProduct.id))

            // Then
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
            // Given
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

            // When
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

            // Tick product id 1 checkbox
            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(productId = product.id))

            // Un-tick product id 1 checkbox
            viewModel.processEvent(ProductListEvent.RemoveProductFromSelection(productId = product.id))

            // Then
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
    @Test
    fun `when remove a product from a list with only one product, the list should be empty`() {
        runBlockingTest {
            // Given
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

            // When
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

            viewModel.processEvent(ProductListEvent.ApplyRemoveProduct(product.id))

            // Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isSelectAllActive)
            assertEquals(setOf<Long>(), actual.selectedProductsIdsToBeRemoved)
            assertEquals(listOf<Product>(), actual.products)
            assertEquals(0, actual.products.size)

            job.cancel()
        }
    }

    @Test
    fun `when remove product with non exist product id, should not remove any product from list`() {
        runBlockingTest {
            // Given
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

            // When
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

            val nonExistProductId: Long = 99
            viewModel.processEvent(ProductListEvent.ApplyRemoveProduct(nonExistProductId))

            // Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isSelectAllActive)
            assertEquals(setOf<Long>(), actual.selectedProductsIdsToBeRemoved)
            assertEquals(
                listOf(product),
                actual.products
            )
            assertEquals(1, actual.products.size)

            job.cancel()
        }
    }

    @Test
    fun `when remove a product, should emit ProductDeleted effect`() {
        runBlockingTest {
            // Given
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

            val emittedEffects = arrayListOf<ProductListEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // When
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

            viewModel.processEvent(ProductListEvent.ApplyRemoveProduct(product.id))

            // Then
            val emittedEffect = emittedEffects.last()

            assertEquals(ProductListEffect.ProductDeleted, emittedEffect)

            job.cancel()
        }
    }
    //endregion

    //region handleTapVariant
    @Test
    fun `when tap a product, should emit ShowVariantBottomSheet effect`() {
        runBlockingTest {
            // Given
            val product = populateProduct().copy(
                id = 1,
                isSelected = true,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = true
                    )
                ),
                selectedVariantsIds = setOf(111)
            )

            val emittedEffects = arrayListOf<ProductListEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // When
            viewModel.processEvent(ProductListEvent.TapVariant(product))

            // Then
            val emittedEffect = emittedEffects.last()

            assertEquals(
                ProductListEffect.ShowVariantBottomSheet(
                    isParentProductSelected = true,
                    selectedProduct = SelectedProduct(parentProductId = 1, variantProductIds = listOf(111)),
                    originalVariantIds = listOf(111),
                    pageMode = PageMode.CREATE
                ),
                emittedEffect
            )

            job.cancel()
        }
    }
    //endregion

    //region handleVariantUpdated
    @Test
    fun `when update a product and all variants is unselected, should remove parent product from list`() {
        runBlockingTest {
            // Given
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

            // When
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

            viewModel.processEvent(
                ProductListEvent.VariantUpdated(
                    modifiedParentProductId = firstProduct.id,
                    selectedVariantIds = setOf()
                )
            )

            // Then
            val actual = emittedValues.last()

            assertEquals(listOf(secondProduct), actual.products)

            job.cancel()
        }
    }

    @Test
    fun `when updating a variant but specified unlisted parent product id, should not update any products`() {
        runBlockingTest {
            // Given
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

            // When
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

            val unlistedParentProductId: Long = 99
            viewModel.processEvent(
                ProductListEvent.VariantUpdated(
                    modifiedParentProductId = unlistedParentProductId,
                    selectedVariantIds = setOf()
                )
            )

            // Then
            val actual = emittedValues.last()

            assertEquals(listOf(firstProduct, secondProduct), actual.products)
            assertEquals(2, actual.products.size)

            job.cancel()
        }
    }

    @Test
    fun `when update a product and new variant were selected, should update the selected variants`() {
        runBlockingTest {
            // Given
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

            // When
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

            viewModel.processEvent(
                ProductListEvent.VariantUpdated(
                    modifiedParentProductId = firstProduct.id,
                    selectedVariantIds = setOf(111)
                )
            )

            // Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstProduct.copy(
                        isSelected = true,
                        selectedVariantsIds = setOf(111)
                    ),
                    secondProduct
                ),
                actual.products
            )

            job.cancel()
        }
    }
    //endregion

    //region handleRedirection
    @Test
    fun `when proceed to next page, should emit ProceedToVoucherPreviewPage with the selected products`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

            val firstSelectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val secondSelectedProduct = populateSelectedProduct(parentProductId = 2, variantIds = emptyList())

            val selectedProductIds = listOf(firstSelectedProduct.parentProductId, secondSelectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)

            val emittedEffects = arrayListOf<ProductListEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // When
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

            viewModel.processEvent(ProductListEvent.TapContinueButton)

            // Then
            val emittedEffect = emittedEffects.last()

            assertEquals(
                ProductListEffect.ProceedToVoucherPreviewPage(
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(
                        SelectedProduct(parentProductId = firstProduct.id, variantProductIds = emptyList()),
                        SelectedProduct(parentProductId = secondProduct.id, variantProductIds = emptyList())
                    ),
                    originalPageMode = pageMode
                ),
                emittedEffect
            )

            job.cancel()
        }
    }
    //endregion

    //region handleBulkDeleteProducts

    @Test
    fun `when performing bulk delete, should remove the marked products`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

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

            // When
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

            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(firstProduct.id))
            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(secondProduct.id))
            viewModel.processEvent(ProductListEvent.ApplyBulkDeleteProduct)

            // Then
            val actual = emittedValues.last()

            assertEquals(emptyList<Product>(), actual.products)
            assertEquals(setOf<Long>(), actual.selectedProductsIdsToBeRemoved)

            job.cancel()
        }
    }

    @Test
    fun `when performing bulk delete, should emit BulkDeleteProductSuccess effect with the selected product for removal count`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

            val firstSelectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val secondSelectedProduct = populateSelectedProduct(parentProductId = 2, variantIds = emptyList())

            val selectedProductIds = listOf(firstSelectedProduct.parentProductId, secondSelectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)

            val emittedEffects = arrayListOf<ProductListEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // When
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

            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(firstProduct.id))
            viewModel.processEvent(ProductListEvent.ApplyBulkDeleteProduct)

            // Then
            val emittedEffect = emittedEffects.last()

            assertEquals(
                ProductListEffect.BulkDeleteProductSuccess(deletedProductCount = 1),
                emittedEffect
            )

            job.cancel()
        }
    }
    //endregion

    //region handleSwitchPageMode
    @Test
    fun `when switch page mode, enableCheckbox and isDeletable should be true`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

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

            // When
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

            viewModel.processEvent(ProductListEvent.TapCtaChangeProduct)

            // Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstProduct.copy(enableCheckbox = true, isDeletable = true),
                    secondProduct.copy(enableCheckbox = true, isDeletable = true)
                ),
                actual.products
            )
            assertEquals(PageMode.CREATE, actual.currentPageMode)

            job.cancel()
        }
    }
    //endregion

    //region handleAddNewProductToSelection
    @Test
    fun `When add new product, then newly selected product should not be marked for removal`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

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

            // When
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

            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(firstProduct.id))
            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(secondProduct.id))

            val thirdProduct = populateProduct().copy(id = 3)
            val fourthProduct = populateProduct().copy(id = 4)
            val newlySelectedProducts = listOf(thirdProduct, fourthProduct)

            viewModel.processEvent(ProductListEvent.AddNewProductToSelection(newlySelectedProducts))

            // Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    thirdProduct.copy(isSelected = false),
                    fourthProduct.copy(isSelected = false)
                ),
                actual.products
            )

            job.cancel()
        }
    }

    @Test
    fun `When add new product, if the newly added product already selected, product should be unchanged`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

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

            // When
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

            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(firstProduct.id))
            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(secondProduct.id))

            val newlySelectedProducts = listOf(firstProduct, secondProduct)

            viewModel.processEvent(ProductListEvent.AddNewProductToSelection(newlySelectedProducts))

            // Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(firstProduct, secondProduct),
                actual.products
            )

            job.cancel()
        }
    }
    //endregion

    //region handleCtaAddNewProduct

    @Test
    fun `When add new product and currently on create mode, should emit RedirectToAddProductPage effect`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

            val firstSelectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val secondSelectedProduct = populateSelectedProduct(parentProductId = 2, variantIds = emptyList())

            val selectedProductIds = listOf(firstSelectedProduct.parentProductId, secondSelectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)

            val emittedEffects = arrayListOf<ProductListEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // When
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

            viewModel.processEvent(ProductListEvent.TapCtaAddProduct)

            // Then
            val emittedEffect = emittedEffects.last()

            assertEquals(
                ProductListEffect.BackToPreviousPage,
                emittedEffect
            )

            job.cancel()
        }
    }

    @Test
    fun `When add new product and currently on create mode & entry point from voucher summary page, should emit RedirectToAddProductPage effect`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.CREATE
            val action = VoucherAction.CREATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

            val firstSelectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val secondSelectedProduct = populateSelectedProduct(parentProductId = 2, variantIds = emptyList())

            val selectedProductIds = listOf(firstSelectedProduct.parentProductId, secondSelectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)

            val emittedEffects = arrayListOf<ProductListEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // When
            viewModel.processEvent(
                ProductListEvent.FetchProducts(
                    pageMode = pageMode,
                    voucherConfiguration = voucherConfiguration,
                    selectedProducts = listOf(firstSelectedProduct, secondSelectedProduct),
                    showCtaUpdateProductOnToolbar = false,
                    isEntryPointFromVoucherSummaryPage = true,
                    selectedWarehouseId = selectedWarehouseId
                )
            )

            viewModel.processEvent(ProductListEvent.TapCtaAddProduct)

            // Then
            val emittedEffect = emittedEffects.last()

            assertEquals(
                ProductListEffect.RedirectToAddProductPage(
                    voucherConfiguration = voucherConfiguration.copy(productIds = listOf(1, 2)),
                    products = listOf(firstProduct, secondProduct)
                ),
                emittedEffect
            )

            job.cancel()
        }
    }

    @Test
    fun `When add new product and currently on edit mode, should emit RedirectToAddProductPage effect`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.EDIT
            val action = VoucherAction.UPDATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

            val firstSelectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val secondSelectedProduct = populateSelectedProduct(parentProductId = 2, variantIds = emptyList())

            val selectedProductIds = listOf(firstSelectedProduct.parentProductId, secondSelectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)

            val emittedEffects = arrayListOf<ProductListEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // When
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

            viewModel.processEvent(ProductListEvent.TapCtaAddProduct)

            // Then
            val emittedEffect = emittedEffects.last()

            assertEquals(
                ProductListEffect.RedirectToAddProductPage(
                    voucherConfiguration = voucherConfiguration.copy(productIds = listOf(1, 2)),
                    products = listOf(
                        firstProduct.copy(
                            enableCheckbox = false,
                            isDeletable = false
                        ),
                        secondProduct.copy(
                            enableCheckbox = false,
                            isDeletable = false
                        )
                    )
                ),
                emittedEffect
            )

            job.cancel()
        }
    }

    @Test
    fun `When add new product and currently on duplicate mode, should emit no effect`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.DUPLICATE
            val action = VoucherAction.UPDATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

            val firstSelectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val secondSelectedProduct = populateSelectedProduct(parentProductId = 2, variantIds = emptyList())

            val selectedProductIds = listOf(firstSelectedProduct.parentProductId, secondSelectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)

            val emittedEffects = arrayListOf<ProductListEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // When
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

            viewModel.processEvent(ProductListEvent.TapCtaAddProduct)

            // Then
            val emittedEffect = emittedEffects.lastOrNull()

            assertEquals(null, emittedEffect)
            assertEquals(0, emittedEffects.size)

            job.cancel()
        }
    }
    //endregion

    //region handleTapToolbarBackIcon
    @Test
    fun `When tap toolbar back, should emit RedirectToPreviousPage effect`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.DUPLICATE
            val action = VoucherAction.UPDATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

            val firstSelectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val secondSelectedProduct = populateSelectedProduct(parentProductId = 2, variantIds = emptyList())

            val selectedProductIds = listOf(firstSelectedProduct.parentProductId, secondSelectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)

            val emittedEffects = arrayListOf<ProductListEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // When
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

            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(firstProduct.id))
            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(secondProduct.id))

            viewModel.processEvent(ProductListEvent.TapToolbarBackIcon)

            // Then
            val emittedEffect = emittedEffects.last()

            assertEquals(ProductListEffect.RedirectToPreviousPage(selectedProductCount = 2), emittedEffect)

            job.cancel()
        }
    }
    //endregion

    //region handleTapBackButton
    @Test
    fun `When tap back button, should emit TapBackButton effect`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.DUPLICATE
            val action = VoucherAction.UPDATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

            val firstSelectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val secondSelectedProduct = populateSelectedProduct(parentProductId = 2, variantIds = emptyList())

            val selectedProductIds = listOf(firstSelectedProduct.parentProductId, secondSelectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)

            val emittedEffects = arrayListOf<ProductListEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // When
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

            viewModel.processEvent(ProductListEvent.TapBackButton)

            // Then
            val emittedEffect = emittedEffects.last()

            assertEquals(
                ProductListEffect.TapBackButton(originalPageMode = pageMode),
                emittedEffect
            )

            job.cancel()
        }
    }
    //endregion

    //region TapRemoveProduct
    @Test
    fun `When tap remove product, should emit ShowDeleteProductConfirmationDialog effect`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.DUPLICATE
            val action = VoucherAction.UPDATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

            val firstSelectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val secondSelectedProduct = populateSelectedProduct(parentProductId = 2, variantIds = emptyList())

            val selectedProductIds = listOf(firstSelectedProduct.parentProductId, secondSelectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)

            val emittedEffects = arrayListOf<ProductListEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // When
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

            viewModel.processEvent(ProductListEvent.TapRemoveProduct(firstProduct.id))

            // Then
            val emittedEffect = emittedEffects.last()

            assertEquals(
                ProductListEffect.ShowDeleteProductConfirmationDialog(productId = firstProduct.id),
                emittedEffect
            )

            job.cancel()
        }
    }
    //endregion

    //region TapBulkDeleteProduct
    @Test
    fun `When tap bulk delete product, should emit ShowBulkDeleteProductConfirmationDialog effect with selected product count`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.DUPLICATE
            val action = VoucherAction.UPDATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

            val firstSelectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val secondSelectedProduct = populateSelectedProduct(parentProductId = 2, variantIds = emptyList())

            val selectedProductIds = listOf(firstSelectedProduct.parentProductId, secondSelectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)

            val emittedEffects = arrayListOf<ProductListEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // When
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

            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(firstProduct.id))
            viewModel.processEvent(ProductListEvent.MarkProductForDeletion(secondProduct.id))

            viewModel.processEvent(ProductListEvent.TapBulkDeleteProduct)

            // Then
            val emittedEffect = emittedEffects.last()

            assertEquals(
                ProductListEffect.ShowBulkDeleteProductConfirmationDialog(
                    toDeleteProductCount = 2
                ),
                emittedEffect
            )

            job.cancel()
        }
    }

    @Test
    fun `When tap bulk delete product with no product selected, should emit ShowBulkDeleteProductConfirmationDialog effect with 0 selected product count`() {
        runBlockingTest {
            // Given
            val pageMode = PageMode.DUPLICATE
            val action = VoucherAction.UPDATE

            val selectedWarehouseId: Long = 1
            val voucherConfiguration = buildVoucherConfiguration().copy(warehouseId = 1)

            val firstSelectedProduct = populateSelectedProduct(parentProductId = 1, variantIds = emptyList())
            val secondSelectedProduct = populateSelectedProduct(parentProductId = 2, variantIds = emptyList())

            val selectedProductIds = listOf(firstSelectedProduct.parentProductId, secondSelectedProduct.parentProductId)

            val firstProduct = populateProduct().copy(id = 1)
            val secondProduct = populateProduct().copy(id = 2)
            val mockedGetProductResponse = listOf(firstProduct, secondProduct)

            mockInitiateVoucherPageGqlCall(action)
            mockGetProductListGqlCall(selectedProductIds, mockedGetProductResponse)

            val emittedEffects = arrayListOf<ProductListEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // When
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

            viewModel.processEvent(ProductListEvent.TapBulkDeleteProduct)

            // Then
            val emittedEffect = emittedEffects.last()

            assertEquals(
                ProductListEffect.ShowBulkDeleteProductConfirmationDialog(
                    toDeleteProductCount = 0
                ),
                emittedEffect
            )

            job.cancel()
        }
    }
    //endregion

    /**
     * this code being commented as paramater is expected to be non-null but supplied with null parameter
     */
    //region processEvent
//    @Test
    fun `When unlisted event is triggered, should not emit any effect`() {
        runBlockingTest {
            // When
            viewModel.processEvent(mockk(relaxed = true))

            val emittedEffects = arrayListOf<ProductListEffect>()

            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            // Then
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

    private fun mockInitiateVoucherPageGqlCall(action: VoucherAction) {
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
            isDeletable = true
        )
    }
}
