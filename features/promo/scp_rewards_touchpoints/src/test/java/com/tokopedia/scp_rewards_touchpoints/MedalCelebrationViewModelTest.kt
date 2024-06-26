package com.tokopedia.scp_rewards_touchpoints

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.domain.CouponAutoApplyUseCase
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.domain.RewardsGetMedaliCelebrationPageUseCase
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.CouponAutoApply
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.CouponAutoApplyResponseModel
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.ScpRewardsCelebrationModel
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.ScpRewardsCouponAutoApply
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.viewmodel.MedalCelebrationViewModel
import com.tokopedia.scp_rewards_touchpoints.common.Error
import com.tokopedia.scp_rewards_touchpoints.common.Success
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class MedalCelebrationViewModelTest {

    @get:Rule
    val rule1 = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private var celebrationViewModel: MedalCelebrationViewModel? = null
    private val getRewardsUseCase = mockk<RewardsGetMedaliCelebrationPageUseCase>()
    private val autoApplyUseCase = mockk<CouponAutoApplyUseCase>()

    private val medaliSlug = "INJECT_BADGE_1"
    private val source = "homepage"

    @Before
    fun setup() {
        celebrationViewModel = MedalCelebrationViewModel(getRewardsUseCase, autoApplyUseCase)
    }

    @Test
    fun `get rewards api success 200`() {
        val response = getCelebrationResponse()
        val actualResult = Success(response)
        coEvery {
            getRewardsUseCase.getRewards(medaliSlug, source)
        } returns response
        celebrationViewModel?.getRewards(medaliSlug, source)
        celebrationViewModel?.badgeLiveData?.verifyValueEquals(actualResult)
    }

    @Test
    fun `get Rewards api failure`() {
        val error = Throwable()
        val actualResult = Error(error)
        coEvery {
            getRewardsUseCase.getRewards(medaliSlug, source)
        } throws error
        celebrationViewModel?.getRewards(medaliSlug, source)
        celebrationViewModel?.badgeLiveData?.verifyValueEquals(actualResult)
    }

    @Test
    fun `get rewards api not success 200`() {
        val response = getCelebrationResponse("400")
        coEvery {
            getRewardsUseCase.getRewards(medaliSlug, source)
        } returns response
        celebrationViewModel?.getRewards(medaliSlug, source)
        assertTrue { celebrationViewModel?.badgeLiveData?.value is Error }
    }

    @Test
    fun `auto apply coupon success`() {
        val response = getAutoApplyResponse()
        val benefitData =
            ScpRewardsCelebrationModel.RewardsGetMedaliCelebrationPage.CelebrationPage.BenefitButton()
        val actualResult = MedalCelebrationViewModel.AutoApplyState.SuccessCoupon(benefitData = benefitData, autoApplyData = response)
        coEvery {
            autoApplyUseCase.applyCoupon(any(), any())
        } returns response
        celebrationViewModel?.autoApplyCoupon(benefitData = benefitData, couponCode = "")
        celebrationViewModel?.autoApplyLiveData?.verifyValueEquals(actualResult)
    }

    @Test
    fun `auto apply coupon failure`() {
        val benefitData =
            ScpRewardsCelebrationModel.RewardsGetMedaliCelebrationPage.CelebrationPage.BenefitButton()
        coEvery {
            autoApplyUseCase.applyCoupon(any(), any())
        } returns CouponAutoApplyResponseModel()
        celebrationViewModel?.autoApplyCoupon(benefitData = benefitData, couponCode = "")
        assertTrue { celebrationViewModel?.autoApplyLiveData?.value is MedalCelebrationViewModel.AutoApplyState.Error }
    }

    private fun getCelebrationResponse(code: String = "200") = ScpRewardsCelebrationModel(
        scpRewardsCelebrationPage = ScpRewardsCelebrationModel.RewardsGetMedaliCelebrationPage(
            resultStatus = ScpRewardsCelebrationModel.RewardsGetMedaliCelebrationPage.ResultStatus(
                code = code
            ),
            celebrationPage = ScpRewardsCelebrationModel.RewardsGetMedaliCelebrationPage.CelebrationPage()
        )
    )

    private fun getAutoApplyResponse() = CouponAutoApplyResponseModel(
        data = ScpRewardsCouponAutoApply(
            couponAutoApply = CouponAutoApply(isSuccess = true)
        )
    )
}
