package com.tokopedia.play.uitest.engagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.content.test.cassava.containsEventAction
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerChannelRepository
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.uitest.robot.PlayActivityRobot
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.tokopedia.play.R as playR

/**
 * @author by astidhiyaa on 31/10/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@CassavaTest
class PlayEngagementUiTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val channelId = "12665"

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val mockUserSession = mockk<UserSessionInterface>(relaxed = true)

    init {
        coEvery { repo.getChannelList(any(), any()) } returns PlayViewerChannelRepository.ChannelListResponse(
            channelData = listOf(
                uiModelBuilder.buildChannelData(
                   channelDetail = PlayChannelDetailUiModel(
                        channelInfo = PlayChannelInfoUiModel(id = channelId, channelType = PlayChannelType.VOD)
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
                            "", VideoOrientation.Vertical, "Video Keren"
                        ),
                    ),
                )
            ),
            cursor = "",
        )

        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(PlayTestModule(targetContext, userSession = {mockUserSession}))
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo))
                .build()
        )
    }

    private fun createRobot() = PlayActivityRobot(channelId, 5000, isYouTube = false)

    private fun turnOffLoader() {
        val robot = createRobot()
        robot.scenario.onActivity {
            it.findViewById<LoaderUnify>(playR.id.iv_loading).apply {
                this.avd?.stop()
            }
        }
    }

    @Test
    fun clickVoucher_openBottomSheetCoupon () {
        val tagItems = uiModelBuilder.buildTagItem(
            voucher = uiModelBuilder.buildVoucherModel(
                voucherList = listOf(uiModelBuilder.buildMerchantVoucher(), uiModelBuilder.buildMerchantVoucher(highlighted = true, id = "1234"))
            )
        )
        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItems

       val robot = createRobot()
        robot.hasEngagement(isGame = false)
        assertCassavaByEventAction("view - voucher widget")
        robot.clickEngagementWidget(0)
        assertCassavaByEventAction("click - voucher widget")
    }

    private fun assertCassavaByEventAction(eventAction: String) {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(ANALYTIC_FILE),
            containsEventAction(eventAction)
        )
    }

    companion object {
        private const val ANALYTIC_FILE = "tracker/content/play/play_engagement_widget.json"
    }
}
