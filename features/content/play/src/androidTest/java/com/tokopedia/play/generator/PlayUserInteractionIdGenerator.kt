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
import com.tokopedia.play.R
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.test.FileWriter
import com.tokopedia.play.test.PrintCondition
import com.tokopedia.play.test.ViewHierarchyPrinter
import com.tokopedia.play.test.espresso.delay
import com.tokopedia.play.test.factory.TestFragmentFactory
import com.tokopedia.play.test.factory.TestViewModelFactory
import com.tokopedia.play.test.util.isSiblingWith
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.fragment.PlayBottomSheetFragment
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.fragment.PlayVideoFragment
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel
import com.tokopedia.play.view.uimodel.mapper.PlayCartMapper
import com.tokopedia.play.view.uimodel.mapper.PlayChannelStatusMapper
import com.tokopedia.play.view.uimodel.mapper.PlayChatUiMapper
import com.tokopedia.play.view.uimodel.mapper.PlayMerchantVoucherUiMapper
import com.tokopedia.play.view.uimodel.mapper.PlayProductTagUiMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUserReportReasoningMapper
import com.tokopedia.play.view.uimodel.recom.PinnedMessageUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelDetailUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelReportUiModel
import com.tokopedia.play.view.uimodel.recom.PlayGeneralVideoPlayerParams
import com.tokopedia.play.view.uimodel.recom.PlayLikeInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.PlayPinnedInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayQuickReplyInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayStatusUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoMetaInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoStreamUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.mapper.PlayChannelInteractiveMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveLeaderboardMapper
import com.tokopedia.play_common.model.ui.PlayLeaderboardWrapperUiModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kenny.hadisaputra on 17/03/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class PlayUserInteractionIdGenerator {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val mapper = PlayUiModelMapper(
        productTagMapper = PlayProductTagUiMapper(),
        merchantVoucherMapper = PlayMerchantVoucherUiMapper(),
        chatMapper = PlayChatUiMapper(userSession),
        channelStatusMapper = PlayChannelStatusMapper(),
        channelInteractiveMapper = PlayChannelInteractiveMapper(),
        interactiveLeaderboardMapper = PlayInteractiveLeaderboardMapper(),
        cartMapper = PlayCartMapper(),
        playUserReportMapper = PlayUserReportReasoningMapper()
    )

    private val mockViewModelFactory = TestViewModelFactory(
        mapOf(
            PlayInteractionViewModel::class.java to {
                mockk<PlayInteractionViewModel>(relaxed = true)
            },
            PlayBottomSheetViewModel::class.java to {
                PlayBottomSheetViewModel(
                    mockk(relaxed = true),
                    userSession,
                    CoroutineDispatchersProvider,
                    repo
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
                getSocketCredentialUseCase = mockk(relaxed = true),
                getReportSummariesUseCase = mockk(relaxed = true),
                trackVisitChannelBroadcasterUseCase = mockk(relaxed = true),
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
                )
            },
            PlayBottomSheetFragment::class.java to {
                PlayBottomSheetFragment(
                    viewModelFactory = mockViewModelFactory,
                    analytic = mockk(relaxed = true),
                )
            },
            PlayVideoFragment::class.java to {
                PlayVideoFragment(
                    dispatchers = CoroutineDispatchersProvider,
                    pipAnalytic = mockk(relaxed = true),
                    analytic = mockk(relaxed = true),
                    pipSessionStorage = mockk(relaxed = true),
                )
            }
        )
    )

    private val printConditions = listOf(
        PrintCondition { view ->
            val parent = (view.parent as? ViewGroup) ?: return@PrintCondition true
            val packageName = parent::class.java.`package`?.name.orEmpty()
            val className = parent::class.java.name
            !packageName.startsWith("com.tokopedia") || !className.contains("unify", ignoreCase = true)
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

    private val parentViewPrinter = ViewHierarchyPrinter(parentPrintCondition)
    private val viewPrinter = ViewHierarchyPrinter(printConditions)
    private val fileWriter = FileWriter()

    @Test
    fun inhousePlayer() {
        val mockChannelStorage = mockk<PlayChannelStateStorage>(relaxed = true)
        every { mockChannelStorage.getChannelList() } returns listOf("12669")
        every { mockChannelStorage.getData(any()) } returns PlayChannelData(
            id = "12669",
            channelDetail = PlayChannelDetailUiModel(),
            partnerInfo = PlayPartnerInfo(name = "test"),
            likeInfo = PlayLikeInfoUiModel(),
            channelReportInfo = PlayChannelReportUiModel(totalViewFmt = "1200"),
            pinnedInfo = PlayPinnedInfoUiModel(
                PinnedMessageUiModel("1", appLink = "", title = "Test pinned"),
            ),
            quickReplyInfo = PlayQuickReplyInfoUiModel(emptyList()),
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
            leaderboardInfo = PlayLeaderboardWrapperUiModel.Unknown,
            upcomingInfo = PlayUpcomingUiModel(),
            tagItems = TagItemUiModel.Empty,
            status = PlayStatusUiModel.Empty,
        )

        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(PlayTestModule(targetContext, mockChannelStorage))
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo))
                .build()
        )

        val intent = RouteManager.getIntent(
            targetContext,
            "tokopedia://play/12669"
        )
        val scenario = ActivityScenario.launch<PlayActivity>(intent)
        scenario.moveToState(Lifecycle.State.RESUMED)

        delay(1000)

        scenario.onActivity {
            val parent = parentViewPrinter.printAsCSV(
                view = (it.findViewById(android.R.id.content) as ViewGroup).getChildAt(0)
            )
            fileWriter.write(
                folderName = folderName,
                fileName = "play_parent.csv",
                text = parent
            )

            val interactionFragment = viewPrinter.printAsCSV(
                view = it.findViewById(R.id.fl_user_interaction)
            )
            fileWriter.write(
                folderName = folderName,
                fileName = "play_interaction.csv",
                text = interactionFragment
            )

            val videoFragment = viewPrinter.printAsCSV(
                view = it.findViewById(R.id.fl_video)
            )
            fileWriter.write(
                folderName = folderName,
                fileName = "play_video.csv",
                text = videoFragment
            )

            val bottomSheetFragment = viewPrinter.printAsCSV(
                view = it.findViewById(R.id.fl_bottom_sheet)
            )
            fileWriter.write(
                folderName = folderName,
                fileName = "play_bottom_sheet.csv",
                text = bottomSheetFragment
            )
        }
    }

    @Test
    fun youTubePlayer() {
        val mockChannelStorage = mockk<PlayChannelStateStorage>(relaxed = true)
        every { mockChannelStorage.getChannelList() } returns listOf("12669")
        every { mockChannelStorage.getData(any()) } returns PlayChannelData(
            id = "12669",
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
            leaderboardInfo = PlayLeaderboardWrapperUiModel.Unknown,
            upcomingInfo = PlayUpcomingUiModel(),
            tagItems = TagItemUiModel.Empty,
            status = PlayStatusUiModel.Empty,
        )

        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(PlayTestModule(targetContext, mockChannelStorage))
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo))
                .build()
        )

        val intent = RouteManager.getIntent(
            targetContext,
            "tokopedia://play/12669"
        )
        val scenario = ActivityScenario.launch<PlayActivity>(intent)
        scenario.moveToState(Lifecycle.State.RESUMED)

        delay(1000)

        scenario.onActivity {
            val youTubeFragment = viewPrinter.printAsCSV(
                view = it.findViewById(R.id.fl_youtube)
            )
            fileWriter.write(
                folderName = folderName,
                fileName = "play_youtube.csv",
                text = youTubeFragment
            )
        }
    }

    companion object {

        //private val dateFormatter = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault())
        //private val folderName = dateFormatter.format(Date())
        private val folderName = "res_id_result"
    }
}
