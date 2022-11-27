package com.tokopedia.videoTabComponent.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.domain.PlayVideoTabRepository
import com.tokopedia.videoTabComponent.domain.model.data.VideoPageParams
import com.tokopedia.videoTabComponent.model.PlayVideoModelBuilder
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test

/**
 * Created by shruti agarwal on 24/11/22.
 */

class PlayFeedVideoTabViewModelInitialDataTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

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
    fun `given there is video tab initial data, then initial data for video tab can be fetched`() {
        val expectedResult = builder.getContentSlotResponse()

        coEvery { mockRepo.getPlayData(any()) } returns expectedResult

        viewModel.getInitialPlayData()

        val result = viewModel.getPlayInitialDataRsp.value

        Assertions
            .assertThat(result)
            .isEqualTo(Success(expectedResult))
    }

    @Test
    fun `given error when fetching initial data, then return error`() {
        coEvery { mockRepo.getPlayData(any()) } throws Throwable()

        viewModel.getInitialPlayData()

        val result = viewModel.getPlayInitialDataRsp.value

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }
}
