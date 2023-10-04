package com.tokopedia.scp_rewards.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.scp_rewards.cabinet.domain.GetUserMedaliUseCase
import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetUserMedalisResponse
import com.tokopedia.scp_rewards.common.utils.MEDALI_DETAIL_PAGE
import com.tokopedia.scp_rewards.common.utils.MEDALI_SLUG_PARAM
import com.tokopedia.scp_rewards.common.utils.PAGESIZE_PARAM
import com.tokopedia.scp_rewards.common.utils.PAGE_NAME_PARAM
import com.tokopedia.scp_rewards.common.utils.PAGE_PARAM
import com.tokopedia.scp_rewards.common.utils.TYPE_PARAM
import com.tokopedia.scp_rewards.detail.domain.CouponAutoApplyUseCase
import com.tokopedia.scp_rewards.detail.domain.GetMedalBenefitUseCase
import com.tokopedia.scp_rewards.detail.domain.MedalDetailUseCase
import com.tokopedia.scp_rewards.detail.domain.model.CouponAutoApply
import com.tokopedia.scp_rewards.detail.domain.model.CouponAutoApplyResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.MdpSection
import com.tokopedia.scp_rewards.detail.domain.model.MedalBenefitResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.MedalDetailResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.MedaliBenefit
import com.tokopedia.scp_rewards.detail.domain.model.MedaliBenefitList
import com.tokopedia.scp_rewards.detail.domain.model.MedaliDetailPage
import com.tokopedia.scp_rewards.detail.domain.model.RewardsGetMedaliBenefit
import com.tokopedia.scp_rewards.detail.domain.model.RewardsGetMedaliDetail
import com.tokopedia.scp_rewards.detail.domain.model.ScpRewardsCouponAutoApply
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.MDP_SECTION_TYPE_BENEFIT
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.MDP_SECTION_TYPE_BRAND_RECOMMENDATIONS
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.MedalDetailViewModel
import com.tokopedia.scp_rewards_widgets.common.model.CtaButton
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.RequestParams
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
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
    private lateinit var viewStates: MutableList<MedalDetailViewModel.MdpState>
    private lateinit var couponAutoApplyViewStates: MutableList<MedalDetailViewModel.AutoApplyState>

    private val medalDetailUseCase: MedalDetailUseCase = mockk()
    private val getMedalBenefitUseCase: GetMedalBenefitUseCase = mockk()
    private val couponApplyUseCase: CouponAutoApplyUseCase = mockk()
    private val getUserMedaliUserCase: GetUserMedaliUseCase = mockk()

    private val medaliSlug = "INJECT_BADGE_1"
    private val sourceName = "HOME"
    private val shopID = null
    private val couponCode = "123"
    private val ctaButton = CtaButton(couponCode = couponCode, appLink = "applink", url = "url")
    private val pageName = MEDALI_DETAIL_PAGE

    @Before
    fun setup() {
        viewModel = MedalDetailViewModel(
            medalDetailUseCase,
            getMedalBenefitUseCase,
            couponApplyUseCase,
            getUserMedaliUserCase
        )
        viewStates = mutableListOf()
        couponAutoApplyViewStates = mutableListOf()
        viewModel.badgeLiveData.observeForever { viewStates.add(it) }
        viewModel.autoApplyCoupon.observeForever { couponAutoApplyViewStates.add(it) }

        mockkStatic(FirebaseCrashlytics::class)
        every {
            FirebaseCrashlytics.getInstance().recordException(any())
        } returns mockk(relaxed = true)
    }

    @Test
    fun `call getMedalDetail api success code 200`() {
        runBlocking {
            coEvery { medalDetailUseCase.getMedalDetail(medaliSlug, sourceName, pageName) } returns getMedalDetail(section = null)
            viewModel.getMedalDetail(medaliSlug, sourceName, pageName)

            assertEquals(viewStates[0], MedalDetailViewModel.MdpState.Loading)
            assertEquals(viewStates[1] is MedalDetailViewModel.MdpState.Success, true)
        }
    }

    @Test
    fun `call getMedalDetail api success code not 200`() {
        runBlocking {
            coEvery {
                medalDetailUseCase.getMedalDetail(
                    medaliSlug,
                    sourceName,
                    pageName
                )
            } returns getMedalDetail("400", section = null)

            viewModel.getMedalDetail(medaliSlug, sourceName, pageName)

            assertEquals(viewStates[0], MedalDetailViewModel.MdpState.Loading)
            assertEquals(viewStates[1] is MedalDetailViewModel.MdpState.Error, true)
            assertEquals((viewStates[1] as MedalDetailViewModel.MdpState.Error).errorCode, "400")
        }
    }

    @Test
    fun `call applyCoupon api success code 200 couponSuccessfully applied`() {
        runBlocking {
            val response = getCouponApply(isSuccess = true)
            coEvery { couponApplyUseCase.applyCoupon(shopID, couponCode) } returns response

            viewModel.applyCoupon(ctaButton, shopID, couponCode)

            assertEquals(couponAutoApplyViewStates[0], MedalDetailViewModel.AutoApplyState.Loading)
            assertEquals(
                couponAutoApplyViewStates[1],
                MedalDetailViewModel.AutoApplyState.SuccessCouponApplied(ctaButton, response.data)
            )
            assertEquals(
                (couponAutoApplyViewStates[1] as MedalDetailViewModel.AutoApplyState.SuccessCouponApplied).data?.couponAutoApply?.isSuccess,
                response.data?.couponAutoApply?.isSuccess
            )
        }
    }

    @Test
    fun `call applyCoupon api success code 200 coupon apply failed`() {
        runBlocking {
            val response = getCouponApply(isSuccess = false)
            coEvery { couponApplyUseCase.applyCoupon(shopID, couponCode) } returns response

            viewModel.applyCoupon(ctaButton, shopID, couponCode)

            assertEquals(couponAutoApplyViewStates[0], MedalDetailViewModel.AutoApplyState.Loading)
            assertEquals(couponAutoApplyViewStates[1], MedalDetailViewModel.AutoApplyState.SuccessCouponFailed(ctaButton, response.data))
        }
    }

    @Test
    fun `call applyCoupon api success code not 200`() {
        runBlocking {
            val response = getCouponApply(code = "400")
            coEvery { couponApplyUseCase.applyCoupon(shopID, couponCode) } returns response

            viewModel.applyCoupon(ctaButton, shopID, couponCode)

            assertEquals(couponAutoApplyViewStates[0], MedalDetailViewModel.AutoApplyState.Loading)
            assertEquals(
                couponAutoApplyViewStates[1] is MedalDetailViewModel.AutoApplyState.Error,
                true
            )
        }
    }

    @Test
    fun `call getMedalBenefits api success code 200`() {
        runBlocking {
            val response = getMedaliBenefits(code = "200")

            coEvery {
                getMedalBenefitUseCase.getMedalBenefits(
                    medaliSlug,
                    sourceName,
                    pageName
                )
            } returns response

            coEvery {
                medalDetailUseCase.getMedalDetail(
                    medaliSlug,
                    sourceName,
                    pageName
                )
            } returns getMedalDetail(section = listOf(MdpSection(type = MDP_SECTION_TYPE_BENEFIT)))

            viewModel.getMedalDetail(medaliSlug, sourceName, pageName)

            assertEquals(viewStates[0], MedalDetailViewModel.MdpState.Loading)
            assertEquals(viewStates[1] is MedalDetailViewModel.MdpState.Success, true)
            assertEquals(
                (viewStates[1] as MedalDetailViewModel.MdpState.Success).benefitData?.benefitList?.first()?.title,
                "Testing123"
            )
        }
    }

    @Test
    fun `call getMedaliRecommendations api success code 200`() {
        runBlocking {
            val response = getMedaliRecommendations(code = "200")

            coEvery {
                getUserMedaliUserCase.getUserMedalis(getRequestParams())
            } returns response

            coEvery {
                medalDetailUseCase.getMedalDetail(
                    medaliSlug,
                    sourceName,
                    pageName
                )
            } returns getMedalDetail(section = listOf(MdpSection(type = MDP_SECTION_TYPE_BRAND_RECOMMENDATIONS)))

            viewModel.getMedalDetail(medaliSlug, sourceName, pageName)

            assertEquals(viewStates[0], MedalDetailViewModel.MdpState.Loading)
            assertEquals(viewStates[1] is MedalDetailViewModel.MdpState.Success, true)
        }
    }

    private fun getMedalDetail(
        code: String = "200",
        section: List<MdpSection>? = listOf(MdpSection(type = MDP_SECTION_TYPE_BENEFIT), MdpSection(type = MDP_SECTION_TYPE_BRAND_RECOMMENDATIONS))
    ) = MedalDetailResponseModel(
        detail = RewardsGetMedaliDetail(
            resultStatus = RewardsGetMedaliDetail.ResultStatus(code = code),
            medaliDetailPage = MedaliDetailPage(
                section = section
            )
        )
    )

    private fun getCouponApply(code: String = "200", isSuccess: Boolean = true) =
        CouponAutoApplyResponseModel(
            data = ScpRewardsCouponAutoApply(
                resultStatus = ScpRewardsCouponAutoApply.ResultStatus(code = code),
                couponAutoApply = CouponAutoApply(isSuccess = isSuccess)
            )
        )

    private fun getMedaliBenefits(code: String = "200") =
        MedalBenefitResponseModel(
            scpRewardsMedaliBenefitList = RewardsGetMedaliBenefit(
                resultStatus = RewardsGetMedaliBenefit.ResultStatus(code = code),
                medaliBenefitList = MedaliBenefitList(benefitList = listOf(MedaliBenefit(title = "Testing123")))
            )
        )

    private fun getMedaliRecommendations(code: String = "200") =
        ScpRewardsGetUserMedalisResponse(
            scpRewardsGetUserMedalisByType = ScpRewardsGetUserMedalisResponse.ScpRewardsGetUserMedalisByType(
                resultStatus = ScpRewardsGetUserMedalisResponse.ScpRewardsGetUserMedalisByType.ResultStatus(
                    code = code
                ),
                medaliList = listOf(
                    ScpRewardsGetUserMedalisResponse.ScpRewardsGetUserMedalisByType.Medal(
                        name = "Testing123"
                    )
                )
            )
        )

    private fun getRequestParams() = RequestParams().apply {
        putObject(MEDALI_SLUG_PARAM, arrayOf(""))
        putString(PAGE_NAME_PARAM, "")
        putInt(PAGE_PARAM, 1)
        putString(TYPE_PARAM, "")
        putInt(PAGESIZE_PARAM, 3)
    }
}
