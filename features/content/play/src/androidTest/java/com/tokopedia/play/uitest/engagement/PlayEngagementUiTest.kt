package com.tokopedia.play.uitest.engagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerChannelRepository
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.uitest.robot.PlayActivityRobot
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayGeneralVideoPlayerParams
import com.tokopedia.play.view.uimodel.recom.PlayVideoMetaInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoStreamUiModel
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.unifycomponents.LoaderUnify
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
@UiTest
class PlayEngagementUiTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val channelId = "12665"

    init {
        coEvery { repo.getChannelList(any(), any()) } returns PlayViewerChannelRepository.ChannelListResponse(
            channelData = listOf(
                uiModelBuilder.buildChannelData(
                    id = channelId,
                    tagItems = uiModelBuilder.buildTagItem(
                        product = uiModelBuilder.buildProductModel(
                            canShow = true,
                        )
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
                )
            ),
            cursor = "",
        )

        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(PlayTestModule(targetContext))
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
        val items = uiModelBuilder.buildTagItem(
            voucher = uiModelBuilder.buildVoucherModel(
                voucherList = listOf(uiModelBuilder.buildMerchantVoucher(), uiModelBuilder.buildMerchantVoucher(highlighted = true))
            )
        )
        coEvery { repo.getTagItem(any(), any(), any()) } returns items
    }
}
