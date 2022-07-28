package com.tokopedia.shop.flashsale.presentation.creation.highlight

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.data.request.DoSellerCampaignProductSubmissionRequest
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct
import com.tokopedia.shop.flashsale.domain.entity.ProductSubmissionResult
import com.tokopedia.shop.flashsale.domain.entity.enums.ProductionSubmissionAction
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignProductListUseCase
import com.tokopedia.shop.flashsale.presentation.creation.highlight.mapper.HighlightProductUiMapper
import com.tokopedia.shop.flashsale.presentation.creation.highlight.mapper.HighlightableProductRequestMapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ManageHighlightedProductViewModelTest {

    @RelaxedMockK
    lateinit var getSellerCampaignProductListUseCase: GetSellerCampaignProductListUseCase

    @RelaxedMockK
    lateinit var doSellerCampaignProductSubmissionUseCase: DoSellerCampaignProductSubmissionUseCase

    @RelaxedMockK
    lateinit var mapper: HighlightableProductRequestMapper

    @RelaxedMockK
    lateinit var highlightProductUiMapper: HighlightProductUiMapper

    @RelaxedMockK
    lateinit var tracker: ShopFlashSaleTracker

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        ManageHighlightedProductViewModel(
            CoroutineTestDispatchersProvider,
            getSellerCampaignProductListUseCase,
            doSellerCampaignProductSubmissionUseCase,
            mapper,
            highlightProductUiMapper,
            tracker
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }


    //region getProducts
    //endregion

    //region submitHighlightedProducts
    @Test
    fun `When submit highlighted products, should send tracker`() {
        //Given
        val campaignId : Long = 1001
        val products = listOf<HighlightableProduct>()

        //When
        viewModel.submitHighlightedProducts(campaignId, products)

        //Then
        coVerify { tracker.sendClickButtonProceedOnManageHighlightPageEvent() }
    }

    @Test
    fun `When submit highlighted products success, observer should successfully receive the data`() =
        runBlocking {
            //Given
            val campaignId : Long = 1001
            val products = listOf<HighlightableProduct>()
            val mappedProducts = listOf<DoSellerCampaignProductSubmissionRequest.ProductData>()
            val submissionResult = ProductSubmissionResult(
                isSuccess = true,
                errorMessage = "",
                failedProducts = listOf()
            )
            val expected = Success(submissionResult)

            coEvery {
                doSellerCampaignProductSubmissionUseCase.execute(
                    campaignId.toString(),
                    ProductionSubmissionAction.SUBMIT,
                    mappedProducts
                )
            } returns submissionResult


            //When
            viewModel.submitHighlightedProducts(campaignId, products)

            //Then
            val actual = viewModel.submit.getOrAwaitValue()
            assertEquals(expected, actual)
        }



    @Test
    fun `When submit highlighted products error, observer should receive error result`() =
        runBlocking {
            //Given
            val campaignId : Long = 1001
            val products = listOf<HighlightableProduct>()
            val mappedProducts = listOf<DoSellerCampaignProductSubmissionRequest.ProductData>()
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery {  doSellerCampaignProductSubmissionUseCase.execute(
                campaignId.toString(),
                ProductionSubmissionAction.SUBMIT,
                mappedProducts
            ) } throws error

            //When
            viewModel.submitHighlightedProducts(campaignId, products)

            //Then
            val actual = viewModel.submit.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    //endregion

    //region saveDraft
    @Test
    fun `When save draft success, observer should successfully receive the result`() =
        runBlocking {
            //Given
            val campaignId : Long = 1001
            val products = listOf<HighlightableProduct>()
            val mappedProducts = listOf<DoSellerCampaignProductSubmissionRequest.ProductData>()
            val submissionResult = ProductSubmissionResult(
                isSuccess = true,
                errorMessage = "",
                failedProducts = listOf()
            )
            val expected = Success(submissionResult)

            coEvery {
                doSellerCampaignProductSubmissionUseCase.execute(
                    campaignId.toString(),
                    ProductionSubmissionAction.SUBMIT,
                    mappedProducts
                )
            } returns submissionResult


            //When
            viewModel.saveDraft(campaignId, products)

            //Then
            val actual = viewModel.saveDraft.getOrAwaitValue()
            assertEquals(expected, actual)
        }



    @Test
    fun `When save draft error, observer should receive error result`() =
        runBlocking {
            //Given
            val campaignId : Long = 1001
            val products = listOf<HighlightableProduct>()
            val mappedProducts = listOf<DoSellerCampaignProductSubmissionRequest.ProductData>()
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery {  doSellerCampaignProductSubmissionUseCase.execute(
                campaignId.toString(),
                ProductionSubmissionAction.SUBMIT,
                mappedProducts
            ) } throws error

            //When
            viewModel.saveDraft(campaignId, products)

            //Then
            val actual = viewModel.saveDraft.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    //endregion

    //region addProductIdToSelection & getSelectedProductIds
    @Test
    fun `When get selected product, should return correct values`() {
        //Given
        val product = buildHighlightableProduct()
        viewModel.addProductIdToSelection(product)

        val expected = listOf(product)

        //When
        val actual = viewModel.getSelectedProductIds()

        //Then
        assertEquals(expected, actual)
    }
    //endregion

    //region removeProductIdFromSelection
    @Test
    fun `When removing a product from selected product list, selected product size should be decremented by one`() {
        //Given
        val product = buildHighlightableProduct()

        viewModel.addProductIdToSelection(product)
        val expectedProductSizeAfterRemoval = 0

        //When
        viewModel.removeProductIdFromSelection(product)

        //Then
        assertEquals(expectedProductSizeAfterRemoval, viewModel.getSelectedProductIds().size)
    }

    @Test
    fun `When removing an unselected product, selected product size should not be decremented`() {
        //Given
        val invalidProductId: Long = 900
        val productToBeRemoved = buildHighlightableProduct().copy(id = invalidProductId)

        val product = buildHighlightableProduct()
        viewModel.addProductIdToSelection(product)
        val expectedProductSizeAfterRemoval = 1

        //When
        viewModel.removeProductIdFromSelection(productToBeRemoved)

        //Then
        assertEquals(expectedProductSizeAfterRemoval, viewModel.getSelectedProductIds().size)
    }
    //endregion

    //region markAsSelected
    @Test
    fun `When parent product not selected, should not update any property`() {
        //Given
        val firstProduct = buildHighlightableProduct().copy(parentId = 0)
        val products = listOf(firstProduct)
        val expected = listOf(firstProduct)

        //When
        val actual = viewModel.markAsSelected(products)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When select a parent product, should update product as selected correctly`() {
        //Given
        val firstProduct = buildHighlightableProduct().copy(parentId = 0)

        viewModel.addProductIdToSelection(firstProduct)

        val expected = listOf(
            firstProduct.copy(
                isSelected = true,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED,
                position = 1
            )
        )

        //When
        val actual = viewModel.markAsSelected(viewModel.getSelectedProductIds())

        //Then
        assertEquals(expected, actual)
    }


    @Test
    fun `When select a variant product within same parent, other variant should be disabled`() {
        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 100, parentId = 200)
        val secondProduct = buildHighlightableProduct().copy(id = 200, parentId = 200)
        val products = listOf(firstProduct, secondProduct)

        viewModel.addProductIdToSelection(secondProduct)

        val expected = listOf(
            secondProduct.copy(isSelected = true, disabled = false, disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED, position = 1),
            firstProduct.copy(isSelected = false, disabled = true, disabledReason = HighlightableProduct.DisabledReason.OTHER_PRODUCT_WITH_SAME_PARENT_ID_ALREADY_SELECTED, position = 2)
        )

        //When
        val actual = viewModel.markAsSelected(products)

        //Then
        assertEquals(expected, actual)
    }


    @Test
    fun `When variant product within same parent but not selected, should not update any properties`() {
        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 100, parentId = 200, position = 1)
        val secondProduct = buildHighlightableProduct().copy(id = 200, parentId = 200, position = 2)
        val products = listOf(firstProduct, secondProduct)

        val expected = listOf(firstProduct, secondProduct)

        //When
        val actual = viewModel.markAsSelected(products)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When select a variant product with different parent, other variant should be stay enabled`() {
        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 100, parentId = 200)
        val secondProduct = buildHighlightableProduct().copy(id = 200, parentId = 400)
        val products = listOf(firstProduct, secondProduct)

        viewModel.addProductIdToSelection(secondProduct)

        val expected = listOf(
            firstProduct.copy(isSelected = true, disabled = false, disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED, position = 1),
            secondProduct.copy(isSelected = true, disabled = false, disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED, position = 2)
        )

        //When
        val actual = viewModel.markAsSelected(products)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When variant product with different parent but not selected,  should not update any properties`() {
        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 100, parentId = 200, position = 1)
        val secondProduct = buildHighlightableProduct().copy(id = 200, parentId = 500, position = 2)
        val products = listOf(firstProduct, secondProduct)

        val expected = listOf(firstProduct, secondProduct)

        //When
        val actual = viewModel.markAsSelected(products)

        //Then
        assertEquals(expected, actual)
    }
    //endregion



    //region markAsUnselected
    @Test
    fun `When unselect a product, should disable the product`() {
        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 100)
        val secondProduct = buildHighlightableProduct().copy(id = 200)

        viewModel.addProductIdToSelection(firstProduct)
        viewModel.addProductIdToSelection(secondProduct)

        val expected = listOf(
            secondProduct.copy(isSelected = true, position = 1),
            firstProduct.copy(isSelected = false, position = 2)
        )

        //When
        val actual = viewModel.markAsUnselected(firstProduct, viewModel.getSelectedProductIds())

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When unselect a product with different parent, other product should be enabled except the current selected variant`() {
        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 100, parentId = 1)
        val secondProduct = buildHighlightableProduct().copy(id = 200, parentId = 2)
        val products = listOf(firstProduct, secondProduct)

        viewModel.addProductIdToSelection(firstProduct)

        val expected = listOf(
            secondProduct.copy(
                position = 1,
                isSelected = true,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            ),
            firstProduct.copy(
                position = 2,
                isSelected = false,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            )
        )

        //When
        val actual = viewModel.markAsUnselected(firstProduct, products)

        //Then
        assertEquals(expected, actual)
    }
    @Test
    fun `When unselect a product with same parent id, other product should be enabled except the current selected variant`() {
        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 100, parentId = 2)
        val secondProduct = buildHighlightableProduct().copy(id = 200, parentId = 2)
        val products = listOf(firstProduct, secondProduct)

        viewModel.addProductIdToSelection(firstProduct)

        val expected = listOf(
            secondProduct.copy(
                position = 1,
                isSelected = true,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            ),
            firstProduct.copy(
                position = 2,
                isSelected = false,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            )
        )

        //When
        val actual = viewModel.markAsUnselected(firstProduct, products)

        //Then
        assertEquals(expected, actual)
    }

    //endregion

    private fun buildHighlightableProduct(): HighlightableProduct {
        return HighlightableProduct(
            id = 100,
            parentId = 1,
            "Adidas AirMax",
            "https://tokopedia.com/adidas.png",
            originalPrice = 50_000,
            discountedPrice = 1,
            discountPercentage = 90,
            customStock = 10,
            warehouses = listOf(),
            maxOrder = 1,
            disabled = false,
            isSelected = true,
            position = 1,
            disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED,
            "Teaser #1"
        )
    }
}