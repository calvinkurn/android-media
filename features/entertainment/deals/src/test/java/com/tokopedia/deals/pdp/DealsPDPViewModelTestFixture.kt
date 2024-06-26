package com.tokopedia.deals.pdp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.ui.pdp.domain.DealsPDPDetailUseCase
import com.tokopedia.deals.ui.pdp.domain.DealsPDPEventContentUseCase
import com.tokopedia.deals.ui.pdp.domain.DealsPDPGetRatingUseCase
import com.tokopedia.deals.ui.pdp.domain.DealsPDPRecentSearchTrackingUseCase
import com.tokopedia.deals.ui.pdp.domain.DealsPDPRecommendTrackingUseCase
import com.tokopedia.deals.ui.pdp.domain.DealsPDPRecommendationUseCase
import com.tokopedia.deals.ui.pdp.domain.DealsPDPUpdateRatingUseCase
import com.tokopedia.deals.ui.pdp.ui.viewmodel.DealsPDPViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import java.lang.reflect.Type

open class DealsPDPViewModelTestFixture {

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

    protected fun onGetPDPDetail_thenReturn(dealsPDPData: com.tokopedia.deals.ui.pdp.data.DealsProductDetail) {
        coEvery {
            dealsPDPDetailUseCase.execute(any())
        } returns dealsPDPData
    }

    protected fun onGetPDPDetail_thenReturn(errorThrowable: Throwable) {
        coEvery {
            dealsPDPDetailUseCase.execute(any())
        } throws errorThrowable
    }

    protected fun onGetContentById_thenReturn(dealsContentById: com.tokopedia.deals.ui.pdp.data.DealsProductEventContent) {
        coEvery {
            dealsPDPEventContentUseCase.execute("4", "12344")
        } returns dealsContentById
    }

    protected fun onGetContentById_thenReturn(errorThrowable: Throwable) {
        coEvery {
            dealsPDPEventContentUseCase.execute("4", "12344")
        } throws errorThrowable
    }

    protected fun onGetRecommendation_thenReturn(recommendation: SearchData) {
        coEvery {
            dealsPDPRecommendationUseCase.execute(any())
        } returns recommendation
    }

    protected fun onGetRecommendation_thenReturn(errorThrowable: Throwable) {
        coEvery {
            dealsPDPRecommendationUseCase.execute(any())
        } throws errorThrowable
    }

    protected fun onGetRating_thenReturn(rating: com.tokopedia.deals.ui.pdp.data.DealsRatingResponse) {
        val restResponse = RestResponse(rating, 200, false)
        val dataRatingMap = mapOf<Type, RestResponse>(
            com.tokopedia.deals.ui.pdp.data.DealsRatingResponse::class.java to restResponse
        )
        coEvery {
            dealsPDPGetRatingUseCase.executeOnBackground()
        } returns dataRatingMap
    }

    protected fun onGetRating_thenReturn() {
        val dataRatingMap = mapOf<Type, RestResponse?>(
            com.tokopedia.deals.ui.pdp.data.DealsRatingResponse::class.java to null
        )
        coEvery {
            dealsPDPGetRatingUseCase.executeOnBackground()
        } returns dataRatingMap
    }

    protected fun onGetRating_thenReturn(errorThrowable: Throwable) {
        coEvery {
            dealsPDPGetRatingUseCase.executeOnBackground()
        } throws errorThrowable
    }

    protected fun onGetRatingUpdate_thenReturn(rating: com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateResponse) {
        val restResponse = RestResponse(rating, 200, false)
        val dataRatingMap = mapOf<Type, RestResponse>(
            com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateResponse::class.java to restResponse
        )
        coEvery {
            dealsPDPUpdateRatingUseCase.executeOnBackground()
        } returns dataRatingMap
    }

    protected fun onGetRatingUpdate_thenReturn() {
        val dataRatingMap = mapOf<Type, RestResponse?>(
            com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateResponse::class.java to null
        )
        coEvery {
            dealsPDPUpdateRatingUseCase.executeOnBackground()
        } returns dataRatingMap
    }

    protected fun onGetRatingUpdate_thenReturn(errorThrowable: Throwable) {
        coEvery {
            dealsPDPUpdateRatingUseCase.executeOnBackground()
        } throws errorThrowable
    }

    protected fun onGetTrackingRecommendation_thenReturn(tracking: com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse) {
        val restResponse = RestResponse(tracking, 200, false)
        val dataTrackingMap = mapOf<Type, RestResponse>(
            com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse::class.java to restResponse
        )
        coEvery {
            dealsPDPRecommendTrackingUseCase.executeOnBackground()
        } returns dataTrackingMap
    }

    protected fun onGetTrackingRecommendation_thenReturn() {
        val dataTrackingMap = mapOf<Type, RestResponse?>(
            com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse::class.java to null
        )
        coEvery {
            dealsPDPRecommendTrackingUseCase.executeOnBackground()
        } returns dataTrackingMap
    }

    protected fun onGetTrackingRecommendation_thenReturn(errorThrowable: Throwable) {
        coEvery {
            dealsPDPRecommendTrackingUseCase.executeOnBackground()
        } throws errorThrowable
    }

    protected fun onGetTrackingRecentSearch_thenReturn(tracking: com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse) {
        val restResponse = RestResponse(tracking, 200, false)
        val dataTrackingMap = mapOf<Type, RestResponse>(
            com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse::class.java to restResponse
        )
        coEvery {
            dealsPDPRecentSearchTrackingUseCase.executeOnBackground()
        } returns dataTrackingMap
    }

    protected fun onGetTrackingRecentSearch_thenReturn() {
        val dataTrackingMap = mapOf<Type, RestResponse?>(
            com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse::class.java to null
        )
        coEvery {
            dealsPDPRecentSearchTrackingUseCase.executeOnBackground()
        } returns dataTrackingMap
    }

    protected fun onGetTrackingRecentSearch_thenReturn(errorThrowable: Throwable) {
        coEvery {
            dealsPDPRecentSearchTrackingUseCase.executeOnBackground()
        } throws errorThrowable
    }
}
