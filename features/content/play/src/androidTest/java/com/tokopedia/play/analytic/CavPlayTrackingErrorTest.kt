package com.tokopedia.play.analytic

import android.view.View
import android.widget.FrameLayout
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.play.R
import com.tokopedia.play.analytic.robot.prepare
import com.tokopedia.play.data.PlayErrorMockModelConfig
import com.tokopedia.play.util.ComponentIdlingResource
import com.tokopedia.play.util.PlayIdlingResource
import com.tokopedia.play.util.register
import com.tokopedia.play.util.unregister
import com.tokopedia.play.view.activity.PlayActivity
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 15/09/20.
 */
class CavPlayTrackingErrorTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(PlayActivity::class.java, false, false)

    @Test
    fun validateTrackingChannelError() {
        prepare {
            setup(intentsTestRule)
            setMockModel(PlayErrorMockModelConfig())
            launch("1")
            setJsonAbsolutePath("tracker/content/play/play_error_analytic.json")
        } test {
            fakeLaunch()
            checkIsDisplayed()
            performClose()
            Thread.sleep(2000)
            validate()
        }
    }

    private fun checkIsDisplayed() {
        register(idlResGlobalError)
        Espresso.onView(ViewMatchers.withId(R.id.text_play_title)).check(ViewAssertions.matches(isDisplayed()))
        unregister(idlResGlobalError)
    }

    private fun performClose() {
        intentsTestRule.finishActivity()
    }

    private val idlResGlobalError by lazy { ComponentIdlingResource(
            object : PlayIdlingResource{
                override fun getName(): String = "showGlobalError"

                override fun idleState(): Boolean {
                    val view = intentsTestRule.activity.findViewById<FrameLayout>(R.id.fl_global_error)
                    return view.visibility == View.VISIBLE
                }
            }
    ) }
}