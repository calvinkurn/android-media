package com.tokopedia.feedplus.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.presentation.model.ChipUiState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiAction
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import com.tokopedia.feedplus.data.FeedBrowseModelBuilder
import com.tokopedia.feedplus.presentation.robot.FeedBrowseViewModelRobot
import com.tokopedia.feedplus.presentation.robot.createFeedBrowseViewModelRobot
import com.tokopedia.tokopedia.feedplus.helper.assertEqualTo
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by meyta.taliti on 10/09/23.
 */
class FeedBrowseInteractionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val mockRepo: FeedBrowseRepository = mockk(relaxed = true)

    private lateinit var robot: FeedBrowseViewModelRobot

    private val uiBuilder = FeedBrowseModelBuilder()
    private val mockTitle = "mock title"
    private val extraParam = uiBuilder.buildExtraParam()
    private val mockSlots = uiBuilder.buildWidgets(extraParam = extraParam)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)

        robot = createFeedBrowseViewModelRobot(mockRepo)

        coEvery { mockRepo.getTitle() } returns mockTitle
        coEvery { mockRepo.getSlots() } returns mockSlots
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `action to fetch cards, given slots that match with existing widgetId, then it should return updated widgets`() {
        val expectedWidget = uiBuilder.buildWidgetChannelItemModel()
        val widgetId = mockSlots.first().id

        coEvery { mockRepo.getWidget(extraParam) } returns expectedWidget

        val expectedResult = FeedBrowseUiState.Success(
            mockTitle,
            widgets = uiBuilder.buildWidgets(extraParam, channelUiState = expectedWidget)
        )

        robot.use {
            val uiState = it.recordState {
                viewModel.submitAction(FeedBrowseUiAction.LoadInitialPage)
                viewModel.submitAction(FeedBrowseUiAction.FetchCards(extraParam, widgetId))
            }
            uiState.assertEqualTo(expectedResult)
        }
    }

    @Test
    fun `action to select chip, given slots that match with existing widgetId, then it should return updated widgets`() {
        val expectedWidget = uiBuilder.buildWidgetChipItemModel(
            uiBuilder.buildChips(selectedIndex = 2)
        )
        val widgetId = mockSlots.first().id

        val selectedChip = expectedWidget.items[2]

        coEvery { mockRepo.getWidget(extraParam) } returns expectedWidget

        val expectedResult = FeedBrowseUiState.Success(
            mockTitle,
            widgets = uiBuilder.buildWidgets(extraParam, chipUiState = expectedWidget)
        )

        robot.use {
            val uiState = it.recordState {
                viewModel.submitAction(FeedBrowseUiAction.LoadInitialPage)
                viewModel.submitAction(FeedBrowseUiAction.SelectChip(selectedChip, widgetId))
            }
            uiState.assertEqualTo(expectedResult)
        }
    }

    @Test
    fun `action to select chip, given chip that doesn't match with existing chip, then it should return existing widgets`() {
        val expectedWidget = ChipUiState.Placeholder
        val widgetId = mockSlots.first().id

        val selectedChip = uiBuilder.buildChips().first()

        coEvery { mockRepo.getWidget(extraParam) } returns expectedWidget

        val expectedResult = FeedBrowseUiState.Success(
            mockTitle,
            widgets = uiBuilder.buildWidgets(extraParam)
        )

        robot.use {
            val uiState = it.recordState {
                viewModel.submitAction(FeedBrowseUiAction.LoadInitialPage)
                viewModel.submitAction(FeedBrowseUiAction.SelectChip(selectedChip, widgetId))
            }
            uiState.assertEqualTo(expectedResult)
        }
    }
}
