package com.tokopedia.gamification.giftbox.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gamification.data.entity.CrackBenefitEntity
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity
import com.tokopedia.gamification.giftbox.data.factory.TapTapBaseDataFactory.createCouponDetailResponse
import com.tokopedia.gamification.giftbox.data.factory.TapTapBaseDataFactory.createTapTapBaseEntity
import com.tokopedia.gamification.giftbox.data.factory.TapTapBaseDataFactory.createTapTapCrackEntity
import com.tokopedia.gamification.giftbox.data.entities.CouponDetailResponse
import com.tokopedia.gamification.giftbox.domain.CouponDetailUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxTapTapCrackUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxTapTapHomeUseCase
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxTapTapViewModel
import com.tokopedia.gamification.giftbox.util.TestUtil.verifyAssertEquals
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.taptap.data.entiity.TapTapBaseEntity
import com.tokopedia.kotlin.extensions.view.EMPTY
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class GiftBoxTapTapViewModelTest {
    @RelaxedMockK
    lateinit var giftBoxTapTapCrackUseCase: GiftBoxTapTapCrackUseCase

    @RelaxedMockK
    lateinit var giftBoxTapTapHomeUseCase: GiftBoxTapTapHomeUseCase

    @RelaxedMockK
    lateinit var couponDetailUseCase: CouponDetailUseCase

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: GiftBoxTapTapViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = GiftBoxTapTapViewModel(
            workerDispatcher = UnconfinedTestDispatcher(),
            crackUseCase = giftBoxTapTapCrackUseCase,
            homeUseCase = giftBoxTapTapHomeUseCase,
            couponDetailUseCase = couponDetailUseCase
        )
    }

    private fun stubGiftBoxTapTapHomeResponse(
        entity: TapTapBaseEntity
    ) {
        coEvery {
            giftBoxTapTapHomeUseCase.getResponse(map = any())
        } returns entity
    }

    private fun stubCouponDetailResponse(
        couponIdList: List<String>,
        response: CouponDetailResponse
    ) {
        coEvery {
            couponDetailUseCase.getResponse(couponIdList = couponIdList)
        } returns response
    }

    private fun stubGiftBoxTapTapCrackResponse(
        tokenId: String,
        campaignId: Long,
        entity: ResponseCrackResultEntity
    ) {
        coEvery {
            giftBoxTapTapCrackUseCase.getResponse(
                map = giftBoxTapTapCrackUseCase.getQueryParams(
                    tokenId = tokenId,
                    campaignId = campaignId
                )
            )
        } returns entity
    }

    private fun stubGiftBoxTapTapHomeResponse(
        error: Throwable
    ) {
        coEvery {
            giftBoxTapTapHomeUseCase.getResponse(map = any())
        } throws error
    }

    private fun stubGiftBoxTapTapCrackResponse(
        error: Throwable
    ) {
        coEvery {
            giftBoxTapTapCrackUseCase.getResponse(map = any())
        } throws error
    }

    private fun stubCouponDetailResponse(
        couponIdList: List<String>,
        error: Throwable
    ) {
        coEvery {
            couponDetailUseCase.getResponse(couponIdList = couponIdList)
        } throws error
    }

    private fun verifyGetCouponDetail(couponIdList: List<String>) {
        coVerify {
            couponDetailUseCase.getResponse(couponIdList = couponIdList)
        }
    }

    private fun verifyGiftBoxTapTapCrack(
        tokenId: String,
        campaignId: Long
    ) {
        coVerify {
            giftBoxTapTapCrackUseCase.getResponse(
                map = giftBoxTapTapCrackUseCase.getQueryParams(
                    tokenId = tokenId,
                    campaignId = campaignId
                )
            )
        }
    }

    @Test
    fun `after successfully getting gift box, livedata should provide the expected result`() {
        val result = createTapTapBaseEntity()

        stubGiftBoxTapTapHomeResponse(result)

        viewModel.getGiftBoxHome()

        viewModel.canShowLoader
            .verifyAssertEquals(true)

        viewModel.giftHomeLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `after successfully getting gift box although canShowLoader is false, livedata should provide the expected result`() {
        val result = createTapTapBaseEntity()

        stubGiftBoxTapTapHomeResponse(result)

        viewModel.canShowLoader = false

        viewModel.getGiftBoxHome()

        viewModel.canShowLoader
            .verifyAssertEquals(false)

        viewModel.giftHomeLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `when getting gift box but there is an error, livedata should provide null as a result`() {
        stubGiftBoxTapTapHomeResponse(Throwable())

        viewModel.getGiftBoxHome()

        viewModel.canShowLoader
            .verifyAssertEquals(true)

        viewModel.giftHomeLiveData
            .verifyAssertEquals(
                expected = null,
                status = LiveDataResult.STATUS.ERROR
            )
    }

    @Test
    fun `after successfully cracking gift box, livedata should provide the expected result`() {
        val result = createTapTapCrackEntity()
        val tokenId = "123121"
        val campaignId = 23121L

        stubGiftBoxTapTapCrackResponse(
            tokenId = tokenId,
            campaignId = campaignId,
            entity = result
        )

        viewModel.waitingForCrackResult = false
        viewModel.tokenId = tokenId
        viewModel.campaignId = campaignId

        viewModel.crackGiftBox()

        verifyGiftBoxTapTapCrack(
            tokenId = tokenId,
            campaignId = campaignId
        )

        viewModel.waitingForCrackResult
            .verifyAssertEquals(true)

        viewModel.giftCrackLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `when cracking gift box but there is an error, livedata should provide null as a result`() {
        stubGiftBoxTapTapCrackResponse(Throwable())

        viewModel.waitingForCrackResult = false

        viewModel.crackGiftBox()

        viewModel.waitingForCrackResult
            .verifyAssertEquals(true)

        viewModel.giftCrackLiveData
            .verifyAssertEquals(
                expected = null,
                status = LiveDataResult.STATUS.ERROR
            )
    }

    @Test
    fun `after successfully getting coupon detail, livedata should provide the expected result`() {
        val referenceId = "12313212"
        val benefitItems = listOf(
            CrackBenefitEntity(
                benefitType = BenefitType.COUPON,
                referenceID = referenceId
            )
        )

        val result = createCouponDetailResponse(referenceId)
        val couponIdList = viewModel.mapBenefitsToIds(benefitItems)

        stubCouponDetailResponse(
            couponIdList = couponIdList,
            response = result
        )

        viewModel.getCouponDetails(
            benefitItems = benefitItems
        )

        verifyGetCouponDetail(
            couponIdList = couponIdList
        )

        viewModel.couponLiveData
            .verifyAssertEquals(
                expected = result,
                status = LiveDataResult.STATUS.SUCCESS
            )
    }

    @Test
    fun `when getting coupon detail but there is an error, livedata should provide null as a result`() {
        val referenceId = "12313212"
        val benefitItems = listOf(
            CrackBenefitEntity(
                benefitType = BenefitType.COUPON,
                referenceID = referenceId
            )
        )

        val couponIdList = viewModel.mapBenefitsToIds(benefitItems)

        stubCouponDetailResponse(
            couponIdList = couponIdList,
            error = Throwable()
        )

        viewModel.getCouponDetails(
            benefitItems = benefitItems
        )

        verifyGetCouponDetail(
            couponIdList = couponIdList
        )

        viewModel.couponLiveData
            .verifyAssertEquals(
                expected = null,
                status = LiveDataResult.STATUS.ERROR
            )
    }

    @Test
    fun `when map benefit to id list should provide expected result`() {
        val referenceId = "12313212"
        var benefitItems = listOf(
            CrackBenefitEntity()
        )

        viewModel.mapBenefitsToIds(benefitItems)
            .verifyAssertEquals(listOf<String>())

        benefitItems = listOf(
            CrackBenefitEntity(
                benefitType = BenefitType.OVO,
                referenceID = referenceId
            )
        )

        viewModel.mapBenefitsToIds(benefitItems)
            .verifyAssertEquals(listOf<String>())

        benefitItems = listOf(
            CrackBenefitEntity(
                benefitType = BenefitType.COUPON,
                referenceID = String.EMPTY
            )
        )

        viewModel.mapBenefitsToIds(benefitItems)
            .verifyAssertEquals(listOf<String>())

        benefitItems = listOf(
            CrackBenefitEntity(
                benefitType = BenefitType.OVO,
                referenceID = String.EMPTY
            )
        )

        viewModel.mapBenefitsToIds(benefitItems)
            .verifyAssertEquals(listOf<String>())

        benefitItems = listOf(
            CrackBenefitEntity(
                benefitType = BenefitType.COUPON,
                referenceID = referenceId
            )
        )

        viewModel.mapBenefitsToIds(benefitItems)
            .verifyAssertEquals(listOf(referenceId))
    }
}
