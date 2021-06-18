package com.tokopedia.shop.score.penalty.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltySummaryTypeWrapper
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltySummaryTypesUseCase
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class ShopPenaltyViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var penaltyMapper: PenaltyMapper

    @RelaxedMockK
    lateinit var getShopPenaltySummaryTypesUseCase: GetShopPenaltySummaryTypesUseCase

    @RelaxedMockK
    lateinit var getShopPenaltyDetailUseCase: GetShopPenaltyDetailUseCase

    lateinit var penaltyViewModel: ShopPenaltyViewModel

    protected lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        penaltyViewModel = ShopPenaltyViewModel(CoroutineTestDispatchersProvider, getShopPenaltySummaryTypesUseCase, getShopPenaltyDetailUseCase, penaltyMapper)
        lifecycle = LifecycleRegistry(mockk()).apply {
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
    }

    @After
    fun finish() {
        unmockkAll()
    }

    protected fun onGetShopPenaltySummaryTypesUseCase_thenReturn(shopPenaltySummaryTypeWrapper: ShopPenaltySummaryTypeWrapper) {
        coEvery { getShopPenaltySummaryTypesUseCase.executeOnBackground() } returns shopPenaltySummaryTypeWrapper
    }

    protected fun onGetShopPenaltySummaryTypesUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getShopPenaltySummaryTypesUseCase.executeOnBackground() } throws exception
    }

    protected fun verifyGetShopPenaltySummaryTypesUseCaseCalled() {
        coVerify { getShopPenaltySummaryTypesUseCase.executeOnBackground() }
    }

    protected fun onGetShopPenaltyDetailUseCase_thenReturn(shopScorePenaltyDetail: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail) {
        coEvery { getShopPenaltyDetailUseCase.executeOnBackground() } returns shopScorePenaltyDetail
    }

    protected fun onGetShopPenaltyDetailUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getShopPenaltyDetailUseCase.executeOnBackground() } throws exception
    }

    protected fun verifyGetShopPenaltyDetailUseCaseCaseCalled() {
        coVerify { getShopPenaltyDetailUseCase.executeOnBackground() }
    }


    protected fun mapToChipsTypePenaltyFilterDummy(): List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel> {
        return mutableListOf<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>().apply {
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = "Bersalah di Pusat Resolusi"))
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = "Pesanan diabaikan"))
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = "Pengiriman diabaikan"))
            add(PenaltyFilterUiModel.ChipsFilterPenaltyUiModel(title = "Pengiriman ditolak"))
        }
    }
}