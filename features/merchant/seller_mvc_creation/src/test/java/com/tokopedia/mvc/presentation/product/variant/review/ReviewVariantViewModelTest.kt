package com.tokopedia.mvc.presentation.product.variant.review

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.Variant
import com.tokopedia.mvc.domain.entity.VariantResult
import com.tokopedia.mvc.domain.usecase.ProductV3UseCase
import com.tokopedia.mvc.presentation.product.variant.review.uimodel.ReviewVariantEvent
import com.tokopedia.mvc.presentation.product.variant.review.uimodel.ReviewVariantUiState
import com.tokopedia.mvc.presentation.product.variant.select.uimodel.SelectVariantEvent
import com.tokopedia.mvc.presentation.product.variant.select.uimodel.SelectVariantUiState
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
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

    //region handleFetchProductVariants
    //endregion

    //region getVariantDetail
    //endregion

    //region getVariantDetail
    //endregion

    //region handleCheckAllVariant
    //endregion

    //region handleUncheckAllVariant
    //endregion

    //region handleAddVariantToSelection
    //endregion

    //region handleRemoveVariantFromSelection
    //endregion

    //region handleRemoveVariant
    //endregion

    //region handleBulkDeleteVariant
    //endregion

    //region shouldSelectVariant
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
