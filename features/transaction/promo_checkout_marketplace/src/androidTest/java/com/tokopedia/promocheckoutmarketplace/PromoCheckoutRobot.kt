package com.tokopedia.promocheckoutmarketplace

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.promocheckoutmarketplace.presentation.viewholder.PromoInputViewHolder
import com.tokopedia.promocheckoutmarketplace.presentation.viewholder.PromoListItemViewHolder
import com.tokopedia.promocheckoutmarketplace.presentation.viewholder.PromoRecommendationViewHolder
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

fun promoCheckoutPage(func: PromoCheckoutRobot.() -> Unit) = PromoCheckoutRobot().apply(func)

class PromoCheckoutRobot {

    fun pullSwipeRefresh() {
        onView(withId(R.id.swipe_refresh_layout)).perform(swipeDown())
    }

    fun clickPromoWithTitle(title: String) {
        onView(withId(R.id.promo_checkout_marketplace_module_recycler_view)).perform(
            actionOnHolderItem(onPromoListItemViewHolderWithTitle(title), clickPromoListItemViewHolder())
        )
    }

    fun clickPilihPromoRecommendation() {
        onView(withId(R.id.promo_checkout_marketplace_module_recycler_view)).perform(
            actionOnHolderItem(onPromoRecommendationViewHolder(), clickPilihInPromoRecommendationViewHolder())
        )
    }

    fun typePromoCode(promoCode: String) {
        onView(withId(R.id.button_input_promo)).perform(click())
        onView(withId(R.id.et_input_promo)).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "type promo input"
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as? TextFieldUnify2)?.editText?.setText(promoCode)
            }
        })
    }

    fun clickTerapkanPromoCode() {
        onView(withId(R.id.btn_apply_promo)).perform(click())
    }

    fun clickPakaiPromo() {
        onView(withId(R.id.container_action_bottom)).perform(clickButtonApplyPromo())
    }
}

fun onPromoListItemViewHolderWithTitle(title: String): BaseMatcher<AbstractViewHolder<*>> {
    return object : BaseMatcher<AbstractViewHolder<*>>() {
        override fun describeTo(description: Description?) {
            description?.appendText("Find PromoListItemViewHolder with title $title")
        }

        override fun matches(item: Any?): Boolean {
            if (item is PromoListItemViewHolder) {
                return item.itemView.findViewById<Typography>(R.id.text_promo_item_title).text == title
            }
            return false
        }
    }
}

fun onPromoRecommendationViewHolder(): BaseMatcher<AbstractViewHolder<*>> {
    return object : BaseMatcher<AbstractViewHolder<*>>() {
        override fun describeTo(description: Description?) {
            description?.appendText("Find PromoRecommendationViewHolder")
        }

        override fun matches(item: Any?): Boolean {
            if (item is PromoRecommendationViewHolder) {
                // Only one PromoRecommendationViewHolder in the recyclerview
                return true
            }
            return false
        }
    }
}

fun onPromoInputViewHolder(): BaseMatcher<AbstractViewHolder<*>> {
    return object : BaseMatcher<AbstractViewHolder<*>>() {
        override fun describeTo(description: Description?) {
            description?.appendText("Find PromoInputViewHolder")
        }

        override fun matches(item: Any?): Boolean {
            if (item is PromoInputViewHolder) {
                // Only one PromoInputViewHolder in the recyclerview
                return true
            }
            return false
        }
    }
}

fun clickPromoListItemViewHolder() = object : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return isDisplayed()
    }

    override fun getDescription(): String {
        return "Click PromoListItemViewHolder"
    }

    override fun perform(uiController: UiController?, view: View?) {
        view?.findViewById<View>(R.id.promo_constraint_wrapper)!!.performClick()
    }
}

fun clickPilihInPromoRecommendationViewHolder() = object : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return isDisplayed()
    }

    override fun getDescription(): String {
        return "Click Pilih in PromoRecommendationViewHolder"
    }

    override fun perform(uiController: UiController?, view: View?) {
        view?.findViewById<View>(R.id.button_apply_promo_recommendation)!!.performClick()
    }
}

fun typePromoCodeInPromoInputViewHolder(promoCode: String) = object : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return isDisplayed()
    }

    override fun getDescription(): String {
        return "Type Promo Code in PromoInputViewHolder"
    }

    override fun perform(uiController: UiController?, view: View?) {
        view?.findViewById<TextFieldUnify2>(R.id.text_field_input_promo)!!.editText.setText(promoCode)
    }
}

fun clickTerapkanInPromoInputViewHolder() = object : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return isDisplayed()
    }

    override fun getDescription(): String {
        return "Type Promo Code in PromoInputViewHolder"
    }

    override fun perform(uiController: UiController?, view: View?) {
        view?.findViewById<View>(R.id.button_apply_promo)!!.performClick()
    }
}

fun clickButtonApplyPromo() = object : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return isDisplayed()
    }

    override fun getDescription(): String {
        return "Click Button Apply Promo in Bottom Container"
    }

    override fun perform(uiController: UiController?, view: View?) {
        view?.findViewById<View>(R.id.button_apply_promo)?.performClick()
    }
}
