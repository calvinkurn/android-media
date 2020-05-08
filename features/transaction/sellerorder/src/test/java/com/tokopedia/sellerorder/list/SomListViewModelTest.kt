package com.tokopedia.sellerorder.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerorder.SomTestDispatcherProvider
import com.tokopedia.sellerorder.list.data.model.*
import com.tokopedia.sellerorder.list.domain.SomGetTickerListUseCase
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomListViewModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by fwidjaja on 2020-02-17.
 */

@RunWith(JUnit4::class)
class SomListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = SomTestDispatcherProvider()
    private lateinit var somListViewModel: SomListViewModel
    private val graphqlRepository = mockk<GraphqlRepository>()
    private var listTickers = listOf<SomListTicker.Data.OrderTickers.Tickers>()

    @RelaxedMockK
    lateinit var somGetTickerListUseCase: SomGetTickerListUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        somListViewModel = SomListViewModel(dispatcher, graphqlRepository, somGetTickerListUseCase)

        val ticker1 = SomListTicker.Data.OrderTickers.Tickers(123, "body1", "shortDesc1", true)
        val ticker2 = SomListTicker.Data.OrderTickers.Tickers(456, "body2", "shortDesc2", true)
        val ticker3 = SomListTicker.Data.OrderTickers.Tickers(789, "body3", "shortDesc3", true)
        listTickers = arrayListOf(ticker1, ticker2, ticker3).toMutableList()
    }

    @Test
    fun getTickerData_shouldReturnSuccess() {
        //given
        /*val ticker1 = SomListTicker.Data.OrderTickers.Tickers(123, "body1", "shortDesc1", true)
        val ticker2 = SomListTicker.Data.OrderTickers.Tickers(456, "body2", "shortDesc2", true)
        val ticker3 = SomListTicker.Data.OrderTickers.Tickers(789, "body3", "shortDesc3", true)
        val listTickers = arrayListOf(ticker1, ticker2, ticker3).toMutableList()*/
        coEvery {
            somGetTickerListUseCase.execute(any(), any())
        } returns Success(SomListTicker.Data.OrderTickers("", "", "", -1, listTickers).listTicker.toMutableList())

        //when
        somListViewModel.loadTickerList("")

        //then
        assert(somListViewModel.tickerListResult.value is Success)
        assert((somListViewModel.tickerListResult.value as Success<MutableList<SomListTicker.Data.OrderTickers.Tickers>>).data[0].tickerId == 123)
    }

    // bisa cek length
    // lanjut yg return failed

    /*@Test
    fun getTickerData_shouldReturnSuccess() {
        //given
        val ticker1 = SomListTicker.Data.OrderTickers.Tickers(123, "body1", "shortDesc1", true)
        val ticker2 = SomListTicker.Data.OrderTickers.Tickers(456, "body2", "shortDesc2", true)
        val ticker3 = SomListTicker.Data.OrderTickers.Tickers(789, "body3", "shortDesc3", true)
        val listTickers = arrayListOf(ticker1, ticker2, ticker3)

        val dataListTickers : ArrayList<SomListTicker.Data.OrderTickers.Tickers> = listTickers
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf(SomListTicker.Data.OrderTickers.Tickers::class.java to dataListTickers),
                mapOf(),
                false)
        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        //when
        somListViewModel.loadTickerList("")

        //then
        assert(somListViewModel.tickerListResult.value is Success)
        // assert((somListViewModel.tickerListResult.value as Success<SomListTicker.Data.OrderTickers>).data.listTicker == dataListTickers)
    }*/
}