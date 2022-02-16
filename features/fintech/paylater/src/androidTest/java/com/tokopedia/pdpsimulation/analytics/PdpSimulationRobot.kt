package com.tokopedia.pdpsimulation.analytics

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.pdpsimulation.test.R
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.unifycomponents.TabsUnify
import kotlinx.android.synthetic.main.paylater_partner_card_item.view.*
import org.hamcrest.MatcherAssert
import org.hamcrest.core.AllOf

class PdpSimulationRobot {

    fun clickPartnerButton() {
        onView(
            CommonMatcher.firstView(AllOf.allOf(withId(R.id.payLaterActionCta), isDisplayed()))
        ).perform(ViewActions.click())
    }

    fun clickPartnerButtonBottomSheet() {
        onView(
            CommonMatcher.firstView(AllOf.allOf(withId(R.id.btnRegister), isDisplayed()))
        ).perform(ViewActions.click())
    }


    fun closeBottomSheet() {
        onView(
            CommonMatcher.firstView(
                AllOf.allOf(
                    withId(R.id.bottom_sheet_close),
                    isDisplayed()
                )
            )
        ).perform(
            click()
        )
    }



    infix fun assertTest(action: PdpSimulationRobot.() -> Unit) = PdpSimulationRobot().apply(action)

    fun hasPassedAnalytics(rule: CassavaTestRule, path: String) {
        MatcherAssert.assertThat(rule.validate(path), hasAllSuccess())
    }


    private fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() =
                AllOf.allOf(isDisplayed(), ViewMatchers.isAssignableFrom(TabsUnify::class.java))

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

            override fun getConstraints() =
                AllOf.allOf(isDisplayed(), ViewMatchers.isAssignableFrom(ViewPager::class.java))

            override fun perform(uiController: UiController, view: View) {
                val pager = view as ViewPager
                pager.pageMargin = 0
            }
        }
    }
}

fun actionTest(action: PdpSimulationRobot.() -> Unit) = PdpSimulationRobot().apply(action)