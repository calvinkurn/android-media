package com.tokopedia.feedplus.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiAction
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import com.tokopedia.feedplus.data.FeedBrowseModelBuilder
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
 * Created by meyta.taliti on 08/09/23.
 */
class FeedBrowseViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val mockRepo: FeedBrowseRepository = mockk(relaxed = true)

    private val uiBuilder = FeedBrowseModelBuilder()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `action to load initial page, when it success then it should return widgets`() {
        val robot = createFeedBrowseViewModelRobot(mockRepo)

        val title = uiBuilder.buildTitle()
        val extraParam = uiBuilder.buildExtraParam()
        val mockSlots = uiBuilder.buildWidgets(extraParam = extraParam)
        val expectedWidget = uiBuilder.buildWidgetChannelItemModel()

        val expectedResult = FeedBrowseUiState.Success(
            title,
            widgets = uiBuilder.buildWidgets(extraParam, channelUiState = expectedWidget)
        )

        coEvery { mockRepo.getTitle() } returns title
        coEvery { mockRepo.getSlots() } returns mockSlots
        coEvery { mockRepo.getWidget(extraParam) } returns expectedWidget

        robot.use {
            val uiState = it.recordState {
                viewModel.submitAction(FeedBrowseUiAction.LoadInitialPage)
            }
            uiState.assertEqualTo(expectedResult)
        }
    }

    @Test
    fun `action to load initial page, when get slots throw error, then it should return error`() {
        val robot = createFeedBrowseViewModelRobot(mockRepo)

        val expectedError = IllegalStateException()

        coEvery { mockRepo.getTitle() } returns ""
        coEvery { mockRepo.getSlots() } throws expectedError

        val expectedResult = FeedBrowseUiState.Error(expectedError)

        robot.use {
            val uiState = it.recordState {
                viewModel.submitAction(FeedBrowseUiAction.LoadInitialPage)
            }
            uiState.assertEqualTo(expectedResult)
        }
    }
}
