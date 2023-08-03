package com.tokopedia.scp_rewards.cabinet

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.scp_rewards.cabinet.analytics.MedalCabinetAnalytics
import com.tokopedia.scp_rewards.cabinet.domain.GetUserMedaliUseCase
import com.tokopedia.scp_rewards.cabinet.domain.MedaliSectionUseCase
import com.tokopedia.scp_rewards.cabinet.domain.model.MedaliCabinetData
import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetMedaliSectionResponse
import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetUserMedalisResponse
import com.tokopedia.scp_rewards.cabinet.presentation.viewmodel.MedalCabinetViewModel
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.ScpResult
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MedalCabinetViewModelTest {

    @get:Rule
    val rule1 = UnconfinedTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewStates: MutableList<ScpResult>
    private lateinit var medalCabinetViewModel: MedalCabinetViewModel
    private val getUserMedalUseCase = mockk<GetUserMedaliUseCase>()
    private val getMedalSectionUseCase = mockk<MedaliSectionUseCase>()
    private val medalCabinetAnalytics = mockk<MedalCabinetAnalytics>(relaxed = true)

    companion object {
        private val medaliSlug = "INJECT_BADGE_1"
    }

    @Before
    fun setup() {
        medalCabinetViewModel = MedalCabinetViewModel(getMedalSectionUseCase, getUserMedalUseCase, medalCabinetAnalytics)
        viewStates = mutableListOf()
        medalCabinetViewModel.cabinetLiveData.observeForever { viewStates.add(it) }
    }

    @Test
    fun `call getMedaliHomePageSection api success code 200`() {
        runBlocking {
            coEvery { getUserMedalUseCase.getUserMedalis(any()) } returns getMedalResponse()
            coEvery { getMedalSectionUseCase.getMedaliHomePageSection(any()) } returns getSectionResponse()

            medalCabinetViewModel.getHomePage(medaliSlug)

            assertEquals(viewStates[0], Loading)
            assertEquals(viewStates[1] is Success<*>, true)
            assertEquals((viewStates[1] as Success<MedaliCabinetData>).data.header != null, true)
            assertEquals((viewStates[1] as Success<MedaliCabinetData>).data.earnedMedaliData != null, true)
            assertEquals((viewStates[1] as Success<MedaliCabinetData>).data.progressMedaliData != null, true)

            assertEquals((viewStates[1] as Success<MedaliCabinetData>).data.earnedMedaliData?.medalList != null, true)
            assertEquals((viewStates[1] as Success<MedaliCabinetData>).data.progressMedaliData?.medalList != null, true)
        }
    }

    @Test
    fun `call getMedaliHomePageSection api success code 200 but getUserMedalis returns not 200`() {
        runBlocking {
            coEvery { getUserMedalUseCase.getUserMedalis(any()) } returns getMedalResponse("400")
            coEvery { getMedalSectionUseCase.getMedaliHomePageSection(any()) } returns getSectionResponse()

            medalCabinetViewModel.getHomePage(medaliSlug)

            assertEquals(viewStates[0], Loading)
            assertEquals(viewStates[1] is Success<*>, true)
            assertEquals((viewStates[1] as Success<MedaliCabinetData>).data.header != null, true)
            assertEquals((viewStates[1] as Success<MedaliCabinetData>).data.earnedMedaliData != null, true)
            assertEquals((viewStates[1] as Success<MedaliCabinetData>).data.progressMedaliData != null, true)

            assertEquals((viewStates[1] as Success<MedaliCabinetData>).data.earnedMedaliData?.medalList.isNullOrEmpty(), true)
            assertEquals((viewStates[1] as Success<MedaliCabinetData>).data.progressMedaliData?.medalList.isNullOrEmpty(), true)
        }
    }

    @Test
    fun `call getMedaliHomePageSection api success code not 200`() {
        runBlocking {
            coEvery { getMedalSectionUseCase.getMedaliHomePageSection(any()) } returns getSectionResponse("300")

            medalCabinetViewModel.getHomePage(medaliSlug)

            assertEquals(viewStates[0], Loading)
            assertEquals(viewStates[1] is Error, true)
        }
    }

    private fun getSectionResponse(code: String = "200") = ScpRewardsGetMedaliSectionResponse(
        scpRewardsGetMedaliSectionLayout = ScpRewardsGetMedaliSectionResponse.ScpRewardsGetMedaliSectionLayout(
            resultStatus = ScpRewardsGetMedaliSectionResponse.ScpRewardsGetMedaliSectionLayout.ResultStatus(code),
            medaliSectionLayoutList = listOf(
                ScpRewardsGetMedaliSectionResponse.ScpRewardsGetMedaliSectionLayout.MedaliSectionLayout(),
                ScpRewardsGetMedaliSectionResponse.ScpRewardsGetMedaliSectionLayout.MedaliSectionLayout(),
                ScpRewardsGetMedaliSectionResponse.ScpRewardsGetMedaliSectionLayout.MedaliSectionLayout()
            )
        )
    )

    private fun getMedalResponse(code: String = "200") = ScpRewardsGetUserMedalisResponse(
        scpRewardsGetUserMedalisByType = ScpRewardsGetUserMedalisResponse.ScpRewardsGetUserMedalisByType(
            resultStatus = ScpRewardsGetUserMedalisResponse.ScpRewardsGetUserMedalisByType.ResultStatus(code),
            medaliList = emptyList()
        )
    )
}
