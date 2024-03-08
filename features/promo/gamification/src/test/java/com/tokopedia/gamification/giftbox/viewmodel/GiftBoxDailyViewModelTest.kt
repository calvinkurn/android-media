package com.tokopedia.gamification.giftbox.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gamification.giftbox.data.entities.AutoApplyResponse
import com.tokopedia.gamification.giftbox.data.entities.CouponDetailResponse
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxEntity
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.data.entities.RemindMeCheckEntity
import com.tokopedia.gamification.giftbox.data.entities.RemindMeEntity
import com.tokopedia.gamification.giftbox.data.entities.ResultStatus
import com.tokopedia.gamification.giftbox.data.entities.TokopointsSetAutoApply
import com.tokopedia.gamification.giftbox.data.factory.TapTapBaseDataFactory.createCouponDetailResponse
import com.tokopedia.gamification.giftbox.data.factory.TapTapBaseDataFactory.createGiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.data.factory.TapTapBaseDataFactory.createRemindMeEntity
import com.tokopedia.gamification.giftbox.data.fakeobject.FakeAutoApply
import com.tokopedia.gamification.giftbox.domain.AutoApplyUseCase
import com.tokopedia.gamification.giftbox.domain.AutoApplyUseCase.AutoApplyParams.CODE
import com.tokopedia.gamification.giftbox.domain.CouponDetailUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyRewardUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyRewardUseCase.Params.CAMPAIGN_SLUG
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyRewardUseCase.Params.UNIQUE_CODE
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyUseCase
import com.tokopedia.gamification.giftbox.domain.RemindMeUseCase
import com.tokopedia.gamification.giftbox.domain.RemindMeUseCase.Params.ABOUT
import com.tokopedia.gamification.giftbox.presentation.fragments.DisplayType
import com.tokopedia.gamification.giftbox.presentation.presenter.CouponListResultPresenter
import com.tokopedia.gamification.giftbox.presentation.viewmodels.AutoApplyCallback
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxDailyViewModel
import com.tokopedia.gamification.giftbox.util.TestUtil.verifyAssertEquals
import com.tokopedia.gamification.pdp.data.LiveDataResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.any

@ExperimentalCoroutinesApi
class GiftBoxDailyViewModelTest {
    @RelaxedMockK
    lateinit var autoApplyUseCase: AutoApplyUseCase

    @RelaxedMockK
    lateinit var couponDetailUseCase: CouponDetailUseCase

    @RelaxedMockK
    lateinit var giftBoxDailyUseCase: GiftBoxDailyUseCase

    @RelaxedMockK
    lateinit var giftBoxDailyRewardUseCase: GiftBoxDailyRewardUseCase

    @RelaxedMockK
    lateinit var remindMeUseCase: RemindMeUseCase

