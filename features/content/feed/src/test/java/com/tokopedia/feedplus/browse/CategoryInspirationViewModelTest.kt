package com.tokopedia.feedplus.browse

import com.tokopedia.content.test.util.assertEqualTo
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.CategoryInspirationViewModel
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationAction
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationData
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChannelListState
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.coEvery
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

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryInspirationViewModelTest {

    @get:Rule
    val coroutineRule = UnconfinedTestRule()

    private val mockSource = "makanan_dan_minuman"
    private val mockTitle = "Makanan & Minuman"

    private val modelGen = ModelGenerator()

    private val mockChannel = mockk<PlayWidgetChannelUiModel>(relaxed = true)
    private val mockChannel2 = mockk<PlayWidgetChannelUiModel>(relaxed = true)

    @Before
    fun setUp() {
    }

    @Test
    fun `when load initial data, should return title and slots`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val viewModel = CategoryInspirationViewModel(mockSource, mockRepo)
        val menus = modelGen.widgetMenuModel.take(2).toList()

        backgroundScope.launch { viewModel.uiState.collect() }

        coEvery { mockRepo.getCategoryInspirationTitle(mockSource) } returns mockTitle
        coEvery { mockRepo.getWidgetContentSlot(any()) } returns ContentSlotModel.TabMenus(menus = menus)
        viewModel.onAction(CategoryInspirationAction.Init)

        viewModel.uiState.value.title.assertEqualTo(mockTitle)
        viewModel.uiState.value.items.assertEqualTo(
            menus.associate {
                it.id to CategoryInspirationData(
                    it,
                    FeedBrowseChannelListState.initLoading()
                )
            }
        )
    }

    @Test
    fun `when load data for certain menus, should return data for that specific menu`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val viewModel = CategoryInspirationViewModel(mockSource, mockRepo)
        val menus = modelGen.widgetMenuModel.take(2).toList()
        val menuResponse = ContentSlotModel.ChannelBlock(
            title = "",
            channels = listOf(mockChannel),
            config = PlayWidgetConfigUiModel.Empty,
            nextCursor = ""
        )

        backgroundScope.launch { viewModel.uiState.collect() }

        coEvery {
            mockRepo.getWidgetContentSlot(
                WidgetMenuModel.Empty.copy(group = mockSource).toRequest("")
            )
        } returns ContentSlotModel.TabMenus(menus = menus)
        coEvery { mockRepo.getWidgetContentSlot(menus[0].toRequest("")) } returns menuResponse

        viewModel.onAction(CategoryInspirationAction.Init)
        viewModel.onAction(CategoryInspirationAction.LoadData(menus[0]))

        viewModel.uiState.value.items.assertEqualTo(
            mapOf(
                menus[0].id to CategoryInspirationData(
                    menus[0],
                    FeedBrowseChannelListState.initSuccess(
                        listOf(mockChannel),
                        nextCursor = menuResponse.nextCursor,
                        hasNextPage = menuResponse.hasNextPage,
                        config = menuResponse.config
                    )
                ),
                menus[1].id to CategoryInspirationData(
                    menus[1],
                    FeedBrowseChannelListState.initLoading()
                )
            )
        )
    }

    @Test
    fun `when request load data for next page, should return data if there's still next page`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val viewModel = CategoryInspirationViewModel(mockSource, mockRepo)
        val menus = modelGen.widgetMenuModel.take(2).toList()
        val menuResponsePage1 = ContentSlotModel.ChannelBlock(
            title = "",
            channels = listOf(mockChannel),
            config = PlayWidgetConfigUiModel.Empty,
            nextCursor = "page_2"
        )
        val menuResponsePage2 = ContentSlotModel.ChannelBlock(
            title = "",
            channels = listOf(mockChannel2),
            config = PlayWidgetConfigUiModel.Empty,
            nextCursor = "page_3"
        )
        val menuResponsePage3 = ContentSlotModel.NoData(nextCursor = "")

        backgroundScope.launch { viewModel.uiState.collect() }

        coEvery {
            mockRepo.getWidgetContentSlot(
                WidgetMenuModel.Empty.copy(group = mockSource).toRequest("")
            )
        } returns ContentSlotModel.TabMenus(menus = menus)
        coEvery { mockRepo.getWidgetContentSlot(menus[0].toRequest("")) } returns menuResponsePage1
        coEvery { mockRepo.getWidgetContentSlot(menus[0].toRequest("page_2")) } returns menuResponsePage2
        coEvery { mockRepo.getWidgetContentSlot(menus[0].toRequest("page_3")) } returns menuResponsePage3

        viewModel.onAction(CategoryInspirationAction.Init)
        viewModel.onAction(CategoryInspirationAction.LoadData(menus[0]))
        viewModel.onAction(CategoryInspirationAction.LoadMoreData)

        viewModel.uiState.value.items.assertEqualTo(
            mapOf(
                menus[0].id to CategoryInspirationData(
                    menus[0],
                    FeedBrowseChannelListState.initSuccess(
                        listOf(mockChannel, mockChannel2),
                        nextCursor = menuResponsePage2.nextCursor,
                        hasNextPage = menuResponsePage2.hasNextPage,
                        config = menuResponsePage2.config
                    )
                ),
                menus[1].id to CategoryInspirationData(
                    menus[1],
                    FeedBrowseChannelListState.initLoading()
                )
            )
        )

        /** Try to load more but it return ContentSlotModel.NoData */
        viewModel.onAction(CategoryInspirationAction.LoadMoreData)

        viewModel.uiState.value.items.assertEqualTo(
            mapOf(
                menus[0].id to CategoryInspirationData(
                    menus[0],
                    FeedBrowseChannelListState.initSuccess(
                        listOf(mockChannel, mockChannel2),
                        nextCursor = menuResponsePage3.nextCursor,
                        hasNextPage = menuResponsePage3.hasNextPage,
                        config = menuResponsePage2.config
                    )
                ),
                menus[1].id to CategoryInspirationData(
                    menus[1],
                    FeedBrowseChannelListState.initLoading()
                )
            )
        )
    }

    @Test
    fun `when select menu, should select that and only that menu`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val viewModel = CategoryInspirationViewModel(mockSource, mockRepo)
        val menus = modelGen.widgetMenuModel.take(2).toList()

        backgroundScope.launch { viewModel.uiState.collect() }

        coEvery {
            mockRepo.getWidgetContentSlot(
                WidgetMenuModel.Empty.copy(group = mockSource).toRequest("")
            )
        } returns ContentSlotModel.TabMenus(menus = menus)

        viewModel.onAction(CategoryInspirationAction.Init)

        viewModel.uiState.value.selectedMenuId.assertEqualTo(menus.first().id)

        viewModel.onAction(CategoryInspirationAction.SelectMenu(menus[1]))

        viewModel.uiState.value.selectedMenuId.assertEqualTo(menus[1].id)
    }

    private fun runTestUnconfined(testBody: suspend TestScope.() -> Unit) = runTest(
        UnconfinedTestDispatcher(),
        testBody = testBody
    )
}
