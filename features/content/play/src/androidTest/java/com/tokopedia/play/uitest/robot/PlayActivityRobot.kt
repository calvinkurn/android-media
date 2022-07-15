package com.tokopedia.play.uitest.robot

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.R
import com.tokopedia.play.test.espresso.delay
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not

/**
 * Created by kenny.hadisaputra on 15/07/22
 */
class PlayActivityRobot(
    channelId: String,
) {

    private val context = InstrumentationRegistry.getInstrumentation().context

    private val intent = RouteManager.getIntent(
        context,
        "tokopedia://play/${channelId}"
    )
    val scenario = ActivityScenario.launch<PlayActivity>(intent)

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)

        delay(1000)
    }

    fun assertHasPinnedItemInCarousel(hasPinned: Boolean) {
        val viewMatcher = hasDescendant(withText(containsString("Pin Dipasang")))
        Espresso.onView(
            RecyclerViewMatcher(R.id.rv_product_featured)
                .atPosition(0)
        ).check(
            if (hasPinned) matches(viewMatcher)
            else matches(not(viewMatcher))
        )
    }
}