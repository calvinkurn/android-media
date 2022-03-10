package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStoreImpl
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetRecommendedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.SetChannelTagsUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.play.broadcaster.util.TestHtmlTextTransformer
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 25/09/20
 */
class PlayBroadcastSummaryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val playBroadcastMapper = PlayBroadcastUiMapper(TestHtmlTextTransformer())

    private lateinit var channelConfigStore: ChannelConfigStore

    private val liveStatisticsUseCase: GetLiveStatisticsUseCase = mockk(relaxed = true)
    private val broadcastUpdateChannelUseCase: PlayBroadcastUpdateChannelUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val mockGetRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase = mockk(relaxed = true)
    private val mockSetChannelTagsUseCase: SetChannelTagsUseCase = mockk(relaxed = true)

    private val modelBuilder = UiModelBuilder()
    private val mockLiveStats by lazy { modelBuilder.buildLiveStats() }

    private lateinit var viewModel: PlayBroadcastSummaryViewModel

    @Before
    fun setUp() {
        channelConfigStore = ChannelConfigStoreImpl()

        viewModel = PlayBroadcastSummaryViewModel(
                channelConfigStore,
                testDispatcher,
                liveStatisticsUseCase,
                broadcastUpdateChannelUseCase,
                userSession,
                playBroadcastMapper,
                mockGetRecommendedChannelTagsUseCase,
                mockSetChannelTagsUseCase,
        )
        channelConfigStore.setChannelId("12345")
    }

    @Test
    fun `when get traffic summary is success, then it should return success`() {
        coEvery { liveStatisticsUseCase.executeOnBackground() } returns mockLiveStats

        rule.runBlockingTest {
            viewModel.fetchLiveTraffic()
            advanceUntilIdle()

            val result = viewModel.observableLiveSummary.getOrAwaitValue()

            Assertions
                .assertThat(result)
                .isEqualTo(
                    NetworkResult.Success(
                        playBroadcastMapper.mapToLiveTrafficUiMetrics(mockLiveStats.channel.metrics)
                    )
                )
        }
    }

    @Test
    fun `when get traffic summary failed, then it should return failed`() {
        coEvery { liveStatisticsUseCase.executeOnBackground() } throws Throwable()

        rule.runBlockingTest {
            viewModel.fetchLiveTraffic()
            advanceUntilIdle()

            val result = viewModel.observableLiveSummary.getOrAwaitValue()

            Assertions
                .assertThat(result)
                .isInstanceOf(NetworkResult.Fail::class.java)
        }
    }
}