    lateinit var viewModel: GiftBoxDailyViewModel

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        val dispatcher = UnconfinedTestDispatcher()
        viewModel = GiftBoxDailyViewModel(
            dispatcher,
            dispatcher,
            giftBoxDailyUseCase,
            giftBoxDailyRewardUseCase,
            couponDetailUseCase,
            remindMeUseCase,
            autoApplyUseCase
        )
    }

    private fun stubAutoApplyQueryParams(
        code: String,
        queryParams: HashMap<String, Any>
    ) {
        every {
            autoApplyUseCase.getQueryParams(code)
        } returns queryParams
    }

    private fun stubRemindMeQueryParams(
        about: String,
        queryParams: HashMap<String, Any>
    ) {
        every {
            remindMeUseCase.getRequestParams(about)
        } returns queryParams
    }

    private fun stubRemindMeResponse(
        entity: RemindMeEntity,
        queryParams: HashMap<String, Any>
    ) {
        coEvery {
            remindMeUseCase.getRemindMeResponse(queryParams)
        } returns entity
    }

    private fun stubUnSetRemindMeResponse(
        entity: RemindMeEntity,
        queryParams: HashMap<String, Any>
    ) {
        coEvery {
            remindMeUseCase.getUnSetRemindMeResponse(queryParams)
        } returns entity
    }

    private fun stubUnSetRemindMeResponse(
        error: Throwable,
        queryParams: HashMap<String, Any>
    ) {
        coEvery {
            remindMeUseCase.getUnSetRemindMeResponse(queryParams)
        } throws error
    }

    private fun stubRemindMeResponse(
        error: Throwable,
        queryParams: HashMap<String, Any>
    ) {
        coEvery {
            remindMeUseCase.getRemindMeResponse(queryParams)
        } throws error
    }

    private fun stubAutoApplyResponse(
        autoApplyResponse: AutoApplyResponse,
        queryParams: HashMap<String, Any>
    ) {
        coEvery {
            autoApplyUseCase.getResponse(queryParams)
        } returns autoApplyResponse
    }

    private fun stubAutoApplyResponse(
        error: Throwable,
        queryParams: HashMap<String, Any>
    ) {
        coEvery {
            autoApplyUseCase.getResponse(queryParams)
        } throws error
    }

    private fun stubBoxRewardQueryParams(
        queryParams: HashMap<String, Any>
    ) {
        every {
            giftBoxDailyRewardUseCase.getRequestParams(viewModel.campaignSlug!!, viewModel.uniqueCode)
        } returns queryParams
    }

    private fun stubBoxRewardEntity(
        giftBoxRewardEntity: GiftBoxRewardEntity,
        queryParams: HashMap<String, Any>
    ) {
        coEvery {
            giftBoxDailyRewardUseCase.getResponse(queryParams)
        } returns giftBoxRewardEntity
    }

    private fun stubBoxRewardEntity(
        error: Throwable,
        queryParams: HashMap<String, Any>
    ) {
        coEvery {
            giftBoxDailyRewardUseCase.getResponse(queryParams)
        } throws error
    }

    private fun stubCouponDetailResponse(
        couponIdList: List<String>,
        response: CouponDetailResponse
    ) {
        coEvery {
            couponDetailUseCase.getResponse(couponIdList = couponIdList)
        } returns response
    }

    private fun stubCouponDetailResponse(
        couponIdList: List<String>,
        error: Throwable
    ) {
        coEvery {
            couponDetailUseCase.getResponse(couponIdList = couponIdList)
        } throws error
    }


    private fun stubIsJobCompleted(job: Job) {
        every { job.isCompleted } returns true
    }

    private fun verifyAutoApplyResponse(
        queryParams: HashMap<String, Any>
    ) {
        coVerify {
            autoApplyUseCase.getResponse(queryParams)
        }
    }

    private fun verifyRemindMeQueryParams(
        about: String,
        inverse: Boolean = false
    ) {
        verify(inverse = inverse) {
            remindMeUseCase.getRequestParams(about)
        }
    }

    private fun verifyRemindMeResponse(
        queryParams: HashMap<String, Any>,
        inverse: Boolean = false
    ) {
        coVerify (inverse = inverse) {
            remindMeUseCase.getRemindMeResponse(queryParams)
        }
    }

    private fun verifyUnSetRemindMeResponse(
        queryParams: HashMap<String, Any>,
        inverse: Boolean = false
    ) {
        coVerify(inverse = inverse) {
            remindMeUseCase.getUnSetRemindMeResponse(queryParams)
        }
    }

    private fun verifyBoxReward(
        queryParams: HashMap<String, Any>,
        inverse: Boolean = false
    ) {
        coVerify(inverse = inverse) {
            giftBoxDailyRewardUseCase.getResponse(any())
        }
    }

    private fun verifyGetCouponDetail(
        couponIdList: List<String>,
        inverse: Boolean = false
    ) {
        coVerify(inverse = inverse) {
            couponDetailUseCase.getResponse(couponIdList = couponIdList)
        }
    }
    private fun verifyAutoApplyCallback(
        response: AutoApplyResponse?,
        autoApplyCallback: AutoApplyCallback,
        inverse: Boolean = false
    ) {
        verify(inverse = inverse) {
            autoApplyCallback.success(response)
        }
    }

    @Test
    fun getGiftBoxSuccess() {
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
        val requestParams: HashMap<String, Any> = mockk()
        every { giftBoxDailyUseCase.getRequestParams(viewModel.pageName) } returns requestParams
        coEvery { giftBoxDailyUseCase.getResponse(requestParams) } throws Exception()

        viewModel.giftBoxLiveData.observeForever { }
        viewModel.getGiftBox()
        assertEquals(viewModel.giftBoxLiveData.value?.status, LiveDataResult.STATUS.ERROR)

    }

    @Test
    fun `after successfully getting rewards with reference id and card display type, livedata should provide the expected entity and null coupon detail as a result`() {
        val referenceId = "12313212"
        val result = createGiftBoxRewardEntity(referenceId, DisplayType.CARD)
        val coupon = createCouponDetailResponse(referenceId)
        val couponIdList = viewModel.mapperGratificationResponseToCouponIds(result)
        val queryParams: HashMap<String, Any> = hashMapOf(
            CAMPAIGN_SLUG to "slug",
            UNIQUE_CODE to "12312FF"
        )

        stubBoxRewardQueryParams(
            queryParams = queryParams
        )
        stubBoxRewardEntity(
            giftBoxRewardEntity = result,
            queryParams = queryParams
        )
        stubCouponDetailResponse(
            couponIdList = couponIdList,
            response = coupon
        )

        viewModel.getRewards()

        verifyBoxReward(
            queryParams = queryParams
        )
        verifyGetCouponDetail(
            couponIdList = couponIdList,
            inverse = true
        )

        viewModel.rewardLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `after successfully getting reward then getting coupon detail but there is an error, livedata should provide null as a result`() {
        val referenceId = "12313212"
        val result = createGiftBoxRewardEntity(referenceId, DisplayType.CATALOG)
        val couponIdList = viewModel.mapperGratificationResponseToCouponIds(result)
        val queryParams: HashMap<String, Any> = hashMapOf(
            CAMPAIGN_SLUG to "slug",
            UNIQUE_CODE to "12312FF"
        )

        stubBoxRewardQueryParams(
            queryParams = queryParams
        )
        stubBoxRewardEntity(
            giftBoxRewardEntity = result,
            queryParams = queryParams
        )
        stubCouponDetailResponse(
            couponIdList = couponIdList,
            error = Throwable()
        )

        viewModel.getRewards()

        verifyBoxReward(
            queryParams = queryParams
        )
        verifyGetCouponDetail(
            couponIdList = couponIdList
        )

        viewModel.rewardLiveData
            .verifyAssertEquals(
                expected = null,
                status = LiveDataResult.STATUS.ERROR
            )
    }

    @Test
    fun `after successfully getting rewards with reference id and catalog display type, livedata should provide the expected entity as a result`() {
        val referenceId = "12313212"
        val result = createGiftBoxRewardEntity(referenceId, DisplayType.CATALOG)
        val coupon = createCouponDetailResponse(referenceId)
        val couponIdList = viewModel.mapperGratificationResponseToCouponIds(result)
        val queryParams: HashMap<String, Any> = hashMapOf(
            CAMPAIGN_SLUG to "slug",
            UNIQUE_CODE to "12312FF"
        )

        stubBoxRewardQueryParams(
            queryParams = queryParams
        )
        stubBoxRewardEntity(
            giftBoxRewardEntity = result,
            queryParams = queryParams
        )
        stubCouponDetailResponse(
            couponIdList = couponIdList,
            response = coupon
        )

        viewModel.getRewards()

        verifyBoxReward(
            queryParams = queryParams
        )
        verifyGetCouponDetail(
            couponIdList = couponIdList
        )

        viewModel.rewardLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `after successfully getting rewards with null reference id and catalog display type, livedata should provide the expected entity and null coupon detail as a result`() {
        val referenceId = null
        val result = createGiftBoxRewardEntity(referenceId, DisplayType.CATALOG)
        val coupon = createCouponDetailResponse(referenceId)
        val couponIdList = viewModel.mapperGratificationResponseToCouponIds(result)
        val queryParams: HashMap<String, Any> = hashMapOf(
            CAMPAIGN_SLUG to "slug",
            UNIQUE_CODE to "12312FF"
        )

        stubBoxRewardQueryParams(
            queryParams = queryParams
        )
        stubBoxRewardEntity(
            giftBoxRewardEntity = result,
            queryParams = queryParams
        )
        stubCouponDetailResponse(
            couponIdList = couponIdList,
            response = coupon
        )

        viewModel.getRewards()

        verifyBoxReward(
            queryParams = queryParams
        )
        verifyGetCouponDetail(
            couponIdList = couponIdList,
            inverse = true
        )

        viewModel.rewardLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `after successfully getting rewards with empty reference id, empty benefits and catalog display type, livedata should provide the expected entity and null coupon detail as a result`() {
        val referenceId = ""
        val result = createGiftBoxRewardEntity(referenceId, DisplayType.CATALOG, listOf())
        val coupon = createCouponDetailResponse(referenceId)
        val couponIdList = viewModel.mapperGratificationResponseToCouponIds(result)
        val queryParams: HashMap<String, Any> = hashMapOf(
            CAMPAIGN_SLUG to "slug",
            UNIQUE_CODE to "12312FF"
        )

        stubBoxRewardQueryParams(
            queryParams = queryParams
        )
        stubBoxRewardEntity(
            giftBoxRewardEntity = result,
            queryParams = queryParams
        )
        stubCouponDetailResponse(
            couponIdList = couponIdList,
            response = coupon
        )

        viewModel.getRewards()

        verifyBoxReward(
            queryParams = queryParams
        )
        verifyGetCouponDetail(
            couponIdList = couponIdList,
            inverse = true
        )

        viewModel.rewardLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `after successfully getting rewards with empty reference id and catalog display type, livedata should provide the expected entity and null coupon detail as a result`() {
        val referenceId = ""
        val result = createGiftBoxRewardEntity(referenceId, DisplayType.CATALOG)
        val coupon = createCouponDetailResponse(referenceId)
        val couponIdList = viewModel.mapperGratificationResponseToCouponIds(result)
        val queryParams: HashMap<String, Any> = hashMapOf(
            CAMPAIGN_SLUG to "slug",
            UNIQUE_CODE to "12312FF"
        )

        stubBoxRewardQueryParams(
            queryParams = queryParams
        )
        stubBoxRewardEntity(
            giftBoxRewardEntity = result,
            queryParams = queryParams
        )
        stubCouponDetailResponse(
            couponIdList = couponIdList,
            response = coupon
        )

        viewModel.getRewards()

        verifyBoxReward(
            queryParams = queryParams
        )
        verifyGetCouponDetail(
            couponIdList = couponIdList,
            inverse = true
        )

        viewModel.rewardLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `after successfully getting rewards with empty reference id, null benefits and catalog display type, livedata should provide the expected entity and null coupon detail as a result`() {
        val referenceId = ""
        val result = createGiftBoxRewardEntity(referenceId, DisplayType.CATALOG, null)
        val coupon = createCouponDetailResponse(referenceId)
        val couponIdList = viewModel.mapperGratificationResponseToCouponIds(result)
        val queryParams: HashMap<String, Any> = hashMapOf(
            CAMPAIGN_SLUG to "slug",
            UNIQUE_CODE to "12312FF"
        )

        stubBoxRewardQueryParams(
            queryParams = queryParams
        )
        stubBoxRewardEntity(
            giftBoxRewardEntity = result,
            queryParams = queryParams
        )
        stubCouponDetailResponse(
            couponIdList = couponIdList,
            response = coupon
        )

        viewModel.getRewards()

        verifyBoxReward(
            queryParams = queryParams
        )
        verifyGetCouponDetail(
            couponIdList = couponIdList,
            inverse = true
        )

        viewModel.rewardLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `when getting rewards but job is not null, livedata should provide null as a result`() {
        val referenceId = ""
        val result = createGiftBoxRewardEntity(referenceId, DisplayType.CATALOG)
        val coupon = createCouponDetailResponse(referenceId)
        val couponIdList = viewModel.mapperGratificationResponseToCouponIds(result)
        val queryParams: HashMap<String, Any> = hashMapOf(
            CAMPAIGN_SLUG to "slug",
            UNIQUE_CODE to "12312FF"
        )

        stubBoxRewardQueryParams(
            queryParams = queryParams
        )
        stubBoxRewardEntity(
            giftBoxRewardEntity = result,
            queryParams = queryParams
        )
        stubCouponDetailResponse(
            couponIdList = couponIdList,
            response = coupon
        )

        val job = Job()

        viewModel.rewardJob = job

        viewModel.getRewards()

        verifyBoxReward(
            queryParams = queryParams,
            inverse = true
        )
        verifyGetCouponDetail(
            couponIdList = couponIdList,
            inverse = true
        )

        viewModel.rewardJob
            .verifyAssertEquals(job)

        viewModel.rewardLiveData
            .verifyAssertEquals(
                expected = null,
                status = null
            )
    }

    @Test
    fun `when getting rewards but job is completed, livedata should provide the expected entity as a result`() {
        val referenceId = ""
        val result = createGiftBoxRewardEntity(referenceId, DisplayType.CATALOG)
        val coupon = createCouponDetailResponse(referenceId)
        val couponIdList = viewModel.mapperGratificationResponseToCouponIds(result)
        val queryParams: HashMap<String, Any> = hashMapOf(
            CAMPAIGN_SLUG to "slug",
            UNIQUE_CODE to "12312FF"
        )

        stubBoxRewardQueryParams(
            queryParams = queryParams
        )
        stubBoxRewardEntity(
            giftBoxRewardEntity = result,
            queryParams = queryParams
        )
        stubCouponDetailResponse(
            couponIdList = couponIdList,
            response = coupon
        )

        val job: Job = mockk(relaxed = true)

        stubIsJobCompleted(job)

        viewModel.rewardJob = job

        viewModel.getRewards()

        verifyBoxReward(
            queryParams = queryParams
        )
        verifyGetCouponDetail(
            couponIdList = couponIdList,
            inverse = true
        )

        viewModel.rewardLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `when getting rewards but campaign is null, livedata should provide null as a result`() {
        val referenceId = ""
        val result = createGiftBoxRewardEntity(referenceId, DisplayType.CATALOG)
        val coupon = createCouponDetailResponse(referenceId)
        val couponIdList = viewModel.mapperGratificationResponseToCouponIds(result)
        val queryParams: HashMap<String, Any> = hashMapOf(
            CAMPAIGN_SLUG to "slug",
            UNIQUE_CODE to "12312FF"
        )

        stubBoxRewardQueryParams(
            queryParams = queryParams
        )
        stubBoxRewardEntity(
            giftBoxRewardEntity = result,
            queryParams = queryParams
        )
        stubCouponDetailResponse(
            couponIdList = couponIdList,
            response = coupon
        )

        viewModel.campaignSlug = null

        viewModel.getRewards()

        verifyBoxReward(
            queryParams = queryParams,
            inverse = true
        )
        verifyGetCouponDetail(
            couponIdList = couponIdList,
            inverse = true
        )

        viewModel.rewardLiveData
            .verifyAssertEquals(
                expected = null,
                status = null
            )
    }

    @Test
    fun `when getting rewards but there is an error, livedata should provide null as a result`() {
        val referenceId = ""
        val result = createGiftBoxRewardEntity(referenceId, DisplayType.CATALOG)
        val coupon = createCouponDetailResponse(referenceId)
        val couponIdList = viewModel.mapperGratificationResponseToCouponIds(result)
        val queryParams: HashMap<String, Any> = hashMapOf(
            CAMPAIGN_SLUG to "slug",
            UNIQUE_CODE to "12312FF"
        )
        stubBoxRewardQueryParams(
            queryParams = queryParams
        )
        stubBoxRewardEntity(
            error = Throwable(),
            queryParams = queryParams
        )
        stubCouponDetailResponse(
            couponIdList = couponIdList,
            response = coupon
        )

        viewModel.getRewards()

        verifyBoxReward(
            queryParams = queryParams
        )
        verifyGetCouponDetail(
            couponIdList = couponIdList,
            inverse = true
        )

        viewModel.rewardLiveData
            .verifyAssertEquals(
                expected = null,
                status = LiveDataResult.STATUS.ERROR
            )
    }

    @Test
    fun `after successfully applying remind me, livedata should provide the expected entity as a result`() {
        val remindMeEntity = createRemindMeEntity(true)
        val queryParams: HashMap<String, Any> = hashMapOf(
            ABOUT to viewModel.about
        )

        stubRemindMeQueryParams(
            about = viewModel.about,
            queryParams = queryParams
        )
        stubRemindMeResponse(
            entity = remindMeEntity,
            queryParams = queryParams
        )

        viewModel.setReminder()

        verifyRemindMeQueryParams(
            about = viewModel.about
        )
        verifyRemindMeResponse(
            queryParams = queryParams
        )

        viewModel.reminderSetLiveData
            .verifyAssertEquals(
                expected = remindMeEntity,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `when applying remind me but the job is not null so the livedata should provide null value`() {
        val remindMeEntity = createRemindMeEntity(true)
        val queryParams: HashMap<String, Any> = hashMapOf(
            ABOUT to viewModel.about
        )

        stubRemindMeQueryParams(
            about = viewModel.about,
            queryParams = queryParams
        )
        stubRemindMeResponse(
            entity = remindMeEntity,
            queryParams = queryParams
        )

        val job = Job()

        viewModel.remindMeJob = job

        viewModel.setReminder()

        verifyRemindMeQueryParams(
            about = viewModel.about,
            inverse = true
        )
        verifyRemindMeResponse(
            queryParams = queryParams,
            inverse = true
        )

        viewModel.remindMeJob
            .verifyAssertEquals(job)

        viewModel.reminderSetLiveData
            .verifyAssertEquals(
                expected = null,
                status = null
            )
    }

    @Test
    fun `when applying remind me but the job is completed then livedata should provide the expected entity as a result`() {
        val remindMeEntity = createRemindMeEntity(true)
        val queryParams: HashMap<String, Any> = hashMapOf(
            ABOUT to viewModel.about
        )

        stubRemindMeQueryParams(
            about = viewModel.about,
            queryParams = queryParams
        )
        stubRemindMeResponse(
            entity = remindMeEntity,
            queryParams = queryParams
        )

        val job: Job = mockk(relaxed = true)

        stubIsJobCompleted(job)

        viewModel.remindMeJob = job

        viewModel.setReminder()

        verifyRemindMeQueryParams(
            about = viewModel.about)
        verifyRemindMeResponse(
            queryParams = queryParams
        )

        viewModel.reminderSetLiveData
            .verifyAssertEquals(
                expected = remindMeEntity,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `when applying remind me but there is an error then livedata should provide null as a result`() {
        val queryParams: HashMap<String, Any> = hashMapOf(
            ABOUT to viewModel.about
        )

        stubRemindMeQueryParams(
            about = viewModel.about,
            queryParams = queryParams
        )
        stubRemindMeResponse(
            error = Throwable(),
            queryParams = queryParams
        )

        viewModel.setReminder()

        verifyRemindMeQueryParams(
            about = viewModel.about
        )
        verifyRemindMeResponse(
            queryParams = queryParams
        )

        viewModel.reminderSetLiveData
            .verifyAssertEquals(
                expected = null,
                status = LiveDataResult.STATUS.ERROR
            )
    }

    @Test
    fun `after successfully applying unremind me, livedata should provide the expected entity as a result`() {
        val remindMeEntity = createRemindMeEntity(false)
        val queryParams: HashMap<String, Any> = hashMapOf(
            ABOUT to viewModel.about
        )

        stubRemindMeQueryParams(
            about = viewModel.about,
            queryParams = queryParams
        )
        stubUnSetRemindMeResponse(
            entity = remindMeEntity,
            queryParams = queryParams
        )

        viewModel.unSetReminder()

        verifyRemindMeQueryParams(
            about = viewModel.about
        )
        verifyUnSetRemindMeResponse(
            queryParams = queryParams
        )

        viewModel.reminderSetLiveData
            .verifyAssertEquals(
                expected = remindMeEntity,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `when applying unremind me but the job is not null so the livedata should provide null value`() {
        val remindMeEntity = createRemindMeEntity(false)
        val queryParams: HashMap<String, Any> = hashMapOf(
            ABOUT to viewModel.about
        )

        stubRemindMeQueryParams(
            about = viewModel.about,
            queryParams = queryParams
        )
        stubUnSetRemindMeResponse(
            entity = remindMeEntity,
            queryParams = queryParams
        )

        val job = Job()

        viewModel.remindMeJob = job

        viewModel.unSetReminder()

        verifyRemindMeQueryParams(
            about = viewModel.about,
            inverse = true
        )
        verifyUnSetRemindMeResponse(
            queryParams = queryParams,
            inverse = true
        )

        viewModel.remindMeJob
            .verifyAssertEquals(job)

        viewModel.reminderSetLiveData
            .verifyAssertEquals(
                expected = null,
                status = null
            )
    }

    @Test
    fun `when applying unremind me but the job is completed then livedata should provide the expected entity as a result`() {
        val remindMeEntity = createRemindMeEntity(false)
        val queryParams: HashMap<String, Any> = hashMapOf(
            ABOUT to viewModel.about
        )

        stubRemindMeQueryParams(
            about = viewModel.about,
            queryParams = queryParams
        )
        stubUnSetRemindMeResponse(
            entity = remindMeEntity,
            queryParams = queryParams
        )

        val job: Job = mockk(relaxed = true)

        stubIsJobCompleted(job)

        viewModel.remindMeJob = job

        viewModel.unSetReminder()

        verifyRemindMeQueryParams(
            about = viewModel.about)
        verifyUnSetRemindMeResponse(
            queryParams = queryParams
        )

        viewModel.reminderSetLiveData
            .verifyAssertEquals(
                expected = remindMeEntity,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `when applying unremind me but there is an error then livedata should provide null as a result`() {
        val queryParams: HashMap<String, Any> = hashMapOf(
            ABOUT to viewModel.about
        )

        stubRemindMeQueryParams(
            about = viewModel.about,
            queryParams = queryParams
        )
        stubUnSetRemindMeResponse(
            error = Throwable(),
            queryParams = queryParams
        )

        viewModel.unSetReminder()

        verifyRemindMeQueryParams(
            about = viewModel.about
        )
        verifyUnSetRemindMeResponse(
            queryParams = queryParams
        )

        viewModel.reminderSetLiveData
            .verifyAssertEquals(
                expected = null,
                status = LiveDataResult.STATUS.ERROR
            )
    }

    @Test
    fun `after successfully applying automatically then should call auto apply callback`(){
        val code = "123111"
        val responseReason = "no reason"
        val response = AutoApplyResponse(
            tokopointsSetAutoApply = TokopointsSetAutoApply(
                resultStatus = ResultStatus(
                    code = CouponListResultPresenter.HTTP_STATUS_OK,
                    message = listOf(),
                    reason = responseReason
                )
            )
        )
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )

        stubAutoApplyQueryParams(
            code = code,
            queryParams = queryParams
        )
        stubAutoApplyResponse(
            autoApplyResponse = response,
            queryParams = queryParams
        )

        val autoApplyCallback: AutoApplyCallback = FakeAutoApply()

        viewModel.autoApplycallback = autoApplyCallback

        viewModel.autoApply(
            code = code
        )

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
    }

    @Test
    fun `after successfully applying automatically but auto apply callback is null then should do nothing`() {
        val code = "123111"
        val responseReason = "no reason"
        val response = AutoApplyResponse(
            tokopointsSetAutoApply = TokopointsSetAutoApply(
                resultStatus = ResultStatus(
                    code = CouponListResultPresenter.HTTP_STATUS_OK,
                    message = listOf(),
                    reason = responseReason
                )
            )
        )
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )

        stubAutoApplyQueryParams(
            code = code,
            queryParams = queryParams
        )
        stubAutoApplyResponse(
            autoApplyResponse = response,
            queryParams = queryParams
        )

        viewModel.autoApplycallback = null

        viewModel.autoApply(
            code = code
        )

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
        verifyAutoApplyCallback(
            response = any(),
            autoApplyCallback = mockk(relaxed = true),
            inverse = true
        )
    }

    @Test
    fun `when applying automatically but there is an error, auto apply callback should not be called`() {
        val code = "123111"
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )
        stubAutoApplyQueryParams(
            code = code,
            queryParams = queryParams
        )
        stubAutoApplyResponse(
            error = Throwable(),
            queryParams = queryParams
        )

        val autoApplyCallback: AutoApplyCallback = mockk(relaxed = true)

        viewModel.autoApplycallback = autoApplyCallback

        viewModel.autoApply(
            code = code
        )

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
        verifyAutoApplyCallback(
            response = any(),
            autoApplyCallback = autoApplyCallback,
            inverse = true
        )
    }

    @Test
    fun `test some variables`() {
        val campaignSlug = "slug"
        val pageName = "giftcircle"
        val uniqueCode = "1UUUEFR"
        val about = "dailycircle"

        viewModel.campaignSlug = campaignSlug
        viewModel.pageName = pageName
        viewModel.uniqueCode = uniqueCode
        viewModel.about = about

        viewModel.campaignSlug
            .verifyAssertEquals(campaignSlug)
        viewModel.pageName
            .verifyAssertEquals(pageName)
        viewModel.uniqueCode
            .verifyAssertEquals(uniqueCode)
        viewModel.about
            .verifyAssertEquals(about)
    }
}
