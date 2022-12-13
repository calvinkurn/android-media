package com.tokopedia.play.uitest.popup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.exoplayer2.Player
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.exoplayer.TestExoPlayer
import com.tokopedia.play.exoplayer.TestExoPlayerCreator
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.uitest.robot.PlayActivityRobot
import com.tokopedia.play.view.storage.PagingChannel
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 30/11/22
 */
/**
 * Find away to make sure follow sheet is visible [Video State]
 */
@UiTest
class PlayFollowPopupUiTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val socket: PlayWebSocket = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val channelId = "12665"
    private val duration = 10L

    private val mockUserSession = mockk<UserSessionInterface>(relaxed = true)
    private val mockPreference: PlayPreference = mockk(relaxed = true)

    private val exoPlayerCreator = TestExoPlayerCreator(targetContext)
    private val videoManager = PlayVideoWrapper.Builder(targetContext)
        .setExoPlayerCreator(exoPlayerCreator)
        .build()

    private val exoPlayer = videoManager.videoPlayer as TestExoPlayer

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

        exoPlayer.setState(true, Player.STATE_READY)

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
                        videoPlayer = PlayVideoPlayerUiModel.General.Complete(
                            params = PlayGeneralVideoPlayerParams(
                                videoUrl = "https://vod.tokopedia.com/view/adaptive.m3u8?id=4d30328d17e948b4b1c4c34c5bb9f372",
                                buffer = PlayBufferControl(),
                                lastMillis = null,
                            ),
                            exoPlayer = exoPlayer,
                            playerType = PlayerType.Client,
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
    }

    private fun createRobot() = PlayActivityRobot(channelId, 5000, isYouTube = false)

    @Test
    fun firstFetch_noOperation_moreThan24h() {
        val robot = createRobot()

        every { mockPreference.isFollowPopup(any()) } returns true
        coEvery { repo.getIsFollowingPartner(any()) } returns false

        /**
         * delay(duration)

        robot.isPopupShown(isShown = false)
         */
    }

    @Test
    fun firstFetch_noOperation_lessThan24h() {
        val robot = createRobot()

        every { mockPreference.isFollowPopup(any()) } returns false
        coEvery { repo.getIsFollowingPartner(any()) } returns false

        /**
         *  delay(duration)

        robot.isPopupShown()
         */
    }

    @Test
    fun openBottomSheet_thenClose() {
        val robot = createRobot()

        every { mockPreference.isFollowPopup(any()) } returns true
        coEvery { repo.getIsFollowingPartner(any()) } returns false

        /**
         * delay(duration)

        robot.openProductBottomSheet()
        robot.closeAnyBottomSheet()

        robot.isPopupShown()
         */
    }

    @Test
    fun already_followed() {
        val robot = createRobot()

        every { mockPreference.isFollowPopup(any()) } returns true
        coEvery { repo.getIsFollowingPartner(any()) } returns true

        /**
         * delay(duration)

        robot.isPopupShown(isShown = false)
         */
    }

    @Test
    fun openSharing_thenClose() {
        val robot = createRobot()

        every { mockPreference.isFollowPopup(any()) } returns true
        coEvery { repo.getIsFollowingPartner(any()) } returns false

        /**
         * delay(duration)

        robot.openSharingBottomSheet()
        robot.closeAnyBottomSheet()

        robot.isPopupShown()
         */
    }

    @Test
    fun openKebab_thenClose() {
        val robot = createRobot()

        every { mockPreference.isFollowPopup(any()) } returns true
        coEvery { repo.getIsFollowingPartner(any()) } returns false

        /**
         * delay(duration)

        robot.openKebabBottomSheet()
        robot.closeAnyBottomSheet()

        robot.isPopupShown()
         */
    }
}
