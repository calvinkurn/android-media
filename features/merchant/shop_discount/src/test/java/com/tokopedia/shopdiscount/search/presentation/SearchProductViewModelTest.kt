package com.tokopedia.shopdiscount.search.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shopdiscount.common.data.response.DoSlashPriceProductReservationResponse
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.shopdiscount.common.domain.MutationDoSlashPriceProductReservationUseCase
import com.tokopedia.shopdiscount.common.entity.ProductType
import com.tokopedia.shopdiscount.manage.data.mapper.ProductMapper
import com.tokopedia.shopdiscount.manage.data.mapper.UpdateDiscountRequestMapper
import com.tokopedia.shopdiscount.manage.data.response.DeleteDiscountResponse
import com.tokopedia.shopdiscount.manage.data.response.GetSlashPriceProductListResponse
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.manage.domain.entity.ProductData
import com.tokopedia.shopdiscount.manage.domain.usecase.DeleteDiscountUseCase
import com.tokopedia.shopdiscount.manage.domain.usecase.GetSlashPriceProductListUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchProductViewModelTest {


    companion object {
        private const val DISCOUNT_STATUS_ID_ONGOING = 2
        private const val FIRST_PAGE = 1
    }


    @RelaxedMockK
    lateinit var getSlashPriceProductListUseCase: GetSlashPriceProductListUseCase

    @RelaxedMockK
    lateinit var deleteDiscountUseCase: DeleteDiscountUseCase

    @RelaxedMockK
    lateinit var reserveProductUseCase: MutationDoSlashPriceProductReservationUseCase

    @RelaxedMockK
    lateinit var productMapper: ProductMapper

    @RelaxedMockK
    lateinit var updateDiscountRequestMapper: UpdateDiscountRequestMapper

    @RelaxedMockK
    lateinit var productsObserver: Observer<in Result<ProductData>>

    @RelaxedMockK
    lateinit var deleteDiscountObserver: Observer<in Result<Boolean>>

    @RelaxedMockK
    lateinit var reserveProductObserver: Observer<in Result<Boolean>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val productIds = listOf("230490", "23234234")
    private val requestId = "1930129309"

    private val viewModel by lazy {
        SearchProductViewModel(
            CoroutineTestDispatchersProvider,
            getSlashPriceProductListUseCase,
            productMapper,
            deleteDiscountUseCase,
            reserveProductUseCase,
            updateDiscountRequestMapper
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.products.observeForever(productsObserver)
        viewModel.deleteDiscount.observeForever(deleteDiscountObserver)
        viewModel.reserveProduct.observeForever(reserveProductObserver)
    }

    @After
    fun tearDown() {
        viewModel.products.removeObserver(productsObserver)
        viewModel.deleteDiscount.removeObserver(deleteDiscountObserver)
        viewModel.reserveProduct.removeObserver(reserveProductObserver)
    }

    @Test
    fun `When get slash price products success, observer should receive success result`() =
        runBlocking {
            //Given
            val searchKeyword = "playstation"
            val firstProduct = buildDummyProduct("first-product-id-111212")
            val secondProduct = buildDummyProduct("second-product-id-123123")
            val products = listOf(firstProduct, secondProduct)

            val response = GetSlashPriceProductListResponse(
                GetSlashPriceProductListResponse.GetSlashPriceProductList(totalProduct = products.size)
            )

            coEvery { getSlashPriceProductListUseCase.executeOnBackground() } returns response
            coEvery { productMapper.map(response) } returns products

            //When
            viewModel.getSlashPriceProducts(
                FIRST_PAGE,
                DISCOUNT_STATUS_ID_ONGOING,
                searchKeyword,
                isMultiSelectEnabled = false,
                shouldDisableProductSelection = false
            )

            //Then
            coVerify { productsObserver.onChanged(Success(ProductData(2, products))) }
        }

    @Test
    fun `When get slash price products success and on multi select turned on, should set model to display checkbox`() =
        runBlocking {
            //Given
            val searchKeyword = "playstation"
            val firstProduct = buildDummyProduct("first-product-id-111212")
            val secondProduct = buildDummyProduct("second-product-id-123123")
            val products = listOf(firstProduct, secondProduct)
            val expected = products.map {
                it.copy(
                    shouldDisplayCheckbox = true,
                    disableClick = false,
                    isCheckboxTicked = false
                )
            }

            val response = GetSlashPriceProductListResponse(
                GetSlashPriceProductListResponse.GetSlashPriceProductList(totalProduct = products.size)
            )

            coEvery { getSlashPriceProductListUseCase.executeOnBackground() } returns response
            coEvery { productMapper.map(response) } returns products

            //When
            viewModel.getSlashPriceProducts(
                FIRST_PAGE,
                DISCOUNT_STATUS_ID_ONGOING,
                searchKeyword,
                isMultiSelectEnabled = true,
                shouldDisableProductSelection = false
            )


            coVerify { productsObserver.onChanged(Success(ProductData(2, expected))) }
        }

    @Test
    fun `When get slash price products success and click disable are turned on, should set model to disable product`() =
        runBlocking {
            //Given
            val disableProduct = true
            val searchKeyword = "playstation"
            val firstProduct = buildDummyProduct("first-product-id-111212").copy(disableClick = disableProduct)
            val products = listOf(firstProduct)

            val response = GetSlashPriceProductListResponse(
                GetSlashPriceProductListResponse.GetSlashPriceProductList(
                    slashPriceProductList = listOf(
                        GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct()
                    ),
                    totalProduct = products.size
                )
            )

            coEvery { getSlashPriceProductListUseCase.executeOnBackground() } returns response
            coEvery { productMapper.map(response) } returns products

            //When
            viewModel.getSlashPriceProducts(
                FIRST_PAGE,
                DISCOUNT_STATUS_ID_ONGOING,
                searchKeyword,
                isMultiSelectEnabled = false,
                shouldDisableProductSelection = disableProduct
            )


            coVerify { productsObserver.onChanged(Success(ProductData(1, products))) }
        }

    @Test
    fun `When get slash price products success and product already added to selection, should set model to make checkbox ticked`() =
        runBlocking {
            //Given
            val searchKeyword = "playstation"
            val product = buildDummyProduct("first-product-id-111212")
            val products = listOf(product)
            viewModel.addProductToSelection(product)

            val tickedProducts = products.map { it.copy(isCheckboxTicked = true) }

            val response = GetSlashPriceProductListResponse(
                GetSlashPriceProductListResponse.GetSlashPriceProductList(totalProduct = products.size)
            )

            coEvery { getSlashPriceProductListUseCase.executeOnBackground() } returns response
            coEvery { productMapper.map(response) } returns products

            //When
            viewModel.getSlashPriceProducts(
                FIRST_PAGE,
                DISCOUNT_STATUS_ID_ONGOING,
                searchKeyword,
                isMultiSelectEnabled = false,
                shouldDisableProductSelection = false
            )


            coVerify { productsObserver.onChanged(Success(ProductData(1, tickedProducts))) }
        }

    @Test
    fun `When get slash price products error, observer should receive error result`() =
        runBlocking {
            val searchKeyword = "playstation"
            val error = MessageErrorException("Server error")
            coEvery { getSlashPriceProductListUseCase.executeOnBackground() } throws error

            //When
            viewModel.getSlashPriceProducts(
                FIRST_PAGE,
                DISCOUNT_STATUS_ID_ONGOING,
                searchKeyword,
                isMultiSelectEnabled = false,
                shouldDisableProductSelection = false
            )

            //Then
            coVerify { productsObserver.onChanged(Fail(error)) }
        }

    @Test
    fun `When delete discount success, observer should receive success result`() =
        runBlocking {
            //Given
            val response = DeleteDiscountResponse(
                DeleteDiscountResponse.DoSlashPriceStop(
                    ResponseHeader(success = true)
                )
            )

            coEvery { deleteDiscountUseCase.executeOnBackground() } returns response

            //When
            viewModel.deleteDiscount(DISCOUNT_STATUS_ID_ONGOING, productIds)

            //Then
            coVerify { deleteDiscountObserver.onChanged(Success(true)) }
        }

    @Test
    fun `When delete discount error, observer should receive error result`() =
        runBlocking {
            val error = MessageErrorException("Server error")
            coEvery { deleteDiscountUseCase.executeOnBackground() } throws error

            //When
            viewModel.deleteDiscount(DISCOUNT_STATUS_ID_ONGOING, productIds)

            //Then
            coVerify { deleteDiscountObserver.onChanged(Fail(error)) }
        }

    @Test
    fun `When reserve product success, observer should receive success result`() =
        runBlocking {
            //Given
            val response = DoSlashPriceProductReservationResponse(
                DoSlashPriceProductReservationResponse.DoSlashPriceProductReservation(
                    ResponseHeader(
                        success = true
                    )
                )
            )

            coEvery { reserveProductUseCase.executeOnBackground() } returns response

            //When
            viewModel.reserveProduct(requestId, productIds)

            //Then
            coVerify { reserveProductObserver.onChanged(Success(true)) }
        }

    @Test
    fun `When reserve product error, observer should receive error result`() =
        runBlocking {
            val error = MessageErrorException("Server error")
            coEvery { reserveProductUseCase.executeOnBackground() } throws error

            //When
            viewModel.reserveProduct(requestId, productIds)

            //Then
            coVerify { reserveProductObserver.onChanged(Fail(error)) }
        }


    @Test
    fun `When set total product, should store total product correctly`() {
        //Given
        val totalProduct = 92
        val expected = totalProduct

        //When
        viewModel.setTotalProduct(totalProduct)
        val actual = viewModel.getTotalProduct()

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When set selected product, should store selected product correctly`() {
        //Given
        val selectedProduct = mockk<Product>()
        val expected = selectedProduct

        //When
        viewModel.setSelectedProduct(selectedProduct)
        val actual = viewModel.getSelectedProduct()

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When set current multi select value, should store current multi select value correctly`() {
        //Given
        val isOnMultiSelect = true
        val expected = isOnMultiSelect

        //When
        viewModel.setInMultiSelectMode(isOnMultiSelect)
        val actual = viewModel.isOnMultiSelectMode()

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When set disable product, should store current disable product value correctly`() {
        //Given
        val shouldDisableProduct = true
        val expected = shouldDisableProduct

        //When
        viewModel.setDisableProductSelection(shouldDisableProduct)
        val actual = viewModel.shouldDisableProductSelection()

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When enabling multi select, should update data model to display checkbox`() {
        //Given
        val firstProduct = buildDummyProduct("first-product-id-111212")
        val secondProduct = buildDummyProduct("second-product-id-123123")
        val products = listOf(firstProduct, secondProduct)
        val expected = products.map { it.copy(shouldDisplayCheckbox = true) }

        //When
        val actual = viewModel.enableMultiSelect(products)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When disabling multi select, should update data model accordingly`() {
        //Given
        val firstProduct = buildDummyProduct("first-product-id-111212")
        val secondProduct = buildDummyProduct("second-product-id-123123")
        val products = listOf(firstProduct, secondProduct)
        val expected = products.map {
            it.copy(
                shouldDisplayCheckbox = false,
                isCheckboxTicked = false,
                disableClick = false
            )
        }

        //When
        val actual = viewModel.disableMultiSelect(products)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When disabling product, non ticked product should be updated to disable state`() {
        //Given
        val firstProduct = buildDummyProduct("first-product-id-111212")
        val secondProduct = buildDummyProduct("second-product-id-123123")
        val thirdProduct = buildDummyProduct("third-product-id-1231245")

        val products = listOf(
            firstProduct.copy(isCheckboxTicked = true, disableClick = false),
            secondProduct.copy(isCheckboxTicked = false, disableClick = false),
            thirdProduct.copy(isCheckboxTicked = false, disableClick = false)
        )

        val expected = listOf(
            firstProduct.copy(isCheckboxTicked = true, disableClick = false),
            secondProduct.copy(isCheckboxTicked = false, disableClick = true),
            thirdProduct.copy(isCheckboxTicked = false, disableClick = true)
        )

        //When
        val actual = viewModel.disableProducts(products)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When enabling product, non ticked product should be updated to enabled state`() {
        //Given
        val firstProduct = buildDummyProduct("first-product-id-111212")
        val secondProduct = buildDummyProduct("second-product-id-123123")
        val thirdProduct = buildDummyProduct("third-product-id-1231245")

        val products = listOf(
            firstProduct.copy(isCheckboxTicked = true, disableClick = false),
            secondProduct.copy(isCheckboxTicked = false, disableClick = false),
            thirdProduct.copy(isCheckboxTicked = false, disableClick = false)
        )

        val expected = listOf(
            firstProduct.copy(isCheckboxTicked = true, disableClick = false),
            secondProduct.copy(isCheckboxTicked = false, disableClick = false),
            thirdProduct.copy(isCheckboxTicked = false, disableClick = false)
        )

        //When
        val actual = viewModel.enableProduct(products)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When add a product to selection, selected product ids should be increase`() {
        //Given
        val product = buildDummyProduct("first-product-id-111212")
        val expected = 1

        //When
        viewModel.addProductToSelection(product)
        val actual = viewModel.getSelectedProductCount()

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When get selected product ids, should return selected product ids correctly`() {
        //Given
        val product = buildDummyProduct("first-product-id-111212")
        val expected = listOf("first-product-id-111212")

        //When
        viewModel.addProductToSelection(product)
        val actual = viewModel.getSelectedProductIds()

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When removing a product, selected product ids should be decreased`() {
        //Given
        val product = buildDummyProduct("first-product-id-111212")

        val expected = 0

        //When
        viewModel.addProductToSelection(product)
        viewModel.removeProductFromSelection(product)

        val actual = viewModel.getSelectedProductCount()

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When removing all products, selected product ids should be zero`() {
        //Given
        val product = buildDummyProduct("first-product-id-111212")

        val expected = 0

        //When
        viewModel.addProductToSelection(product)
        viewModel.removeAllProductFromSelection()

        val actual = viewModel.getSelectedProductCount()

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When set request id, should store request id value correctly`() {
        //Given
        val expected = "92322342"

        //When
        viewModel.setRequestId(expected)
        val actual = viewModel.getRequestId()

        //Then
        assertEquals(expected, actual)
    }

    private fun buildDummyProduct(id: String): Product {
        return Product(
            id,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            0,
            false,
            "",
            "",
            ProductType.SINGLE,
            false,
            false,
            false,
            false,
            false,
            false,
            "",
            false
        )
    }
}