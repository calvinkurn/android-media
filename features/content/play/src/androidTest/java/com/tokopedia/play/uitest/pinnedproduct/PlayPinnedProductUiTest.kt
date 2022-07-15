package com.tokopedia.play.uitest.pinnedproduct

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.test.espresso.delay
import com.tokopedia.play.view.activity.PlayActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 15/07/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class PlayPinnedProductUiTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun pinnedProduct_noPinned() {
        val intent = RouteManager.getIntent(
            targetContext,
            "tokopedia://play/12669"
        )
        val scenario = ActivityScenario.launch<PlayActivity>(intent)
        scenario.moveToState(Lifecycle.State.RESUMED)

        delay(1000)
    }
}