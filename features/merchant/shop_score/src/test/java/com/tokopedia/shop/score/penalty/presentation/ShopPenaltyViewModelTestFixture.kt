package com.tokopedia.shop.score.penalty.presentation

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.shop.score.penalty.domain.old.mapper.PenaltyMapperOld
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.shop.score.penalty.domain.old.usecase.GetShopPenaltyDetailMergeUseCaseOld
import com.tokopedia.shop.score.penalty.domain.old.usecase.GetShopPenaltyDetailUseCaseOld
import com.tokopedia.shop.score.penalty.presentation.old.model.PenaltyDataWrapperOld
import com.tokopedia.shop.score.penalty.presentation.old.model.PenaltyFilterUiModelOld
import com.tokopedia.shop.score.penalty.presentation.viewmodel.old.ShopPenaltyViewModelOld
import dagger.Lazy
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class ShopPenaltyViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var penaltyMapperOld: PenaltyMapperOld

    @RelaxedMockK
    lateinit var getShopPenaltyDetailMergeUseCaseOld: Lazy<GetShopPenaltyDetailMergeUseCaseOld>

    @RelaxedMockK
    lateinit var getShopPenaltyDetailUseCaseOld: Lazy<GetShopPenaltyDetailUseCaseOld>

    lateinit var penaltyViewModel: ShopPenaltyViewModelOld

    protected lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        val context = mockk<Context>(relaxed = true)
        penaltyMapperOld = PenaltyMapperOld(context)
        penaltyViewModel = ShopPenaltyViewModelOld(
            CoroutineTestDispatchersProvider,
            getShopPenaltyDetailMergeUseCaseOld,
            getShopPenaltyDetailUseCaseOld,
            penaltyMapperOld
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
            getShopPenaltyDetailMergeUseCaseOld.get().executeOnBackground()
        } returns PenaltyDataWrapperOld()
    }

    protected fun onGetShopPenaltyDetailMergeUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getShopPenaltyDetailMergeUseCaseOld.get().executeOnBackground() } throws exception
    }

    protected fun verifyGetShopPenaltyDetailMergeUseCaseCalled() {
        coVerify { getShopPenaltyDetailMergeUseCaseOld.get().executeOnBackground() }
    }

    protected fun onGetShopPenaltyDetailUseCase_thenReturn(
        shopScorePenaltyDetail:
        ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail
    ) {
        coEvery {
            getShopPenaltyDetailUseCaseOld.get().executeOnBackground()
        } returns shopScorePenaltyDetail
    }

    protected fun onGetShopPenaltyDetailUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getShopPenaltyDetailUseCaseOld.get().executeOnBackground() } throws exception
    }

    protected fun verifyGetShopPenaltyDetailUseCaseCaseCalled() {
        coVerify { getShopPenaltyDetailUseCaseOld.get().executeOnBackground() }
    }


    protected fun mapToChipsTypePenaltyFilterDummy():
            List<PenaltyFilterUiModelOld.ChipsFilterPenaltyUiModel> {
        return mutableListOf<PenaltyFilterUiModelOld.ChipsFilterPenaltyUiModel>().apply {
            add(PenaltyFilterUiModelOld.ChipsFilterPenaltyUiModel(title = "Bersalah di Pusat Resolusi"))
            add(PenaltyFilterUiModelOld.ChipsFilterPenaltyUiModel(title = "Pesanan diabaikan"))
            add(PenaltyFilterUiModelOld.ChipsFilterPenaltyUiModel(title = "Pengiriman diabaikan"))
            add(PenaltyFilterUiModelOld.ChipsFilterPenaltyUiModel(title = "Pengiriman ditolak"))
        }
    }
}
