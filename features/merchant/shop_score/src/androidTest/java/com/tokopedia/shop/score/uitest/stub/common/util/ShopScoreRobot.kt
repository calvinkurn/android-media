package com.tokopedia.shop.score.uitest.stub.common.util

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.uitest.stub.performance.presentation.activity.ShopPerformanceActivityStub
import com.tokopedia.shop.score.uitest.stub.performance.presentation.fragment.ShopPerformanceFragmentStub
import com.tokopedia.unifycomponents.HtmlLinkHelper
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not

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

inline fun <reified T : Visitable<*>> ShopPerformanceActivityStub.scrollTo(isLastIndex: Boolean = false) {
    val fragment = getShopPerformanceFragment()
    val positionItem = if (isLastIndex) {
        fragment.shopPerformanceAdapter.list.indexOfLast { it is T }
    } else {
        fragment.shopPerformanceAdapter.list.indexOfFirst { it is T }
    }

    smoothScrollTo(positionItem)
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

fun ShopPerformanceActivityStub.getShopPerformanceFragment(): ShopPerformanceFragmentStub {
    return this.supportFragmentManager.findFragmentByTag("TAG_FRAGMENT") as ShopPerformanceFragmentStub
}