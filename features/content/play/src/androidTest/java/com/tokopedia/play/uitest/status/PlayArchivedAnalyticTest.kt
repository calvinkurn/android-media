package com.tokopedia.play.uitest.status

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.test.cassava.containsEvent
import com.tokopedia.content.test.cassava.containsEventAction
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.uitest.robot.PlayActivityRobot
import com.tokopedia.play.view.storage.PagingChannel
import com.tokopedia.play.view.uimodel.recom.PlayChannelStatus
import com.tokopedia.play.view.uimodel.recom.PlayStatusSource
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.websocket.PlayWebSocket
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 03/01/23
 */
class PlayArchivedAnalyticTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val channelId = "12669"

    private val socket: PlayWebSocket = mockk(relaxed = true)

    private fun createRobot() = PlayActivityRobot(channelId, 3000, isErrorPage = true, isYouTube = true)

    init {
        coEvery { repo.getChannels(any(), any()) } returns PagingChannel(
            channelList = listOf(
                uiModelBuilder.buildChannelData(
                    id = channelId,
                    tagItems = uiModelBuilder.buildTagItem(
                        product = uiModelBuilder.buildProductModel(
                            canShow = true,
                        )
                    ),
                    status = uiModelBuilder.buildStatus(
                        config = uiModelBuilder.buildStatusConfig(),
                        channelStatus = PlayChannelStatus(
                            statusType = PlayStatusType.Archived,
                            statusSource = PlayStatusSource.Network,
                            waitingDuration = 100
                        )
                    ),
                )
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
    }

    @Test
    fun impressArchived(){
        val robot = createRobot()
        robot.isErrorViewAvailable()

        assertCassavaByEvent("openScreen")
    }

    @Test
    fun exitArchived(){
        val robot = createRobot()
        robot.clickExitError()

        assertCassavaByEventAction("click - exit archive page")
    }

    @Test
    fun sendToPlayChannel(){
        val robot = createRobot()
        robot.clickGlobalErrorCta()

        assertCassavaByEventAction("click - to tokopedia play")
    }

    private fun assertCassavaByEventAction(eventAction: String) {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(ANALYTIC_FILE),
            containsEventAction(eventAction)
        )
    }

    private fun assertCassavaByEvent(event: String) {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(ANALYTIC_FILE),
            containsEvent(event)
        )
    }

    companion object {
        private const val ANALYTIC_FILE = "tracker/content/play/play_analytic.json"
    }
}
