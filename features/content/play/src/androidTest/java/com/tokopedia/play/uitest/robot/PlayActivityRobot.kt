package com.tokopedia.play.uitest.robot

import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.R
import com.tokopedia.play.test.espresso.clickOnViewChild
import com.tokopedia.play.test.espresso.delay
import com.tokopedia.play.ui.view.carousel.viewholder.ProductCarouselViewHolder
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import org.hamcrest.CoreMatchers.allOf
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

    fun openProductBottomSheet() = chainable {
        Espresso.onView(
            withId(R.id.view_product_see_more)
        ).perform(ViewActions.click())
    }

    fun scrollProductBottomSheet(position: Int) = chainable {
        Espresso.onView(
            withId(R.id.rv_product_list)
        ).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    fun clickPinnedProductCarousel() = chainable {
        Espresso.onView(
            withId(R.id.rv_product_featured)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductCarouselViewHolder.PinnedProduct>(
                0, ViewActions.click()
            )
        )
    }

    fun clickBuyPinnedProductCarousel() = chainable {
        Espresso.onView(
            withId(R.id.rv_product_featured)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductCarouselViewHolder.PinnedProduct>(
                0, clickOnViewChild(R.id.btn_buy)
            )
        )
    }

    fun clickAtcPinnedProductCarousel() = chainable {
        Espresso.onView(
            withId(R.id.rv_product_featured)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductCarouselViewHolder.PinnedProduct>(
                0, clickOnViewChild(R.id.btn_atc)
            )
        )
    }

    fun clickToasterAction() = chainable {
        Espresso.onView(
            withId(R.id.snackbar_btn)
        ).perform(
            ViewActions.click()
        )
    }

    fun wait(delayInMillis: Long = 500) = chainable {
        delay(delayInMillis)
    }

    /**
     * Assertion
     */
    fun assertHasPinnedItemInCarousel(
        hasPinned: Boolean,
        productName: String? = null,
    ) = chainable {
        val viewMatcher = hasDescendant(withText(containsString("Pin Dipasang")))
        val interaction = Espresso.onView(
            RecyclerViewMatcher(R.id.rv_product_featured)
                .atPosition(0)
        ).check(
            if (hasPinned) matches(viewMatcher)
            else matches(not(viewMatcher))
        )

        if (!hasPinned || productName == null) return@chainable
        interaction.check(
            matches(
                hasDescendant(withText(containsString(productName)))
            )
        )
    }

    fun assertHasPinnedItemInProductBottomSheet(hasPinned: Boolean) = chainable {
        val viewMatcher = hasDescendant(withText(containsString("Pin Dipasang")))
        Espresso.onView(
            RecyclerViewMatcher(R.id.rv_product_list)
                .atPosition(0)
        ).check(
            if (hasPinned) matches(viewMatcher)
            else matches(not(viewMatcher))
        )
    }

    fun assertHasPinnedItemInProductBottomSheet(
        productName: String,
        hasPinned: Boolean,
    ) = chainable {
        val viewMatcher = hasDescendant(withText(containsString("Pin Dipasang")))

        Espresso.onView(
            allOf(
                withId(R.id.rv_product_list),
                hasDescendant(withText(containsString(productName)))
            )
        ).check(
            if (hasPinned) matches(viewMatcher)
            else matches(not(viewMatcher))
        )
    }

    private fun chainable(fn: () -> Unit): PlayActivityRobot {
        fn()
        return this
    }
}