package com.tokopedia.mvc.presentation.product.variant.select

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.Variant
import com.tokopedia.mvc.domain.entity.VariantResult
import com.tokopedia.mvc.domain.usecase.ProductV3UseCase
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
class SelectVariantViewModelTest {

    @RelaxedMockK
    lateinit var productV3UseCase: ProductV3UseCase

    private lateinit var viewModel: SelectVariantViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SelectVariantViewModel(CoroutineTestDispatchersProvider, productV3UseCase)
    }

    //region getAllVariantsByParentProductId
    @Test
    fun `when get variants success, should set variants to ui state`() {
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

    @Test
    fun `when get variants error, should set error to ui state`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(id = 1)

            val error = MessageErrorException("Server Error")

            val param = ProductV3UseCase.Param(productId = product.id)
            coEvery { productV3UseCase.execute(param) } throws error

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
            assertEquals(error, actual.error)


            job.cancel()
        }
    }

    @Test
    fun `when get variants error, should emit ShowError effect`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(id = 1)

            val error = MessageErrorException("Server Error")

            val param = ProductV3UseCase.Param(productId = product.id)
            coEvery { productV3UseCase.execute(param) } throws error

            val emittedEffects = arrayListOf<SelectVariantEffect>()

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


    //region isVariantEligible
    @Test
    fun `when variant is previously selected and also eligible, it should be selected`() {
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
            val thirdVariant = populateVariant().copy(variantId = 113, combinations = listOf(0))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant, thirdVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Ungu"))
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

            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru", isSelected = true),
                    secondVariant.copy(variantName = "Biru")
                ),
                actual.variants
            )


            job.cancel()
        }
    }

    @Test
    fun `when variant is previously selected but not eligible, isSelected should be false`() {
        runBlockingTest {
            //Given
            val product = populateProduct().copy(
                id = 1,
                originalVariants = listOf(
                    Product.Variant(
                        variantProductId = 111,
                        isEligible = false,
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
            val thirdVariant = populateVariant().copy(variantId = 113, combinations = listOf(0))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant, thirdVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Ungu"))
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

            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru", isSelected = false, isEligible = false),
                    secondVariant.copy(variantName = "Biru")
                ),
                actual.variants
            )


            job.cancel()
        }
    }

    @Test
    fun `when get variant from remote not found, isEligible should be false`() {
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
            val thirdVariant = populateVariant().copy(variantId = 113, combinations = listOf(0))

            mockResponse(
                productId = product.id,
                variants = listOf(firstVariant, secondVariant, thirdVariant),
                selections = listOf(
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Biru"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Merah"))
                    ),
                    VariantResult.Selection(
                        options = listOf(VariantResult.Selection.Option(value = "Ungu"))
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

            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru"),
                    secondVariant.copy(variantName = "Biru")
                ),
                actual.variants
            )


            job.cancel()
        }
    }
    //endregion


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

            viewModel.processEvent(SelectVariantEvent.EnableSelectAllCheckbox)


            //Then
            val actual = emittedValues.last()

            assertEquals(true, actual.isSelectAllActive)
            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru", isSelected = true),
                    secondVariant.copy(variantName = "Biru", isSelected = true)
                ),
                actual.variants
            )
            assertEquals(setOf<Long>(111, 112), actual.selectedVariantIds)

            job.cancel()
        }
    }
    //endregion


    //region handleUncheckAllProduct
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

            viewModel.processEvent(SelectVariantEvent.DisableSelectAllCheckbox)


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


    //region handleAddProductToSelection
    @Test
    fun `when add variant to selection, should add it to list of selected variants`() {
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
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0), isSelected = false)
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0), isSelected = false)

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

            viewModel.processEvent(SelectVariantEvent.AddProductToSelection(firstVariant.variantId))


            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru", isSelected = true),
                    secondVariant.copy(variantName = "Biru", isSelected = false)
                ),
                actual.variants
            )
            assertEquals(setOf(firstVariant.variantId), actual.selectedVariantIds)

            job.cancel()
        }
    }
    //endregion

    //region handleRemoveProductFromSelection
    @Test
    fun `when remove variant from a selection, should remove it from list of selected variants`() {
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
            val firstVariant = populateVariant().copy(variantId = 111, combinations = listOf(0), isSelected = false)
            val secondVariant = populateVariant().copy(variantId = 112, combinations = listOf(0), isSelected = false)

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

            viewModel.processEvent(SelectVariantEvent.AddProductToSelection(firstVariant.variantId))
            viewModel.processEvent(SelectVariantEvent.AddProductToSelection(secondVariant.variantId))

            viewModel.processEvent(SelectVariantEvent.RemoveProductFromSelection(firstVariant.variantId))


            //Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    firstVariant.copy(variantName = "Biru", isSelected = false),
                    secondVariant.copy(variantName = "Biru", isSelected = true)
                ),
                actual.variants
            )
            assertEquals(setOf(secondVariant.variantId), actual.selectedVariantIds)

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

            val emittedEffects = arrayListOf<SelectVariantEffect>()

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

    //region TapSelectButton
    @Test
    fun `When select button tapped, should emit ConfirmUpdateVariant effect`() {
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


            //When
            viewModel.processEvent(SelectVariantEvent.FetchProductVariants(product))
            viewModel.processEvent(SelectVariantEvent.EnableSelectAllCheckbox)

            viewModel.processEvent(SelectVariantEvent.TapSelectButton)

            val emittedEffects = arrayListOf<SelectVariantEffect>()

            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            //Then
            val actualEffect = emittedEffects.lastOrNull()

            assertEquals(
                SelectVariantEffect.ConfirmUpdateVariant(
                    selectedVariantIds = setOf(
                        firstVariant.variantId,
                        secondVariant.variantId
                    )
                ),
                actualEffect
            )

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
