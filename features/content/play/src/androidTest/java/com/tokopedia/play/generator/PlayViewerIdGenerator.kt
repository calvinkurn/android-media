package com.tokopedia.play.generator

import android.view.View
import android.view.ViewGroup
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.BuildConfig
import com.tokopedia.play.R
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.factory.TestFragmentFactory
import com.tokopedia.content.test.factory.TestViewModelFactory
import com.tokopedia.content.test.util.isSiblingWith
import com.tokopedia.play.domain.repository.PlayViewerChannelRepository
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.fragment.PlayBottomSheetFragment
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.fragment.PlayVideoFragment
import com.tokopedia.play.view.storage.PagingChannel
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel
import com.tokopedia.play.view.uimodel.mapper.*
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.interactive.LeaderboardUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.mapper.PlayChannelInteractiveMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveLeaderboardMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveMapper
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by kenny.hadisaputra on 17/03/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@UiTest
class PlayViewerIdGenerator {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val decodeHtml = DefaultHtmlTextTransformer()

    private val socket: PlayWebSocket = mockk(relaxed = true)

    private val mapper = PlayUiModelMapper(
        productTagMapper = PlayProductTagUiMapper(),
        merchantVoucherMapper = PlayMerchantVoucherUiMapper(),
        chatMapper = PlayChatUiMapper(userSession),
        channelStatusMapper = PlayChannelStatusMapper(),
        channelInteractiveMapper = PlayChannelInteractiveMapper(),
        interactiveLeaderboardMapper = PlayInteractiveLeaderboardMapper(decodeHtml),
        cartMapper = PlayCartMapper(),
        playUserReportMapper = PlayUserReportReasoningMapper(),
        interactiveMapper = PlayInteractiveMapper(decodeHtml),
    )

    private val mockViewModelFactory = TestViewModelFactory(
        mapOf(
            PlayInteractionViewModel::class.java to {
                mockk<PlayInteractionViewModel>(relaxed = true)
            },
            PlayBottomSheetViewModel::class.java to {
                PlayBottomSheetViewModel(
                    userSession,
                )
            }
        )
    )

    private val playViewModelFactory = object : PlayViewModel.Factory {
        override fun create(channelId: String): PlayViewModel {
            return PlayViewModel(
                channelId = "12345",
                playVideoBuilder = mockk(relaxed = true),
                videoStateProcessorFactory = mockk(relaxed = true),
                channelStateProcessorFactory = mockk(relaxed = true),
                videoBufferGovernorFactory = mockk(relaxed = true),
                getReportSummariesUseCase = mockk(relaxed = true),
                playSocketToModelMapper = mockk(relaxed = true),
                playUiModelMapper = mapper,
                userSession = mockk(relaxed = true),
                dispatchers = CoroutineDispatchersProvider,
                remoteConfig = mockk(relaxed = true),
                playPreference = mockk(relaxed = true),
                videoLatencyPerformanceMonitoring = mockk(relaxed = true),
                playChannelWebSocket = mockk(relaxed = true),
                repo = mockk(relaxed = true),
                playAnalytic = mockk(relaxed = true),
                timerFactory = mockk(relaxed = true),
                castPlayerHelper = mockk(relaxed = true),
                playShareExperience = mockk(relaxed = true),
                playLog = mockk(relaxed = true),
                chatManagerFactory = mockk(relaxed = true),
                chatStreamsFactory = mockk(relaxed = true),
                liveRoomMetricsCommon = mockk(relaxed = true),
            )
        }
    }

