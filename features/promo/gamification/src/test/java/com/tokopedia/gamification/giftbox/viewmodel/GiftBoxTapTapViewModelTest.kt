package com.tokopedia.gamification.giftbox.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity
import com.tokopedia.gamification.giftbox.data.entities.CouponDetailResponse
import com.tokopedia.gamification.giftbox.domain.CouponDetailUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxTapTapCrackUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxTapTapHomeUseCase
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxTapTapViewModel
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.taptap.data.entiity.TapTapBaseEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class GiftBoxTapTapViewModelTest {

    lateinit var viewModel: GiftBoxTapTapViewModel
    val dispatcher = TestCoroutineDispatcher()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()
    val crackUseCase: GiftBoxTapTapCrackUseCase = mockk()
    val homeUseCase: GiftBoxTapTapHomeUseCase = mockk()
    val couponDetailUseCase: CouponDetailUseCase = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private fun getRealViewModel(): GiftBoxTapTapViewModel {
        return (GiftBoxTapTapViewModel(dispatcher, crackUseCase, homeUseCase, couponDetailUseCase))
    }

    private fun prepareViewModel() {
        viewModel = getRealViewModel()
    }

    private fun prepareRelaxedViewModel() {
        viewModel = spyk(getRealViewModel())
    }

    @Test
    fun getGiftBoxHomeSuccess() {
        prepareViewModel()
        val tapTapBaseEntity: TapTapBaseEntity = mockk()
        coEvery { homeUseCase.getResponse(HashMap()) } returns tapTapBaseEntity

        viewModel.giftHomeLiveData.observeForever { }
        viewModel.getGiftBoxHome()

        assertEquals(viewModel.giftHomeLiveData.value?.status, LiveDataResult.STATUS.SUCCESS)
    }

    @Test
    fun getGiftBoxHomeFail() {
        prepareViewModel()
        coEvery { homeUseCase.getResponse(HashMap()) } throws Exception()

        viewModel.giftHomeLiveData.observeForever { }
        viewModel.getGiftBoxHome()

        assertEquals(viewModel.giftHomeLiveData.value?.status, LiveDataResult.STATUS.ERROR)
    }

//    @Test
//    fun crackGiftBoxSuccess() {
//        prepareRelaxedViewModel()
//        val responseCrackResultEntity: ResponseCrackResultEntity = mockk()
//
//        coEvery { crackUseCase.getResponse(crackUseCase.getQueryParams(any(), any())) } returns responseCrackResultEntity
//
//        val couponDetailResponse: CouponDetailResponse = mockk(relaxed = true)
//        coEvery { viewModel.composeApi(responseCrackResultEntity) } returns couponDetailResponse
//
//
//        viewModel.giftCrackLiveData.observeForever { }
//        viewModel.crackGiftBox()
//        assertEquals(viewModel.giftCrackLiveData.value?.status, LiveDataResult.STATUS.SUCCESS)
//    }

    @Test
    fun getRewardsFail() {
        prepareRelaxedViewModel()
        val responseCrackResultEntity: ResponseCrackResultEntity = mockk()

        coEvery { crackUseCase.getResponse(crackUseCase.getQueryParams(any(), any())) } throws Exception()

        val couponDetailResponse: CouponDetailResponse = mockk(relaxed = true)
        coEvery { viewModel.composeApi(responseCrackResultEntity) } returns couponDetailResponse

        viewModel.giftCrackLiveData.observeForever { }
        viewModel.crackGiftBox()
        assertEquals(viewModel.giftCrackLiveData.value?.status, LiveDataResult.STATUS.ERROR)
    }

    @After
    fun cleanup() {
        dispatcher.cleanupTestCoroutines()
    }
}