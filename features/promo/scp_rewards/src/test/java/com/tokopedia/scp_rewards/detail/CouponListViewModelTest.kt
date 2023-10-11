package com.tokopedia.scp_rewards.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.scp_rewards.detail.domain.CouponAutoApplyUseCase
import com.tokopedia.scp_rewards.detail.domain.GetMedalBenefitUseCase
import com.tokopedia.scp_rewards.detail.domain.model.CouponAutoApply
import com.tokopedia.scp_rewards.detail.domain.model.CouponAutoApplyResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.MedalBenefitResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.MedaliBenefit
import com.tokopedia.scp_rewards.detail.domain.model.MedaliBenefitList
import com.tokopedia.scp_rewards.detail.domain.model.RewardsGetMedaliBenefit
import com.tokopedia.scp_rewards.detail.domain.model.ScpRewardsCouponAutoApply
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.CouponListViewModel
import com.tokopedia.scp_rewards_widgets.constants.CouponStatus
import com.tokopedia.scp_rewards_widgets.model.FilterModel
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel
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
class CouponListViewModelTest {

    @get:Rule
    val rule1 = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: CouponListViewModel
    private lateinit var couponState: MutableList<CouponListViewModel.CouponState>
    private lateinit var couponAutoApplyViewStates: MutableList<CouponListViewModel.AutoApplyState>

    private val getMedalBenefitUseCase: GetMedalBenefitUseCase = mockk()
    private val couponAutoApplyUseCase: CouponAutoApplyUseCase = mockk()

    private val sourceName = "HOME"
    private val shopID = null
    private val couponCode = "123"
    private val medalBenefit = MedalBenefitModel(appLink = "applink", url = "url")

    private val filters = listOf(FilterModel(id = 1L), FilterModel(id = 2L))

    @Before
    fun setup() {
        viewModel = CouponListViewModel(
            getMedalBenefitUseCase,
            couponAutoApplyUseCase
        )

        couponState = mutableListOf()
        couponAutoApplyViewStates = mutableListOf()
        viewModel.couponListLiveData.observeForever { couponState.add(it) }
        viewModel.autoApplyCoupon.observeForever { couponAutoApplyViewStates.add(it) }

        viewModel.setFilters(filters)
    }

    @Test
    fun `set medali list explicitly when page status is active`() {
        runBlocking {
            val medaliList = listOf(MedalBenefitModel(categoryIds = listOf(1L)), MedalBenefitModel(categoryIds = listOf(5L)))
            viewModel.setPageStatus(CouponStatus.ACTIVE)
            viewModel.getFilteredData(FilterModel(1L).apply { isSelected = true })
            viewModel.setCouponsList(medaliList)

            assertEquals(viewModel.totalItemsCount, medaliList.size)
            assertEquals(couponState[0] is CouponListViewModel.CouponState.Loading, true)
            assertEquals(couponState[1] is CouponListViewModel.CouponState.Loading, true)
            assertEquals(couponState[2] is CouponListViewModel.CouponState.Success, true)
            assertEquals((couponState[2] as CouponListViewModel.CouponState.Success).list, listOf(MedalBenefitModel(categoryIds = listOf(1L))))
        }
    }

    @Test
    fun `set empty medali list explicitly when page status is empty`() {
        runBlocking {
            val medaliList = emptyList<MedalBenefitModel>()

            viewModel.setPageStatus(CouponStatus.EMPTY)
            viewModel.getFilteredData(FilterModel(1L).apply { isSelected = true })
            viewModel.setCouponsList(medaliList)

            assertEquals(viewModel.totalItemsCount, 0)
            assertEquals(couponState[0] is CouponListViewModel.CouponState.Loading, true)
            assertEquals(couponState[1] is CouponListViewModel.CouponState.Loading, true)
            assertEquals(couponState[2] is CouponListViewModel.CouponState.ActiveTabEmpty, true)
        }
    }

    @Test
    fun `call get medali list when page status is active retuns 200`() {
        runBlocking {
            val response = getMedaliBenefits()
            coEvery {
                getMedalBenefitUseCase.getMedalBenefits(
                    medaliSlug = "",
                    sourceName = "",
                    pageName = "",
                    type = CouponStatus.ACTIVE
                )
            } returns response

            viewModel.setPageStatus(CouponStatus.ACTIVE)
            viewModel.getFilteredData(FilterModel(1L).apply { isSelected = true })

            viewModel.getCouponList(sourceName = "")
            assertEquals(viewModel.totalItemsCount, response.scpRewardsMedaliBenefitList?.medaliBenefitList?.benefitList?.size)
            assertEquals(couponState[0] is CouponListViewModel.CouponState.Loading, true)
            assertEquals(couponState[1] is CouponListViewModel.CouponState.Loading, true)
            assertEquals(couponState[2] is CouponListViewModel.CouponState.Success, true)
            assertEquals((couponState[2] as CouponListViewModel.CouponState.Success).list?.size, 1)
        }
    }

