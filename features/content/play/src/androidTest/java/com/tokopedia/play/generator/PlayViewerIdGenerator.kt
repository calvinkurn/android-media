package com.tokopedia.play.generator

import android.view.View
import android.view.ViewGroup
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.BuildConfig
import com.tokopedia.play.R
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.test.espresso.delay
import com.tokopedia.play.test.util.isSiblingWith
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel
import com.tokopedia.play.view.uimodel.mapper.*
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.interactive.LeaderboardUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds
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
class PlayViewerIdGenerator {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

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

        val mockChannelStorage = mockk<PlayChannelStateStorage>(relaxed = true)
        coEvery { repo.getTagItem(any()) } returns tagItem
        every { mockChannelStorage.getChannelList() } returns listOf("12669")
        every { mockChannelStorage.getData(any()) } returns uiModelBuilder.buildChannelData(
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
            upcomingInfo = PlayUpcomingUiModel(),
            tagItems = TagItemUiModel.Empty,
            status = PlayStatusUiModel.Empty,
            leaderboard = LeaderboardUiModel.Empty
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
            fileWriter.writeGeneratedViewIds(
                fileName = "play_youtube.csv",
                text = youTubeFragment
            )
        }
    }
}
