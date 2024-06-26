package com.tokopedia.pdpCheckout.testing.product.detail

import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.android.material.chip.ChipGroup
import com.tokopedia.product.detail.R
import com.tokopedia.test.application.espresso_component.CommonActions.clickChildViewWithId
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.AllOf
import com.tkpd.atcvariant.R as atcvariantR
import com.tokopedia.unifycomponents.R as unifyComponentsR

class ProductDetailRobot {

    fun clickAtcNormal() {
        // select variant with normal button
        selectVariant(2)

        onView(withId(com.tkpd.atcvariant.R.id.btn_atc_variant))
            .check(matches(isDisplayed()))
            .check(matches(ViewMatchers.withText("+ Keranjang test")))
            .perform(click())
    }

    fun clickBuyNormal() {
        // show and select variant with normal button
        onView(withId(R.id.btn_buy_now)).perform(click())
        selectVariant(2)

        onView(withId(com.tkpd.atcvariant.R.id.btn_buy_variant))
            .check(matches(isDisplayed()))
            .check(matches(ViewMatchers.withText("Beli test")))
            .perform(click())
    }

    fun clickBeliLangsungOcc() {
        selectVariantOnVbs(3)
        Thread.sleep(5_000)
        onView(withId(atcvariantR.id.btn_buy_variant))
            .check(matches(isDisplayed()))
            .check(matches(ViewMatchers.withText("Beli Langsung")))
            .perform(click())
    }

    fun clickLihatKeranjangToaster(pdpInterceptor: ProductDetailInterceptor? = null) {
        pdpInterceptor?.customRecomWidgetRecomAtcResponsePath = RESPONSE_RECOM_AFTER_ATC_PATH
        clickAtcNormal()
        Thread.sleep(1_000)
        onView(withId(unifyComponentsR.id.snackbar_btn))
            .perform(click())
    }

    private fun selectVariantOnVbs(index: Int) {
        showVariantBottomSheet(index)

        selectVariant(index)
    }

    private fun showVariantBottomSheet(index: Int) {
        onView(withId(R.id.rv_pdp)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.rv_single_variant))), ViewActions.scrollTo()))
        val viewInteraction = onView(AllOf.allOf(withId(R.id.rv_single_variant))).check(
            matches(isDisplayed())
        )
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(index, clickChildViewWithId(com.tokopedia.product.detail.common.R.id.atc_variant_chip_container))
        )
    }

    private fun selectVariant(index: Int) {
        // checking recyclerview variant bottom sheet is display or not
        onView(AllOf.allOf(withId(com.tkpd.atcvariant.R.id.rv_atc_variant_bottomsheet))).check(matches(isDisplayed()))
        // click item on position
        onView(AllOf.allOf(withId(com.tkpd.atcvariant.R.id.rv_variant_viewholder))).check(matches(isDisplayed()))
        val viewInteraction = onView(AllOf.allOf(withId(com.tkpd.atcvariant.R.id.chip_group_atc_variant))).check(
            matches(
                isDisplayed()
            )
        )
        viewInteraction.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(isAssignableFrom(View::class.java), isDisplayed())
            }

            override fun getDescription(): String {
                return "123123"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val chipGroup = view as? ChipGroup
                chipGroup?.let {
                    it[index].performClick()
                    uiController?.loopMainThreadUntilIdle()
                }
            }
        })
    }
}