    private val fragmentFactory = TestFragmentFactory(
        mapOf(
            PlayFragment::class.java to {
                PlayFragment(
                    playViewModelFactory,
                    mockk(relaxed = true),
                    mockk(relaxed = true),
                    mockk(relaxed = true),
                    mockk(relaxed = true),
                )
            },
            PlayUserInteractionFragment::class.java to {
                PlayUserInteractionFragment(
                    viewModelFactory = mockViewModelFactory,
                    dispatchers = CoroutineDispatchersProvider,
                    pipAnalytic = mockk(relaxed = true),
                    analytic = mockk(relaxed = true),
                    multipleLikesIconCacheStorage = mockk(relaxed = true),
                    castAnalyticHelper = mockk(relaxed = true),
                    performanceClassConfig = mockk(relaxed = true),
                    newAnalytic = mockk(relaxed = true),
                    analyticManager = mockk(relaxed = true),
                    router = mockk(relaxed = true),
                )
            },
            PlayBottomSheetFragment::class.java to {
                PlayBottomSheetFragment(
                    viewModelFactory = mockViewModelFactory,
                    analytic = mockk(relaxed = true),
                    newAnalytic = mockk(relaxed = true),
                    router = mockk(relaxed = true),
                )
            },
            PlayVideoFragment::class.java to {
                PlayVideoFragment(
                    dispatchers = CoroutineDispatchersProvider,
                    pipAnalytic = mockk(relaxed = true),
                    analytic = mockk(relaxed = true),
                    pipSessionStorage = mockk(relaxed = true),
                    playLog = mockk(relaxed = true),
                    router = mockk(relaxed = true),
                )
            }
        )
    )
    private val uiModelBuilder = UiModelBuilder.get()

    private val printConditions = listOf(
        PrintCondition { view ->
            val parent = (view.parent as? ViewGroup) ?: return@PrintCondition true
            val packageName = parent::class.java.`package`?.name.orEmpty()
            val className = parent::class.java.name
            !packageName.startsWith("com.tokopedia") || !className.contains(
                "unify",
                ignoreCase = true
            )
        },
        PrintCondition { view ->
            view.id != View.NO_ID || view is ViewGroup
        }
    )

    private val parentPrintCondition = listOf(
        PrintCondition { view ->
            val parent = (view.parent as? ViewGroup) ?: return@PrintCondition true
            !parent.isSiblingWith { sibling ->
                val idName = sibling.resources.getResourceEntryName(sibling.id)
                idName.equals("iv_close", ignoreCase = true)
            }
        }
    ) + printConditions

    private val parentViewPrinter = ViewHierarchyPrinter(
        parentPrintCondition, customIdPrefix = "P", packageName = BuildConfig.LIBRARY_PACKAGE_NAME
    )
    private val viewPrinter = ViewHierarchyPrinter(
        printConditions, packageName = BuildConfig.LIBRARY_PACKAGE_NAME
    )
    private val fileWriter = FileWriter()

