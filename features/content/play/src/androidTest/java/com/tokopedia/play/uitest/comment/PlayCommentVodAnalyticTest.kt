package com.tokopedia.play.uitest.comment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.test.cassava.containsEventAction
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.uitest.robot.PlayActivityRobot
import com.tokopedia.play.view.storage.PagingChannel
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.play.model.UiModelBuilder
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.tokopedia.content.common.R as contentR

/**
 * @author by astidhiyaa on 29/03/23
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@CassavaTest
class PlayCommentVodAnalyticTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val channelId = "12665"

    private val socket: PlayWebSocket = mockk(relaxed = true)
    private val mockUserSession = mockk<UserSessionInterface>(relaxed = true)
    private val mockRemoteConfig = mockk<RemoteConfig>(relaxed = true)

    private val modelBuilder = UiModelBuilder.get()

    init {
        coEvery { repo.getChannels(any(), any()) } returns PagingChannel(
            channelList = listOf(
                modelBuilder.buildChannelData(
                    channelDetail = PlayChannelDetailUiModel(
                        channelInfo = PlayChannelInfoUiModel(id = channelId, channelType = PlayChannelType.VOD)
                    ),
                    id = channelId,
                    tagItems = modelBuilder.buildTagItem(
                        voucher = modelBuilder.buildVoucherModel(
                            voucherList = listOf(modelBuilder.buildMerchantVoucher(), modelBuilder.buildMerchantVoucher())
                        ),
                        product = modelBuilder.buildProductModel(
                            canShow = true,
                            productList = listOf(
                                modelBuilder.buildProductSection(
                                    productList = listOf(modelBuilder.buildProduct("Warung"))
                                )
                            )
                        )
                    ),
                    status = modelBuilder.buildStatus(channelStatus = PlayChannelStatus(statusType = PlayStatusType.Active, statusSource = PlayStatusSource.Network, waitingDuration = 100)),
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

        coEvery { repo.getCountComment(any()) } returns PlayCommentUiModel(shouldShow = true, total = "12k")

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
    fun clickCommentIcon() {
        val robot = createRobot()
        robot.openCommentBottomSheet()

        assertCassavaByEventAction("click - comment button")
    }

    @Test
    fun clickSendReply() {
        val robot = createRobot()
        robot.clickWithId(contentR.id.iv_comment_send)

        assertCassavaByEventAction("click - reply button")
    }

    @Test
    fun clickCloseBottomSheet() {
        val robot = createRobot()
        robot.clickWithId(contentR.id.iv_sheet_close)

        assertCassavaByEventAction("click - close comment bottomsheet")
    }

    @Test
    fun clickCommentOwnerName() {
        val robot = createRobot()
        robot.clickWithId(contentR.id.tv_comment_content)

        //ask / find click spannable in ui test

        assertCassavaByEventAction("click - name commenter")
    }

    @Test
    fun clickProfPictOwner() {
        val robot = createRobot()
        robot.clickWithId(contentR.id.iv_comment_photo)

        assertCassavaByEventAction("click - profile picture commenter")
    }

    @Test
    fun impressAndClickExpandComment() {
        //initial state
        val robot = createRobot()
        robot.impressIsAvailable(contentR.id.tv_comment_expandable)
        robot.clickWithId(contentR.id.tv_comment_expandable)

        assertCassavaByEventAction("view - lihat x balasan")
        assertCassavaByEventAction("click - lihat x balasan")
    }

    @Test
    fun impressAndClickHideComment() {
        //after click expand with no next page
        val robot = createRobot()
        robot.impressIsAvailable(contentR.id.tv_comment_expandable)
        robot.clickWithId(contentR.id.tv_comment_expandable)

        assertCassavaByEventAction("click - sembunyikan comment")
    }

    @Test
    fun clickTextBox() {
        val robot = createRobot()
        robot.clickWithId(contentR.id.new_comment)

        assertCassavaByEventAction("click - text box")
    }

    @Test
    fun clickSendMainComment() {
        val robot = createRobot()
        robot.typeInEditText(text = "Ini main comment", viewId = contentR.id.new_comment)

        assertCassavaByEventAction("click - send main comment")
    }

    @Test
    fun clickSendChildComment() {
        //type after span?
        val robot = createRobot()
        robot.typeInEditText(text = "Ini child comment", viewId = contentR.id.tv_comment_reply)

        assertCassavaByEventAction("click - send reply comment")
    }

    @Test
    fun longClickComment() {
        //open three dots bottomsheet
        assertCassavaByEventAction("click - long press slide comment")
    }

    @Test
    fun removeComment() {
        assertCassavaByEventAction("click - hapus comment")
    }

    @Test
    fun reportComment() {
        assertCassavaByEventAction("click - laporkan comment")
    }

    @Test
    fun reportItemComment() {
        assertCassavaByEventAction("click - reason laporkan comment")
    }

    @Test
    fun impressSuccessReport() {
        val robot = createRobot()
        robot.impressIsAvailable(contentR.id.report_final_layout)
        assertCassavaByEventAction("view - success report comment")
    }

    private fun assertCassavaByEventAction(eventAction: String) {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(ANALYTIC_FILE),
            containsEventAction(eventAction)
        )
    }

    companion object {
        private const val ANALYTIC_FILE = "tracker/content/play/play_comment_vod_analytics.json"
    }

}
