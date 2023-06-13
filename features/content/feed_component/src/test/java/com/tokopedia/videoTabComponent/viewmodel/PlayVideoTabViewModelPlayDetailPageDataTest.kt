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
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test

/**
 * Created by shruti agarwal on 24/11/22.
 */

class PlayVideoTabViewModelPlayDetailPageDataTest {

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
    fun `given user visits live detail page data, then live play data can be fetched`() {
        val expectedResult = builder.getContentSlotResponse()
        val widgetType = "live"

        coEvery { mockRepo.getPlayDetailPageResult(any(), any(), any(), any()) } returns expectedResult

        viewModel.getPlayDetailPageData(widgetType, "", "")

        val result = viewModel.getLiveOrUpcomingPlayDataRsp.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isEqualTo(Success(expectedResult))
    }

    @Test
    fun `given error when fetching play live data, then return error`() {
        val widgetType = "live"

        coEvery { mockRepo.getPlayDetailPageResult(any(), any(), any(), any()) } throws Throwable()

        viewModel.getPlayDetailPageData(widgetType, "", "")

        val result = viewModel.getLiveOrUpcomingPlayDataRsp.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }

    @Test
    fun `given user visits upcoming detail page data, then upcoming play data can be fetched`() {
        val expectedResult = builder.getContentSlotResponse()
        val widgetType = "upcoming"

        coEvery { mockRepo.getPlayDetailPageResult(any(), any(), any(), any()) } returns expectedResult

        viewModel.getPlayDetailPageData(widgetType, "", "")

        val result = viewModel.getLiveOrUpcomingPlayDataRsp.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isEqualTo(Success(expectedResult))
    }

    @Test
    fun `given user visits with widget empty on detail page data, then upcoming play data can be fetched`() {
        val expectedResult = builder.getContentSlotResponse()
        val widgetType = ""

        coEvery { mockRepo.getPlayDetailPageResult(any(), any(), any(), any()) } returns expectedResult

        viewModel.getPlayDetailPageData(widgetType, sourceType = "")

        val result = viewModel.getLiveOrUpcomingPlayDataRsp.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isEqualTo(Success(expectedResult))
    }

    @Test
    fun `given error when fetching play upcoming data, then return error`() {
        val widgetType = "live"

        coEvery { mockRepo.getPlayDetailPageResult(any(), any(), any(), any()) } throws Throwable()

        viewModel.getPlayDetailPageData(widgetType, "", "")

        val result = viewModel.getLiveOrUpcomingPlayDataRsp.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }
}