    @Test
    fun inhousePlayer() {
        val tagItem = uiModelBuilder.buildTagItem(
            product = uiModelBuilder.buildProductModel(
                productList = listOf(
                    uiModelBuilder.buildProductSection(
                        productList = listOf(
                            uiModelBuilder.buildProduct(
                                id = "1",
                                shopId = "2",
                                title = "Barang 1",
                                stock = StockAvailable(1),
                            ),
                        ),
                        config = uiModelBuilder.buildSectionConfig(
                            type = ProductSectionType.Other,
                            title = "Section 1",
                        ),
                        id = "1"
                    )
                ),
                canShow = true,
            ),
            voucher = uiModelBuilder.buildVoucherModel(
                voucherList = listOf(
                    uiModelBuilder.buildMerchantVoucher()
                )
            ),
            maxFeatured = 1,
            bottomSheetTitle = "Product List",
            resultState = ResultState.Success,
        )

        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem
        coEvery { repo.getChannels(any(), any()) } returns PagingChannel(
            channelList = listOf(
                uiModelBuilder.buildChannelData(
                    id = "12669",
                    partnerInfo = PlayPartnerInfo(name = "test"),
                    channelReportInfo = PlayChannelReportUiModel(totalViewFmt = "1200"),
                    pinnedInfo = PlayPinnedInfoUiModel(
                        PinnedMessageUiModel("1", appLink = "", title = "Test pinned"),
                    ),
                    videoMetaInfo = PlayVideoMetaInfoUiModel(
                        videoPlayer = PlayVideoPlayerUiModel.General.Incomplete(
                            params = PlayGeneralVideoPlayerParams(
                                videoUrl = "https://vod.tokopedia.com/view/adaptive.m3u8?id=4d30328d17e948b4b1c4c34c5bb9f372",
                                buffer = PlayBufferControl(),
                                lastMillis = null,
                            )
                        ),
                        videoStream = PlayVideoStreamUiModel(
                            "", VideoOrientation.Vertical, "Video Keren"
                        ),
                    ),
                    tagItems = tagItem,
                )
            ),
            cursor = "",
        )

        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(PlayTestModule(targetContext))
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo, webSocket = socket))
                .build()
        )

        val intent = RouteManager.getIntent(
            targetContext,
            "tokopedia://play/12669"
        )
        val scenario = ActivityScenario.launch<PlayActivity>(intent)
        scenario.moveToState(Lifecycle.State.RESUMED)

        delay(5000)

        scenario.onActivity {
            val parent = parentViewPrinter.printAsCSV(
                view = (it.findViewById(android.R.id.content) as ViewGroup).getChildAt(0)
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "play_parent.csv",
                text = parent
            )

            val interactionFragment = viewPrinter.printAsCSV(
                view = it.findViewById(R.id.fl_user_interaction)
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "play_interaction.csv",
                text = interactionFragment
            )

            val videoFragment = viewPrinter.printAsCSV(
                view = it.findViewById(R.id.fl_video)
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "play_video.csv",
                text = videoFragment
            )

            val bottomSheetFragment = viewPrinter.printAsCSV(
                view = it.findViewById(R.id.fl_bottom_sheet)
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "play_bottom_sheet.csv",
                text = bottomSheetFragment
            )
        }
    }

    @Test
    fun youTubePlayer() {
        coEvery { repo.getChannels(any(), any()) } returns PagingChannel(
            channelList = listOf(
                PlayChannelData(
                    id = "12680",
                    channelDetail = PlayChannelDetailUiModel(),
                    partnerInfo = PlayPartnerInfo(name = "test"),
                    likeInfo = PlayLikeInfoUiModel(),
                    channelReportInfo = PlayChannelReportUiModel(totalViewFmt = "1200"),
                    pinnedInfo = PlayPinnedInfoUiModel(
                        PinnedMessageUiModel("1", appLink = "", title = "Test pinned"),
                    ),
                    quickReplyInfo = PlayQuickReplyInfoUiModel(emptyList()),
                    videoMetaInfo = PlayVideoMetaInfoUiModel(
                        videoPlayer = PlayVideoPlayerUiModel.YouTube("E4qo_PkR7WE"),
                        videoStream = PlayVideoStreamUiModel(
                            "", VideoOrientation.Horizontal(16, 9), "Video Keren"
                        ),
                    ),
                    upcomingInfo = PlayUpcomingUiModel(),
                    tagItems = TagItemUiModel.Empty,
                    status = PlayStatusUiModel.Empty,
                    leaderboard = LeaderboardUiModel.Empty
                ),
            ),
            cursor = "",
        )

        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(PlayTestModule(targetContext))
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo, socket))
                .build()
        )

        val intent = RouteManager.getIntent(
            targetContext,
            "tokopedia://play/12680"
        )
        val scenario = ActivityScenario.launch<PlayActivity>(intent)
        scenario.moveToState(Lifecycle.State.RESUMED)

        delay(5000)

        scenario.onActivity {
            val youTubeFragment = viewPrinter.printAsCSV(
                view = it.findViewById(R.id.fl_youtube)
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "play_youtube.csv",
                text = youTubeFragment
            )
        }
    }
}
