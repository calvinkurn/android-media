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
import com.tokopedia.play.view.uimodel.ExploreWidgetItemUiModel
import com.tokopedia.play.view.uimodel.ExploreWidgetState
import com.tokopedia.play.view.uimodel.ExploreWidgetType
import com.tokopedia.play.view.uimodel.TabMenuUiModel
import com.tokopedia.play.view.uimodel.WidgetParamUiModel
import com.tokopedia.play.view.uimodel.WidgetUiModel
import com.tokopedia.play.view.uimodel.action.DismissExploreWidget
import com.tokopedia.play.view.uimodel.action.EmptyPageWidget
import com.tokopedia.play.view.uimodel.action.FetchWidgets
import com.tokopedia.play.view.uimodel.action.NextPageWidgets
import com.tokopedia.play.view.uimodel.action.RefreshWidget
import com.tokopedia.play.view.uimodel.recom.CategoryWidgetConfig
import com.tokopedia.play.view.uimodel.recom.ExploreWidgetConfig
import com.tokopedia.play.view.uimodel.recom.PlayChannelRecommendationConfig
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMock
import com.tokopedia.play.widget.ui.model.PartnerType
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelTypeTransition
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetShareUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalView
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 07/07/23
 */
class PlayChannelRecomTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()

    private val config =
        PlayChannelRecommendationConfig(
            categoryWidgetConfig = CategoryWidgetConfig(
                categoryGroup = "explore-cate",
                categorySourceType = "Suneo",
                categoryId = "25490",
                hasCategory = true,
                categoryName = "UHT"
            ),
            exploreWidgetConfig = ExploreWidgetConfig(
                group = "explore",
                sourceType = "dora-emon",
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
            state.channel.channelRecomConfig.categoryWidgetConfig.assertType<CategoryWidgetConfig> { c ->
                c.categorySourceId.assertEqualTo(config.categoryWidgetConfig.categorySourceId)
                c.categoryGroup.assertEqualTo(config.categoryWidgetConfig.categoryGroup)
                c.categorySourceType.assertEqualTo(config.categoryWidgetConfig.categorySourceType)
                c.categoryName.assertEqualTo(config.categoryWidgetConfig.categoryName)
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
            state.channel.channelRecomConfig.categoryWidgetConfig.assertType<CategoryWidgetConfig> { c ->
                c.categorySourceId.assertEqualTo(config.categoryWidgetConfig.categorySourceId)
                c.categoryGroup.assertEqualTo(config.categoryWidgetConfig.categoryGroup)
                c.categorySourceType.assertEqualTo(config.categoryWidgetConfig.categorySourceType)
                c.categoryName.assertEqualTo(config.categoryWidgetConfig.categoryName)
            }
            state.exploreWidget.category.data.assertEmpty()
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
                it.submitAction(FetchWidgets(ExploreWidgetType.Category))
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
                it.submitAction(FetchWidgets(ExploreWidgetType.Category))
            }
            it.viewModel.isAnyBottomSheetsShown.assertTrue()
            stateAndEvent.first.exploreWidget.category.state.isFail.assertTrue()

            val param =
                it.viewModel.getPrivateField<MutableStateFlow<Map<ExploreWidgetType, WidgetParamUiModel>>>(
                    "widgetQuery"
                )
            stateAndEvent.first.channel.channelRecomConfig.categoryWidgetConfig.assertType<CategoryWidgetConfig> { c ->
                c.categorySourceId.assertEqualTo(config.categoryWidgetConfig.categorySourceId)
                c.categoryGroup.assertEqualTo(config.categoryWidgetConfig.categoryGroup)
                c.categorySourceType.assertEqualTo(config.categoryWidgetConfig.categorySourceType)
                c.categoryName.assertEqualTo(config.categoryWidgetConfig.categoryName)
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
                it.submitAction(FetchWidgets(ExploreWidgetType.Category))
                it.submitAction(EmptyPageWidget(ExploreWidgetType.Category))
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
                it.submitAction(FetchWidgets(ExploreWidgetType.Category))
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
                it.submitAction(FetchWidgets(ExploreWidgetType.Category))
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
                it.submitAction(FetchWidgets(ExploreWidgetType.Category))
                it.submitAction(NextPageWidgets(ExploreWidgetType.Category))
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
                it.submitAction(FetchWidgets(ExploreWidgetType.Category))
                it.submitAction(NextPageWidgets(ExploreWidgetType.Category))
            }
            it.viewModel.isAnyBottomSheetsShown.assertTrue()
            stateAndEvent.first.exploreWidget.data.state.isFail.assertTrue()
        }
    }
}
