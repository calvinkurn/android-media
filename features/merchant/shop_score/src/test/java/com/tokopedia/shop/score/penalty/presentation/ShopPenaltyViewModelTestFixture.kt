package com.tokopedia.shop.score.penalty.presentation

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.ShopScorePrefManager
import com.tokopedia.shop.score.common.format
import com.tokopedia.shop.score.common.getNowTimeStamp
import com.tokopedia.shop.score.common.getPastDaysPenaltyTimeStamp
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.domain.response.GetTargetedTicker
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltySummaryTypeWrapper
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.shop.score.penalty.domain.usecase.GetNotYetDeductedPenaltyUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.ShopPenaltyTickerUseCase
import com.tokopedia.shop.score.penalty.presentation.model.ChipsFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class ShopPenaltyViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var penaltyMapper: PenaltyMapper

    @RelaxedMockK
    lateinit var getShopPenaltyDetailMergeUseCase: Lazy<GetShopPenaltyDetailMergeUseCase>

    @RelaxedMockK
    lateinit var getShopPenaltyDetailUseCase: Lazy<GetShopPenaltyDetailUseCase>

    @RelaxedMockK
    lateinit var getNotYetDeductedPenaltyUseCase: Lazy<GetNotYetDeductedPenaltyUseCase>

    @RelaxedMockK
    lateinit var shopPenaltyTickerUseCase: Lazy<ShopPenaltyTickerUseCase>

    lateinit var penaltyViewModel: ShopPenaltyViewModel

    protected lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        val context = mockk<Context>(relaxed = true)
        val sharedPrefManager = mockk<ShopScorePrefManager>(relaxed = true)
        penaltyMapper = PenaltyMapper(context, sharedPrefManager)
        penaltyViewModel = ShopPenaltyViewModel(
            CoroutineTestDispatchersProvider,
            getShopPenaltyDetailMergeUseCase,
            getShopPenaltyDetailUseCase,
            getNotYetDeductedPenaltyUseCase,
            shopPenaltyTickerUseCase,
            penaltyMapper
        )
        lifecycle = LifecycleRegistry(mockk()).apply {
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
    }

    @After
    fun finish() {
        unmockkAll()
    }

    protected fun onGetShopPenaltyDetailMergeUseCase_thenReturn(
        penaltyDetail: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail = ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail()
    ) {
        coEvery {
            getShopPenaltyDetailMergeUseCase.get().executeOnBackground()
        } returns Pair(ShopPenaltySummaryTypeWrapper(), penaltyDetail)
    }

    protected fun onGetShopPenaltyDetailMergeUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getShopPenaltyDetailMergeUseCase.get().executeOnBackground() } throws exception
    }

    protected fun onGetNotYetDeductedPenaltyUseCase_thenReturn() {
        coEvery {
            getNotYetDeductedPenaltyUseCase.get().execute(
                format(getPastDaysPenaltyTimeStamp(true).time, ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM),
                format(getNowTimeStamp(), ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM)
            )
        } returns listOf(ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail.Result())
    }

    protected fun onGetShopPenaltyTickerUseCase_thenReturn(pageType: String) {
        coEvery {
            shopPenaltyTickerUseCase.get().execute(pageType)
        } returns listOf(
            GetTargetedTicker.TickerResponse(
                "",
                "",
                GetTargetedTicker.TickerResponse.Action("", "", "", "")
            )
        )
    }

    protected fun verifyGetShopPenaltyDetailMergeUseCaseCalled() {
        coVerify { getShopPenaltyDetailMergeUseCase.get().executeOnBackground() }
    }

    protected fun onGetShopPenaltyDetailUseCase_thenReturn(
        shopScorePenaltyDetail:
        ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail
    ) {
        coEvery {
            getShopPenaltyDetailUseCase.get().executeOnBackground()
        } returns shopScorePenaltyDetail
    }

    protected fun onGetShopPenaltyDetailUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getShopPenaltyDetailUseCase.get().executeOnBackground() } throws exception
    }

    protected fun verifyGetShopPenaltyDetailUseCaseCaseCalled() {
        coVerify { getShopPenaltyDetailUseCase.get().executeOnBackground() }
    }


    protected fun mapToChipsTypePenaltyFilterDummy():
            List<ChipsFilterPenaltyUiModel> {
        return mutableListOf<ChipsFilterPenaltyUiModel>().apply {
            add(ChipsFilterPenaltyUiModel(title = "Bersalah di Pusat Resolusi"))
            add(ChipsFilterPenaltyUiModel(title = "Pesanan diabaikan"))
            add(ChipsFilterPenaltyUiModel(title = "Pengiriman diabaikan"))
            add(ChipsFilterPenaltyUiModel(title = "Pengiriman ditolak"))
        }
    }
}
