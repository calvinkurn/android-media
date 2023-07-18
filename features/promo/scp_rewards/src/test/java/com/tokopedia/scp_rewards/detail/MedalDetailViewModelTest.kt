package com.tokopedia.scp_rewards.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.ScpResult
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.common.utils.MEDALI_DETAIL_PAGE
import com.tokopedia.scp_rewards.detail.domain.CouponAutoApplyUseCase
import com.tokopedia.scp_rewards.detail.domain.MedalDetailUseCase
import com.tokopedia.scp_rewards.detail.domain.model.CouponAutoApply
import com.tokopedia.scp_rewards.detail.domain.model.CouponAutoApplyResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.MedalDetailResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.RewardsGetMedaliDetail
import com.tokopedia.scp_rewards.detail.domain.model.ScpRewardsCouponAutoApply
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.MedalDetailViewModel
import com.tokopedia.scp_rewards_widgets.medal_footer.FooterData
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MedalDetailViewModelTest {

    @get:Rule
    val rule1 = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: MedalDetailViewModel
    private lateinit var viewStates: MutableList<ScpResult>
    private lateinit var couponAutoApplyViewStates: MutableList<MedalDetailViewModel.AutoApplyState>

    private val medalDetailUseCase: MedalDetailUseCase = mockk()
    private val couponApplyUseCase: CouponAutoApplyUseCase = mockk()

    private val medaliSlug = "INJECT_BADGE_1"
    private val sourceName = "HOME"
    private val shopID = null
    private val couponCode = "123"
    private val footerData = FooterData(couponCode = couponCode, id = 123, text = "Hello")
    private val pageName = MEDALI_DETAIL_PAGE

    @Before
    fun setup() {
        viewModel = MedalDetailViewModel(medalDetailUseCase, couponApplyUseCase)
        viewStates = mutableListOf()
        couponAutoApplyViewStates = mutableListOf()
        viewModel.badgeLiveData.observeForever { viewStates.add(it) }
        viewModel.autoApplyCoupon.observeForever { couponAutoApplyViewStates.add(it) }
    }

    @Test
    fun `call getMedalDetail api success code 200`() {
        runBlocking {
            coEvery { medalDetailUseCase.getMedalDetail(medaliSlug, sourceName, pageName) } returns getMedalDetail()
            viewModel.getMedalDetail(medaliSlug, sourceName, pageName)

            assertEquals(viewStates[0], Loading)
            assertEquals(viewStates[1] is Success<*>, true)
        }
    }

    @Test
    fun `call getMedalDetail api success code not 200`() {
        runBlocking {
            coEvery { medalDetailUseCase.getMedalDetail(medaliSlug, sourceName, pageName) } returns getMedalDetail("400")

            viewModel.getMedalDetail(medaliSlug, sourceName, pageName)

            assertEquals(viewStates[0], Loading)
            assertEquals(viewStates[1] is Error, true)
            assertEquals((viewStates[1] as Error).errorCode, "400")
        }
    }

    @Test
    fun `call applyCoupon api success code 200 couponSuccessfully applied`() {
        runBlocking {
            val response = getCouponApply(isSuccess = true)
            coEvery { couponApplyUseCase.applyCoupon(shopID, couponCode) } returns response

            viewModel.applyCoupon(footerData, shopID, couponCode)

            assertEquals(couponAutoApplyViewStates[0], MedalDetailViewModel.AutoApplyState.Loading(footerData))
            assertEquals(couponAutoApplyViewStates[1], MedalDetailViewModel.AutoApplyState.SuccessCouponApplied(footerData, response.data))
            assertEquals((couponAutoApplyViewStates[1] as MedalDetailViewModel.AutoApplyState.SuccessCouponApplied).footerData.id, footerData.id)
            assertEquals((couponAutoApplyViewStates[1] as MedalDetailViewModel.AutoApplyState.SuccessCouponApplied).data?.couponAutoApply?.isSuccess, response.data?.couponAutoApply?.isSuccess)
        }
    }

    @Test
    fun `call applyCoupon api success code 200 coupon apply failed`() {
        runBlocking {
            val response = getCouponApply(isSuccess = false)
            coEvery { couponApplyUseCase.applyCoupon(shopID, couponCode) } returns response

            viewModel.applyCoupon(footerData, shopID, couponCode)

            assertEquals(couponAutoApplyViewStates[0], MedalDetailViewModel.AutoApplyState.Loading(footerData))
            assertEquals(couponAutoApplyViewStates[1], MedalDetailViewModel.AutoApplyState.SuccessCouponFailed(footerData, response.data))
        }
    }

    @Test
    fun `call applyCoupon api success code not 200`() {
        runBlocking {
            val response = getCouponApply(code = "400")
            coEvery { couponApplyUseCase.applyCoupon(shopID, couponCode) } returns response

            viewModel.applyCoupon(footerData, shopID, couponCode)

            assertEquals(couponAutoApplyViewStates[0], MedalDetailViewModel.AutoApplyState.Loading(footerData))
            assertEquals(couponAutoApplyViewStates[1] is MedalDetailViewModel.AutoApplyState.Error, true)
        }
    }

    private fun getMedalDetail(code: String = "200") = MedalDetailResponseModel(
        detail = RewardsGetMedaliDetail(
            resultStatus = RewardsGetMedaliDetail.ResultStatus(code = code)
        )
    )

    private fun getCouponApply(code: String = "200", isSuccess: Boolean = true) = CouponAutoApplyResponseModel(
        data = ScpRewardsCouponAutoApply(
            resultStatus = ScpRewardsCouponAutoApply.ResultStatus(code = code),
            couponAutoApply = CouponAutoApply(isSuccess = isSuccess)
        )
    )
}
