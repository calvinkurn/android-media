package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStoreImpl
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.model.ModelBuilder
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.TestCoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 25/09/20
 */
class PlayBroadcastViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatcherProvider = TestCoroutineDispatcherProvider(testDispatcher)

    private lateinit var channelConfigStore: ChannelConfigStore

    private val liveStatisticsUseCase: GetLiveStatisticsUseCase = mockk(relaxed = true)

    private val modelBuilder = ModelBuilder()
    private val mockLiveStats by lazy { modelBuilder.buildLiveStats() }

    private lateinit var viewModel: PlayBroadcastSummaryViewModel

    @Before
    fun setUp() {
        channelConfigStore = ChannelConfigStoreImpl()

        viewModel = PlayBroadcastSummaryViewModel(
                channelConfigStore,
                dispatcherProvider,
                liveStatisticsUseCase
        )
        channelConfigStore.setChannelId("12345")
    }

    @Test
    fun `test success get traffic summary`() {
        coEvery { liveStatisticsUseCase.executeOnBackground() } returns mockLiveStats

        viewModel.fetchLiveTraffic()

        //Consume loading
        viewModel.observableTrafficMetrics.getOrAwaitValue()

        val result = viewModel.observableTrafficMetrics.getOrAwaitValue()

        Assertions
                .assertThat(result)
                .isEqualTo(
                        NetworkResult.Success(
                                PlayBroadcastUiMapper.mapToLiveTrafficUiMetrics(mockLiveStats)
                        )
                )
    }

    @Test
    fun `test failed get traffic summary`() {
        coEvery { liveStatisticsUseCase.executeOnBackground() } throws Throwable()

        viewModel.fetchLiveTraffic()

        //Consume loading
        viewModel.observableTrafficMetrics.getOrAwaitValue()

        val result = viewModel.observableTrafficMetrics.getOrAwaitValue()

        Assertions
                .assertThat(result)
                .isInstanceOf(NetworkResult.Fail::class.java)
    }
}