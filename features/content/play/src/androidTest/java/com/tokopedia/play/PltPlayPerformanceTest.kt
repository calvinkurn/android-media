package com.tokopedia.play

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.play.data.PlayMockModelConfig
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import com.tokopedia.unifycomponents.LoaderUnify
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 15/05/20.
 */
class PltPlayPerformanceTest {

    @get:Rule
    var activityTestRule: ActivityTestRule<PlayActivity> = ActivityTestRule(PlayActivity::class.java, false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    private val idlingResource: IdlingResource by lazy {
        object : IdlingResource {
            override fun getName(): String = "prepare"

            private var callback: IdlingResource.ResourceCallback? = null

            override fun isIdleNow(): Boolean {
                val textView = activityTestRule.activity.findViewById<AppCompatTextView>(R.id.tv_follow)
                val isIdle =  textView.isVisible
                if (isIdle) callback?.onTransitionToIdle()
                return isIdle
            }

            override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
                this.callback = callback
            }
        }
    }

    @Before
    fun setup() {
        setupGraphqlMockResponseWithCheck(PlayMockModelConfig())
    }

    @Test
    fun testPageLoadTimePerformance() {
        launchActivity()

        onView(withId(R.id.loader_page)).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(LoaderUnify::class.java)
            }

            override fun getDescription(): String {
                return "hide loader unify"
            }

            override fun perform(uiController: UiController?, view: View?) {
                view?.hide()
            }
        })

        IdlingRegistry.getInstance().register(idlingResource)

        onIdle()
        Thread.sleep(2000)
        getPerformanceReport()

        clearTask()
    }

    private fun launchActivity() {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        activityTestRule.launchActivity(Intent(targetContext, PlayActivity::class.java).apply {
            data = Uri.parse("${ApplinkConstInternalContent.INTERNAL_PLAY}/15774")
        })
    }

    private fun clearTask() {
        activityTestRule.activity.finishAndRemoveTask()
    }

    private fun getPerformanceReport() {
        activityTestRule.activity.getPltPerformanceResultData()?.let { data->
            PerformanceDataFileUtils.writePLTPerformanceFile(
                    activityTestRule.activity,
                    TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE,
                    data)
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    companion object {
        const val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "play_test_case_page_load_time"
    }
}