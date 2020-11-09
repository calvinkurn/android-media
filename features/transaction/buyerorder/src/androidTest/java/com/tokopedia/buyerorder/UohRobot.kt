package com.tokopedia.buyerorder

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.buyerorder.unifiedhistory.list.view.activity.UohListActivity
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.MatcherAssert

/**
 * Created by fwidjaja on 08/11/20.
 */
class UohRobot {
    fun launchFrom(rule: ActivityTestRule<UohListActivity>) {
        rule.launchActivity(null)
    }

    fun login(rule: ActivityTestRule<UohListActivity>) {
        InstrumentationAuthHelper.loginToAnUser(rule.activity.application)
    }
}

fun runBot(func: UohRobot.() -> Unit) = UohRobot().apply(func)

fun submit(func: ResultRobot.() -> Unit): ResultRobot {
    return ResultRobot().apply(func)
}

class ResultRobot {
    fun hasPassedAnalytics(repository: GtmLogDBSource, queryString: String) {
        MatcherAssert.assertThat(getAnalyticsWithQuery(repository, queryString), hasAllSuccess())
    }
}