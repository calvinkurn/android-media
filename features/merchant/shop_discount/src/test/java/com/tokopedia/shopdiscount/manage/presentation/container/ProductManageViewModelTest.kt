package com.tokopedia.shopdiscount.manage.presentation.container

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shopdiscount.manage.data.mapper.ProductListMetaMapper
import com.tokopedia.shopdiscount.manage.data.response.GetSlashPriceProductListMetaResponse
import com.tokopedia.shopdiscount.manage.domain.entity.DiscountStatusMeta
import com.tokopedia.shopdiscount.manage.domain.entity.PageTab
import com.tokopedia.shopdiscount.manage.domain.usecase.GetSlashPriceProductListMetaUseCase
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

class ProductManageViewModelTest {


    @RelaxedMockK
    lateinit var getSlashPriceProductListMetaUseCase: GetSlashPriceProductListMetaUseCase

    @RelaxedMockK
    lateinit var productListMetaMapper: ProductListMetaMapper


    @RelaxedMockK
    lateinit var productsMetaObserver: Observer<in Result<List<DiscountStatusMeta>>>

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val viewModel by lazy {
        ProductManageViewModel(
            CoroutineTestDispatchersProvider,
            getSlashPriceProductListMetaUseCase,
            productListMetaMapper
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.productsMeta.observeForever(productsMetaObserver)
    }

    @After
    fun tearDown() {
        viewModel.productsMeta.removeObserver(productsMetaObserver)
    }

    @Test
    fun `When get slash price product meta success, observer should receive success result`() =
        runBlocking {
            //Given
            val response = buildDummyResponse()
            val tab = response.getSlashPriceProductListMeta.data.tab
            val formattedTabs = listOf(
                DiscountStatusMeta(
                    "ONGOING",
                    "Berlangsung",
                    20
                )
            )

            coEvery { getSlashPriceProductListMetaUseCase.executeOnBackground() } returns response
            coEvery { productListMetaMapper.map(tab) } returns formattedTabs

            //When
            viewModel.getSlashPriceProductsMeta()

            //Then
            coVerify { productsMetaObserver.onChanged(Success(formattedTabs)) }
        }

    @Test
    fun `When get slash price product meta error, observer should receive error result`() =
        runBlocking {
            val error = MessageErrorException("Server error")
            coEvery { getSlashPriceProductListMetaUseCase.executeOnBackground() } throws error

            //When
            viewModel.getSlashPriceProductsMeta()

            //Then
            coVerify { productsMetaObserver.onChanged(Fail(error)) }
        }

    @Test
    fun `When discount status is match, should update product count from remote data`()  {
        //Given
        val ongoingDiscountStatusId = 2
        val localData = listOf(PageTab("Berlangsung", "ONGOING", ongoingDiscountStatusId, 0, 0))
        val remoteData = listOf(DiscountStatusMeta("ONGOING", "Berlangsung", 20))
        val expected = listOf(PageTab("Berlangsung", "ONGOING", ongoingDiscountStatusId, 20, 0))

        //When
        val actual = viewModel.findDiscountStatusCount(localData, remoteData)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When discount status is not match, product count should be 0`()  {
        //Given
        val ongoingDiscountStatusId = 2
        val localData = listOf(PageTab("Berlangsung", "ONGOING", ongoingDiscountStatusId, 0, 0))
        val remoteData = listOf(DiscountStatusMeta("ONGOING_EVENT", "Berlangsung", 20))
        val expected = listOf(PageTab("Berlangsung", "ONGOING", ongoingDiscountStatusId, 0,0))

        //When
        val actual = viewModel.findDiscountStatusCount(localData, remoteData)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When set selected tab position, should store tab position correctly`()  {
        //Given
        val currentTabPosition = 2
        val expected = 2

        //When
        viewModel.setSelectedTabPosition(currentTabPosition)
        val actual = viewModel.getSelectedTabPosition()

        //Then
        assertEquals(expected, actual)
    }

    private fun buildDummyResponse(): GetSlashPriceProductListMetaResponse {
        return GetSlashPriceProductListMetaResponse(
            GetSlashPriceProductListMetaResponse.GetSlashPriceProductListMeta(
                GetSlashPriceProductListMetaResponse.GetSlashPriceProductListMeta.Data(
                    listOf(
                        GetSlashPriceProductListMetaResponse.GetSlashPriceProductListMeta.Data.Tab(
                            "ONGOING",
                            "Berlangsung",
                            20
                        )
                    )
                )
            )
        )
    }
}