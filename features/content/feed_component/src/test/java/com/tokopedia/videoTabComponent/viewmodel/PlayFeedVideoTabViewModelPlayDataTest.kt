package com.tokopedia.videoTabComponent.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.domain.PlayVideoTabRepository
import com.tokopedia.videoTabComponent.domain.model.data.VideoPageParams
import com.tokopedia.videoTabComponent.model.PlayVideoModelBuilder
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test

/**
 * Created by shruti agarwal on 24/11/22.
 */

class PlayFeedVideoTabViewModelPlayDataTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val playWidgetTools: PlayWidgetTools = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private val testDispatcher = rule.dispatchers

    private val mockRepo: PlayVideoTabRepository = mockk(relaxed = true)
    private val viewModel = PlayFeedVideoTabViewModel(
        testDispatcher,
        mockRepo,
        playWidgetTools,
        userSession
    )

    val params = VideoPageParams(cursor = "", group = "", sourceId = "", sourceType = "")
    private val builder = PlayVideoModelBuilder()

    @Test
    fun `given there is video tab play data, then data for video tab can be fetched`() {
        val expectedResult = builder.getContentSlotResponse()

        coEvery { mockRepo.getPlayData(any()) } returns expectedResult

        viewModel.getPlayData(false, params)

        val result = viewModel.getPlayDataRsp.value

        Assertions
            .assertThat(result)
            .isEqualTo(Success(expectedResult))
    }

    @Test
    fun `given error when fetching play data, then return error`() {
        coEvery { mockRepo.getPlayData(any()) } throws Throwable()

        viewModel.getPlayData(false, params)

        val result = viewModel.getPlayDataRsp.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }

    @Test
    fun `given user clicks on any chip to get play data, then data for video tab can be fetched`() {
        val expectedResult = builder.getContentSlotResponse()

        coEvery { mockRepo.getPlayData(any()) } returns expectedResult

        viewModel.getPlayData(true, params)

        val result = viewModel.getPlayDataForSlotRsp.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isEqualTo(Success(expectedResult))
    }

    @Test
    fun `given user clicks on any chip to get play data, then if fail then return error`() {
        coEvery { mockRepo.getPlayData(any()) } throws Throwable()

        viewModel.getPlayData(true, null)

        val result = viewModel.getPlayDataForSlotRsp.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }
}
