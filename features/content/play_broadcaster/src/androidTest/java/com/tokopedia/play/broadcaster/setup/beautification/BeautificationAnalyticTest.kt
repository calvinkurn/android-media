package com.tokopedia.play.broadcaster.setup.beautification

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.effect.EffectManager
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.di.PlayBroadcastInjector
import com.tokopedia.play.broadcaster.di.PlayBroadcastModule
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.helper.PlayBroadcastActivityLauncher
import com.tokopedia.play.broadcaster.setup.accountListResponse
import com.tokopedia.play.broadcaster.setup.buildBroadcastingConfigUiModel
import com.tokopedia.play.broadcaster.setup.buildConfigurationUiModel
import com.tokopedia.play.broadcaster.setup.channelResponse
import com.tokopedia.play.broadcaster.setup.di.DaggerPlayBroadcastTestComponent
import com.tokopedia.play.broadcaster.setup.di.PlayBroadcastRepositoryTestModule
import com.tokopedia.play.broadcaster.setup.di.PlayBroadcastTestModule
import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUiModel
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
@CassavaTest
@RunWith(AndroidJUnit4ClassRunner::class)
class BeautificationAnalyticTest {

    private val beautificationRobot = BeautificationRobot()

    init {
        beautificationRobot.init()
    }

    @Before
    fun setUp() {
        beautificationRobot.setUp()
    }


    @Test
    fun testAnalytic_clickBeautificationEntryPointOnPreparationPage() {
        beautificationRobot.launch()
            .clickBeautificationMenu()
            .verifyEventAction("click - beautification entry point")
    }
}
