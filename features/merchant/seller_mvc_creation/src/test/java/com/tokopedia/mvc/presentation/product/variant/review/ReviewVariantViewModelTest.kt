package com.tokopedia.mvc.presentation.product.variant.review

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.Variant
import com.tokopedia.mvc.domain.entity.VariantResult
import com.tokopedia.mvc.domain.usecase.ProductV3UseCase
import com.tokopedia.mvc.presentation.product.variant.review.uimodel.ReviewVariantEffect
import com.tokopedia.mvc.presentation.product.variant.review.uimodel.ReviewVariantEvent
import com.tokopedia.mvc.presentation.product.variant.review.uimodel.ReviewVariantUiState
import com.tokopedia.mvc.presentation.product.variant.select.uimodel.SelectVariantEffect
import com.tokopedia.mvc.presentation.product.variant.select.uimodel.SelectVariantEvent
import com.tokopedia.mvc.presentation.product.variant.select.uimodel.SelectVariantUiState
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

@ExperimentalCoroutinesApi
class ReviewVariantViewModelTest {

    @RelaxedMockK
    lateinit var productV3UseCase: ProductV3UseCase

    private lateinit var viewModel: ReviewVariantViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReviewVariantViewModel(CoroutineTestDispatchersProvider, productV3UseCase)
    }

    //region getAllVariantsByParentProductId
    @Test
    fun `when get variant detail success, should set variants to ui state`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0))
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0))
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))
            
            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedValues = arrayListOf<ReviewVariantUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = true,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(111,112),
                    isVariantCheckable = true,
                    isVariantDeletable = true
                )
            )


            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(product.id, actual.parentProductId)
            assertEquals(listOf(111L,112L), actual.originalVariantIds)
            assertEquals("productName", actual.parentProductName)
            assertEquals(1, actual.parentProductStock)
            assertEquals(5000, actual.parentProductPrice)
            assertEquals(1, actual.parentProductSoldCount)
            assertEquals("imageUrl", actual.parentProductImageUrl)
            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru", isSelected = true),
                    secondVariant.copy(variantName = "Biru", isSelected = true)
                ),
                actual.variants
            )
           assertEquals(setOf(111L,112L), actual.selectedVariantIds)

            job.cancel()
        }
    }

    @Test
    fun `when get variant detail error, should set error to ui state`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(id = 1)

            val error = MessageErrorException("Server Error")

            val param = ProductV3UseCase.Param(productId = product.id)
            coEvery { productV3UseCase.execute(param) } throws error

            val emittedValues = arrayListOf<ReviewVariantUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            //When
            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = true,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(111,112),
                    isVariantCheckable = true,
                    isVariantDeletable = true
                )
            )

            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(error, actual.error)


            job.cancel()
        }
    }

    @Test
    fun `when parent product is not selected, all variant should be unselected`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0))
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0))
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedValues = arrayListOf<ReviewVariantUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = false,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(111,112),
                    isVariantCheckable = true,
                    isVariantDeletable = true
                )
            )


            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru", isSelected = false),
                    secondVariant.copy(variantName = "Biru", isSelected = false)
                ),
                actual.variants
            )


            job.cancel()
        }
    }


    @Test
    fun `when variants configured as uncheckable, all variant should be uncheckable`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                ),
                selectedVariantsIds = setOf(111)
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0))
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0))
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedValues = arrayListOf<ReviewVariantUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = true,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(111,112),
                    isVariantCheckable = false,
                    isVariantDeletable = true
                )
            )


            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru", isCheckable = false, isSelected = true),
                    secondVariant.copy(variantName = "Biru", isCheckable = false, isSelected = true)
                ),
                actual.variants
            )


            job.cancel()
        }
    }

    @Test
    fun `when variants configured as non-deletable, all variant isDeletable should be false`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0))
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0))
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedValues = arrayListOf<ReviewVariantUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = false,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(111,112),
                    isVariantCheckable = true,
                    isVariantDeletable = false
                )
            )


            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru", isDeletable = false),
                    secondVariant.copy(variantName = "Biru", isDeletable = false)
                ),
                actual.variants
            )


            job.cancel()
        }
    }

    @Test
    fun `when displaying list of variants, should display list of variants that were previously selected`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0))
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0))
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            val previouslySelectedVariants = listOf(firstVariant.variantId)

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedValues = arrayListOf<ReviewVariantUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = true,
                    selectedProduct = selectedProduct,
                    originalVariantIds = previouslySelectedVariants,
                    isVariantCheckable = true,
                    isVariantDeletable = true
                )
            )


            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru", isSelected = true)
                ),
                actual.variants
            )


            job.cancel()
        }
    }
    //endregion

    //region handleFetchProductVariants
    //endregion

    //region getVariantDetail
    //endregion

    //region getVariantDetail
    //endregion

    //region handleCheckAllVariant
    //region handleCheckAllProduct
    @Test
    fun `when check all variants, should select all eligible variants`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = false,
                        reason = "",
                        isSelected = false
                    )
                )
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0), isEligible = true)
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0), isEligible = false)
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedValues = arrayListOf<ReviewVariantUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = true,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(firstVariant.variantId, secondVariant.variantId),
                    isVariantCheckable = true,
                    isVariantDeletable = true
                )
            )

            viewModel.processEvent(ReviewVariantEvent.EnableSelectAllCheckbox)


            //Then
            val actual = emittedValues.last()

            assertEquals(true, actual.isSelectAllActive)
            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru", isSelected = true),
                    secondVariant.copy(variantName = "Biru", isSelected = false)
                ),
                actual.variants
            )
            assertEquals(setOf<Long>(111), actual.selectedVariantIds)

            job.cancel()
        }
    }
    //endregion

    //region handleUncheckAllVariant
    @Test
    fun `when uncheck all variants, should unselect all variants`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0), isEligible = true)
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0), isEligible = true)
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedValues = arrayListOf<ReviewVariantUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = true,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(firstVariant.variantId, secondVariant.variantId),
                    isVariantCheckable = true,
                    isVariantDeletable = true
                )
            )

            viewModel.processEvent(ReviewVariantEvent.AddVariantToSelection(firstVariant.variantId))
            viewModel.processEvent(ReviewVariantEvent.AddVariantToSelection(secondVariant.variantId))

            //When
            viewModel.processEvent(ReviewVariantEvent.DisableSelectAllCheckbox)


            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isSelectAllActive)
            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru", isSelected = false),
                    secondVariant.copy(variantName = "Biru", isSelected = false)
                ),
                actual.variants
            )
            assertEquals(emptySet<Long>(), actual.selectedVariantIds)

            job.cancel()
        }
    }
    //endregion

    //region handleRemoveVariantFromSelection
    @Test
    fun `when removing a variant from selection, its isSelected property should be false`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0), isEligible = true)
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0), isEligible = true)
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedValues = arrayListOf<ReviewVariantUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = true,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(firstVariant.variantId, secondVariant.variantId),
                    isVariantCheckable = true,
                    isVariantDeletable = true
                )
            )

            viewModel.processEvent(ReviewVariantEvent.AddVariantToSelection(firstVariant.variantId))
            viewModel.processEvent(ReviewVariantEvent.AddVariantToSelection(secondVariant.variantId))

            //When
            viewModel.processEvent(ReviewVariantEvent.RemoveVariantFromSelection(firstVariant.variantId))

            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru", isSelected = false),
                    secondVariant.copy(variantName = "Biru", isSelected = true)
                ),
                actual.variants
            )
            assertEquals(setOf<Long>(112), actual.selectedVariantIds)

            job.cancel()
        }
    }
    //endregion

    //region handleRemoveVariant
    @Test
    fun `when removing a variant, should delete the variant from list`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0), isEligible = true)
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0), isEligible = true)
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedValues = arrayListOf<ReviewVariantUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = false,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(firstVariant.variantId, secondVariant.variantId),
                    isVariantCheckable = true,
                    isVariantDeletable = true
                )
            )


            //When
            viewModel.processEvent(ReviewVariantEvent.ApplyRemoveVariant(firstVariant.variantId))

            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(secondVariant.copy(variantName = "Biru")),
                actual.variants
            )
            assertEquals(emptySet<Long>(), actual.selectedVariantIds)

            job.cancel()
        }
    }

    @Test
    fun `when removing a non exist variant, should not delete any variant from list`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0), isEligible = true)
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0), isEligible = true)
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedValues = arrayListOf<ReviewVariantUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = false,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(firstVariant.variantId, secondVariant.variantId),
                    isVariantCheckable = true,
                    isVariantDeletable = true
                )
            )


            //When
            val unknownVariantId: Long = 99
            viewModel.processEvent(ReviewVariantEvent.ApplyRemoveVariant(unknownVariantId))

            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru"),
                    secondVariant.copy(variantName = "Biru")
                ),
                actual.variants
            )
            assertEquals(emptySet<Long>(), actual.selectedVariantIds)

            job.cancel()
        }
    }

    //endregion

    //region handleBulkDeleteVariant
    @Test
    fun `when bulk delete variants, should delete selected variants from list`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0), isEligible = true)
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0), isEligible = true)
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedValues = arrayListOf<ReviewVariantUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = false,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(firstVariant.variantId, secondVariant.variantId),
                    isVariantCheckable = true,
                    isVariantDeletable = true
                )
            )


            viewModel.processEvent(ReviewVariantEvent.EnableSelectAllCheckbox)

            //When
            viewModel.processEvent(ReviewVariantEvent.ApplyBulkDeleteVariant)

            //Then
            val actual = emittedValues.last()

            assertEquals(
                emptyList<Variant>(),
                actual.variants
            )
            assertEquals(emptySet<Long>(), actual.selectedVariantIds)

            job.cancel()
        }
    }
    //endregion

    //region shouldSelectVariant
    //endregion

    //region TapSelectButton
    @Test
    fun `when tap select button, should emit ConfirmUpdateVariant along with variant ids`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0), isEligible = true)
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0), isEligible = true)
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedEffects = arrayListOf<ReviewVariantEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = false,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(firstVariant.variantId, secondVariant.variantId),
                    isVariantCheckable = true,
                    isVariantDeletable = true
                )
            )

            //When
            viewModel.processEvent(ReviewVariantEvent.TapSelectButton)

            //Then
            val emittedEffect = emittedEffects.last()


            assertEquals(
                ReviewVariantEffect.ConfirmUpdateVariant(
                    setOf(firstVariant.variantId, secondVariant.variantId)
                ),
                emittedEffect
            )

            job.cancel()
        }
    }
    //endregion


    //region TapBulkDeleteVariant
    @Test
    fun `when tap bulk delete variant button, should emit ShowBulkDeleteVariantConfirmationDialog along with selected variant count`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0), isEligible = true)
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0), isEligible = true)
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedEffects = arrayListOf<ReviewVariantEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = false,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(firstVariant.variantId, secondVariant.variantId),
                    isVariantCheckable = true,
                    isVariantDeletable = true
                )
            )

            viewModel.processEvent(ReviewVariantEvent.AddVariantToSelection(firstVariant.variantId))

            //When
            viewModel.processEvent(ReviewVariantEvent.TapBulkDeleteVariant)

            //Then
            val emittedEffect = emittedEffects.last()


            assertEquals(
                ReviewVariantEffect.ShowBulkDeleteVariantConfirmationDialog(toDeleteProductCount = 1),
                emittedEffect
            )

            job.cancel()
        }
    }
    //endregion

    //region TapRemoveVariant
    @Test
    fun `when tap remove variant, should emit ShowDeleteVariantConfirmationDialog`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    ),
                    Product.Variant(
                        variantProductId = 112,
                        isEligible = true,
                        reason = "",
                        isSelected = false
                    )
                )
            )
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0), isEligible = true)
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0), isEligible = true)
            val selectedProduct =
                SelectedProduct(parentProductId = product.id, variantProductIds = listOf(111, 112))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    )
                )
            )

            val emittedEffects = arrayListOf<ReviewVariantEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            viewModel.processEvent(
                ReviewVariantEvent.FetchProductVariants(
                    isParentProductSelected = false,
                    selectedProduct = selectedProduct,
                    originalVariantIds = listOf(firstVariant.variantId, secondVariant.variantId),
                    isVariantCheckable = true,
                    isVariantDeletable = true
                )
            )

            //When
            viewModel.processEvent(ReviewVariantEvent.TapRemoveVariant(firstVariant.variantId))

            //Then
            val emittedEffect = emittedEffects.last()


            assertEquals(
                ReviewVariantEffect.ShowDeleteVariantConfirmationDialog(productId = firstVariant.variantId),
                emittedEffect
            )

            job.cancel()
        }
    }
    //endregion

    //region processEvent
    @Test
    fun `When unlisted event is triggered, should not emit any effect`() {
        runBlockingTest {
            //When
            viewModel.processEvent(mockk())

            val emittedEffects = arrayListOf<ReviewVariantEffect>()

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

    private fun mockResponse(
        productId: Long,
        variants: List<Variant>,
        selections: List<VariantResult.Selection>
    ) {
        val param = ProductV3UseCase.Param(productId = productId)
        coEvery { productV3UseCase.execute(param) } returns VariantResult(
            parentProductName = "productName",
            parentProductPrice = 5_000,
            parentProductStock = 1,
            parentProductSoldCount = 1,
            parentProductImageUrl = "imageUrl",
            selections = selections,
            products = variants
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
            stock = 1,
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

    private fun populateVariant(): Variant {
        return Variant(
            variantId = 1,
            variantName = "",
            combinations = listOf(),
            imageUrl = "imageUrl",
            price = 5000L,
            isSelected = false,
            stockCount = 10,
            soldCount = 10,
            isEligible = true,
            reason = "",
            isCheckable = true,
            isDeletable = true
        )
    }
}
