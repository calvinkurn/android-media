package com.tokopedia.play.uitest.engagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.test.cassava.containsEventAction
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerChannelRepository
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.uitest.robot.PlayActivityRobot
import com.tokopedia.play.view.storage.PagingChannel
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketResponse
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * @author by astidhiyaa on 31/10/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@CassavaTest
class PlayEngagementAnalyticTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val channelId = "12665"

    private val channelInteractiveExistStatus = """
        {
          "type" : "CHANNEL_INTERACTIVE_STATUS",
          "data" : {
            "channel_id" : $channelId,
            "exist" : true 
          }
        }
    """.trimIndent()

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)
    private val socket: PlayWebSocket = mockk(relaxed = true)
    private val socketFlow = MutableStateFlow<WebSocketAction>(
        WebSocketAction.NewMessage(
            Gson().fromJson(channelInteractiveExistStatus, WebSocketResponse::class.java)
        )
    )

    private val mockUserSession = mockk<UserSessionInterface>(relaxed = true)
    private val mockRemoteConfig = mockk<RemoteConfig>(relaxed = true)

    init {
        coEvery { repo.getChannels(any(), any()) } returns PagingChannel(
            channelList = listOf(
                uiModelBuilder.buildChannelData(
                   channelDetail = PlayChannelDetailUiModel(
                        channelInfo = PlayChannelInfoUiModel(id = channelId, channelType = PlayChannelType.Live)
                    ),
                    id = channelId,
                    tagItems = uiModelBuilder.buildTagItem(
                        voucher = uiModelBuilder.buildVoucherModel(
                            voucherList = listOf(uiModelBuilder.buildMerchantVoucher(), uiModelBuilder.buildMerchantVoucher())
                        ),
                        product = uiModelBuilder.buildProductModel(
                            canShow = true,
                            productList = listOf(
                                uiModelBuilder.buildProductSection(
                                    productList = listOf(uiModelBuilder.buildProduct("Warung"))
                                )
                            )
                        )
                    ),
                    status = uiModelBuilder.buildStatus(channelStatus = PlayChannelStatus(statusType = PlayStatusType.Active, statusSource = PlayStatusSource.Network, waitingDuration = 100)),
                    videoMetaInfo = PlayVideoMetaInfoUiModel(
                      videoPlayer = PlayVideoPlayerUiModel.General.Incomplete(
                            params = PlayGeneralVideoPlayerParams(
                                videoUrl = "https://vod.tokopedia.com/view/adaptive.m3u8?id=4d30328d17e948b4b1c4c34c5bb9f372",
                                buffer = PlayBufferControl(),
                                lastMillis = null,
                            )
                        ),
                        videoStream = PlayVideoStreamUiModel(
                            "", VideoOrientation.Vertical, "Iklan Popmie"
                        ),
                    ),
                )
            ),
            cursor = "",
        )

        every { mockRemoteConfig.getBoolean(any(), any()) } returns true

        every { socket.listenAsFlow() } returns socketFlow

        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(PlayTestModule(targetContext, userSession = {mockUserSession}, remoteConfig = mockRemoteConfig))
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo, socket))
                .build()
        )
    }

    private fun createRobot() = PlayActivityRobot(channelId, 5000, isYouTube = false)

    @Test
    fun clickVoucher_openBottomSheetCoupon () {
        val tagItems = uiModelBuilder.buildTagItem(
            voucher = uiModelBuilder.buildVoucherModel(
                voucherList = listOf(uiModelBuilder.buildMerchantVoucher(), uiModelBuilder.buildMerchantVoucher(highlighted = true, id = "1234"))
            )
        )
        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItems

       val robot = createRobot()
        with(robot) {
            hasEngagement(isGame = false)
            assertCassavaByEventAction("view - voucher widget")
            clickEngagementWidget(0)
            assertCassavaByEventAction("click - voucher widget")
        }
    }

    @Test
    fun bottomSheet_privateVoucher() {
        val tagItems = uiModelBuilder.buildTagItem(
            voucher = uiModelBuilder.buildVoucherModel(
                voucherList = listOf(uiModelBuilder.buildMerchantVoucher
                    (highlighted = true, id = "1234", isPrivate = true, copyable = true, title = "Voucher KFC", description = "Diskon hingga 100rb", code = "ASHH"))
            )
        )
        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItems

        val robot = createRobot()
        with(robot) {
            hasEngagement(isGame = false)
            assertCassavaByEventAction("view - voucher widget")
            clickEngagementWidget(0)
            assertCassavaByEventAction("click - voucher widget")
            hasVoucherInBottomSheet()
            clickVoucherInBottomSheet(0)
            assertCassavaByEventAction("view - toaster private voucher")
//            clickToasterAction()
//            assertCassavaByEventAction("click - lihat toaster private voucher")
        }
    }

    @Test
    fun bottomSheet_publicVoucher() {
        val tagItems = uiModelBuilder.buildTagItem(
            voucher = uiModelBuilder.buildVoucherModel(
                voucherList = listOf(
                    uiModelBuilder.buildMerchantVoucher(highlighted = true, id = "1234", isPrivate = true, copyable = true, title = "Voucher KFC", description = "Diskon hingga 100rb", code = "ASHH"),
                    uiModelBuilder.buildMerchantVoucher(id = "1235",title = "Voucher Gopay", description = "Potongan hingga 100rb"))
            )
        )
        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItems

        val robot = createRobot()
        with(robot) {
            hasEngagement(isGame = false)
            hasVoucherInBottomSheet()
            clickEngagementWidget(0)
            assertCassavaByEventAction("click - voucher widget")
            clickVoucherInBottomSheet(1)
            assertCassavaByEventAction("view - toaster public voucher")
        }
    }

    @Test
    fun swipeEngagement() {
        val tagItems = uiModelBuilder.buildTagItem(
            voucher = uiModelBuilder.buildVoucherModel(
                voucherList = listOf(uiModelBuilder.buildMerchantVoucher(highlighted = true, id = "1234", title = "Voucher Upin Ipin"))
            )
        )

        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItems

        val game = uiModelBuilder.buildQuiz(id = "11", "Quiz Sepeda", status = GameUiModel.Quiz.Status.Ongoing(200L.millisFromNow()))

        coEvery { repo.getCurrentInteractive(any()) } returns game

        val robot = createRobot()
        robot.hasEngagement(isGame = true)
        robot.swipeEngagement(1)
    }

    private fun assertCassavaByEventAction(eventAction: String) {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(ANALYTIC_FILE),
            containsEventAction(eventAction)
        )
    }

    fun Long.millisFromNow(): Calendar {
        return Calendar.getInstance().apply {
            add(Calendar.MILLISECOND, this@millisFromNow.toInt())
        }
    }

    companion object {
        private const val ANALYTIC_FILE = "tracker/content/play/play_engagement_widget.json"
    }
}
