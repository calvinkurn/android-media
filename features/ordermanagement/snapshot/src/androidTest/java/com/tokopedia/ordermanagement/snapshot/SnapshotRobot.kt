package com.tokopedia.ordermanagement.snapshot

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import org.hamcrest.MatcherAssert

/**
 * Created by fwidjaja on 2/6/21.
 */
class SnapshotRobot {
    private fun waitForData() {
        Thread.sleep(5000)
    }

    fun loading() {
        waitForData()
    }

    fun clickShopArea() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Espresso.onView(ViewMatchers.withId(R.id.rv_snapshot))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                        clickOnViewChild(R.id.cl_shop)))
        waitForData()
    }

    fun clickBtnLihatHalamanProduk() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Espresso.onView(ViewMatchers.withId(R.id.btn_snapshot_to_pdp))
                .perform(ViewActions.click())
        waitForData()
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = ViewActions.click().perform(uiController, view.findViewById<View>(viewId))
    }

    infix fun submit(func: ResultRobot.() -> Unit): ResultRobot {
        return ResultRobot().apply(func)
    }
}

class ResultRobot {
    fun hasPassedAnalytics(repository: GtmLogDBSource, queryString: String) {
        MatcherAssert.assertThat(getAnalyticsWithQuery(repository, queryString), hasAllSuccess())
    }
}

fun runBot(func: SnapshotRobot.() -> Unit) = SnapshotRobot().apply(func)