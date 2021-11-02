package com.tokopedia.shop.score.penalty.presentation

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailUseCase
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyDataWrapper
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import dagger.Lazy
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
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

    lateinit var penaltyViewModel: ShopPenaltyViewModel

    protected lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        val context = mockk<Context>(relaxed = true)
        penaltyMapper = PenaltyMapper(context)
        penaltyViewModel = ShopPenaltyViewModel(
            CoroutineTestDispatchersProvider,
            getShopPenaltyDetailMergeUseCase,
            getShopPenaltyDetailUseCase,
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

    protected fun onGetShopPenaltyDetailMergeUseCase_thenReturn() {
        coEvery {
            getShopPenaltyDetailMergeUseCase.get().executeOnBackground()
        } returns PenaltyDataWrapper()
    }

    protected fun onGetShopPenaltyDetailMergeUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getShopPenaltyDetailMergeUseCase.get().executeOnBackground() } throws exception
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
            List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel> {
        return mutableListOf<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>().apply {
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = "Bersalah di Pusat Resolusi"))
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = "Pesanan diabaikan"))
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = "Pengiriman diabaikan"))
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = "Pengiriman ditolak"))
        }
    }
}