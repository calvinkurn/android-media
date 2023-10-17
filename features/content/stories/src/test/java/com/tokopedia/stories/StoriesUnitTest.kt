package com.tokopedia.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.types.ResultState
import androidx.lifecycle.SavedStateHandle
import com.tokopedia.stories.data.mock.mockDetailResetValue
import com.tokopedia.stories.data.mock.mockGroupResetValue
import com.tokopedia.stories.data.mock.mockInitialDataModel
import com.tokopedia.stories.data.mock.mockInitialDataModelFetchNext
import com.tokopedia.stories.data.mock.mockInitialDataModelFetchPrev
import com.tokopedia.stories.data.mock.mockInitialDataModelFetchPrevAndNext
import com.tokopedia.stories.data.mock.mockMainDataResetValue
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.data.utils.mockContentTaggedProductUiModel
import com.tokopedia.stories.data.utils.mockDetailResetValue
import com.tokopedia.stories.data.utils.mockGroupResetValue
import com.tokopedia.stories.data.utils.mockInitialDataModel
import com.tokopedia.stories.data.utils.mockInitialDataModelFetchNext
import com.tokopedia.stories.data.utils.mockInitialDataModelFetchPrev
import com.tokopedia.stories.data.utils.mockInitialDataModelFetchPrevAndNext
import com.tokopedia.stories.data.utils.mockInitialDataModelForDeleteStories
import com.tokopedia.stories.data.utils.mockMainDataResetValue
import com.tokopedia.stories.robot.StoriesViewModelRobot
import com.tokopedia.stories.util.assertEqualTo
import com.tokopedia.stories.util.assertFalse
import com.tokopedia.stories.util.assertNotEqualTo
import com.tokopedia.stories.util.assertTrue
import com.tokopedia.stories.util.assertType
import com.tokopedia.stories.utils.StoriesPreference
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.model.StoriesGroupItem
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.viewmodel.action.StoriesProductAction
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.stories.view.viewmodel.state.BottomSheetType
import com.tokopedia.stories.view.viewmodel.state.ProductBottomSheetUiState
import com.tokopedia.stories.view.viewmodel.state.isAnyShown
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
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
    private val mockRepository: StoriesRepository = mockk(relaxed = true)
    private val mockSharedPref: StoriesPreference = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    private fun getStoriesRobot() = StoriesViewModelRobot(
        dispatchers = testDispatcher,
        args = args,
        repository = mockRepository,
        userSession = mockUserSession,
    )

    @Test
    fun `when open stories from entry point and success fetch initial data`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)
        val resultGroupItem = expectedData.groupItems[selectedGroup]
        val resultDetailItems = resultGroupItem.detail.detailItems[selectedDetail]

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData
        coEvery { mockUserSession.userId } returns "123"

        getStoriesRobot().use { robot ->
            val state = robot.recordState {
                robot.entryPointTestCase(selectedGroup)

                val actualGroup = robot.getViewModel().mGroup
                val actualDetail = robot.getViewModel().mDetail
                val userId = robot.getViewModel().userId

                val expectedGroup =
                    resultGroupItem.mockGroupResetValue(actualGroup.detail.detailItems)
                val expectedDetail = resultDetailItems.mockDetailResetValue(actualDetail)

                actualGroup.assertEqualTo(expectedGroup)
                actualDetail.assertEqualTo(expectedDetail)
                userId.assertEqualTo("123")
            }

            val actualMainData = expectedData.mockMainDataResetValue(state.storiesMainData)
            state.storiesMainData.assertEqualTo(actualMainData)
        }
    }

    @Test
    fun `when open stories from entry point and success fetch initial data but data is empty`() {
        coEvery { mockRepository.getStoriesInitialData(any()) } returns StoriesUiModel()
        coEvery { mockUserSession.userId } returns ""

        getStoriesRobot().use { robot ->
            val state = robot.recordStateAndEvents {
                robot.initialDataTestCase()

                val actualGroup = robot.getViewModel().mGroup
                val actualDetail = robot.getViewModel().mDetail
                val userId = robot.getViewModel().userId

                actualGroup.assertEqualTo(StoriesGroupItem())
                actualDetail.assertEqualTo(StoriesDetailItem())
                userId.assertEqualTo("0")
            }

            state.first.storiesMainData.assertEqualTo(StoriesUiModel())
            state.second.last().assertEqualTo(StoriesUiEvent.EmptyGroupPage)
        }
    }

    @Test
    fun `when open stories from entry point and success fetch initial data but data group is index out of bound`() {
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
    fun `when open stories from entry point and success fetch initial data but data detail is index out of bound 1`() {
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
    fun `when open stories from entry point and success fetch initial data but data detail is index out of bound 2`() {
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
    fun `when open stories from entry point and success fetch initial data but data detail is index out of bound 3`() {
        val selectedGroup = 3
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData
        coEvery { mockRepository.getStoriesDetailData(any()) } returns expectedData.groupItems[selectedGroup.minus(
            1
        )].detail

        getStoriesRobot().use { robot ->
            robot.entryPointTestCase(selectedGroup)

            val actualGroup = robot.getViewModel().mDetail
            actualGroup.assertEqualTo(StoriesDetailItem())
        }
    }

    @Test
    fun `when open stories from entry point and success fetch initial data but data detail is index out of bound 4`() {
        val selectedGroup = 0
        val selectedDetail = 10
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData
        coEvery { mockRepository.getStoriesDetailData(any()) } returns expectedData.groupItems[selectedGroup].detail

        getStoriesRobot().use { robot ->
            robot.entryPointTestCase(selectedGroup)

            val actualGroup = robot.getViewModel().mDetail
            val expectedDetail = expectedData.groupItems[selectedGroup].detail.detailItems.last()
                .mockDetailResetValue(actualGroup)
            actualGroup.assertEqualTo(expectedDetail)
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

            event.last().assertType<StoriesUiEvent.ErrorGroupPage> {}
            (event.last() as StoriesUiEvent.ErrorGroupPage).onClick()
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

        every {
            mockSharedPref.isVisited()
        } returns true

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

        every { mockSharedPref.isVisited() } returns true

        getStoriesRobot().use { robot ->
            val state = robot.recordStateAndEvents {
                robot.mainDataTestCase(selectedGroup)
            }

            state.first.storiesMainData.groupItems[selectedGroup].detail.assertEqualTo(StoriesDetail())
            state.second.last().assertType<StoriesUiEvent.ErrorDetailPage> { }
            (state.second.last() as StoriesUiEvent.ErrorDetailPage).onClick()
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

                val expectedGroup =
                    resultGroupItem.mockGroupResetValue(actualGroup.detail.detailItems)
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

                val expectedGroup =
                    resultGroupItem.mockGroupResetValue(actualGroup.detail.detailItems)
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
            actualDetail.isContentLoaded.assertTrue()
        }
    }

    @Test
    fun `when stories open and index out of bound hit stories track activity`() {
        val selectedGroup = 0
        val selectedDetail = -1
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData
        coEvery { mockRepository.setStoriesTrackActivity(any()) } returns true

        getStoriesRobot().use { robot ->
            robot.setTrackActivity(selectedGroup)

            val actualDetail = robot.getViewModel().mDetail
            actualDetail.isContentLoaded.assertFalse()
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
                actualDetail.isContentLoaded.assertTrue()
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
    fun `when stories open and user tap next detail to next stories with index out of bound group position 1`() {
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
    fun `when stories open and user tap next detail to next stories with index out of bound group position 2`() {
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
    fun `when stories open and user tap resume but content not loaded resume`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            robot.tapResumeStoriesButContentNotLoaded(selectedGroup)

            val actualEvent = robot.getViewModel().mDetail.event
            actualEvent.assertEqualTo(StoriesDetailItemUiEvent.PAUSE)
        }
    }

    @Test
    fun `when stories open and user tap resume but page is not selected resume`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            robot.tapResumeStoriesButPageIsNotSelected(selectedGroup)

            val actualEvent = robot.getViewModel().mDetail.event
            actualEvent.assertEqualTo(StoriesDetailItemUiEvent.PAUSE)
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
            robot.collectImpressionGroupDuplicate(
                selectedGroup,
                expectedData.groupHeader[selectedGroup]
            )

            val actualImpression = robot.getViewModel().impressedGroupHeader.first()
            actualImpression.assertEqualTo(expectedData.groupHeader.first())
        }
    }

    @Test
    fun `when stories deleted and stories should be deleted and move to next stories`() {
        val selectedGroup = 0
        val selectedDetail = 0
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            val event = robot.recordEvent {
                robot.deleteStories()
            }

            val actualSize = robot.getViewModel().mGroup.detail.detailItems.size
            actualSize.assertEqualTo(
                expectedData.groupItems[selectedGroup].detail.detailItems.size.minus(
                    1
                )
            )

            event.contains(StoriesUiEvent.ShowDeleteDialog)
        }
    }

    @Test
    fun `when stories deleted and stories should be deleted and move to next group`() {
        val selectedGroup = 0
        val selectedDetail = 2
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            robot.deleteStories()

            val actualSize = robot.getViewModel().mGroup.detail.detailItems.size
            actualSize.assertEqualTo(
                expectedData.groupItems[selectedGroup].detail.detailItems.size.minus(
                    1
                )
            )
        }
    }

    @Test
    fun `when stories deleted and stories should be deleted and close stories room`() {
        val selectedGroup = 2
        val selectedDetail = 2
        val expectedData = mockInitialDataModel(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            robot.deleteStories(selectedGroup)

            val actualSize = robot.getViewModel().mGroup.detail.detailItems.size
            actualSize.assertEqualTo(
                expectedData.groupItems[selectedGroup].detail.detailItems.size.minus(
                    1
                )
            )
        }
    }

    @Test
    fun `when stories deleted and stories should be deleted until empty`() {
        val selectedGroup = 2
        val selectedDetail = 2
        val expectedData = mockInitialDataModelForDeleteStories(selectedGroup, selectedDetail)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData

        getStoriesRobot().use { robot ->
            robot.deleteStoriesUntilEmpty(selectedGroup)

            val actualSize = robot.getViewModel().mGroup.detail.detailItems.size
            actualSize.assertEqualTo(0)
        }
    }

    @Test
    fun `when open kebab bottom sheet and close`() {
        getStoriesRobot().use { robot ->
            val stateEventOpen = robot.recordStateAndEvents {
                robot.openKebabBottomSheet()
            }
            robot.getViewModel().isAnyBottomSheetShown.assertTrue()
            stateEventOpen.second.last().assertEqualTo(StoriesUiEvent.OpenKebab)
            robot.getBottomSheetState().isAnyShown.assertTrue()
            robot.getBottomSheetState().mapValues {
                if (it.key == BottomSheetType.Kebab) it.value.assertTrue()
                else it.value.assertFalse()
            }
            robot.recordState {
                robot.closeBottomSheet(BottomSheetType.Kebab)
            }
            robot.getBottomSheetState().isAnyShown.assertFalse()
        }
    }

    @Test
    fun `when open product bottom sheet and close`() {
    val expectedData = mockInitialDataModel(productCount = 5)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData
        coEvery { mockRepository.getStoriesDetailData(any()) } returns expectedData.groupItems.first().detail

        getStoriesRobot().use { robot ->
            val stateEventOpen = robot.recordStateAndEvents {
                robot.openProductBottomSheet()
            }
            robot.getViewModel().isAnyBottomSheetShown.assertTrue()
            stateEventOpen.second.last().assertEqualTo(StoriesUiEvent.OpenProduct)
            robot.getBottomSheetState().mapValues {
                if (it.key == BottomSheetType.Product) it.value.assertTrue()
                else it.value.assertFalse()
            }
            robot.getBottomSheetState().isAnyShown.assertTrue()

            val stateClose = robot.recordState {
                robot.closeBottomSheet(BottomSheetType.Product)
            }
            robot.getViewModel().isAnyBottomSheetShown.assertFalse()
            robot.getBottomSheetState().mapValues {
                if (it.key == BottomSheetType.Product) it.value.assertFalse()
                else it.value.assertFalse()
            }
            robot.getBottomSheetState().isAnyShown.assertFalse()
        }
    }

    @Test
    fun `when open product bottom sheet and unable to open 1`() {
        val expectedData = mockInitialDataModel(productCount = 5)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData
        coEvery { mockRepository.getStoriesDetailData(any()) } returns expectedData.groupItems.first().detail

        getStoriesRobot().use { robot ->
            val stateEventOpen = robot.recordStateAndEvents {
                robot.openKebabBottomSheet()
                robot.openProductBottomSheet()
            }
            robot.getViewModel().isAnyBottomSheetShown.assertTrue()
            stateEventOpen.second.contains(StoriesUiEvent.OpenKebab)
            robot.getBottomSheetState().mapValues {
                if (it.key == BottomSheetType.Kebab) it.value.assertTrue()
                else it.value.assertFalse()
            }
            robot.getBottomSheetState().isAnyShown.assertTrue()
        }
    }

    @Test
    fun `when open product bottom sheet and unable to open 2`() {
    val expectedData = mockInitialDataModel(isProductCountEmpty = true)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData
        coEvery { mockRepository.getStoriesDetailData(any()) } returns expectedData.groupItems.first().detail

        getStoriesRobot().use { robot ->
            val state = robot.recordState {
                robot.openProductBottomSheet()
            }
            robot.getViewModel().isProductAvailable.assertFalse()
            robot.getViewModel().isAnyBottomSheetShown.assertFalse()
            robot.getBottomSheetState().mapValues {
                if (it.key == BottomSheetType.Product) it.value.assertFalse()
                else it.value.assertFalse()
            }
            robot.getBottomSheetState().isAnyShown.assertFalse()

        }
    }

    @Test
    fun `when open product bottom sheet and unable to open 3`() {
    val expectedData = mockInitialDataModel(isProductCountEmpty = true)

        coEvery { mockRepository.getStoriesInitialData(any()) } returns expectedData
        coEvery { mockRepository.getStoriesDetailData(any()) } returns expectedData.groupItems.first().detail

        getStoriesRobot().use { robot ->
            robot.recordState {
                robot.openProductBottomSheet()
            }
            robot.getViewModel().isProductAvailable.assertFalse()
            robot.getViewModel().isAnyBottomSheetShown.assertFalse()
            robot.getBottomSheetState().mapValues {
                if (it.key == BottomSheetType.Product) it.value.assertFalse()
                else it.value.assertFalse()
            }
            robot.getBottomSheetState().isAnyShown.assertFalse()
        }
    }

    @Test
    fun `when open share bottom sheet and close`() {
        val mockSharingData = StoriesDetailItem.Sharing.Empty

        getStoriesRobot().use { robot ->
            val stateEventOpen = robot.recordStateAndEvents {
                robot.openShareBottomSheet()
            }
            stateEventOpen.second.last().assertEqualTo(StoriesUiEvent.TapSharing(mockSharingData))
            robot.getBottomSheetState().mapValues {
                if (it.key == BottomSheetType.Sharing) it.value.assertTrue()
                else it.value.assertFalse()
            }
            robot.getBottomSheetState().isAnyShown.assertTrue()

            robot.recordState {
                robot.closeBottomSheet(BottomSheetType.Sharing)
            }
            robot.getBottomSheetState().mapValues {
                if (it.key == BottomSheetType.Sharing) it.value.assertFalse()
                else it.value.assertFalse()
            }
            robot.getBottomSheetState().isAnyShown.assertFalse()
        }
    }

    @Test
    fun `when open variant bottom sheet and close`() {
        val mockSharingData = mockContentTaggedProductUiModel()

        getStoriesRobot().use { robot ->
            val stateEventOpen = robot.recordStateAndEvents {
                robot.openVariantBottomSheet(mockSharingData)
            }
            stateEventOpen.second.last()
                .assertEqualTo(StoriesUiEvent.ShowVariantSheet(mockSharingData))
            robot.getBottomSheetState().mapValues {
                if (it.key == BottomSheetType.GVBS) it.value.assertTrue()
                else it.value.assertFalse()
            }
            robot.getBottomSheetState().isAnyShown.assertTrue()

            robot.recordState {
                robot.closeBottomSheet(BottomSheetType.GVBS)
            }
            robot.getBottomSheetState().mapValues {
                if (it.key == BottomSheetType.GVBS) it.value.assertFalse()
                else it.value.assertFalse()
            }
            robot.getBottomSheetState().isAnyShown.assertFalse()
        }
    }

    @Test
    fun `when get product and success`() {
        val mockProduct = ProductBottomSheetUiState.Empty

        coEvery { mockRepository.getStoriesProducts(any(), any(), any()) } returns mockProduct

        getStoriesRobot().use { robot ->
            val state = robot.recordState {
                robot.testGetProducts()
            }

            state.productSheet.resultState.assertEqualTo(ResultState.Loading)
            state.productSheet.assertEqualTo(mockProduct)
        }
    }

    @Test
    fun `when get product and fail`() {
        val mockThrows = Throwable("any fail")

        coEvery { mockRepository.getStoriesProducts(any(), any(), any()) } throws mockThrows

        getStoriesRobot().use { robot ->
            val state = robot.recordState {
                robot.testGetProducts()
            }

            state.productSheet.resultState.assertEqualTo(ResultState.Fail(mockThrows))
        }
    }

    @Test
    fun `when product action and success atc`() {
        val action = StoriesProductAction.Atc
        val product = mockContentTaggedProductUiModel()

        coEvery { mockRepository.addToCart(any(), any(), any(), any()) } returns true
        coEvery { mockUserSession.isLoggedIn } returns true

        getStoriesRobot().use { robot ->
            val event = robot.recordEvent {
                robot.testProductAction(action, product)
            }

            event.last().assertEqualTo(
                StoriesUiEvent.ProductSuccessEvent(
                    action,
                    R.string.stories_product_atc_success
                )
            )
        }
    }

    @Test
    fun `when product action and success buy`() {
        val action = StoriesProductAction.Buy
        val product = mockContentTaggedProductUiModel()

        coEvery { mockRepository.addToCart(any(), any(), any(), any()) } returns true
        coEvery { mockUserSession.isLoggedIn } returns true

        getStoriesRobot().use { robot ->
            val event = robot.recordEvent {
                robot.testProductAction(action, product)
            }

            event.last().assertEqualTo(StoriesUiEvent.NavigateEvent(ApplinkConst.CART))
        }
    }

    @Test
    fun `when product action and fail`() {
        val action = StoriesProductAction.Buy
        val product = mockContentTaggedProductUiModel()

        coEvery { mockRepository.addToCart(any(), any(), any(), any()) } returns false
        coEvery { mockUserSession.isLoggedIn } returns true

        getStoriesRobot().use { robot ->
            val event = robot.recordEvent {
                robot.testProductAction(action, product)
            }

            event.last().assertType<StoriesUiEvent.ShowErrorEvent> {}
        }
    }

    @Test
    fun `when product action and not login`() {
        val action = StoriesProductAction.Atc
        val product = mockContentTaggedProductUiModel()

        coEvery { mockRepository.addToCart(any(), any(), any(), any()) } returns true
        coEvery { mockUserSession.isLoggedIn } returns false

        getStoriesRobot().use { robot ->
            val event = robot.recordEvent {
                robot.testProductAction(action, product)
            }

            event.last().assertType<StoriesUiEvent.Login> {}
            (event.last() as StoriesUiEvent.Login).onLoggedIn()
        }
    }

    @Test
    fun `when navigate`() {
        val appLink = "tokopedia://stories/shop/1234"
        getStoriesRobot().use { robot ->
            val event = robot.recordEvent {
                robot.testNav(appLink)
            }

            event.last().assertEqualTo(StoriesUiEvent.NavigateEvent(appLink))
        }
    }
}
