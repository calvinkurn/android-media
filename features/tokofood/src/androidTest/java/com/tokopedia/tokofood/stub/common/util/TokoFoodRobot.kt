package com.tokopedia.tokofood.stub.common.util

import android.app.Activity
import android.content.Context
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.tokofood.feature.ordertracking.presentation.partialview.OrderDetailStickyActionButton
import com.tokopedia.tokofood.stub.base.presentation.activity.BaseTokofoodActivityStub
import com.tokopedia.tokofood.stub.postpurchase.presentation.activity.TokoFoodOrderTrackingActivityStub
import com.tokopedia.tokofood.stub.postpurchase.presentation.fragment.BaseTokoFoodOrderTrackingFragmentStub
import com.tokopedia.tokofood.stub.purchase.presentation.fragment.TokoFoodPurchaseFragmentStub
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun onIdView(id: Int): ViewInteraction =
    Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(id)))

fun ViewInteraction.isViewDisplayed(): ViewInteraction = check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
fun ViewInteraction.isViewNotDisplayed(): ViewInteraction = check(
    ViewAssertions.matches(
        CoreMatchers.not(ViewMatchers.isDisplayed())
    )
)
fun ViewInteraction.onClick(): ViewInteraction = perform(ViewActions.click())
fun ViewInteraction.withTextStr(text: String?): ViewInteraction = check(
    ViewAssertions.matches(
        ViewMatchers.withText(text)
    )
)

fun getTextHtml(context: Context, text: String): CharSequence? {
    val htmlString = HtmlLinkHelper(context, text)
    return htmlString.spannedString
}

fun getHyperlinkText(context: Context, text: String): String {
    val htmlString = HtmlLinkHelper(context, text)
    return htmlString.urlList.getOrNull(0)?.linkText.orEmpty()
}

inline fun <reified T : Visitable<*>> Activity.scrollTo(isLastIndex: Boolean = false, recyclerViewId: Int) {
    val fragment = (this as TokoFoodOrderTrackingActivityStub).getBaseTokoFoodOrderTrackingFragment()
    val positionItem = if (isLastIndex) {
        fragment.orderTrackingAdapter.list.indexOfLast { it is T }
    } else {
        fragment.orderTrackingAdapter.list.indexOfFirst { it is T }
    }

    smoothScrollTo(positionItem, recyclerViewId)
}

inline fun <reified T : Visitable<*>> Activity.purchasePageScrollTo(isLastIndex: Boolean = false, recyclerViewId: Int) {
    val fragment = (this as BaseTokofoodActivityStub).getCheckoutFragment()
    val positionItem = if (isLastIndex) {
        fragment.getCurrentAdapter()?.list?.indexOfLast { it is T } ?: RecyclerView.NO_POSITION
    } else {
        fragment.getCurrentAdapter()?.list?.indexOfFirst { it is T } ?: RecyclerView.NO_POSITION
    }

    smoothScrollTo(positionItem, recyclerViewId)
}

fun smoothScrollTo(positionItem: Int, recyclerViewId: Int) {
    if (positionItem != RecyclerView.NO_POSITION) {
        onIdView(recyclerViewId).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                positionItem,
                ViewActions.scrollTo()
            )
        )
    }
}

fun withButtonInsideStickyButtonView(idView: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("with button inside sticky button view")
        }

        override fun matchesSafely(view: View): Boolean {
            if (view is OrderDetailStickyActionButton) {
                val button = view.findViewById<UnifyButton>(idView)
                return button != null
            }
            return false
        }
    }
}

fun clickStickyButtonButton(idView: Int) {
    clickView(CommonMatcher.firstView(ViewMatchers.withId(idView)))
}

private fun clickView(matcher: Matcher<View>) {
    Espresso.onView(matcher).perform(ViewActions.click())
}

fun clickClickableSpan(textToClick: CharSequence): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return CoreMatchers.instanceOf(TextView::class.java)
        }

        override fun getDescription(): String {
            return "clicking on a ClickableSpan"
        }

        override fun perform(uiController: UiController?, view: View) {
            val textView = view as TextView
            val spannableString = textView.text as SpannableString
            if (spannableString.isEmpty()) {
                // TextView is empty, nothing to do
                throw NoMatchingViewException.Builder()
                    .includeViewHierarchy(true)
                    .withRootView(textView)
                    .build()
            }

            // Get the links inside the TextView and check if we find textToClick
            val spans = spannableString.getSpans(0, spannableString.length, ClickableSpan::class.java)
            if (spans.isNotEmpty()) {
                var spanCandidate: ClickableSpan?
                for (span in spans) {
                    spanCandidate = span
                    val start = spannableString.getSpanStart(spanCandidate)
                    val end = spannableString.getSpanEnd(spanCandidate)
                    val sequence = spannableString.subSequence(start, end)
                    if (textToClick.toString() == sequence.toString()) {
                        span.onClick(textView)
                        return
                    }
                }
            }
            throw NoMatchingViewException.Builder()
                .includeViewHierarchy(true)
                .withRootView(textView)
                .build()
        }
    }
}

fun TokoFoodOrderTrackingActivityStub.getBaseTokoFoodOrderTrackingFragment(): BaseTokoFoodOrderTrackingFragmentStub {
    return this.supportFragmentManager.findFragmentByTag("TAG_FRAGMENT") as BaseTokoFoodOrderTrackingFragmentStub
}

fun BaseTokofoodActivityStub.getCheckoutFragment(): TokoFoodPurchaseFragmentStub {
    val fragment = this.supportFragmentManager.fragments.firstOrNull()
    return this.supportFragmentManager.findFragmentByTag(fragment?.javaClass?.name.orEmpty()) as TokoFoodPurchaseFragmentStub
}
