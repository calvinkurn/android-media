package com.tokopedia.pdpCheckout.testing.product.detail

import android.view.View
import androidx.core.view.get
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.android.material.chip.ChipGroup
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneAddedProductViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions.clickChildViewWithId
import com.tokopedia.unifycomponents.UnifyButton
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.AllOf

class ProductDetailRobot {

    fun clickAtcNormal() {
        // select variant with normal button
        selectVariant(2)

        onView(withId(R.id.btn_atc_variant))
            .check(matches(isDisplayed()))
            .check(matches(ViewMatchers.withText("+ Keranjang test")))
            .perform(click())
    }

    fun clickBuyNormal() {
        // show and select variant with normal button
        selectVariantOnVbs(2)
        onView(withId(R.id.btn_buy_variant))
            .check(matches(isDisplayed()))
            .check(matches(ViewMatchers.withText("Beli test")))
            .perform(click())
    }

    fun clickBeliPakaiOvoOcs() {
        selectVariantOnVbs(0)
        onView(withId(R.id.btn_buy_variant))
            .check(matches(isDisplayed()))
            .check(matches(ViewMatchers.withText("Beli pakai OVO")))
            .check(
                matches(
                    ViewAttributeMatcher {
                        val buttonUnify = (it as UnifyButton)
                        buttonUnify.buttonVariant == UnifyButton.Variant.GHOST && buttonUnify.buttonType == UnifyButton.Type.MAIN
                    }
                )
            )

        onView(withId(R.id.btn_buy_variant)).perform(click())
    }

    fun clickBeliLangsungOcc() {
        selectVariantOnVbs(3)
        Thread.sleep(5_000)
        onView(withId(R.id.btn_buy_variant))
            .check(matches(isDisplayed()))
            .check(matches(ViewMatchers.withText("Beli Langsung")))
            .perform(click())
    }

    fun clickLihatKeranjangBottomSheetAtc(pdpInterceptor: ProductDetailInterceptor? = null) {
        pdpInterceptor?.customRecomWidgetRecomAtcResponsePath = RESPONSE_RECOM_AFTER_ATC_PATH
        clickAtcNormal()
        Thread.sleep(5_000)
        onView(withId(R.id.recycler_view_add_to_cart_done)).perform(
            RecyclerViewActions.actionOnItemAtPosition<AddToCartDoneAddedProductViewHolder>(
                0,
                clickChildViewWithId(R.id.button_go_to_cart)
            )
        )
    }

    fun clickLihatKeranjangToaster(pdpInterceptor: ProductDetailInterceptor? = null) {
        pdpInterceptor?.customRecomWidgetRecomAtcResponsePath = RESPONSE_RECOM_AFTER_ATC_PATH
        clickAtcNormal()
        Thread.sleep(1_000)
        onView(withId(R.id.snackbar_btn))
            .perform(click())
    }

    fun closeBottomSheetAtc() {
        onView(withId(R.id.btn_close)).perform(click())
    }

    private fun selectVariantOnVbs(position: Int) {
        showVariantBottomSheet()

        selectVariant(position)
    }

    private fun showVariantBottomSheet() {
        onView(withId(R.id.btn_buy_now)).perform(click())
    }

    private fun selectVariant(position: Int) {
        // checking recyclerview variant bottom sheet is display or not
        onView(AllOf.allOf(withId(R.id.rv_atc_variant_bottomsheet))).check(matches(isDisplayed()))
        // click item on position
        onView(AllOf.allOf(withId(R.id.rv_variant_viewholder))).check(matches(isDisplayed()))
        val viewInteraction = onView(AllOf.allOf(withId(R.id.chip_group_atc_variant))).check(
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
                    it[position].performClick()
                    uiController?.loopMainThreadUntilIdle()
                }
            }
        })
    }
}

class ViewAttributeMatcher(
    private val checkAttribute: (View?) -> Boolean
) : BoundedMatcher<View?, View>(View::class.java) {
    override fun describeTo(description: Description?) {
        description?.appendText("Check attribute view")
    }

    override fun matchesSafely(item: View?): Boolean {
        return checkAttribute.invoke(item)
    }
}
