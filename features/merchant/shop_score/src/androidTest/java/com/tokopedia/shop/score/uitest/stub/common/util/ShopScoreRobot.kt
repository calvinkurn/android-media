package com.tokopedia.shop.score.uitest.stub.common.util

import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.score.uitest.stub.performance.presentation.activity.ShopPerformanceActivityStub
import com.tokopedia.shop.score.uitest.stub.performance.presentation.fragment.ShopPerformanceFragmentStub
import org.hamcrest.CoreMatchers.allOf


fun ViewInteraction.isGone() = getViewAssertion(ViewMatchers.Visibility.GONE)

fun ViewInteraction.isVisible() = getViewAssertion(ViewMatchers.Visibility.VISIBLE)

fun ViewInteraction.isInvisible() = getViewAssertion(ViewMatchers.Visibility.INVISIBLE)

private fun getViewAssertion(visibility: ViewMatchers.Visibility): ViewAssertion? {
    return ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(visibility))
}

fun onIdView(id: Int): ViewInteraction = onView(withId(id))
fun onWithText(text: String?): ViewInteraction = onView(allOf(withText(text)))

fun onContentDescPopup(string: String?): ViewInteraction =
    onView(withText(string))
        .inRoot(RootMatchers.isPlatformPopup())


fun ViewInteraction.isViewDisplayed(): ViewInteraction = check(matches(isDisplayed()))
fun ViewInteraction.onClick(): ViewInteraction = perform(click())
fun ViewInteraction.withTextStr(text: String?): ViewInteraction = check(matches(withText(text)))


inline fun <reified T : Visitable<*>> ShopPerformanceActivityStub.scrollTo(isLastIndex: Boolean = false) {
    val fragment =
        this.supportFragmentManager.findFragmentByTag("TAG_FRAGMENT") as ShopPerformanceFragmentStub
    val positionItem = if (isLastIndex) {
        fragment.shopPerformanceAdapter.list.indexOfLast { it is T }
    } else {
        fragment.shopPerformanceAdapter.list.indexOfFirst { it is T }
    }

    fragment.smoothScroll(positionItem)
}

inline fun <reified T : Visitable<*>> ShopPerformanceActivityStub.scrollToWithPos(position: Int) {
    val fragment =
        this.supportFragmentManager.findFragmentByTag("TAG_FRAGMENT") as ShopPerformanceFragmentStub
    val positionItem =
            fragment.shopPerformanceAdapter.list.indexOfFirst { it is T } + position
    fragment.smoothScroll(positionItem)
}


fun ShopPerformanceFragmentStub.smoothScroll(positionItem: Int) {
    if (positionItem != RecyclerView.NO_POSITION) {
        val smoothScroller: RecyclerView.SmoothScroller =
            object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_END
                }
            }
        smoothScroller.targetPosition = positionItem
        binding?.rvShopPerformance?.layoutManager?.startSmoothScroll(smoothScroller)
    }
}