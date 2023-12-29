package com.tokopedia.scp_rewards.cabinet

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.scp_rewards.cabinet.domain.GetUserMedaliUseCase
import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetUserMedalisResponse
import com.tokopedia.scp_rewards.cabinet.presentation.viewmodel.SeeMoreMedaliViewModel
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
class SeeMoreMedaliViewModelTest {

    @get:Rule
    val rule1 = UnconfinedTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewStates: MutableList<ScpResult>
    private lateinit var hasMoreLiveData: MutableList<Boolean>
    private lateinit var viewModel: SeeMoreMedaliViewModel
    private val getUserMedalUseCase = mockk<GetUserMedaliUseCase>()

    @Before
    fun setup() {
        viewModel = SeeMoreMedaliViewModel(getUserMedalUseCase)
        viewStates = mutableListOf()
        hasMoreLiveData = mutableListOf()
        viewModel.medalLiveData.observeForever { viewStates.add(it) }
        viewModel.hasNextLiveData.observeForever { hasMoreLiveData.add(it) }
    }

    @Test
    fun `call getUserMedalis api success code 200`() {
        runBlocking {
            coEvery { getUserMedalUseCase.getUserMedalis(any()) } returns getMedalResponse()

            viewModel.getUserMedalis()

            assertEquals(viewStates[0], Loading)
            assertEquals(viewStates[1] is Success<*>, true)
            assertEquals((viewStates[1] as Success<ScpRewardsGetUserMedalisResponse>).data.scpRewardsGetUserMedalisByType != null, true)
            assertEquals(hasMoreLiveData[0], true)
            assertEquals(hasMoreLiveData[1], true)
        }
    }

    @Test
    fun `call getUserMedalis api success code 200 but no more data`() {
        runBlocking {
            coEvery { getUserMedalUseCase.getUserMedalis(any()) } returns getMedalResponse(hasNext = false)

            viewModel.getUserMedalis()

            assertEquals(viewStates[0], Loading)
            assertEquals(viewStates[1] is Success<*>, true)
            assertEquals((viewStates[1] as Success<ScpRewardsGetUserMedalisResponse>).data.scpRewardsGetUserMedalisByType != null, true)
            assertEquals(hasMoreLiveData[0], true)
            assertEquals(hasMoreLiveData[1], false)
        }
    }

    @Test
    fun `call getUserMedalis api success code not 200`() {
        runBlocking {
            coEvery { getUserMedalUseCase.getUserMedalis(any()) } returns getMedalResponse("300")

            viewModel.getUserMedalis()

            assertEquals(viewStates[0], Loading)
            assertEquals(viewStates[1] is Error, true)
            assertEquals(hasMoreLiveData[0], true)
        }
    }

    private fun getMedalResponse(code: String = "200", hasNext: Boolean = true) = ScpRewardsGetUserMedalisResponse(
        scpRewardsGetUserMedalisByType = ScpRewardsGetUserMedalisResponse.ScpRewardsGetUserMedalisByType(
            resultStatus = ScpRewardsGetUserMedalisResponse.ScpRewardsGetUserMedalisByType.ResultStatus(code),
            medaliList = emptyList(),
            paging = ScpRewardsGetUserMedalisResponse.ScpRewardsGetUserMedalisByType.Paging(hasNext = hasNext)
        )
    )
}