    @Test
    fun `call get medali list when page status is history retuns empty list`() {
        runBlocking {
            val response = getMedaliBenefits(list = listOf(MedaliBenefit(status = CouponStatus.EMPTY)))
            coEvery {
                getMedalBenefitUseCase.getMedalBenefits(
                    medaliSlug = "",
                    sourceName = "",
                    pageName = "",
                    type = CouponStatus.INACTIVE
                )
            } returns response

            viewModel.setPageStatus(CouponStatus.EXPIRED)
            viewModel.getFilteredData(FilterModel(1L).apply { isSelected = true })

            viewModel.getCouponList(sourceName = "")
            assertEquals(viewModel.totalItemsCount, 0)
            assertEquals(couponState[0] is CouponListViewModel.CouponState.Loading, true)
            assertEquals(couponState[1] is CouponListViewModel.CouponState.Loading, true)
            assertEquals(couponState[2] is CouponListViewModel.CouponState.HistoryTabEmpty, true)
        }
    }

    @Test
    fun `call applyCoupon api success code 200 couponSuccessfully applied`() {
        runBlocking {
            val response = getCouponApply(isSuccess = true)
            coEvery { couponAutoApplyUseCase.applyCoupon(shopID, couponCode) } returns response

            viewModel.applyCoupon(medalBenefit, shopID, couponCode, 0)

            assertEquals(couponAutoApplyViewStates[0], CouponListViewModel.AutoApplyState.Loading(medalBenefit, 0))
            assertEquals(
                couponAutoApplyViewStates[1],
                CouponListViewModel.AutoApplyState.SuccessCouponApplied(response.data, medalBenefit, 0)
            )
            assertEquals(
                (couponAutoApplyViewStates[1] as CouponListViewModel.AutoApplyState.SuccessCouponApplied).data?.couponAutoApply?.isSuccess,
                response.data?.couponAutoApply?.isSuccess
            )
        }
    }

    @Test
    fun `call applyCoupon api success code 200 coupon apply failed`() {
        runBlocking {
            val response = getCouponApply(isSuccess = false)
            coEvery { couponAutoApplyUseCase.applyCoupon(shopID, couponCode) } returns response

            viewModel.applyCoupon(medalBenefit, shopID, couponCode, 0)

            assertEquals(couponAutoApplyViewStates[0], CouponListViewModel.AutoApplyState.Loading(medalBenefit, 0))
            assertEquals(couponAutoApplyViewStates[1], CouponListViewModel.AutoApplyState.SuccessCouponFailed(response.data, medalBenefit, 0))
        }
    }

    @Test
    fun `call applyCoupon api success code not 200`() {
        runBlocking {
            val response = getCouponApply(code = "400")
            coEvery { couponAutoApplyUseCase.applyCoupon(shopID, couponCode) } returns response

            viewModel.applyCoupon(medalBenefit, shopID, couponCode, 0)

            assertEquals(couponAutoApplyViewStates[0], CouponListViewModel.AutoApplyState.Loading(medalBenefit, 0))
            assertEquals(
                couponAutoApplyViewStates[1] is CouponListViewModel.AutoApplyState.Error,
                true
            )
        }
    }

    private fun getCouponApply(code: String = "200", isSuccess: Boolean = true) =
        CouponAutoApplyResponseModel(
            data = ScpRewardsCouponAutoApply(
                resultStatus = ScpRewardsCouponAutoApply.ResultStatus(code = code),
                couponAutoApply = CouponAutoApply(isSuccess = isSuccess)
            )
        )

    private fun getMedaliBenefits(
        code: String = "200",
        list: List<MedaliBenefit> = listOf(MedaliBenefit(title = "Testing123", categoryIdList = listOf(1L)))
    ) =
        MedalBenefitResponseModel(
            scpRewardsMedaliBenefitList = RewardsGetMedaliBenefit(
                resultStatus = RewardsGetMedaliBenefit.ResultStatus(code = code),
                medaliBenefitList = MedaliBenefitList(benefitList = list)
            )
        )
}
