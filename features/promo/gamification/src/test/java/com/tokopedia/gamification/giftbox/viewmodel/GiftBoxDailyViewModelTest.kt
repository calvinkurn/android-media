package com.tokopedia.gamification.giftbox.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gamification.giftbox.data.entities.*
import com.tokopedia.gamification.giftbox.domain.*
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxDailyViewModel
import com.tokopedia.gamification.pdp.data.LiveDataResult
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
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
        val remindMeEntity:RemindMeEntity = RemindMeEntity(GameRemindMe(ResultStatus("200", arrayListOf("message"),""),false))

        val requestParams:HashMap<String,Any> = mockk()
        every { remindMeUseCase.getRequestParams(viewModel.about) } returns requestParams
        coEvery { remindMeUseCase.getRemindMeResponse(requestParams) } returns remindMeEntity

        val arrayListOfLiveDataResult :ArrayList<LiveDataResult<RemindMeEntity>> = arrayListOf()
        viewModel.reminderSetLiveData.observeForever {
            arrayListOfLiveDataResult.add(it)
        }
        viewModel.setReminder()

        assertEquals(arrayListOfLiveDataResult[0].status, LiveDataResult.STATUS.LOADING)
        assertEquals(arrayListOfLiveDataResult[1].status, LiveDataResult.STATUS.SUCCESS)
    }

    @Test
    fun setReminderFail() {
        prepareViewModel()
        coEvery { remindMeUseCase.getRemindMeResponse(remindMeUseCase.getRequestParams(viewModel.about)) } throws Exception()

        viewModel.rewardLiveData.observeForever { }
        viewModel.setReminder()

        assertEquals(viewModel.reminderSetLiveData.value?.status, LiveDataResult.STATUS.ERROR)
    }

    @Test
    fun testUnSetReminderSuccess(){
        prepareViewModel()
        assertEquals(viewModel.remindMeJob, null)

        val requestParams:HashMap<String,Any> = mockk()
        val remindMeEntity:RemindMeEntity = RemindMeEntity(GameRemindMe(ResultStatus("200", arrayListOf("message"),""),false))
        every { remindMeUseCase.getRequestParams(viewModel.about) } returns requestParams
        coEvery { remindMeUseCase.getUnSetRemindMeResponse(requestParams) } returns remindMeEntity

        val arrayListOfLiveDataResult :ArrayList<LiveDataResult<RemindMeEntity>> = arrayListOf()
        viewModel.reminderSetLiveData.observeForever {
            arrayListOfLiveDataResult.add(it)
        }
        viewModel.unSetReminder()

        assertEquals(arrayListOfLiveDataResult[0].status, LiveDataResult.STATUS.LOADING)
        assertEquals(arrayListOfLiveDataResult[1].status, LiveDataResult.STATUS.SUCCESS)
    }

    @Test
    fun testUnSetReminderFail(){
        prepareViewModel()
        assertEquals(viewModel.remindMeJob, null)

        val requestParams:HashMap<String,Any> = mockk()
        every { remindMeUseCase.getRequestParams(viewModel.about) } returns requestParams
        coEvery { remindMeUseCase.getUnSetRemindMeResponse(requestParams) } throws Exception()

        val arrayListOfLiveDataResult :ArrayList<LiveDataResult<RemindMeEntity>> = arrayListOf()
        viewModel.reminderSetLiveData.observeForever {
            arrayListOfLiveDataResult.add(it)
        }
        viewModel.unSetReminder()

        assertEquals(arrayListOfLiveDataResult[0].status, LiveDataResult.STATUS.LOADING)
        assertEquals(arrayListOfLiveDataResult[1].status, LiveDataResult.STATUS.ERROR)
    }

    @Test
    fun testAutoApply(){
        val autoApplyCode = "123"
        var a = false
//        prepareRelaxedViewModel()
        prepareViewModel()
        val requestParams:HashMap<String,Any> = mockk()
        every { autoApplyUseCase.getQueryParams(autoApplyCode) } returns requestParams

//        val autoApplyResponse :AutoApplyResponse = AutoApplyResponse(TokopointsSetAutoApply(ResultStatus("", arrayListOf(""),"")))
        val autoApplyResponse :AutoApplyResponse = mockk()
        coEvery { autoApplyUseCase.getResponse(requestParams) } returns autoApplyResponse
        val autoApplySlot = slot<AutoApplyResponse>()
        viewModel.autoApplycallback = mockk()
//        every { viewModel.autoApplycallback?.success(capture(autoApplySlot)) } answers {
//            a = true
//            return
//        }
        viewModel.autoApply(autoApplyCode)
        coVerify {
            viewModel.autoApplycallback?.success(autoApplyResponse)
        }
    }

    @After
    fun cleanup() {
        dispatcher.cleanupTestCoroutines()
    }
}