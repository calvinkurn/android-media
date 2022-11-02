package com.tokopedia.play.uitest.transaction

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
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author by astidhiyaa on 02/11/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@UiTest
class PlayTransactionUiTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val channelId = "12665"

    private val mockUserSession = mockk<UserSessionInterface>(relaxed = true)

    init {
        coEvery {
            repo.getChannelList(
                any(),
                any()
            )
        } returns PlayViewerChannelRepository.ChannelListResponse(
            channelData = listOf(
                uiModelBuilder.buildChannelData(
                    channelDetail = PlayChannelDetailUiModel(
                        channelInfo = PlayChannelInfoUiModel(
                            id = channelId,
                            channelType = PlayChannelType.Live
                        )
                    ),
                    id = channelId,
                    status = uiModelBuilder.buildStatus(
                        channelStatus = PlayChannelStatus(
                            statusType = PlayStatusType.Active,
                            statusSource = PlayStatusSource.Network,
                            waitingDuration = 100
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
                            "", VideoOrientation.Vertical, "Iklan Popmie"
                        ),
                    ),
                )
            ),
            cursor = "",
        )

        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(PlayTestModule(targetContext, userSession = { mockUserSession }))
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo))
                .build()
        )
    }

    private fun createRobot() = PlayActivityRobot(channelId, 5000, isYouTube = false)

    @Test
    fun atc_with_occ () {
        val tagItems = uiModelBuilder.buildTagItem(
            product = uiModelBuilder.buildProductModel(
                canShow = true,
                productList = listOf(
                    uiModelBuilder.buildProductSection(
                        productList = listOf(
                            uiModelBuilder.buildProduct("Warung", buttons = listOf(uiModelBuilder.buildButton()))
                        )
                    )
                )
            )
        )

        coEvery { repo.getTagItem(any(), any()) } returns tagItems

        val robot = createRobot()
        robot.openProductBottomSheet()

        //assert button
    }
}
