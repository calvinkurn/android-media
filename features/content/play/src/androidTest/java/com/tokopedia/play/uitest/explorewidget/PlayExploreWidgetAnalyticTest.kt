package com.tokopedia.play.uitest.explorewidget

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
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * @author by astidhiyaa on 06/01/23
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@CassavaTest
class PlayExploreWidgetAnalyticTest {
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

    init {
        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(PlayTestModule(targetContext, userSession = {mockUserSession}, remoteConfig = mockRemoteConfig))
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo, socket))
                .build()
        )
    }

    private fun createRobot() = PlayActivityRobot(channelId, 5000, isYouTube = false)

    private fun assertCassavaByEventAction(eventAction: String) {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(ANALYTIC_FILE),
            containsEventAction(eventAction)
        )
    }
    companion object {
        private const val ANALYTIC_FILE = "tracker/content/play/play_explore_widget_analytic.json"
    }
}
