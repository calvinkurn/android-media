package com.tokopedia.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.data.utils.mockDetailResetValue
import com.tokopedia.stories.data.utils.mockGroupResetValue
import com.tokopedia.stories.data.utils.mockInitialDataModel
import com.tokopedia.stories.data.utils.mockInitialDataModelFetchNext
import com.tokopedia.stories.data.utils.mockInitialDataModelFetchPrev
import com.tokopedia.stories.data.utils.mockInitialDataModelFetchPrevAndNext
import com.tokopedia.stories.data.utils.mockMainDataResetValue
import com.tokopedia.stories.robot.StoriesViewModelRobot
import com.tokopedia.stories.util.assertEqualTo
import com.tokopedia.stories.util.assertFalse
import com.tokopedia.stories.util.assertNotEqualTo
import com.tokopedia.stories.util.assertTrue
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.model.StoriesGroupItem
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class StoriesUnitTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val args = StoriesArgsModel(
        authorId = "123",
        authorType = "shop",
        source = "shop-entrypoint",
        sourceId = "1234"
    )
    private val handle: SavedStateHandle = SavedStateHandle()
    private val mockRepository: StoriesRepository = mockk(relaxed = true)

    private fun getStoriesRobot() = StoriesViewModelRobot(
        dispatchers = testDispatcher,
        args = args,
        handle = handle,
        repository = mockRepository,
    )

    @Test
    fun `when open stories from entry point and success fetch initial data`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)
        val resultGroupItem = expectedData.groupItems[selectedGroup]
        val resultDetailItems = resultGroupItem.detail.detailItems[selectedDetail]

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            val state = robot.recordState {
                robot.entryPointTestCase(selectedGroup)

                val actualGroup = robot.getViewModel().mGroup
                val actualDetail = robot.getViewModel().mDetail

                val expectedGroup = resultGroupItem.mockGroupResetValue(actualGroup.detail.detailItems)
                val expectedDetail = resultDetailItems.mockDetailResetValue(actualDetail)

                actualGroup.assertEqualTo(expectedGroup)
                actualDetail.assertEqualTo(expectedDetail)
            }

            val actualMainData = expectedData.mockMainDataResetValue(state.storiesMainData)
            state.storiesMainData.assertEqualTo(actualMainData)
        }
    }

    @Test
    fun `when open stories from entry point and success fetch initial data but data is empty`() {
        coEvery { mockRepository.getStoriesInitialData(any()) } returns StoriesUiModel()

        getStoriesRobot().use { robot ->
            val state = robot.recordStateAndEvents {
                robot.initialDataTestCase()

                val actualGroup = robot.getViewModel().mGroup
                val actualDetail = robot.getViewModel().mDetail

                actualGroup.assertEqualTo(StoriesGroupItem())
                actualDetail.assertEqualTo(StoriesDetailItem())
            }

            state.first.storiesMainData.assertEqualTo(StoriesUiModel())
            state.second.last().assertEqualTo(StoriesUiEvent.EmptyGroupPage)
        }
    }

    @Test
    fun `when open stories from entry point and success fetch initial data but data group is invalid`() {
        val selectedGroup = 5
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            robot.entryPointTestCase(selectedGroup)

            val actualGroup = robot.getViewModel().mGroup
            actualGroup.assertEqualTo(StoriesGroupItem())
        }
    }

    @Test
    fun `when open stories from entry point and success fetch initial data but data detail is invalid 1`() {
        val selectedGroup = -1
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            robot.entryPointTestCase(selectedGroup)

            val actualGroup = robot.getViewModel().mDetail
            actualGroup.assertEqualTo(StoriesDetailItem())
        }
    }

    @Test
    fun `when open stories from entry point and success fetch initial data but data detail is invalid 2`() {
        val selectedGroup = 0
        val selectedDetail = -1
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            robot.entryPointTestCase(selectedGroup)

            val actualGroup = robot.getViewModel().mDetail
            actualGroup.assertEqualTo(StoriesDetailItem())
        }
    }

    @Test
    fun `when open stories from entry point and success fetch initial data but data detail is invalid 3`() {
        val selectedGroup = 3
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData
        coEvery { mockRepository.getStoriesDetailData(any()) } returns expectedData.groupItems[selectedGroup.minus(1)].detail

        getStoriesRobot().use { robot ->
            robot.entryPointTestCase(selectedGroup)

            val actualGroup = robot.getViewModel().mDetail
            actualGroup.assertEqualTo(StoriesDetailItem())
        }
    }

    @Test
    fun `when open stories from entry point and success fetch initial data but data detail is invalid 4`() {
        val selectedGroup = 0
        val selectedDetail = 5
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData
        coEvery { mockRepository.getStoriesDetailData(any()) } returns expectedData.groupItems[selectedGroup].detail

        getStoriesRobot().use { robot ->
            robot.entryPointTestCase(selectedGroup)

            val actualGroup = robot.getViewModel().mDetail
            actualGroup.assertEqualTo(StoriesDetailItem())
        }
    }

    @Test
    fun `when open stories from entry point and fail fetch initial data`() {
        val expectedThrowable = Throwable("fail fetch")

        coEvery { mockRepository.getStoriesInitialData(any()) } throws expectedThrowable

        getStoriesRobot().use { robot ->
            val event = robot.recordEvent {
                robot.initialDataTestCase()
            }

            event.last().assertEqualTo(StoriesUiEvent.ErrorGroupPage(expectedThrowable))
        }
    }

    @Test
    fun `when open stories from entry point and using saved state data`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)
        val resultGroupItem = expectedData.groupItems[selectedGroup]
        val resultDetailItems = resultGroupItem.detail.detailItems[selectedDetail]

        getStoriesRobot().use { robot ->
            val state = robot.recordState {
                robot.entryPointTestCaseUsingSavedState(
                    mainData = expectedData,
                    selectedGroup = selectedGroup,
                    selectedDetail = selectedDetail,
                )

                val actualGroup = robot.getViewModel().mGroup
                val actualDetail = robot.getViewModel().mDetail

                val expectedGroup = resultGroupItem.mockGroupResetValue(actualGroup.detail.detailItems)
                val expectedDetail = resultDetailItems.mockDetailResetValue(actualDetail)

                actualGroup.assertEqualTo(expectedGroup)
                actualDetail.assertEqualTo(expectedDetail)
            }

            val actualMainData = expectedData.mockMainDataResetValue(state.storiesMainData)
            state.storiesMainData.assertEqualTo(actualMainData)
        }
    }

    @Test
    fun `when open stories and success fetch main data`() {
        val selectedGroup = 0
        val expectedData = mockInitialDataModel(isCached = false)

        coEvery {
            mockRepository.getStoriesInitialData(any())
        } returns expectedData

        coEvery {
            mockRepository.getStoriesDetailData(any())
        } returns expectedData.groupItems[selectedGroup].detail

        getStoriesRobot().use { robot ->
            val state = robot.recordState {
                robot.mainDataTestCase(selectedGroup)
            }

            val actualMainData = expectedData.mockMainDataResetValue(state.storiesMainData)
            state.storiesMainData.groupItems[selectedGroup].detail
                .assertEqualTo(
                    actualMainData.groupItems[selectedGroup].detail
                )
        }
    }

    @Test
    fun `when open stories and success fetch main data but empty`() {
        val selectedGroup = 0
        val expectedData = mockInitialDataModel(isCached = false)

        coEvery {
            mockRepository.getStoriesInitialData(any())
        } returns expectedData

        coEvery {
            mockRepository.getStoriesDetailData(any())
        } returns StoriesDetail()

        getStoriesRobot().use { robot ->
            val state = robot.recordStateAndEvents {
                robot.mainDataTestCase(selectedGroup)
            }

            state.first.storiesMainData.groupItems[selectedGroup].detail.assertEqualTo(StoriesDetail())
            state.second.last().assertEqualTo(StoriesUiEvent.EmptyDetailPage)
        }
    }

    @Test
    fun `when open stories and fail fetch main data`() {
        val selectedGroup = 0
        val expectedData = mockInitialDataModel(isCached = false)
        val expectedThrowable = Throwable("fail fetch")

        coEvery {
            mockRepository.getStoriesInitialData(any())
        } returns expectedData

        coEvery {
            mockRepository.getStoriesDetailData(any())
        } throws expectedThrowable

        getStoriesRobot().use { robot ->
            val state = robot.recordStateAndEvents {
                robot.mainDataTestCase(selectedGroup)
            }

            state.first.storiesMainData.groupItems[selectedGroup].detail.assertEqualTo(StoriesDetail())
            state.second.last().assertEqualTo(StoriesUiEvent.ErrorDetailPage(expectedThrowable))
        }
    }

    @Test
    fun `when open stories and success fetch n-1 & n+1 data`() {
        val selectedGroup = 1
        val selectedDetail = 0
        val expectedData = mockInitialDataModelFetchPrevAndNext()
        val resultGroupItem = expectedData.groupItems[selectedGroup]
        val resultDetailItems = resultGroupItem.detail.detailItems[selectedDetail]

        coEvery {
            mockRepository.getStoriesInitialData(any())
        } returns expectedData

        coEvery {
            mockRepository.getStoriesDetailData(any())
        } returns expectedData.groupItems[selectedGroup].detail

        getStoriesRobot().use { robot ->
            robot.entryPointTestCase(selectedGroup)

            val actualGroup = robot.getViewModel().mGroup
            val actualDetail = robot.getViewModel().mDetail

            val expectedGroup = resultGroupItem.mockGroupResetValue(actualGroup.detail.detailItems)
            val expectedDetail = resultDetailItems.mockDetailResetValue(actualDetail)

            actualGroup.assertEqualTo(expectedGroup)
            actualDetail.assertEqualTo(expectedDetail)
        }
    }

    @Test
    fun `when open stories and fail fetch n-1 data`() {
        val selectedGroup = 2
        val selectedDetail = 0
        val expectedData = mockInitialDataModelFetchPrev()
        val expectedThrowable = Throwable("fail")
        val resultGroupItem = expectedData.groupItems[selectedGroup]
        val resultDetailItems = resultGroupItem.detail.detailItems[selectedDetail]

        coEvery {
            mockRepository.getStoriesInitialData(any())
        } returns expectedData

        coEvery {
            mockRepository.getStoriesDetailData(any())
        } throws expectedThrowable

        getStoriesRobot().use { robot ->
            val event = robot.recordEvent {
                robot.entryPointTestCase(selectedGroup)

                val actualGroup = robot.getViewModel().mGroup
                val actualDetail = robot.getViewModel().mDetail

                val expectedGroup = resultGroupItem.mockGroupResetValue(actualGroup.detail.detailItems)
                val expectedDetail = resultDetailItems.mockDetailResetValue(actualDetail)

                actualGroup.assertEqualTo(expectedGroup)
                actualDetail.assertEqualTo(expectedDetail)
            }
            event.contains(StoriesUiEvent.ErrorFetchCaching(expectedThrowable))
        }
    }

    @Test
    fun `when open stories and fail fetch n+1 data`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModelFetchNext()
        val expectedThrowable = Throwable("fail")
        val resultGroupItem = expectedData.groupItems[selectedGroup]
        val resultDetailItems = resultGroupItem.detail.detailItems[selectedDetail]

        coEvery {
            mockRepository.getStoriesInitialData(any())
        } returns expectedData

        coEvery {
            mockRepository.getStoriesDetailData(any())
        } throws expectedThrowable

        getStoriesRobot().use { robot ->
            val event = robot.recordEvent {
                robot.entryPointTestCase(selectedGroup)

                val actualGroup = robot.getViewModel().mGroup
                val actualDetail = robot.getViewModel().mDetail

                val expectedGroup = resultGroupItem.mockGroupResetValue(actualGroup.detail.detailItems)
                val expectedDetail = resultDetailItems.mockDetailResetValue(actualDetail)

                actualGroup.assertEqualTo(expectedGroup)
                actualDetail.assertEqualTo(expectedDetail)
            }
            event.contains(StoriesUiEvent.ErrorFetchCaching(expectedThrowable))
        }
    }

    @Test
    fun `when stories open and success hit stories track activity`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData
        coEvery { mockRepository.setStoriesTrackActivity(any()) } returns true

        getStoriesRobot().use { robot ->
            robot.setTrackActivity(selectedGroup)

            val actualDetail = robot.getViewModel().mDetail
            actualDetail.isSameContent.assertTrue()
        }
    }

    @Test
    fun `when stories open and invalid hit stories track activity`() {
        val selectedGroup = 0
        val selectedDetail = -1
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData
        coEvery { mockRepository.setStoriesTrackActivity(any()) } returns true

        getStoriesRobot().use { robot ->
            robot.setTrackActivity(selectedGroup)

            val actualDetail = robot.getViewModel().mDetail
            actualDetail.isSameContent.assertFalse()
        }
    }

    @Test
    fun `when stories open and fail hit stories track activity`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedThrowable = Throwable("fail")
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData
        coEvery { mockRepository.setStoriesTrackActivity(any()) } throws expectedThrowable

        getStoriesRobot().use { robot ->
            val event = robot.recordEvent {
                robot.setTrackActivity(selectedGroup)

                val actualDetail = robot.getViewModel().mDetail
                actualDetail.isSameContent.assertTrue()
            }
            event.last().assertEqualTo(StoriesUiEvent.ErrorSetTracking(expectedThrowable))
        }
    }

    @Test
    fun `when stories open and user tap next detail to next stories`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            val state = robot.recordState {
                robot.tapNextDetailToNextDetail(selectedGroup)
            }
            state.storiesMainData.selectedGroupPosition
                .assertEqualTo(selectedGroup)
            state.storiesMainData.groupItems[selectedGroup].detail.selectedDetailPosition
                .assertEqualTo(selectedDetail.plus(1))
        }
    }

    @Test
    fun `when stories open and user tap next detail to next stories with invalid group position 1`() {
        val selectedGroup = -1
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup.plus(1), selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            val state = robot.recordState {
                robot.tapNextDetailToNextDetail(selectedGroup)
            }
            state.storiesMainData.selectedGroupPosition
                .assertEqualTo(selectedGroup.plus(1))
            state.storiesMainData.groupItems[selectedGroup.plus(1)].detail.selectedDetailPosition
                .assertEqualTo(selectedDetail)
        }
    }

    @Test
    fun `when stories open and user tap next detail to next stories with invalid group position 2`() {
        val selectedGroup = 5
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(0, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            val state = robot.recordState {
                robot.tapNextDetailToNextDetail(selectedGroup)
            }
            state.storiesMainData.selectedGroupPosition
                .assertEqualTo(0)
            state.storiesMainData.groupItems[0].detail.selectedDetailPosition
                .assertEqualTo(selectedDetail)
        }
    }

    @Test
    fun `when stories open and user tap next detail to next group`() {
        val selectedGroup = 0
        val selectedDetail = 2
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            val state = robot.recordState {
                robot.tapNextDetailToNextGroup(selectedGroup)
            }
            state.storiesMainData.selectedGroupPosition
                .assertEqualTo(selectedGroup.plus(1))
            state.storiesMainData.groupItems[selectedGroup].detail.selectedDetailPosition
                .assertEqualTo(selectedDetail.plus(0))
        }
    }

    @Test
    fun `when stories open and user tap next detail to close room`() {
        val selectedGroup = 2
        val selectedDetail = 2
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            val event = robot.recordEvent {
                robot.tapNextDetailToCloseRoom(selectedGroup)
            }
            event.last().assertEqualTo(StoriesUiEvent.FinishedAllStories)
        }
    }

    @Test
    fun `when stories open and user tap prev detail to prev stories`() {
        val selectedGroup = 0
        val selectedDetail = 2
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            val state = robot.recordState {
                robot.tapPreviousDetailToPrevDetail(selectedGroup)
            }
            state.storiesMainData.selectedGroupPosition
                .assertEqualTo(selectedGroup)
            state.storiesMainData.groupItems[selectedGroup].detail.selectedDetailPosition
                .assertEqualTo(selectedDetail.minus(1))
        }
    }

    @Test
    fun `when stories open and user tap prev detail to prev group`() {
        val selectedGroup = 1
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            val state = robot.recordState {
                robot.tapPrevDetailToPrevGroup(selectedGroup)
            }
            state.storiesMainData.selectedGroupPosition
                .assertEqualTo(selectedGroup.minus(1))
            state.storiesMainData.groupItems[selectedGroup].detail.selectedDetailPosition
                .assertEqualTo(selectedDetail)
        }
    }

    @Test
    fun `when stories open and user tap prev detail to reset timer`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            robot.tapPrevDetailToResetTimer(selectedGroup)

            val reset = robot.getViewModel().mDetail.resetValue
            reset.assertNotEqualTo(expectedData.groupItems[selectedGroup].detail.detailItems[selectedDetail].resetValue)
        }
    }

    @Test
    fun `when stories open and user tap pause`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            robot.tapPauseStories(selectedGroup)

            val actualEvent = robot.getViewModel().mDetail.event
            actualEvent.assertEqualTo(StoriesDetailItemUiEvent.PAUSE)
        }
    }

    @Test
    fun `when stories open and user tap resume`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            robot.tapResumeStories(selectedGroup)

            val actualEvent = robot.getViewModel().mDetail.event
            actualEvent.assertEqualTo(StoriesDetailItemUiEvent.RESUME)
        }
    }

    @Test
    fun `when stories open and collect impression group`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            robot.collectImpressionGroup(selectedGroup, expectedData.groupHeader[selectedGroup])

            val actualImpression = robot.getViewModel().impressedGroupHeader.first()
            actualImpression.assertEqualTo(expectedData.groupHeader.first())
        }
    }

    @Test
    fun `when stories open and collect impression group duplicate`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            robot.collectImpressionGroupDuplicate(selectedGroup, expectedData.groupHeader[selectedGroup])

            val actualImpression = robot.getViewModel().impressedGroupHeader.first()
            actualImpression.assertEqualTo(expectedData.groupHeader.first())
        }
    }

}
