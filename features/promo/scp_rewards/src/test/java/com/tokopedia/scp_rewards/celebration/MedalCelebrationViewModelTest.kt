package com.tokopedia.scp_rewards.celebration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.scp_rewards.celebration.domain.RewardsGetMedaliCelebrationPageUseCase
import com.tokopedia.scp_rewards.celebration.domain.model.ScpRewardsCelebrationModel
import com.tokopedia.scp_rewards.celebration.presentation.viewmodel.MedalCelebrationViewModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class MedalCelebrationViewModelTest {

    @get:Rule
    val rule1 = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private var celebrationViewModel: MedalCelebrationViewModel? = null
    private val getRewardsUseCase = mockk<RewardsGetMedaliCelebrationPageUseCase>()

    private val medaliSlug = "INJECT_BADGE_1"
    private val source = "homepage"

    @Before
    fun setup() {
        celebrationViewModel = MedalCelebrationViewModel(getRewardsUseCase)
    }

    @Test
    fun `get rewards api success 200`() {
        val response = getResponse()
        coEvery {
            getRewardsUseCase.getRewards(medaliSlug, source)
        } returns response
        celebrationViewModel?.getRewards(medaliSlug, source)
        assertEquals(
            (celebrationViewModel?.badgeLiveData?.value as MedalCelebrationViewModel.ScpResult.Success<ScpRewardsCelebrationModel>).data.scpRewardsCelebrationPage?.resultStatus?.code,
            "200"
        )
    }

    @Test
    fun `get rewards api all medals celebrated error`() {
        val response = getResponse("45008")
        coEvery {
            getRewardsUseCase.getRewards(medaliSlug, source)
        } returns response
        celebrationViewModel?.getRewards(medaliSlug, source)
        assertEquals(
            celebrationViewModel?.badgeLiveData?.value is MedalCelebrationViewModel.ScpResult.AllMedaliCelebratedError,
            true
        )
    }

    @Test
    fun `get rewards api success code not 200`() {
        val response = getResponse(code = "300")
        coEvery {
            getRewardsUseCase.getRewards(medaliSlug, source)
        } returns response
        celebrationViewModel?.getRewards(medaliSlug, source)
        assertEquals(
            celebrationViewModel?.badgeLiveData?.value is MedalCelebrationViewModel.ScpResult.Error,
            true
        )
    }

    @Test
    fun `get Rewards api failure`() {
        coEvery {
            getRewardsUseCase.getRewards(medaliSlug, source)
        } throws Throwable()
        celebrationViewModel?.getRewards(medaliSlug, source)
        assertEquals(
            celebrationViewModel?.badgeLiveData?.value is MedalCelebrationViewModel.ScpResult.Error,
            true
        )
    }

    private fun getResponse(code: String = "200") = ScpRewardsCelebrationModel(
        scpRewardsCelebrationPage = ScpRewardsCelebrationModel.RewardsGetMedaliCelebrationPage(
            resultStatus = ScpRewardsCelebrationModel.RewardsGetMedaliCelebrationPage.ResultStatus(
                code = code
            ),
            celebrationPage = ScpRewardsCelebrationModel.RewardsGetMedaliCelebrationPage.CelebrationPage()
        )
    )
}
