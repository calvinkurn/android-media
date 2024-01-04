package com.tokopedia.feedplus.browse

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.test.util.assertEqualTo
import com.tokopedia.content.test.util.assertNotEqualTo
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
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class FeedBrowseViewModelTest {

    @get:Rule
    val coroutineRule = UnconfinedTestRule()

    private val mockChannel = mockk<PlayWidgetChannelUiModel>(relaxed = true)

    private val indexGenerator = generateSequence(1) { it + 1 }
    private val widgetMenuModelGenerator = indexGenerator.map {
        WidgetMenuModel(
            id = it.toString(),
            label = "Menu $it",
            group = "Menu Group $it",
            sourceType = "Source type $it",
            sourceId = "Source Id $it"
        )
    }
    private val bannerModelGenerator = indexGenerator.map {
        BannerWidgetModel(
            title = "Banner $it",
            imageUrl = "Url Banner $it",
            appLink = "AppLink Banner $it"
        )
    }
    private val authorModelGenerator = indexGenerator.map {
        AuthorWidgetModel(
            id = it.toString(),
            name = "Author widget $it",
            avatarUrl = "Avatar $it",
            coverUrl = "Cover $it",
            totalViewFmt = "$it rb",
            appLink = "AppLink Author $it",
            contentAppLink = "Content AppLink $it",
            channelType = "live"
        )
    }

    private val channelWithMenusSlotGenerator = indexGenerator.map {
        val randomSlotId = UUID.randomUUID().mostSignificantBits.toString()
        FeedBrowseSlotUiModel.ChannelsWithMenus(
            slotId = randomSlotId,
            title = "Channel with Menus $it",
            group = "Group $it",
            menus = emptyMap(),
            selectedMenuId = ""
        )
    }
    private val bannerSlotGenerator = indexGenerator.map {
        val randomSlotId = UUID.randomUUID().mostSignificantBits.toString()
        FeedBrowseSlotUiModel.InspirationBanner(
            slotId = randomSlotId,
            title = "Banner Slot model $it",
            identifier = "banner_slot_$it",
            bannerList = emptyList()
        )
    }
    private val authorSlotGenerator = indexGenerator.map {
        val randomSlotId = UUID.randomUUID().mostSignificantBits.toString()
        FeedBrowseSlotUiModel.Authors(
            slotId = randomSlotId,
            title = "Author Slot model $it",
            identifier = "author_slot_$it",
            authorList = emptyList()
        )
    }

    private val mockTitle = "Feed Browse Title"

    @Before
    fun setUp() {
    }

    @Test
    fun `test load initial data`() = runTestUnconfined {
        val slotChannel = channelWithMenusSlotGenerator.first()
        val slotAuthor = authorSlotGenerator.first()
        val slotBanner = bannerSlotGenerator.first()

        val mockAuthorModel = authorModelGenerator.first()
        val mockBannerModel = bannerModelGenerator.first()

        val slots = listOf(slotChannel, slotAuthor, slotBanner)

        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        coEvery { mockRepo.getTitle() } returns mockTitle
        coEvery { mockRepo.getSlots() } returns slots
        coEvery { mockRepo.getWidgetContentSlot(WidgetRequestModel(slotChannel.group)) } returns ContentSlotModel.ChannelBlock(
            channels = listOf(mockChannel),
            config = PlayWidgetConfigUiModel.Empty,
            nextCursor = ""
        )
        coEvery { mockRepo.getWidgetRecommendation(any()) } answers {
            when (val identifier = arg<String>(0)) {
                slotAuthor.identifier -> WidgetRecommendationModel.Authors(
                    listOf(mockAuthorModel)
                )
                slotBanner.identifier -> WidgetRecommendationModel.Banners(
                    listOf(mockBannerModel)
                )
                else -> error("Not support for $identifier")
            }
        }

        val viewModel = FeedBrowseViewModel(mockRepo)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onIntent(FeedBrowseIntent.LoadInitialPage)

        val value = viewModel.uiState.value
        value.assertType<FeedBrowseUiState.Success> {
            it.title.assertEqualTo(mockTitle)
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        slotChannel.copy(
                            menus = mapOf(
                                WidgetMenuModel.Empty.copy(group = slotChannel.group) to FeedBrowseChannelListState.initSuccess(
                                    items = listOf(mockChannel)
                                )
                            )
                        )
                    ),
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        slotAuthor.copy(authorList = listOf(mockAuthorModel))
                    ),
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        slotBanner.copy(bannerList = listOf(mockBannerModel))
                    )
                )
            )
        }
    }

    @Test
    fun `test select chip from channel with menus`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val widgetMenus = widgetMenuModelGenerator.take(2).toList()
        val channelMenuSlotModel = channelWithMenusSlotGenerator.first()
        coEvery { mockRepo.getTitle() } returns mockTitle
        coEvery { mockRepo.getSlots() } returns listOf(channelMenuSlotModel)
        coEvery { mockRepo.getWidgetContentSlot(WidgetRequestModel(channelMenuSlotModel.group)) } returns ContentSlotModel.TabMenus(
            widgetMenus
        )

        val viewModel = FeedBrowseViewModel(mockRepo)
        backgroundScope.launch { viewModel.uiState.collect() }
        viewModel.onIntent(FeedBrowseIntent.LoadInitialPage)
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        channelMenuSlotModel.copy(
                            menus = widgetMenus.associateWith { FeedBrowseChannelListState.initLoading() },
                            selectedMenuId = widgetMenus[0].id
                        )
                    )
                )
            )
        }

        viewModel.onIntent(FeedBrowseIntent.SelectChipWidget(channelMenuSlotModel.slotId, widgetMenus[1]))
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        channelMenuSlotModel.copy(
                            menus = widgetMenus.associateWith { FeedBrowseChannelListState.initLoading() },
                            selectedMenuId = widgetMenus[1].id
                        )
                    )
                )
            )
        }
    }

    @Test
    fun `test trigger fetch banner widgets from outside (mostly for retry purpose)`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val bannerSlotModel = bannerSlotGenerator.first()
        coEvery { mockRepo.getTitle() } returns mockTitle
        coEvery { mockRepo.getSlots() } returns listOf(bannerSlotModel)
        coEvery { mockRepo.getWidgetRecommendation(bannerSlotModel.identifier) } returns WidgetRecommendationModel.Banners(
            listOf(bannerModelGenerator.first())
        )

        val viewModel = FeedBrowseViewModel(mockRepo)
        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onIntent(FeedBrowseIntent.LoadInitialPage)
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        bannerSlotModel.copy(bannerList = listOf(bannerModelGenerator.first()))
                    )
                )
            )
        }

        coEvery { mockRepo.getWidgetRecommendation(bannerSlotModel.identifier) } returns WidgetRecommendationModel.Banners(
            listOf(bannerModelGenerator.elementAt(1))
        )
        viewModel.onIntent(FeedBrowseIntent.FetchCardsWidget(bannerSlotModel.slotId, WidgetMenuModel.Empty))
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertNotEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        bannerSlotModel.copy(bannerList = listOf(bannerModelGenerator.first()))
                    )
                )
            )
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        bannerSlotModel.copy(bannerList = listOf(bannerModelGenerator.elementAt(1)))
                    )
                )
            )
        }
    }

    @Test
    fun `test trigger fetch author widgets from outside (mostly for retry purpose)`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val authorSlotModel = authorSlotGenerator.first()
        coEvery { mockRepo.getTitle() } returns mockTitle
        coEvery { mockRepo.getSlots() } returns listOf(authorSlotModel)
        coEvery { mockRepo.getWidgetRecommendation(authorSlotModel.identifier) } returns WidgetRecommendationModel.Authors(
            listOf(authorModelGenerator.first())
        )

        val viewModel = FeedBrowseViewModel(mockRepo)
        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onIntent(FeedBrowseIntent.LoadInitialPage)
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        authorSlotModel.copy(authorList = listOf(authorModelGenerator.first()))
                    )
                )
            )
        }

        coEvery { mockRepo.getWidgetRecommendation(authorSlotModel.identifier) } returns WidgetRecommendationModel.Authors(
            listOf(authorModelGenerator.elementAt(1))
        )
        viewModel.onIntent(FeedBrowseIntent.FetchCardsWidget(authorSlotModel.slotId, WidgetMenuModel.Empty))
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertNotEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        authorSlotModel.copy(authorList = listOf(authorModelGenerator.first()))
                    )
                )
            )
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        authorSlotModel.copy(authorList = listOf(authorModelGenerator.elementAt(1)))
                    )
                )
            )
        }
    }

    @Test
    fun `test trigger fetch channel menus from outside`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val menusModel = widgetMenuModelGenerator.take(2).toList()
        val channelWithMenuSlotModel = channelWithMenusSlotGenerator.first()
        val menuResponse = ContentSlotModel.ChannelBlock(
            channels = listOf(mockChannel),
            config = PlayWidgetConfigUiModel.Empty,
            nextCursor = ""
        )

        coEvery { mockRepo.getTitle() } returns mockTitle
        coEvery { mockRepo.getSlots() } returns listOf(channelWithMenuSlotModel)
        coEvery { mockRepo.getWidgetContentSlot(WidgetRequestModel(channelWithMenuSlotModel.group)) } returns ContentSlotModel.TabMenus(
            menusModel
        )
        coEvery { mockRepo.getWidgetContentSlot(menusModel[1].toRequest("")) } returns menuResponse

        val viewModel = FeedBrowseViewModel(mockRepo)
        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onIntent(FeedBrowseIntent.LoadInitialPage)
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        channelWithMenuSlotModel.copy(
                            menus = menusModel.associateWith { FeedBrowseChannelListState.initLoading() },
                            selectedMenuId = menusModel.first().id
                        )
                    )
                )
            )
        }

        viewModel.onIntent(FeedBrowseIntent.FetchCardsWidget(channelWithMenuSlotModel.slotId, menusModel[1]))
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertNotEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        channelWithMenuSlotModel.copy(
                            menus = mapOf(
                                menusModel[0] to FeedBrowseChannelListState.initLoading(),
                                menusModel[1] to FeedBrowseChannelListState.initSuccess(
                                    items = menuResponse.channels,
                                    config = menuResponse.config
                                )
                            ),
                            selectedMenuId = menusModel.first().id
                        )
                    )
                )
            )
        }
    }

    private fun runTestUnconfined(testBody: suspend TestScope.() -> Unit) = runTest(
        UnconfinedTestDispatcher(),
        testBody = testBody
    )
}
