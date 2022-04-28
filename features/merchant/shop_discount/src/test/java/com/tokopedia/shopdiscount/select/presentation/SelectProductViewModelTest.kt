package com.tokopedia.shopdiscount.select.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.shopdiscount.common.data.response.DoSlashPriceProductReservationResponse
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.shopdiscount.common.domain.MutationDoSlashPriceProductReservationUseCase
import com.tokopedia.shopdiscount.select.data.mapper.ReservableProductMapper
import com.tokopedia.shopdiscount.select.data.mapper.ReserveProductRequestMapper
import com.tokopedia.shopdiscount.select.data.mapper.ShopBenefitMapper
import com.tokopedia.shopdiscount.select.data.response.GetSlashPriceProductListToReserveResponse
import com.tokopedia.shopdiscount.select.domain.entity.ReservableProduct
import com.tokopedia.shopdiscount.select.domain.entity.ShopBenefit
import com.tokopedia.shopdiscount.select.domain.usecase.GetSlashPriceProductListToReserveUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class SelectProductViewModelTest {

    companion object {
        private const val FIRST_PAGE = 1
    }


    @RelaxedMockK
    lateinit var getSlashPriceProductListToReserveUseCase: GetSlashPriceProductListToReserveUseCase

    @RelaxedMockK
    lateinit var reserveProductUseCase: MutationDoSlashPriceProductReservationUseCase

    @RelaxedMockK
    lateinit var getSlashPriceBenefitUseCase: GetSlashPriceBenefitUseCase

    @RelaxedMockK
    lateinit var reservableProductMapper: ReservableProductMapper

    @RelaxedMockK
    lateinit var reserveProductRequestMapper: ReserveProductRequestMapper

    @RelaxedMockK
    lateinit var shopBenefitMapper: ShopBenefitMapper

    @RelaxedMockK
    lateinit var productsObserver: Observer<in Result<List<ReservableProduct>>>

    @RelaxedMockK
    lateinit var reserveProductObserver: Observer<in Result<Boolean>>

    @RelaxedMockK
    lateinit var benefitObserver: Observer<in Result<ShopBenefit>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val productIds = listOf("230490", "23234234")
    private val requestId = "1930129309"

    private val viewModel by lazy {
        SelectProductViewModel(
            CoroutineTestDispatchersProvider,
            getSlashPriceProductListToReserveUseCase,
            reserveProductUseCase,
            getSlashPriceBenefitUseCase,
            reservableProductMapper,
            reserveProductRequestMapper,
            shopBenefitMapper
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.products.observeForever(productsObserver)
        viewModel.reserveProduct.observeForever(reserveProductObserver)
        viewModel.benefit.observeForever(benefitObserver)

    }

    @After
    fun tearDown() {
        viewModel.products.removeObserver(productsObserver)
        viewModel.reserveProduct.removeObserver(reserveProductObserver)
        viewModel.benefit.removeObserver(benefitObserver)
    }

    @Test
    fun `When get reservable product success, observer should receive success result`() =
        runBlocking {
            //Given
            val response = GetSlashPriceProductListToReserveResponse()
            val reservableProducts = listOf<ReservableProduct>()

            coEvery { getSlashPriceProductListToReserveUseCase.executeOnBackground() } returns response
            coEvery { reservableProductMapper.map(response) } returns reservableProducts

            //When
            viewModel.getReservableProducts(requestId, FIRST_PAGE, anyString(), false)

            //Then
            coVerify { productsObserver.onChanged(Success(reservableProducts)) }
        }

    @Test
    fun `When get reservable product success and disable product turned on, should set model to disable product`() =
        runBlocking {
            //Given
            val response = GetSlashPriceProductListToReserveResponse()
            val firstProduct = buildDummyProduct("product-123123")
            val secondProduct = buildDummyProduct("product-123555")

            viewModel.addProductToSelection(firstProduct)

            val products = listOf(firstProduct, secondProduct)
            val expected = listOf(
                firstProduct.copy(disableClick = false, isCheckboxTicked = true),
                secondProduct.copy(disableClick = true, isCheckboxTicked = false)
            )

            coEvery { getSlashPriceProductListToReserveUseCase.executeOnBackground() } returns response
            coEvery { reservableProductMapper.map(response) } returns products

            //When
            viewModel.getReservableProducts(requestId, FIRST_PAGE, anyString(), true)

            //Then
            coVerify { productsObserver.onChanged(Success(expected)) }
        }

    @Test
    fun `When get reservable product success and product is already on user selection, should set model to make checkbox ticked`() =
        runBlocking {
            //Given
            val response = GetSlashPriceProductListToReserveResponse()
            val product = buildDummyProduct("product-123123")

            viewModel.addProductToSelection(product)

            val products = listOf(product)
            val expected = listOf(product.copy(disableClick = false, isCheckboxTicked = true))

            coEvery { getSlashPriceProductListToReserveUseCase.executeOnBackground() } returns response
            coEvery { reservableProductMapper.map(response) } returns products

            //When
            viewModel.getReservableProducts(requestId, FIRST_PAGE, anyString(), false)

            //Then
            coVerify { productsObserver.onChanged(Success(expected)) }
        }

    @Test
    fun `When get reservable product error, observer should receive error result`() =
        runBlocking {
            //Given
            val error = MessageErrorException("Server error")
            coEvery { getSlashPriceProductListToReserveUseCase.executeOnBackground() } throws error

            //When
            viewModel.getReservableProducts(requestId, FIRST_PAGE, anyString(), false)

            //Then
            coVerify { productsObserver.onChanged(Fail(error)) }
        }

    @Test
    fun `When get shop benefit success, observer should receive success result`() =
        runBlocking {
            //Given
            val response = GetSlashPriceBenefitResponse()
            val shopBenefit = ShopBenefit(false, emptyList())

            coEvery { getSlashPriceBenefitUseCase.executeOnBackground() } returns response
            coEvery { shopBenefitMapper.map(response) } returns shopBenefit

            //When
            viewModel.getSellerBenefits()

            //Then
            coVerify { benefitObserver.onChanged(Success(shopBenefit)) }
        }

    @Test
    fun `When  get shop benefit error, observer should receive error result`() =
        runBlocking {
            //Given
            val error = MessageErrorException("Server error")
            coEvery { getSlashPriceBenefitUseCase.executeOnBackground() } throws error

            //When
            viewModel.getSellerBenefits()

            //Then
            coVerify { benefitObserver.onChanged(Fail(error)) }
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
    fun `When removing a product, selected product ids should be decreased`() {
        //Given
        val product = buildDummyProduct("first-product-id-111212")

        val expected = 0

        //When
        viewModel.addProductToSelection(product)
        viewModel.removeProductFromSelection(product)

        val actual = viewModel.getSelectedProducts().size

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

    @Test
    fun `When set remaining quota, should store remaining quota value correctly`() {
        //Given
        val remainingQuota = 5
        val expected = remainingQuota

        //When
        viewModel.setRemainingQuota(remainingQuota)
        val actual = viewModel.getRemainingQuota()

        //Then
        assertEquals(expected, actual)
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


    private fun buildDummyProduct(id: String): ReservableProduct {
        return ReservableProduct(
            id,
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
            false,
            false,
            false
        )
    }
}