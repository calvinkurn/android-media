package com.tokopedia.play

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import androidx.appcompat.widget.AppCompatTextView
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.play.data.PlayMockModelConfig
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 15/05/20.
 */
class PltPlayPerformanceTest {

    @get:Rule
    var intentsTestRule: IntentsTestRule<PlayActivity> = IntentsTestRule(PlayActivity::class.java, false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()


    @Before
    fun setup() {
        setupGraphqlMockResponseWithCheck(PlayMockModelConfig())
    }

    @Test
    fun testPageLoadTimePerformance() {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        intentsTestRule.launchActivity(Intent(targetContext, PlayActivity::class.java).apply {
            data = Uri.parse("${ApplinkConstInternalContent.INTERNAL_PLAY}/2413")
        })
        IdlingRegistry.getInstance().register(idlingResource)

        onIdle()
        intentsTestRule.activity.getPltPerformanceResultData()?.let { data->
            PerformanceDataFileUtils.writePLTPerformanceFile(
                    intentsTestRule.activity,
                    TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE,
                    data)
        }
    }

    @After
    fun tearDown() {
        intentsTestRule.activity.finishAndRemoveTask()
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    private val idlingResource: IdlingResource by lazy {
        object : IdlingResource {
            override fun getName(): String = "prepare"

            private var callback: IdlingResource.ResourceCallback? = null

            override fun isIdleNow(): Boolean {
                val textView = intentsTestRule.activity.findViewById<AppCompatTextView>(R.id.tv_partner_name)
                val isIdle =  !TextUtils.isEmpty(textView.text.toString())
                if (isIdle) callback?.onTransitionToIdle()
                return isIdle
            }

            override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
                this.callback = callback
            }
        }
    }

    companion object {
        const val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "play_test_case_page_load_time"
    }
}