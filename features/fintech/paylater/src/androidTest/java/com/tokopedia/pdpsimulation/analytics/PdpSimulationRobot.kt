package com.tokopedia.pdpsimulation.analytics

import android.content.Context
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.pdpsimulation.paylater.presentation.registration.PayLaterPaymentMethodViewHolder
import com.tokopedia.pdpsimulation.test.R
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.unifycomponents.ContentSwitcherUnify
import com.tokopedia.unifycomponents.TabsUnify
import org.hamcrest.core.AllOf

class PdpSimulationRobot {

    fun testClickTabs(tabIndex: Int) {
        onView(CommonMatcher.firstView(withId(R.id.paylaterTabLayout)))
                .perform(selectTabAtPosition(tabIndex))
    }

    fun testClickPayLaterItem(itemPosition: Int) {
        onView(withId(R.id.baseList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<PayLaterPaymentMethodViewHolder>(itemPosition, ViewActions.click()))
        onView(CommonMatcher.firstView(AllOf.allOf(
                withId(R.id.btnRegister),
                isDisplayed()))
        ).perform(ViewActions.click())
    }


    fun testPayLaterProductActionImpressions() {
        onView(CommonMatcher.firstView(withId(R.id.paymentOptionViewPager)))
                .perform(setMargin())
        onView(CommonMatcher.firstView(AllOf.allOf(
                withId(R.id.btnHowToUse)))
        ).perform(ViewActions.click())
    }

    fun testClickRegisterWidget() {
        onView(CommonMatcher.firstView(AllOf.allOf(
                withId(R.id.paylaterDaftarWidget),
                isDisplayed()))
        ).perform(ViewActions.click())
    }

    infix fun assertTest(action: PdpSimulationRobot.() -> Unit) = PdpSimulationRobot().apply(action)

    fun validate(gtmLogDbSource: GtmLogDBSource,
                 targetContext: Context,
                 fileName: String) {
        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
    }

    private fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() = AllOf.allOf(isDisplayed(), ViewMatchers.isAssignableFrom(TabsUnify::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabsUnify = view as TabsUnify
                val tabAtIndex: TabLayout.Tab = tabsUnify.tabLayout.getTabAt(tabIndex)
                        ?: throw PerformException.Builder()
                                .withCause(Throwable("No tab at index $tabIndex"))
                                .build()

                tabAtIndex.select()
            }
        }
    }

    private fun setMargin(): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index"

            override fun getConstraints() = AllOf.allOf(isDisplayed(), ViewMatchers.isAssignableFrom(ViewPager::class.java))

            override fun perform(uiController: UiController, view: View) {
                val pager = view as ViewPager
                pager.pageMargin = 0
            }
        }
    }
}

fun actionTest(action: PdpSimulationRobot.() -> Unit) = PdpSimulationRobot().apply(action)