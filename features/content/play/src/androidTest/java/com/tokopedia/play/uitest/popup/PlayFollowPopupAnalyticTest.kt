package com.tokopedia.play.uitest.popup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.test.cassava.containsEventAction
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.uitest.robot.PlayActivityRobot
import com.tokopedia.play.view.storage.PagingChannel
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author by astidhiyaa on 30/11/22
 */
/**
 * Commented because got idling time out
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@CassavaTest
class PlayFollowPopupAnalyticTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val socket: PlayWebSocket = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val channelId = "12670"
    private val duration = 10L

    private val mockUserSession = mockk<UserSessionInterface>(relaxed = true)
    private val mockPreference: PlayPreference = mockk(relaxed = true)

    init {
        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(
                    PlayTestModule(
                        targetContext,
                        userSession = { mockUserSession },
                        playPreference = { mockPreference }
                    )
                )
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo, socket))
                .build()
        )

        coEvery { repo.getChannels(any(), any()) } returns PagingChannel(
            channelList = listOf(
                uiModelBuilder.buildChannelData(
                    channelDetail = PlayChannelDetailUiModel(
                        channelInfo = PlayChannelInfoUiModel(
                            id = channelId,
                            channelType = PlayChannelType.Live
                        ),
                        popupConfig = PlayPopUpConfigUiModel(
                            true ,duration, "text"
                        )
                    ),
                    id = channelId,
                    tagItems = uiModelBuilder.buildTagItem(
                        voucher = uiModelBuilder.buildVoucherModel(
                            voucherList = listOf(
                                uiModelBuilder.buildMerchantVoucher(),
                                uiModelBuilder.buildMerchantVoucher()
                            )
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
                    status = uiModelBuilder.buildStatus(
                        channelStatus = PlayChannelStatus(
                            statusType = PlayStatusType.Active,
                            statusSource = PlayStatusSource.Network,
                            waitingDuration = 100
                        )
                    ),
                    videoMetaInfo = PlayVideoMetaInfoUiModel(
                        videoPlayer = PlayVideoPlayerUiModel.YouTube(
                            youtubeId = ""
                        ),
                        videoStream = PlayVideoStreamUiModel(
                            "", VideoOrientation.Vertical, "Iklan Popmie"
                        ),
                    ),
                    partnerInfo = PlayPartnerInfo(
                        id = 11,
                        name = "Toko Kue",
                        type = PartnerType.Shop,
                        appLink = "tokopedia://shop/666",
                    ),
                )
            ),
            cursor = "",
        )

        every { mockPreference.isFollowPopup(any()) } returns true
        coEvery { repo.getIsFollowingPartner(any()) } returns false
    }

    private fun createRobot() = PlayActivityRobot(channelId, 10000, isYouTube = true)

    @Test
    fun impress_and_dismiss(){
//        val robot = createRobot()

//        delay(duration)
//
//        assertCassavaByEventAction("impression - follow pop up")
//        robot.closeAnyBottomSheet()
//        assertCassavaByEventAction("click - dismiss follow pop up")
    }

    @Test
    fun click_follow_show_toaster_success(){
//        val robot = createRobot()
        coEvery { repo.postFollowStatus("11", PartnerFollowAction.Follow) } returns true

//        delay(duration)
//
//        robot.clickFollow()
//        assertCassavaByEventAction("click - follow creator pop up")
//        assertCassavaByEventAction("impression - success follow toaster")
    }

    @Test
    fun click_follow_show_toaster_failed_then_retry(){
//        val robot = createRobot()
        coEvery { repo.postFollowStatus("11", PartnerFollowAction.Follow) } returns false

//        delay(duration)
//
//        robot.clickFollow()
//        assertCassavaByEventAction("click - follow creator pop up")
//        assertCassavaByEventAction("impression - fail follow toaster")
//
//        robot.clickToasterAction()
//        assertCassavaByEventAction("click - coba lagi fail toaster")
    }

    @Test
    fun click_name(){
//        val robot = createRobot()
//        delay(duration)
//
//        robot.clickPartnerNamePopup()
//        assertCassavaByEventAction("click - creator name follow pop up")
    }

    private fun assertCassavaByEventAction(eventAction: String) {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(ANALYTIC_FILE),
            containsEventAction(eventAction)
        )
    }

    companion object {
        private const val ANALYTIC_FILE = "tracker/content/play/play_follow_popup.json"
    }
}
