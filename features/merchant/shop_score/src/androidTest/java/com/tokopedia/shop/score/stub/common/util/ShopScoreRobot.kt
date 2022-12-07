package com.tokopedia.shop.score.stub.common.util

import android.content.Context
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.stub.penalty.presentation.activity.ShopPenaltyPageActivityStub
import com.tokopedia.shop.score.stub.penalty.presentation.fragment.ShopPenaltyPageFragmentStub
import com.tokopedia.shop.score.stub.performance.presentation.activity.ShopPerformanceActivityStub
import com.tokopedia.shop.score.stub.performance.presentation.fragment.ShopPerformanceFragmentStub
import com.tokopedia.unifycomponents.HtmlLinkHelper
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

fun onIdView(id: Int): ViewInteraction = onView(allOf(withId(id)))

fun onContentDescPopup(string: String?): ViewInteraction =
    onView(withText(string))
        .inRoot(RootMatchers.isPlatformPopup())


fun ViewInteraction.isViewDisplayed(): ViewInteraction = check(matches(isDisplayed()))
fun ViewInteraction.isViewNotDisplayed(): ViewInteraction = check(matches(not(isDisplayed())))
fun ViewInteraction.onClick(): ViewInteraction = perform(click())
fun ViewInteraction.withTextStr(text: String?): ViewInteraction = check(matches(withText(text)))


fun getTextHtml(context: Context, text: String): CharSequence? {
    val htmlString = HtmlLinkHelper(context, text)
    return htmlString.spannedString
}

fun getHyperlinkText(context: Context, text: String): String {
    val htmlString = HtmlLinkHelper(context, text)
    return htmlString.urlList.getOrNull(0)?.linkText.orEmpty()
}

inline fun <reified T : Visitable<*>> ShopPerformanceActivityStub.scrollTo(isLastIndex: Boolean = false) {
    val fragment = getShopPerformanceFragment()
    val positionItem = if (isLastIndex) {
        fragment.shopPerformanceAdapter.list.indexOfLast { it is T }
    } else {
        fragment.shopPerformanceAdapter.list.indexOfFirst { it is T }
    }

    smoothScrollTo(positionItem)
}

inline fun <reified T : Visitable<*>> ShopPenaltyPageActivityStub.scrollTo() {
    val fragment = getShopPenaltyFragment()
    val positionItem = fragment.penaltyPageAdapter.list.indexOfFirst { it is T }
    smoothScrollToPenalty(positionItem)
}

fun smoothScrollToPenalty(positionItem: Int) {
    if (positionItem != RecyclerView.NO_POSITION) {
        onIdView(R.id.rvPenaltyPage).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                positionItem, scrollTo()
            )
        )
    }
}

fun smoothScrollTo(positionItem: Int) {
    if (positionItem != RecyclerView.NO_POSITION) {
        onIdView(R.id.rvShopPerformance).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                positionItem, scrollTo()
            )
        )
    }
}

fun smoothScrollToFaq(positionItem: Int) {
    if (positionItem != RecyclerView.NO_POSITION) {
        onIdView(R.id.rv_faq_shop_score).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                positionItem, scrollTo()
            )
        )
    }
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

fun ShopPerformanceActivityStub.getShopPerformanceFragment(): ShopPerformanceFragmentStub {
    return this.supportFragmentManager.findFragmentByTag("TAG_FRAGMENT") as ShopPerformanceFragmentStub
}

fun ShopPenaltyPageActivityStub.getShopPenaltyFragment(): ShopPenaltyPageFragmentStub {
    return this.supportFragmentManager.findFragmentByTag("TAG_FRAGMENT") as ShopPenaltyPageFragmentStub
}
