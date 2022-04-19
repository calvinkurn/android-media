package com.tokopedia.pdpCheckout.testing.product.detail

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneAddedProductViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.unifycomponents.UnifyButton
import org.hamcrest.Description
import org.hamcrest.core.AllOf

class ProductDetailRobot {

    fun clickAtcNormal() {
        // select variant with normal button
        clickVariantAtPosition(2)
        onView(withId(R.id.btn_add_to_cart))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("+ Keranjang test")))

        onView(withId(R.id.btn_add_to_cart)).perform(click())
    }

    fun clickBuyNormal() {
        // select variant with normal button
        clickVariantAtPosition(2)
        onView(withId(R.id.btn_buy_now))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Beli test")))

        onView(withId(R.id.btn_buy_now)).perform(click())
    }

    fun clickBeliPakaiOvoOcs() {
        clickVariantAtPosition(0)
        onView(withId(R.id.btn_buy_now))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Beli pakai OVO")))
                .check(matches(ViewAttributeMatcher {
                    val buttonUnify = (it as UnifyButton)
                    buttonUnify.buttonVariant == UnifyButton.Variant.GHOST && buttonUnify.buttonType == UnifyButton.Type.MAIN
                }))

        onView(withId(R.id.btn_buy_now)).perform(click())
    }

    fun clickBeliLangsungOcc() {
        clickVariantAtPosition(3)
        onView(withId(R.id.btn_buy_now))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .check(matches(ViewMatchers.withText("Beli Langsung")))

        onView(withId(R.id.btn_buy_now)).perform(click())
    }

    fun clickLihatKeranjangBottomSheetAtc(pdpInterceptor: ProductDetailInterceptor? = null) {
        pdpInterceptor?.customRecomWidgetRecomAtcResponsePath = RESPONSE_RECOM_AFTER_ATC_PATH
        clickAtcNormal()
        Thread.sleep(5000)
        onView(withId(R.id.recycler_view_add_to_cart_done)).perform(RecyclerViewActions.actionOnItemAtPosition<AddToCartDoneAddedProductViewHolder>(
                0, CommonActions.clickChildViewWithId(R.id.button_go_to_cart)))
    }

    fun closeBottomSheetAtc() {
        onView(withId(R.id.btn_close)).perform(click())
    }

    fun clickVariantAtPosition(position: Int = 0) {
        onView(withId(R.id.rv_pdp)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.rvContainerVariant))), ViewActions.scrollTo()))
        val viewInteraction = onView(AllOf.allOf(withId(R.id.rv_variant))).check(matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, CommonActions.clickChildViewWithId(R.id.containerChipVariant)))
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