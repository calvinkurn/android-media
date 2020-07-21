package com.tokopedia.gamification.giftbox.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gamification.giftbox.data.entities.*
import com.tokopedia.gamification.giftbox.domain.*
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxDailyViewModel
import com.tokopedia.gamification.pdp.data.LiveDataResult
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class GiftBoxDailyViewModelTest {

    lateinit var viewModel: GiftBoxDailyViewModel
    val dispatcher = TestCoroutineDispatcher()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()
    val giftBoxDailyUseCase: GiftBoxDailyUseCase = mockk()
    val giftBoxDailyRewardUseCase: GiftBoxDailyRewardUseCase = mockk()
    val couponDetailUseCase: CouponDetailUseCase = mockk()
    val remindMeUseCase: RemindMeUseCase = mockk()
    val autoApplyUseCase: AutoApplyUseCase = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private fun getRealViewModel(): GiftBoxDailyViewModel {
        return (GiftBoxDailyViewModel(dispatcher, dispatcher, giftBoxDailyUseCase, giftBoxDailyRewardUseCase, couponDetailUseCase, remindMeUseCase, autoApplyUseCase))
    }

    private fun prepareViewModel() {
        viewModel = getRealViewModel()
    }

    private fun prepareRelaxedViewModel() {
        viewModel = spyk(getRealViewModel())
    }

    @Test
    fun getGiftBoxSuccess() {
        prepareViewModel()
        val requestParams: HashMap<String, Any> = mockk()
        val giftBoxEntity: GiftBoxEntity = mockk(relaxed = true)
        every { giftBoxDailyUseCase.getRequestParams(viewModel.pageName) } returns requestParams
        coEvery { giftBoxDailyUseCase.getResponse(requestParams) } returns giftBoxEntity

        val remindMeParams: HashMap<String, Any> = mockk()
        every { remindMeUseCase.getRequestParams(viewModel.about) } returns remindMeParams

        val remindMeCheckEntity: RemindMeCheckEntity = mockk()
        coEvery { remindMeUseCase.getRemindMeCheckResponse(remindMeParams) } returns remindMeCheckEntity

        viewModel.giftBoxLiveData.observeForever { }
        viewModel.getGiftBox()

        coVerifyOrder {
            giftBoxDailyUseCase.getRequestParams(viewModel.pageName)
            giftBoxDailyUseCase.getResponse(requestParams)
            remindMeUseCase.getRequestParams(viewModel.about)
            remindMeUseCase.getRemindMeCheckResponse(remindMeParams)
        }
        assertEquals(viewModel.giftBoxLiveData.value?.status, LiveDataResult.STATUS.SUCCESS)
    }

    @Test
    fun getGiftBoxFail() {
        prepareViewModel()
        val requestParams: HashMap<String, Any> = mockk()
        every { giftBoxDailyUseCase.getRequestParams(viewModel.pageName) } returns requestParams
        coEvery { giftBoxDailyUseCase.getResponse(requestParams) } throws Exception()

        viewModel.giftBoxLiveData.observeForever { }
        viewModel.getGiftBox()
        assertEquals(viewModel.giftBoxLiveData.value?.status, LiveDataResult.STATUS.ERROR)

    }

    @Test
    fun getRewardsSuccess() {
        prepareRelaxedViewModel()
        val params: HashMap<String, Any> = mockk()
        val giftBoxRewardEntity: GiftBoxRewardEntity = mockk()
        every { giftBoxDailyRewardUseCase.getRequestParams(viewModel.campaignSlug!!, viewModel.uniqueCode) } returns mockk()
        coEvery { giftBoxDailyRewardUseCase.getResponse(any()) } returns giftBoxRewardEntity

        val couponDetailResponse: CouponDetailResponse = mockk(relaxed = true)
        coEvery { viewModel.composeApi(giftBoxRewardEntity) } returns couponDetailResponse

        every { giftBoxRewardEntity.couponDetailResponse = any() } returns Unit

        viewModel.rewardLiveData.observeForever { }
        viewModel.getRewards()
        assertEquals(viewModel.rewardLiveData.value?.status, LiveDataResult.STATUS.SUCCESS)
    }

    @Test
    fun getRewardsFail() {
        prepareViewModel()
        every { giftBoxDailyRewardUseCase.getRequestParams(viewModel.campaignSlug!!, viewModel.uniqueCode) } returns mockk()
        coEvery { giftBoxDailyRewardUseCase.getResponse(any()) } throws Exception()

        viewModel.rewardLiveData.observeForever { }
        viewModel.getRewards()
        assertEquals(viewModel.rewardLiveData.value?.status, LiveDataResult.STATUS.ERROR)
    }

    @Test
    fun setReminderSuccess() {
        prepareViewModel()
        val remindMeEntity: RemindMeEntity = mockk()

        coEvery { remindMeUseCase.getRemindMeResponse(remindMeUseCase.getRequestParams(viewModel.about)) } returns remindMeEntity

        viewModel.rewardLiveData.observeForever { }
        viewModel.setReminder()

        assertEquals(viewModel.reminderSetLiveData.value?.status, LiveDataResult.STATUS.SUCCESS)
    }

    @Test
    fun setReminderFail() {
        prepareViewModel()
        coEvery { remindMeUseCase.getRemindMeResponse(remindMeUseCase.getRequestParams(viewModel.about)) } throws Exception()

        viewModel.rewardLiveData.observeForever { }
        viewModel.setReminder()

        assertEquals(viewModel.reminderSetLiveData.value?.status, LiveDataResult.STATUS.ERROR)
    }

    @After
    fun cleanup() {
        dispatcher.cleanupTestCoroutines()
    }
}