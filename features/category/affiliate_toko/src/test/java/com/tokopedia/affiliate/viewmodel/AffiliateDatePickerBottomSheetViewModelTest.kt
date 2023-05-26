package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.affiliate.model.response.*
import com.tokopedia.affiliate.usecase.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateDatePickerBottomSheetViewModelTest {
    private val affiliateUserPerformanceUseCase: AffiliateUserPerformanceUseCase = mockk()
    private var aff = spyk(AffiliateDatePickerBottomSheetViewModel(affiliateUserPerformanceUseCase))

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getAffiliateFilterDataTest() {
        var ticker: AffiliateDateFilterResponse.Data.Ticker? = null
        var list: ArrayList<AffiliateDateFilterResponse.Data.GetAffiliateDateFilter>? = null
        val filterResponse = AffiliateDateFilterResponse(null)
        coEvery { affiliateUserPerformanceUseCase.getAffiliateFilter() } returns filterResponse
        aff.getAffiliateFilterData()
        ticker = AffiliateDateFilterResponse.Data.Ticker("info", "")
        list = arrayListOf(
            AffiliateDateFilterResponse.Data.GetAffiliateDateFilter("", "", "30 Hari Terakhir", "LastThirtyDays", "30"),
            AffiliateDateFilterResponse.Data.GetAffiliateDateFilter("", "", "7 Hari Terakhir", "LastSevenDays", "7")
        )
        filterResponse.dateFilterData = AffiliateDateFilterResponse.Data(null, null)
        aff.getAffiliateFilterData()
        filterResponse.dateFilterData?.ticker = arrayListOf()
        aff.getAffiliateFilterData()
        filterResponse.dateFilterData?.ticker = arrayListOf(null)
        aff.getAffiliateFilterData()
        filterResponse.dateFilterData?.ticker = arrayListOf(ticker)
        filterResponse.dateFilterData?.getAffiliateDateFilter = list
        val response = aff.convertFilterToVisitable(filterResponse.dateFilterData?.getAffiliateDateFilter)
        aff.getAffiliateFilterData()
        assertEquals(Gson().toJson(aff.getAffiliateFilterItems().value), Gson().toJson(response))
        assertEquals(Gson().toJson(aff.getItemList()), Gson().toJson(response))
        assertEquals(aff.getTickerInfo().value, ticker.tickerDescription)
        assertEquals(aff.getShimmerVisibility().value, false)
    }

    @Test
    fun updateItemTest() {
        val ticker = AffiliateDateFilterResponse.Data.Ticker("info", "")
        val list = arrayListOf(
            AffiliateDateFilterResponse.Data.GetAffiliateDateFilter("", "", "30 Hari Terakhir", "LastThirtyDays", "30"),
            AffiliateDateFilterResponse.Data.GetAffiliateDateFilter("", "", "7 Hari Terakhir", "LastSevenDays", "7")
        )
        val filterResponse = AffiliateDateFilterResponse(AffiliateDateFilterResponse.Data(list, arrayListOf(ticker)))
        coEvery { affiliateUserPerformanceUseCase.getAffiliateFilter() } returns filterResponse
        aff.getAffiliateFilterData()
        val response = aff.convertFilterToVisitable(filterResponse.dateFilterData?.getAffiliateDateFilter)
        assertEquals(Gson().toJson(aff.getItemList()), Gson().toJson(response))
        aff.updateItem(1)
    }

    @Test
    fun getAffiliateFilterDataTestException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateUserPerformanceUseCase.getAffiliateFilter() } throws throwable
        aff.getAffiliateFilterData()
        assertEquals(aff.getShimmerVisibility().value, false)
        assertEquals(aff.getError().value, true)
    }
}
