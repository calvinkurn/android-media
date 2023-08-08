package com.tokopedia.play.viewmodel.explorewidget

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.robot.play.getPrivateField
import com.tokopedia.play.util.assertEmpty
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play.util.assertType
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.action.*
import com.tokopedia.play.view.uimodel.recom.ExploreWidgetConfig
import com.tokopedia.play.view.uimodel.recom.PlayChannelRecommendationConfig
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMock
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on  31/10/22
 */
class PlayExploreWidgetTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()

    private val config =
        PlayChannelRecommendationConfig(
            exploreWidgetConfig = ExploreWidgetConfig(
                group = "explore",
                sourceType = "Suneo",
                sourceId = "25490"
            )
        )

    private val mockChannelData = channelDataBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(
                channelType = PlayChannelType.Live
            ),
            widgetConfig = config
        )
    )

    private val mockRemoteConfig = mockk<RemoteConfig>(relaxed = true)
    private val repo: PlayViewerRepository = mockk(relaxed = true)

    val widgets = listOf<WidgetUiModel>(
        TabMenuUiModel(
            items = listOf(
                ChipWidgetUiModel(
                    group = "LIVE",
                    sourceId = "11",
                    sourceType = "a",
                    isSelected = false,
                    text = "Hehe"
                ),
                ChipWidgetUiModel(
                    group = "KK",
                    sourceId = "11111",
                    sourceType = "aaa",
                    isSelected = false,
                    text = "HeDoraemonkuhe"
                )

            ),
            state = ResultState.Loading
        ),
        SubSlotUiModel,
        ExploreWidgetItemUiModel(item = PlayWidgetUiMock.getSamplePlayWidget(), id = 1)
    )

    init {
        every { mockRemoteConfig.getBoolean(any(), any()) } returns true
    }

    @Test
    fun `when create page, setup param for explore`() {
        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val state = it.recordState {}
            state.channel.channelRecomConfig.exploreWidgetConfig.assertType<ExploreWidgetConfig> {
                c -> c.sourceId.assertEqualTo(config.exploreWidgetConfig.sourceId)
                c.group.assertEqualTo(config.exploreWidgetConfig.group)
                c.sourceType.assertEqualTo(config.exploreWidgetConfig.sourceType)
                c.categoryName.assertEqualTo(config.exploreWidgetConfig.categoryName)
            }
        }
    }

    @Test
    fun `dismiss explore widget`() {
        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val state = it.recordState {
                it.submitAction(DismissExploreWidget)
            }
            state.channel.channelRecomConfig.exploreWidgetConfig.assertType<ExploreWidgetConfig> {
                    c -> c.sourceId.assertEqualTo(config.exploreWidgetConfig.sourceId)
                c.group.assertEqualTo(config.exploreWidgetConfig.group)
                c.sourceType.assertEqualTo(config.exploreWidgetConfig.sourceType)
                c.categoryName.assertEqualTo(config.exploreWidgetConfig.categoryName)
            }
            state.exploreWidget.data.widgets.assertEmpty()
            state.exploreWidget.data.chips.assertEqualTo(TabMenuUiModel.Empty)
            it.viewModel.isAnyBottomSheetsShown.assertFalse()
        }
    }

    @Test
    fun `explore widget - handle empty`() {
        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val state = it.recordState {
            }
        }
    }

    @Test
    fun `user click eksplor widget`() {
        coEvery { repo.getWidgets(any(), any(), any(), any()) } returns widgets

        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val stateAndEvent = it.recordStateAndEvent {
                it.submitAction(FetchWidgets(ExploreWidgetType.Default))
            }
            stateAndEvent.second.any { it is ExploreWidgetState }
        }
    }

    @Test
    fun `user click eksplor widget error`() {
        coEvery { repo.getWidgets(any(), any(), any(), any()) } throws MessageErrorException("")
        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val stateAndEvent = it.recordStateAndEvent {
                it.submitAction(FetchWidgets(ExploreWidgetType.Default))
            }
            it.viewModel.isAnyBottomSheetsShown.assertTrue()
            stateAndEvent.first.exploreWidget.data.state.isFail.assertTrue()

            val param = it.viewModel.getPrivateField<MutableStateFlow<Map<ExploreWidgetType, WidgetParamUiModel>>>("widgetQuery")
            param.value.getValue(ExploreWidgetType.Default).assertType<WidgetParamUiModel> {
                c ->
                c.sourceId.assertEqualTo(config.exploreWidgetConfig.sourceId)
                c.sourceType.assertEqualTo(config.exploreWidgetConfig.sourceType)
                c.group.assertEqualTo(config.exploreWidgetConfig.group)
                c.isRefresh.assertFalse()
            }
        }
    }

    @Test
    fun `user click eksplor widget return empty`() {
        coEvery { repo.getWidgets(any(), any(), any(), any()) } returns widgets
        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val stateAndEvent = it.recordStateAndEvent {
                it.submitAction(FetchWidgets(ExploreWidgetType.Default))
                it.submitAction(EmptyPageWidget(ExploreWidgetType.Default))
            }
            it.viewModel.isAnyBottomSheetsShown.assertTrue()
        }
    }

    @Test
    fun `user refresh widget`() {
        coEvery { repo.getWidgets(any(), any(), any(), any()) } returns widgets
        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val stateAndEvent = it.recordStateAndEvent {
                it.submitAction(FetchWidgets(ExploreWidgetType.Default))
                it.submitAction(RefreshWidget)
            }
            it.viewModel.isAnyBottomSheetsShown.assertTrue()
            stateAndEvent.second.any { it is ExploreWidgetState }
        }
    }

    @Test
    fun `user refresh widget failed`() {
        coEvery { repo.getWidgets(any(), any(), any(), any()) } throws MessageErrorException("")
        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val stateAndEvent = it.recordStateAndEvent {
                it.submitAction(FetchWidgets(ExploreWidgetType.Default))
                it.submitAction(RefreshWidget)
            }
            it.viewModel.isAnyBottomSheetsShown.assertTrue()
            stateAndEvent.first.exploreWidget.data.state.isFail.assertTrue()
        }
    }

    @Test
    fun `user next page widget`() {
        coEvery { repo.getWidgets(any(), any(), any(), any()) } returns widgets
        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val stateAndEvent = it.recordStateAndEvent {
                it.submitAction(FetchWidgets(ExploreWidgetType.Default))
                it.submitAction(NextPageWidgets(ExploreWidgetType.Default))
            }
            it.viewModel.isAnyBottomSheetsShown.assertTrue()
            stateAndEvent.second.any { it !is ExploreWidgetState }
        }
    }

    @Test
    fun `user next page widget failed`() {
        coEvery { repo.getWidgets(any(), any(), any(), any()) } throws MessageErrorException("")
        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val stateAndEvent = it.recordStateAndEvent {
                it.submitAction(FetchWidgets(ExploreWidgetType.Default))
                it.submitAction(NextPageWidgets(ExploreWidgetType.Default))
            }
            it.viewModel.isAnyBottomSheetsShown.assertTrue()
            stateAndEvent.first.exploreWidget.data.state.isFail.assertTrue()
        }
    }
}
