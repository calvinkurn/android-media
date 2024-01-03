package com.tokopedia.feedplus.browse

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.test.util.assertType
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.browse.data.model.BannerWidgetModel
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.FeedBrowseViewModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChannelListState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseIntent
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseStatefulModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import com.tokopedia.feedplus.helper.assertEqualTo
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedBrowseViewModelTest {

    @get:Rule
    val coroutineRule = UnconfinedTestRule()

    private val mockChannel = mockk<PlayWidgetChannelUiModel>(relaxed = true)

    private val mockAuthor = AuthorWidgetModel(
        id = "1",
        name = "Author 1",
        avatarUrl = "",
        coverUrl = "",
        totalViewFmt = "1.5rb",
        appLink = "",
        contentAppLink = "",
        channelType = ""
    )

    private val mockBanner = BannerWidgetModel(
        title = "Banner 1",
        imageUrl = "",
        appLink = ""
    )

    @Test
    fun `test load initial data`() = runTestUnconfined {
        val pageTitle = "Page Title"
        val slotChannel = FeedBrowseSlotUiModel.ChannelsWithMenus(
            "1",
            "Channel with menus",
            menus = emptyMap(),
            group = "channel_menu",
            selectedMenuId = ""
        )
        val slotAuthor = FeedBrowseSlotUiModel.Authors("2", "Author Widgets", "author", emptyList())
        val slotBanner = FeedBrowseSlotUiModel.InspirationBanner("3", "Inspiration Banner", "banner", emptyList())

        val slots = listOf(
            slotChannel,
            slotAuthor,
            slotBanner
        )

        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        coEvery { mockRepo.getTitle() } returns pageTitle
        coEvery { mockRepo.getSlots() } returns slots
        coEvery { mockRepo.getWidgetContentSlot(WidgetRequestModel("channel_menu")) } returns ContentSlotModel.ChannelBlock(
            channels = listOf(mockChannel),
            config = PlayWidgetConfigUiModel.Empty,
            nextCursor = ""
        )
        coEvery { mockRepo.getWidgetRecommendation(any()) } answers {
            when (val identifier = arg<String>(0)) {
                "author" -> WidgetRecommendationModel.Authors(
                    listOf(mockAuthor)
                )
                "banner" -> WidgetRecommendationModel.Banners(
                    listOf(mockBanner)
                )
                else -> error("Not support for $identifier")
            }
        }

        val viewModel = FeedBrowseViewModel(mockRepo)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onIntent(FeedBrowseIntent.LoadInitialPage)

        val value = viewModel.uiState.value
        value.assertType<FeedBrowseUiState.Success> {
            it.title.assertEqualTo(pageTitle)
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        slotChannel.copy(
                            menus = mapOf(
                                WidgetMenuModel.Empty.copy(group = "channel_menu") to FeedBrowseChannelListState.initSuccess(
                                    items = listOf(mockChannel),
                                    config = PlayWidgetConfigUiModel.Empty
                                )
                            )
                        )
                    ),
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        slotAuthor.copy(authorList = listOf(mockAuthor))
                    ),
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        slotBanner.copy(bannerList = listOf(mockBanner))
                    )
                )
            )
        }
    }

    private fun runTestUnconfined(testBody: suspend TestScope.() -> Unit) = runTest(
        UnconfinedTestDispatcher(),
        testBody = testBody
    )

    private fun runTestStandard(testBody: suspend TestScope.() -> Unit) = runTest(
        StandardTestDispatcher(),
        testBody = testBody
    )
}
