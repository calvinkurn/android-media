package com.tokopedia.mvc.presentation.product.variant.select

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.Variant
import com.tokopedia.mvc.domain.entity.VariantResult
import com.tokopedia.mvc.domain.usecase.ProductV3UseCase
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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SelectVariantViewModelTest {

    @RelaxedMockK
    lateinit var productV3UseCase: ProductV3UseCase

    private lateinit var viewModel: SelectVariantViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SelectVariantViewModel(CoroutineTestDispatchersProvider, productV3UseCase)
    }

    //region getProductsAndProductsMetadata
    @Test
    fun `when get products success, should set product to ui state`() {
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

            val emittedValues = arrayListOf<SelectVariantUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(
                SelectVariantEvent.FetchProductVariants(product)
            )


            //Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(product.id, actual.parentProductId)
            assertEquals("productName", actual.parentProductName)
            assertEquals(20, actual.parentProductStock)
            assertEquals(5000, actual.parentProductPrice)
            assertEquals(1, actual.parentProductSoldCount)
            assertEquals("imageUrl", actual.parentProductImageUrl)
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
