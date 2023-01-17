package com.tokopedia.shop.flashsale.presentation.creation.highlight

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.data.request.DoSellerCampaignProductSubmissionRequest
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignProductListRequest
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct
import com.tokopedia.shop.flashsale.domain.entity.ProductSubmissionResult
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ProductionSubmissionAction
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignProductList
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

    companion object {
        private const val CAMPAIGN_ID: Long = 1001
        private const val PAGE_SIZE = 10
        private const val OFFSET = 0
        private const val PRODUCT_LIST_TYPE_ID = 0
        private const val PRODUCT_NAME = ""
        private const val PARENT_PRODUCT_ID: Long = 0
    }
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
    @Test
    fun `When get products and already have 5 selected products, the first 5 products should be selected`() {

        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 100, parentId = 1, isSelected = false, highlightProductWording = "Teaser #1")
        val secondProduct = buildHighlightableProduct().copy(id = 200,  parentId = 2, isSelected = false, highlightProductWording = "Teaser #2")
        val thirdProduct = buildHighlightableProduct().copy(id = 300,  parentId = 3, isSelected = false, highlightProductWording = "Teaser #3")
        val fourthProduct = buildHighlightableProduct().copy(id = 400, parentId = 4,  isSelected = false, highlightProductWording = "Teaser #4")
        val fifthProduct = buildHighlightableProduct().copy(id = 500,  parentId = 5, isSelected = false, highlightProductWording = "Teaser #5")
        val sixthProduct = buildHighlightableProduct().copy(id = 600, parentId = 6,  isSelected = false, highlightProductWording = "")
        val response = SellerCampaignProductList()

        val expected = Success(listOf(
            buildHighlightableProduct().copy(id = 100, isSelected = true, parentId = 1, highlightProductWording = "Teaser #1", position = 1),
            buildHighlightableProduct().copy(id = 200, isSelected = true, parentId = 2, highlightProductWording = "Teaser #2", position = 2),
            buildHighlightableProduct().copy(id = 300, isSelected = true, parentId = 3, highlightProductWording = "Teaser #3", position = 3),
            buildHighlightableProduct().copy(id = 400, isSelected = true, parentId = 4, highlightProductWording = "Teaser #4", position = 4),
            buildHighlightableProduct().copy(id = 500, isSelected = true, parentId = 5, highlightProductWording = "Teaser #5", position = 5),
            buildHighlightableProduct().copy(id = 600, isSelected = false, parentId = 6, disabled = true, disabledReason = HighlightableProduct.DisabledReason.MAX_PRODUCT_REACHED, highlightProductWording = "", position = 0),
        ))

        coEvery {
            getSellerCampaignProductListUseCase.execute(
                campaignId = CAMPAIGN_ID,
                productName = PRODUCT_NAME,
                listType = PRODUCT_LIST_TYPE_ID,
                pagination = GetSellerCampaignProductListRequest.Pagination(PAGE_SIZE, OFFSET)
            )
        } returns response

        val mappedProductsResponse = listOf(firstProduct, secondProduct, thirdProduct, fourthProduct, fifthProduct, sixthProduct)
        coEvery { highlightProductUiMapper.map(response) } returns mappedProductsResponse

        viewModel.setIsFirstLoad(true)

        //When
        viewModel.getProducts(CAMPAIGN_ID, PRODUCT_NAME, PAGE_SIZE, OFFSET)

        //Then
        val actual = viewModel.products.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `When get products and selected products less than 5 product, should enable all product`() {

        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 100, parentId = 1, isSelected = false, highlightProductWording = "Teaser #1")
        val secondProduct = buildHighlightableProduct().copy(id = 200,  parentId = 2, isSelected = false, highlightProductWording = "Teaser #2")
        val thirdProduct = buildHighlightableProduct().copy(id = 300,  parentId = 3, isSelected = false, highlightProductWording = "Teaser #3")
        val fourthProduct = buildHighlightableProduct().copy(id = 400, parentId = 4,  isSelected = false, highlightProductWording = "")
        val fifthProduct = buildHighlightableProduct().copy(id = 500,  parentId = 5, isSelected = false, highlightProductWording = "")
        val sixthProduct = buildHighlightableProduct().copy(id = 600, parentId = 6,  isSelected = false, highlightProductWording = "")
        val response = SellerCampaignProductList()

        val expected = Success(listOf(
            buildHighlightableProduct().copy(id = 100, isSelected = true, parentId = 1, highlightProductWording = "Teaser #1", position = 1),
            buildHighlightableProduct().copy(id = 200, isSelected = true, parentId = 2, highlightProductWording = "Teaser #2", position = 2),
            buildHighlightableProduct().copy(id = 300, isSelected = true, parentId = 3, highlightProductWording = "Teaser #3", position = 3),
            buildHighlightableProduct().copy(id = 400, isSelected = false, parentId = 4, highlightProductWording = "", position = 0),
            buildHighlightableProduct().copy(id = 500, isSelected = false, parentId = 5, highlightProductWording = "", position = 0),
            buildHighlightableProduct().copy(id = 600, isSelected = false, parentId = 6, highlightProductWording = "", position = 0)
        ))

        coEvery {
            getSellerCampaignProductListUseCase.execute(
                campaignId = CAMPAIGN_ID,
                productName = PRODUCT_NAME,
                listType = PRODUCT_LIST_TYPE_ID,
                pagination = GetSellerCampaignProductListRequest.Pagination(PAGE_SIZE, OFFSET)
            )
        } returns response

        val mappedProductsResponse = listOf(firstProduct, secondProduct, thirdProduct, fourthProduct, fifthProduct, sixthProduct)
        coEvery { highlightProductUiMapper.map(response) } returns mappedProductsResponse

        viewModel.setIsFirstLoad(true)

        //When
        viewModel.getProducts(CAMPAIGN_ID, PRODUCT_NAME, PAGE_SIZE, OFFSET)

        //Then
        val actual = viewModel.products.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `When get products and there is one variant selected, other variant with same parent should be disabled`() {

        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 100, parentId = 1, isSelected = false, highlightProductWording = "")
        val secondProduct = buildHighlightableProduct().copy(id = 200,  parentId = 2, isSelected = false, highlightProductWording = "")
        val thirdProduct = buildHighlightableProduct().copy(id = 300,  parentId = 3, isSelected = false, highlightProductWording = "")
        val fourthProduct = buildHighlightableProduct().copy(id = 400, parentId = 4,  isSelected = false, highlightProductWording = "")
        val fifthProduct = buildHighlightableProduct().copy(id = 500,  parentId = 5, isSelected = false, highlightProductWording = "")
        val sixthProduct = buildHighlightableProduct().copy(id = 600, parentId = 1,  isSelected = false, highlightProductWording = "")
        val response = SellerCampaignProductList()

        viewModel.addProductIdToSelection(firstProduct)

        //Sixth product should be disabled since first product who has same parent already selected
        val expected = Success(listOf(
            buildHighlightableProduct().copy(id = 100, isSelected = true, parentId = 1, highlightProductWording = "", position = 1),
            buildHighlightableProduct().copy(id = 200, isSelected = false, parentId = 2, highlightProductWording = "", position = 0),
            buildHighlightableProduct().copy(id = 300, isSelected = false, parentId = 3, highlightProductWording = "", position = 0),
            buildHighlightableProduct().copy(id = 400, isSelected = false, parentId = 4, highlightProductWording = "", position = 0),
            buildHighlightableProduct().copy(id = 500, isSelected = false, parentId = 5, highlightProductWording = "", position = 0),
            buildHighlightableProduct().copy(id = 600, isSelected = false, parentId = 1, disabled = true, disabledReason = HighlightableProduct.DisabledReason.OTHER_PRODUCT_WITH_SAME_PARENT_ID_ALREADY_SELECTED, highlightProductWording = "", position = 0)
        ))

        coEvery {
            getSellerCampaignProductListUseCase.execute(
                campaignId = CAMPAIGN_ID,
                productName = PRODUCT_NAME,
                listType = PRODUCT_LIST_TYPE_ID,
                pagination = GetSellerCampaignProductListRequest.Pagination(PAGE_SIZE, OFFSET)
            )
        } returns response

        val mappedProductsResponse = listOf(firstProduct, secondProduct, thirdProduct, fourthProduct, fifthProduct, sixthProduct)
        coEvery { highlightProductUiMapper.map(response) } returns mappedProductsResponse

        viewModel.setIsFirstLoad(false)

        //When
        viewModel.getProducts(CAMPAIGN_ID, PRODUCT_NAME, PAGE_SIZE, OFFSET)

        //Then
        val actual = viewModel.products.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `When one parent product selected, it should be the only product selected`() {

        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 100, parentId = PARENT_PRODUCT_ID, isSelected = false, highlightProductWording = "")
        val secondProduct = buildHighlightableProduct().copy(id = 200,  parentId = PARENT_PRODUCT_ID, isSelected = false, highlightProductWording = "")
        val thirdProduct = buildHighlightableProduct().copy(id = 300,  parentId = PARENT_PRODUCT_ID, isSelected = false, highlightProductWording = "")
        val fourthProduct = buildHighlightableProduct().copy(id = 400, parentId = PARENT_PRODUCT_ID,  isSelected = false, highlightProductWording = "")
        val fifthProduct = buildHighlightableProduct().copy(id = 500,  parentId = PARENT_PRODUCT_ID, isSelected = false, highlightProductWording = "")
        val sixthProduct = buildHighlightableProduct().copy(id = 600, parentId = PARENT_PRODUCT_ID,  isSelected = false, highlightProductWording = "")
        val response = SellerCampaignProductList()

        viewModel.addProductIdToSelection(firstProduct)

        val expected = Success(listOf(
            buildHighlightableProduct().copy(id = 100, isSelected = true, parentId = 0, highlightProductWording = "", position = 1),
            buildHighlightableProduct().copy(id = 200, isSelected = false, parentId = 0, highlightProductWording = "", position = 0),
            buildHighlightableProduct().copy(id = 300, isSelected = false, parentId = 0, highlightProductWording = "", position = 0),
            buildHighlightableProduct().copy(id = 400, isSelected = false, parentId = 0, highlightProductWording = "", position = 0),
            buildHighlightableProduct().copy(id = 500, isSelected = false, parentId = 0, highlightProductWording = "", position = 0),
            buildHighlightableProduct().copy(id = 600, isSelected = false, parentId = 0, highlightProductWording = "", position = 0)
        ))

        coEvery {
            getSellerCampaignProductListUseCase.execute(
                campaignId = CAMPAIGN_ID,
                productName = PRODUCT_NAME,
                listType = PRODUCT_LIST_TYPE_ID,
                pagination = GetSellerCampaignProductListRequest.Pagination(PAGE_SIZE, OFFSET)
            )
        } returns response

        val mappedProductsResponse = listOf(firstProduct, secondProduct, thirdProduct, fourthProduct, fifthProduct, sixthProduct)
        coEvery { highlightProductUiMapper.map(response) } returns mappedProductsResponse

        viewModel.setIsFirstLoad(false)

        //When
        viewModel.getProducts(CAMPAIGN_ID, PRODUCT_NAME, PAGE_SIZE, OFFSET)

        //Then
        val actual = viewModel.products.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `When load next page and seller already select products, isSelected should be true for the selected products`() {

        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 100, parentId = 1, isSelected = false, highlightProductWording = "Teaser #1")
        val secondProduct = buildHighlightableProduct().copy(id = 200,  parentId = 2, isSelected = false, highlightProductWording = "Teaser #2")
        val thirdProduct = buildHighlightableProduct().copy(id = 300,  parentId = 3, isSelected = false, highlightProductWording = "Teaser #3")
        val fourthProduct = buildHighlightableProduct().copy(id = 400, parentId = 4,  isSelected = false, highlightProductWording = "")
        val fifthProduct = buildHighlightableProduct().copy(id = 500,  parentId = 5, isSelected = false, highlightProductWording = "")
        val sixthProduct = buildHighlightableProduct().copy(id = 600, parentId = 6,  isSelected = false, highlightProductWording = "")
        val response = SellerCampaignProductList()

        viewModel.addProductIdToSelection(firstProduct)
        viewModel.addProductIdToSelection(secondProduct)
        viewModel.addProductIdToSelection(thirdProduct)
        viewModel.addProductIdToSelection(fourthProduct)
        viewModel.addProductIdToSelection(fifthProduct)

        val expected = Success(listOf(
            buildHighlightableProduct().copy(id = 100, isSelected = true, parentId = 1, highlightProductWording = "Teaser #1", position = 1),
            buildHighlightableProduct().copy(id = 200, isSelected = true, parentId = 2, highlightProductWording = "Teaser #2", position = 2),
            buildHighlightableProduct().copy(id = 300, isSelected = true, parentId = 3, highlightProductWording = "Teaser #3", position = 3),
            buildHighlightableProduct().copy(id = 400, isSelected = true, parentId = 4, highlightProductWording = "", position = 4),
            buildHighlightableProduct().copy(id = 500, isSelected = true, parentId = 5, highlightProductWording = "", position = 5),
            buildHighlightableProduct().copy(id = 600, isSelected = false, disabled = true, disabledReason = HighlightableProduct.DisabledReason.MAX_PRODUCT_REACHED, parentId = 6, highlightProductWording = "", position = 0)
        ))

        coEvery {
            getSellerCampaignProductListUseCase.execute(
                campaignId = CAMPAIGN_ID,
                productName = PRODUCT_NAME,
                listType = PRODUCT_LIST_TYPE_ID,
                pagination = GetSellerCampaignProductListRequest.Pagination(PAGE_SIZE, OFFSET)
            )
        } returns response

        val mappedProductsResponse = listOf(firstProduct, secondProduct, thirdProduct, fourthProduct, fifthProduct, sixthProduct)
        coEvery { highlightProductUiMapper.map(response) } returns mappedProductsResponse

        viewModel.setIsFirstLoad(false)

        //When
        viewModel.getProducts(CAMPAIGN_ID, PRODUCT_NAME, PAGE_SIZE, OFFSET)

        //Then
        val actual = viewModel.products.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `When get products error, observer should receive error result`() {
        //Given
        val response = SellerCampaignProductList()
        val error = MessageErrorException("Server error")
        val expected = Fail(error)

        coEvery {
            getSellerCampaignProductListUseCase.execute(
                campaignId = CAMPAIGN_ID,
                productName = PRODUCT_NAME,
                listType = PRODUCT_LIST_TYPE_ID,
                pagination = GetSellerCampaignProductListRequest.Pagination(PAGE_SIZE, OFFSET)
            )
        } returns response

        coEvery { highlightProductUiMapper.map(response) } throws  error
        viewModel.setIsFirstLoad(true)

        //When

        viewModel.getProducts(CAMPAIGN_ID, PRODUCT_NAME, PAGE_SIZE, OFFSET)

        //Then
        val actual = viewModel.products.getOrAwaitValue()
        assertEquals(expected, actual)
    }
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
    fun `When seller already select 5 product, the remaining products should be disabled`() {
        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 1, parentId = 100, isSelected = false)
        val secondProduct = buildHighlightableProduct().copy(id = 2, parentId = 200, isSelected = false)
        val thirdProduct = buildHighlightableProduct().copy(id = 3, parentId = 300, isSelected = false)
        val fourthProduct = buildHighlightableProduct().copy(id = 4, parentId = 400, isSelected = false)
        val fifthProduct = buildHighlightableProduct().copy(id = 5, parentId = 500, isSelected = false)
        val sixthProduct = buildHighlightableProduct().copy(id = 6, parentId = 600, isSelected = false)
        val seventhProduct = buildHighlightableProduct().copy(id = 7, parentId = 700, isSelected = false)

        viewModel.addProductIdToSelection(firstProduct)
        viewModel.addProductIdToSelection(secondProduct)
        viewModel.addProductIdToSelection(thirdProduct)
        viewModel.addProductIdToSelection(fourthProduct)
        viewModel.addProductIdToSelection(fifthProduct)

        val allProducts = listOf(
            firstProduct,
            secondProduct,
            thirdProduct,
            fourthProduct,
            fifthProduct,
            sixthProduct,
            seventhProduct
        )

        val expected = listOf(
            firstProduct.copy(isSelected = true, disabled = false, position = 1),
            secondProduct.copy(isSelected = true, disabled = false, position = 2),
            thirdProduct.copy(isSelected = true, disabled = false, position = 3),
            fourthProduct.copy(isSelected = true, disabled = false, position = 4),
            fifthProduct.copy(isSelected = true, disabled = false, position = 5),
            sixthProduct.copy(isSelected = false, disabled = true, position = 6),
            seventhProduct.copy(isSelected = false, disabled = true, position = 7),
        )

        //When
        val actual = viewModel.markAsSelected(allProducts)

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

    @Test
    fun `When already select 5 parent product then unselect one product, other parent product should be enabled`() {
        //Given
        val firstProduct = buildHighlightableProduct().copy(id = 100, parentId = 1)
        val secondProduct = buildHighlightableProduct().copy(id = 200, parentId = 2)
        val thirdProduct = buildHighlightableProduct().copy(id = 300, parentId = 3)
        val fourthProduct = buildHighlightableProduct().copy(id = 400, parentId = 4)
        val fifthProduct = buildHighlightableProduct().copy(id = 500, parentId = 5)
        val sixthProduct = buildHighlightableProduct().copy(id = 600, parentId = 6, isSelected = false, disabled = true, disabledReason = HighlightableProduct.DisabledReason.MAX_PRODUCT_REACHED)
        val seventhProduct = buildHighlightableProduct().copy(id = 700, parentId = 7, isSelected = false, disabled = true, disabledReason = HighlightableProduct.DisabledReason.MAX_PRODUCT_REACHED)

        val products = listOf(firstProduct, secondProduct, thirdProduct, fourthProduct, fifthProduct, sixthProduct, seventhProduct)

        viewModel.addProductIdToSelection(firstProduct)
        viewModel.addProductIdToSelection(secondProduct)
        viewModel.addProductIdToSelection(thirdProduct)
        viewModel.addProductIdToSelection(fourthProduct)
        viewModel.addProductIdToSelection(fifthProduct)

        val expected = listOf(
            firstProduct.copy(
                position = 1,
                isSelected = true,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            ),
            secondProduct.copy(
                position = 2,
                isSelected = true,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            ),
            thirdProduct.copy(
                position = 3,
                isSelected = true,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            ),
            fourthProduct.copy(
                position = 4,
                isSelected = true,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            ),
            fifthProduct.copy(
                position = 5,
                isSelected = false,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            ),
            sixthProduct.copy(
                position = 6,
                isSelected = false,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            ),
            seventhProduct.copy(
                position = 7,
                isSelected = false,
                disabled = false,
                disabledReason = HighlightableProduct.DisabledReason.NOT_DISABLED
            )
        )

        //When
        val actual = viewModel.markAsUnselected(fifthProduct, products)

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