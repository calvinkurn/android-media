package com.tokopedia.deals.pdp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.pdp.data.DealsProductDetail
import com.tokopedia.deals.pdp.data.DealsProductEventContent
import com.tokopedia.deals.pdp.domain.DealsPDPDetailUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPEventContentUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPGetRatingUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPRecentSearchTrackingUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPRecommendTrackingUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPRecommendationUseCase
import com.tokopedia.deals.pdp.domain.DealsPDPUpdateRatingUseCase
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class DealsPDPViewModelTestFixture {

    @RelaxedMockK
    lateinit var dealsPDPDetailUseCase: DealsPDPDetailUseCase
    @RelaxedMockK
    lateinit var dealsPDPEventContentUseCase: DealsPDPEventContentUseCase
    @RelaxedMockK
    lateinit var dealsPDPRecommendationUseCase: DealsPDPRecommendationUseCase
    @RelaxedMockK
    lateinit var dealsPDPGetRatingUseCase: DealsPDPGetRatingUseCase
    @RelaxedMockK
    lateinit var dealsPDPUpdateRatingUseCase: DealsPDPUpdateRatingUseCase
    @RelaxedMockK
    lateinit var dealsPDPRecommendTrackingUseCase: DealsPDPRecommendTrackingUseCase
    @RelaxedMockK
    lateinit var dealsPDPRecentSearchTrackingUseCase: DealsPDPRecentSearchTrackingUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: DealsPDPViewModel
    protected val errorMessageGeneral: String = "Error Message"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DealsPDPViewModel(
            dealsPDPDetailUseCase,
            dealsPDPEventContentUseCase,
            dealsPDPRecommendationUseCase,
            dealsPDPGetRatingUseCase,
            dealsPDPUpdateRatingUseCase,
            dealsPDPRecommendTrackingUseCase,
            dealsPDPRecentSearchTrackingUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    protected fun onGetPDPDetail_thenReturn(dealsPDPData : DealsProductDetail) {
        coEvery {
            dealsPDPDetailUseCase.execute(any())
        } returns dealsPDPData
    }

    protected fun onGetPDPDetail_thenReturn(errorThrowable: Throwable) {
        coEvery {
            dealsPDPDetailUseCase.execute(any())
        } throws errorThrowable
    }

    protected fun onGetContentById_thenReturn(dealsContentById : DealsProductEventContent) {
        coEvery {
            dealsPDPEventContentUseCase.execute("4", "12344")
        } returns dealsContentById
    }

    protected fun onGetContentById_thenReturn(errorThrowable: Throwable) {
        coEvery {
            dealsPDPEventContentUseCase.execute("4", "12344")
        } throws errorThrowable
    }

    protected fun onGetRecommendation_thenReturn(recommendation : SearchData) {
        coEvery {
            dealsPDPRecommendationUseCase.execute(any())
        } returns recommendation
    }

    protected fun onGetRecommendation_thenReturn(errorThrowable: Throwable) {
        coEvery {
            dealsPDPRecommendationUseCase.execute(any())
        } throws errorThrowable
    }
}