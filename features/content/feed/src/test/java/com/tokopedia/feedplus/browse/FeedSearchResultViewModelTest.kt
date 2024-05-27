package com.tokopedia.feedplus.browse

import com.tokopedia.content.test.util.assertEmpty
import com.tokopedia.content.test.util.assertEqualTo
import com.tokopedia.content.test.util.assertFalse
import com.tokopedia.content.test.util.assertTrue
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.presentation.FeedSearchResultViewModel
import com.tokopedia.feedplus.browse.presentation.model.action.FeedSearchResultAction
import com.tokopedia.feedplus.browse.presentation.model.exception.RestrictedKeywordException
import com.tokopedia.feedplus.browse.presentation.model.srp.FeedSearchResultContent
import com.tokopedia.feedplus.browse.presentation.model.state.FeedSearchResultPageState
import com.tokopedia.feedplus.data.FeedBrowseModelBuilder
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Jonathan Darwin on 28 March 2024
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FeedSearchResultViewModelTest {

    @get:Rule
    val coroutineRule = UnconfinedTestRule()

    private val modelBuilder = FeedBrowseModelBuilder()
    private val mockRepo: FeedBrowseRepository = mockk(relaxed = true)

    private val mockSearchKeyword = "pokemon"
    private val mockNextCursor = "123"
    private val mockTitle = "Konten yang kamu cari"
    private val mockChannelList = modelBuilder.buildChannelList()
    private val mockChannelConfig = PlayWidgetConfigUiModel.Empty

    private val mockSearchResultWithCursor = ContentSlotModel.ChannelBlock(
        title = mockTitle,
        channels = mockChannelList,
        config = mockChannelConfig,
        nextCursor = mockNextCursor,
    )

    private val mockSearchResultWithoutData = ContentSlotModel.ChannelBlock(
        title = mockTitle,
        channels = emptyList(),
        config = mockChannelConfig,
        nextCursor = "",
    )

    private val mockSearchResultTypeNotHandled = ContentSlotModel.NoData(nextCursor = "")

    private val mockSearchResultTabsMenu = ContentSlotModel.TabMenus(menus = emptyList())

    @Test
    fun feedLocalSearchSRP_loadResult_success() = runTestUnconfined {
        /** Prepare */
        coEvery { mockRepo.getWidgetContentSlot(any()) } returns mockSearchResultWithCursor

        val viewModel = getViewModel()

        /** Test */
        viewModel.submitAction(FeedSearchResultAction.LoadResult)

        /** Verify */
        viewModel.uiState.value.apply {
            searchKeyword.assertEqualTo(mockSearchKeyword)
            pageState.assertEqualTo(FeedSearchResultPageState.SUCCESS)
            hasNextPage.assertTrue()
            contents.size.assertEqualTo(mockChannelList.size + 1) /** + 1 for title */
            contents.forEachIndexed { index, item ->
                if (index == 0) {
                    item.assertEqualTo(FeedSearchResultContent.Title(mockTitle))
                } else {
                    item.assertEqualTo(FeedSearchResultContent.Channel(mockChannelList[index - 1], mockChannelConfig))
                }
            }
        }
    }

    @Test
    fun feedLocalSearchSRP_loadResult_noData() = runTestUnconfined {
        /** Prepare */
        coEvery { mockRepo.getWidgetContentSlot(any()) } returns mockSearchResultWithoutData

        val viewModel = getViewModel()

        /** Test */
        viewModel.submitAction(FeedSearchResultAction.LoadResult)

        /** Verify */
        viewModel.uiState.value.apply {
            searchKeyword.assertEqualTo(mockSearchKeyword)
            pageState.assertEqualTo(FeedSearchResultPageState.NOT_FOUND)
            hasNextPage.assertFalse()
            contents.assertEmpty()
        }
    }

    @Test
    fun feedLocalSearchSRP_loadResult_dataNotHandled() = runTestUnconfined {
        /** Prepare */
        coEvery { mockRepo.getWidgetContentSlot(any()) } returns mockSearchResultTypeNotHandled

        val viewModel = getViewModel()

        /** Test */
        viewModel.submitAction(FeedSearchResultAction.LoadResult)

        /** Verify */
        viewModel.uiState.value.apply {
            searchKeyword.assertEqualTo(mockSearchKeyword)
            pageState.assertEqualTo(FeedSearchResultPageState.NOT_FOUND)
            hasNextPage.assertFalse()
        }
    }

    @Test
    fun feedLocalSearchSRP_loadResult_tabMenus() = runTestUnconfined {
        /** Prepare */
        coEvery { mockRepo.getWidgetContentSlot(any()) } returns mockSearchResultTabsMenu

        val viewModel = getViewModel()

        /** Test */
        viewModel.submitAction(FeedSearchResultAction.LoadResult)

        /** Verify */
        viewModel.uiState.value.apply {
            searchKeyword.assertEqualTo(mockSearchKeyword)
            pageState.assertEqualTo(FeedSearchResultPageState.INTERNAL_ERROR)
        }
    }

    @Test
    fun feedLocalSearchSRP_loadResult_loadMore_success() = runTestUnconfined {
        /** Prepare */
        coEvery { mockRepo.getWidgetContentSlot(any()) } returns mockSearchResultWithCursor

        val viewModel = getViewModel()

        /** Test */
        viewModel.submitAction(FeedSearchResultAction.LoadResult)
        viewModel.submitAction(FeedSearchResultAction.LoadResult)

        /** Verify */
        viewModel.uiState.value.apply {
            searchKeyword.assertEqualTo(mockSearchKeyword)
            pageState.assertEqualTo(FeedSearchResultPageState.SUCCESS)
            hasNextPage.assertTrue()
            contents.size.assertEqualTo((mockChannelList.size * 2) + 1) /** + 1 for title */
            contents.forEachIndexed { index, item ->
                if (index == 0) {
                    item.assertEqualTo(FeedSearchResultContent.Title(mockTitle))
                } else {
                    val mockIndex = (index - 1) % mockChannelList.size
                    item.assertEqualTo(FeedSearchResultContent.Channel(mockChannelList[mockIndex], mockChannelConfig))
                }
            }
        }
    }

    @Test
    fun feedLocalSearchSRP_loadResult_loadMore_noMorePage() = runTestUnconfined {
        /** Prepare */
        val viewModel = getViewModel()

        /** Test */
        coEvery { mockRepo.getWidgetContentSlot(any()) } returns mockSearchResultWithCursor
        viewModel.submitAction(FeedSearchResultAction.LoadResult)
        coEvery { mockRepo.getWidgetContentSlot(any()) } returns mockSearchResultWithoutData
        viewModel.submitAction(FeedSearchResultAction.LoadResult)
        viewModel.submitAction(FeedSearchResultAction.LoadResult)

        /** Verify */
        viewModel.uiState.value.apply {
            coVerify(exactly = 2) { mockRepo.getWidgetContentSlot(any()) }
            searchKeyword.assertEqualTo(mockSearchKeyword)
            pageState.assertEqualTo(FeedSearchResultPageState.SUCCESS)
            hasNextPage.assertFalse()
            contents.size.assertEqualTo(mockChannelList.size + 1) /** + 1 for title */
            contents.forEachIndexed { index, item ->
                if (index == 0) {
                    item.assertEqualTo(FeedSearchResultContent.Title(mockTitle))
                } else {
                    item.assertEqualTo(FeedSearchResultContent.Channel(mockChannelList[index - 1], mockChannelConfig))
                }
            }
        }
    }

    @Test
    fun feedLocalSearchSRP_loadResult_loadMore_dataNotHandled() = runTestUnconfined {
        /** Prepare */
        val viewModel = getViewModel()

        /** Test */
        coEvery { mockRepo.getWidgetContentSlot(any()) } returns mockSearchResultWithCursor
        viewModel.submitAction(FeedSearchResultAction.LoadResult)
        coEvery { mockRepo.getWidgetContentSlot(any()) } returns mockSearchResultTypeNotHandled
        viewModel.submitAction(FeedSearchResultAction.LoadResult)

        /** Verify */
        viewModel.uiState.value.apply {
            coVerify(exactly = 2) { mockRepo.getWidgetContentSlot(any()) }
            searchKeyword.assertEqualTo(mockSearchKeyword)
            pageState.assertEqualTo(FeedSearchResultPageState.SUCCESS)
            hasNextPage.assertFalse()
            contents.size.assertEqualTo(mockChannelList.size + 1) /** + 1 for title */
            contents.forEachIndexed { index, item ->
                if (index == 0) {
                    item.assertEqualTo(FeedSearchResultContent.Title(mockTitle))
                } else {
                    item.assertEqualTo(FeedSearchResultContent.Channel(mockChannelList[index - 1], mockChannelConfig))
                }
            }
        }
    }

    @Test
    fun feedLocalSearchSRP_loadResult_error() = runTestUnconfined {
        /** Prepare */
        coEvery { mockRepo.getWidgetContentSlot(any()) } throws Exception()

        val viewModel = getViewModel()

        /** Test */
        viewModel.submitAction(FeedSearchResultAction.LoadResult)

        /** Verify */
        viewModel.uiState.value.apply {
            searchKeyword.assertEqualTo(mockSearchKeyword)
            pageState.assertEqualTo(FeedSearchResultPageState.INTERNAL_ERROR)
            hasNextPage.assertTrue()
        }
    }

    @Test
    fun feedLocalSearchSRP_loadResult_restricted() = runTestUnconfined {
        /** Prepare */
        coEvery { mockRepo.getWidgetContentSlot(any()) } throws RestrictedKeywordException()

        val viewModel = getViewModel()

        /** Test */
        viewModel.submitAction(FeedSearchResultAction.LoadResult)

        /** Verify */
        viewModel.uiState.value.apply {
            searchKeyword.assertEqualTo(mockSearchKeyword)
            pageState.assertEqualTo(FeedSearchResultPageState.RESTRICTED)
            hasNextPage.assertTrue()
        }
    }

    private fun TestScope.getViewModel(): FeedSearchResultViewModel {
        val viewModel = FeedSearchResultViewModel(
            searchKeyword = mockSearchKeyword,
            repo = mockRepo,
        )

        backgroundScope.launch { viewModel.uiState.collect() }

        return viewModel
    }

    private fun runTestUnconfined(testBody: suspend TestScope.() -> Unit) = runTest(
        UnconfinedTestDispatcher(),
        testBody = testBody
    )
}
