package com.tokopedia.feedplus.browse

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.test.util.assertEqualTo
import com.tokopedia.content.test.util.assertNotEqualTo
import com.tokopedia.content.test.util.assertType
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.HeaderDetailModel
import com.tokopedia.feedplus.browse.data.model.StoryGroupsModel
import com.tokopedia.feedplus.browse.data.model.StoryNodeModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.FeedBrowseViewModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseAction
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChannelListState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseStatefulModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import com.tokopedia.network.exception.MessageErrorException
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
class FeedBrowseViewModelTest {

    @get:Rule
    val coroutineRule = UnconfinedTestRule()

    private val mockChannel = mockk<PlayWidgetChannelUiModel>(relaxed = true)
    private val mockHeaderModel = HeaderDetailModel(
        title = "Feed Browse Title",
        true,
        "searchbar placeholder",
        "appLink"
    )

    private val modelGen = ModelGenerator()

    @Before
    fun setUp() {
    }

    @Test
    fun `test load initial data`() = runTestUnconfined {
        val slotStoryGroup = modelGen.storyGroupSlot.first()
        val slotChannel = modelGen.channelWithMenusSlot.first()
        val slotAuthor = modelGen.authorSlot.first()
        val slotBanner = modelGen.bannerSlot.first()

        val mockStoryGroup = mockk<StoryNodeModel>(relaxed = true)
        val mockAuthorModel = modelGen.authorModel.first()
        val mockBannerModel = modelGen.bannerModel.first()

        val slots = listOf(slotStoryGroup, slotChannel, slotAuthor, slotBanner)

        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        coEvery { mockRepo.getHeaderDetail() } returns mockHeaderModel
        coEvery { mockRepo.getSlots() } returns slots
        coEvery { mockRepo.getWidgetContentSlot(WidgetRequestModel(slotChannel.group)) } returns ContentSlotModel.ChannelBlock(
            title = "",
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
        coEvery { mockRepo.getStoryGroups(any(), any()) } returns StoryGroupsModel(
            storyList = listOf(mockStoryGroup),
            nextCursor = "",
        )

        val viewModel = FeedBrowseViewModel(mockRepo)

        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FeedBrowseAction.LoadInitialPage)

        val value = viewModel.uiState.value
        value.assertType<FeedBrowseUiState.Success> {
            it.headerDetail.title.assertEqualTo(mockHeaderModel.title)
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        slotStoryGroup.copy(storyList = listOf(mockStoryGroup))
                    ),
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
        val widgetMenus = modelGen.widgetMenuModel.take(2).toList()
        val channelMenuSlotModel = modelGen.channelWithMenusSlot.first()
        coEvery { mockRepo.getHeaderDetail() } returns mockHeaderModel
        coEvery { mockRepo.getSlots() } returns listOf(channelMenuSlotModel)
        coEvery { mockRepo.getWidgetContentSlot(WidgetRequestModel(channelMenuSlotModel.group)) } returns ContentSlotModel.TabMenus(
            widgetMenus
        )

        val viewModel = FeedBrowseViewModel(mockRepo)
        backgroundScope.launch { viewModel.uiState.collect() }
        viewModel.onAction(FeedBrowseAction.LoadInitialPage)
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

        viewModel.onAction(FeedBrowseAction.SelectChipWidget(channelMenuSlotModel.slotId, widgetMenus[1]))
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
        val bannerSlotModel = modelGen.bannerSlot.first()
        coEvery { mockRepo.getHeaderDetail() } returns mockHeaderModel
        coEvery { mockRepo.getSlots() } returns listOf(bannerSlotModel)
        coEvery { mockRepo.getWidgetRecommendation(bannerSlotModel.identifier) } returns WidgetRecommendationModel.Banners(
            listOf(modelGen.bannerModel.first())
        )

        val viewModel = FeedBrowseViewModel(mockRepo)
        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FeedBrowseAction.LoadInitialPage)
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        bannerSlotModel.copy(bannerList = listOf(modelGen.bannerModel.first()))
                    )
                )
            )
        }

        coEvery { mockRepo.getWidgetRecommendation(bannerSlotModel.identifier) } returns WidgetRecommendationModel.Banners(
            listOf(modelGen.bannerModel.elementAt(1))
        )
        viewModel.onAction(FeedBrowseAction.FetchCardsWidget(bannerSlotModel.slotId, WidgetMenuModel.Empty))
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertNotEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        bannerSlotModel.copy(bannerList = listOf(modelGen.bannerModel.first()))
                    )
                )
            )
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        bannerSlotModel.copy(bannerList = listOf(modelGen.bannerModel.elementAt(1)))
                    )
                )
            )
        }
    }

    @Test
    fun `test trigger fetch author widgets from outside (mostly for retry purpose)`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val authorSlotModel = modelGen.authorSlot.first()
        coEvery { mockRepo.getHeaderDetail() } returns mockHeaderModel
        coEvery { mockRepo.getSlots() } returns listOf(authorSlotModel)
        coEvery { mockRepo.getWidgetRecommendation(authorSlotModel.identifier) } returns WidgetRecommendationModel.Authors(
            listOf(modelGen.authorModel.first())
        )

        val viewModel = FeedBrowseViewModel(mockRepo)
        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FeedBrowseAction.LoadInitialPage)
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        authorSlotModel.copy(authorList = listOf(modelGen.authorModel.first()))
                    )
                )
            )
        }

        coEvery { mockRepo.getWidgetRecommendation(authorSlotModel.identifier) } returns WidgetRecommendationModel.Authors(
            listOf(modelGen.authorModel.elementAt(1))
        )
        viewModel.onAction(FeedBrowseAction.FetchCardsWidget(authorSlotModel.slotId, WidgetMenuModel.Empty))
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertNotEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        authorSlotModel.copy(authorList = listOf(modelGen.authorModel.first()))
                    )
                )
            )
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        authorSlotModel.copy(authorList = listOf(modelGen.authorModel.elementAt(1)))
                    )
                )
            )
        }
    }

    @Test
    fun `test trigger fetch channel menus from outside`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val menusModel = modelGen.widgetMenuModel.take(2).toList()
        val channelWithMenuSlotModel = modelGen.channelWithMenusSlot.first()
        val menuResponse = ContentSlotModel.ChannelBlock(
            title = "",
            channels = listOf(mockChannel),
            config = PlayWidgetConfigUiModel.Empty,
            nextCursor = ""
        )

        coEvery { mockRepo.getHeaderDetail() } returns mockHeaderModel
        coEvery { mockRepo.getSlots() } returns listOf(channelWithMenuSlotModel)
        coEvery { mockRepo.getWidgetContentSlot(WidgetRequestModel(channelWithMenuSlotModel.group)) } returns ContentSlotModel.TabMenus(
            menusModel
        )
        coEvery { mockRepo.getWidgetContentSlot(menusModel[1].toRequest("")) } returns menuResponse

        val viewModel = FeedBrowseViewModel(mockRepo)
        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.onAction(FeedBrowseAction.LoadInitialPage)
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

        viewModel.onAction(FeedBrowseAction.FetchCardsWidget(channelWithMenuSlotModel.slotId, menusModel[1]))
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

    @Test
    fun `test failed fetch channel with menus`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val channelSlotModel = modelGen.channelWithMenusSlot.take(2).toList()
        val exception = MessageErrorException()

        coEvery { mockRepo.getHeaderDetail() } returns mockHeaderModel
        coEvery { mockRepo.getSlots() } returns channelSlotModel
        coEvery {
            mockRepo.getWidgetContentSlot(
                WidgetRequestModel(channelSlotModel[0].group)
            )
        } returns ContentSlotModel.TabMenus(emptyList())
        coEvery {
            mockRepo.getWidgetContentSlot(
                WidgetRequestModel(channelSlotModel[1].group)
            )
        } throws exception

        val viewModel = FeedBrowseViewModel(mockRepo)
        backgroundScope.launch { viewModel.uiState.collect() }
        viewModel.onAction(FeedBrowseAction.LoadInitialPage)
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        channelSlotModel[0].copy(menus = emptyMap())
                    ),
                    FeedBrowseStatefulModel(
                        ResultState.Fail(exception),
                        channelSlotModel[1]
                    )
                )
            )
        }
    }

    @Test
    fun `test failed fetch author`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val authorSlot = modelGen.authorSlot.take(2).toList()
        val exception = MessageErrorException()

        coEvery { mockRepo.getHeaderDetail() } returns mockHeaderModel
        coEvery { mockRepo.getSlots() } returns authorSlot
        coEvery {
            mockRepo.getWidgetRecommendation(
                authorSlot[0].identifier
            )
        } returns WidgetRecommendationModel.Authors(emptyList())
        coEvery {
            mockRepo.getWidgetRecommendation(
                authorSlot[1].identifier
            )
        } throws exception

        val viewModel = FeedBrowseViewModel(mockRepo)
        backgroundScope.launch { viewModel.uiState.collect() }
        viewModel.onAction(FeedBrowseAction.LoadInitialPage)
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        authorSlot[0].copy(authorList = emptyList())
                    ),
                    FeedBrowseStatefulModel(
                        ResultState.Fail(exception),
                        authorSlot[1]
                    )
                )
            )
        }
    }

    @Test
    fun `test failed fetch banner`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val bannerSlot = modelGen.bannerSlot.take(2).toList()
        val exception = MessageErrorException()

        coEvery { mockRepo.getHeaderDetail() } returns mockHeaderModel
        coEvery { mockRepo.getSlots() } returns bannerSlot
        coEvery {
            mockRepo.getWidgetRecommendation(
                bannerSlot[0].identifier
            )
        } returns WidgetRecommendationModel.Banners(emptyList())
        coEvery {
            mockRepo.getWidgetRecommendation(
                bannerSlot[1].identifier
            )
        } throws exception

        val viewModel = FeedBrowseViewModel(mockRepo)
        backgroundScope.launch { viewModel.uiState.collect() }
        viewModel.onAction(FeedBrowseAction.LoadInitialPage)
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Success,
                        bannerSlot[0].copy(bannerList = emptyList())
                    ),
                    FeedBrowseStatefulModel(
                        ResultState.Fail(exception),
                        bannerSlot[1]
                    )
                )
            )
        }
    }

    @Test
    fun `test failed fetch story group`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val storyGroupSlot = modelGen.storyGroupSlot.first()
        val exception = MessageErrorException()

        coEvery { mockRepo.getHeaderDetail() } returns mockHeaderModel
        coEvery { mockRepo.getSlots() } returns listOf(storyGroupSlot)
        coEvery { mockRepo.getStoryGroups(any(), any()) } throws exception

        val viewModel = FeedBrowseViewModel(mockRepo)
        backgroundScope.launch { viewModel.uiState.collect() }
        viewModel.onAction(FeedBrowseAction.LoadInitialPage)
        viewModel.uiState.value.assertType<FeedBrowseUiState.Success> {
            it.widgets.assertEqualTo(
                listOf(
                    FeedBrowseStatefulModel(
                        ResultState.Fail(exception),
                        storyGroupSlot
                    ),
                )
            )
        }
    }

    @Test
    fun `test failed fetch slot`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        val bannerSlot = modelGen.bannerSlot.take(2).toList()
        val exception = MessageErrorException()

        coEvery { mockRepo.getHeaderDetail() } returns mockHeaderModel
        coEvery { mockRepo.getSlots() } throws exception
        coEvery {
            mockRepo.getWidgetRecommendation(
                bannerSlot[0].identifier
            )
        } returns WidgetRecommendationModel.Banners(emptyList())

        val viewModel = FeedBrowseViewModel(mockRepo)
        backgroundScope.launch { viewModel.uiState.collect() }
        viewModel.onAction(FeedBrowseAction.LoadInitialPage)
        viewModel.uiState.value.assertType<FeedBrowseUiState.Error> {
            it.throwable.assertEqualTo(exception)
        }
    }

    @Test
    fun `should get feed search header data`() = runTestUnconfined {
        val mockRepo = mockk<FeedBrowseRepository>(relaxed = true)
        var headerData: HeaderDetailModel = HeaderDetailModel.DEFAULT

        coEvery { mockRepo.getHeaderDetail() } returns mockHeaderModel
        val viewModel = FeedBrowseViewModel(mockRepo)
        viewModel.onAction(FeedBrowseAction.LoadInitialPage)
        headerData = viewModel.getHeaderDetail()

        headerData.assertNotEqualTo(HeaderDetailModel.DEFAULT)
    }

    private fun runTestUnconfined(testBody: suspend TestScope.() -> Unit) = runTest(
        UnconfinedTestDispatcher(),
        testBody = testBody
    )
}
