package com.tokopedia.homenav.component

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderPaymentRevampViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderProductRevampViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import org.hamcrest.CoreMatchers

/**
 * Created by dhaba
 */
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_ORDER_TRANSACTION =
    "tracker/home_nav/order_transaction.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_REVIEW = "tracker/home_nav/review.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_SHOP_AFFILIATE = "tracker/home_nav/shop_affiliate.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_TOKOPEDIA_PLUS = "tracker/home_nav/tokopedia_plus.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_USER_MENU = "tracker/home_nav/user_menu.json"

fun clickOnOrderHistory(viewHolder: RecyclerView.ViewHolder) {
    clickOnEachItemRecyclerViewOrderHistory(viewHolder.itemView, R.id.transaction_rv, 0)
}

fun clickOnReview(viewHolder: RecyclerView.ViewHolder) {
    clickOnEachItemRecyclerViewReview(viewHolder.itemView, R.id.review_rv, 0)
}

fun clickOrderProduct(recyclerViewId: Int, cardPosition: Int) {
    Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            cardPosition,
            clickOnViewChild(R.id.order_product_container)
        )
    )
}

fun clickPayment(recyclerViewId: Int, cardPosition: Int) {
    Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            cardPosition,
            clickOnViewChild(R.id.order_payment_container)
        )
    )
}

fun clickReviewProduct(recyclerViewId: Int, cardPosition: Int) {
    Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            cardPosition,
            clickOnViewChild(R.id.star_1)
        )
    )
    Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            cardPosition,
            clickOnViewChild(R.id.order_review_container)
        )
    )
}

fun clickOnEachItemRecyclerViewOrderHistory(
    view: View,
    recyclerViewId: Int,
    fixedItemPositionLimit: Int
) {
    val childRecyclerView: RecyclerView = view.findViewById(recyclerViewId)

    var childItemCountExcludeViewAllCard = (childRecyclerView.adapter?.itemCount ?: 0) - 1
    if (fixedItemPositionLimit > 0) {
        childItemCountExcludeViewAllCard = fixedItemPositionLimit
    }
    for (i in 0 until childItemCountExcludeViewAllCard) {
        try {
            Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                    i
                )
            )
            when (childRecyclerView.findViewHolderForAdapterPosition(i)) {
                is OrderPaymentRevampViewHolder -> {
                    clickPayment(recyclerViewId, i)
                }
                is OrderProductRevampViewHolder -> {
                    clickOrderProduct(recyclerViewId, i)
                }
            }
        } catch (e: PerformException) {
            e.printStackTrace()
        }
    }
    Espresso.onView(
        CoreMatchers.allOf(
            ViewMatchers.withId(recyclerViewId)
        )
    )
        .perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                childItemCountExcludeViewAllCard,
                ViewActions.click()
            )
        )
}

fun clickOnEachItemRecyclerViewReview(
    view: View,
    recyclerViewId: Int,
    fixedItemPositionLimit: Int
) {
    val childRecyclerView: RecyclerView = view.findViewById(recyclerViewId)

    var childItemCountExcludeViewAllCard = (childRecyclerView.adapter?.itemCount ?: 0) - 1
    if (fixedItemPositionLimit > 0) {
        childItemCountExcludeViewAllCard = fixedItemPositionLimit
    }
    for (i in 0 until childItemCountExcludeViewAllCard) {
        try {
            Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                    i
                )
            )
            clickReviewProduct(recyclerViewId, i)
        } catch (e: PerformException) {
            e.printStackTrace()
        }
    }
    Espresso.onView(
        CoreMatchers.allOf(
            ViewMatchers.withId(recyclerViewId)
        )
    )
        .perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                childItemCountExcludeViewAllCard,
                ViewActions.click()
            )
        )
}

fun clickOnShopAndAffiliate(viewHolder: RecyclerView.ViewHolder) {
    clickOnEachItemRecyclerViewOrderHistory(viewHolder.itemView, R.id.recycler_seller, 0)
}

fun clickOnTokopediaPlus() {
    Espresso.onView(ViewMatchers.withId(R.id.tokopedia_plus_widget)).perform(
        ViewActions.click()
    )
}

fun clickUserMenu(recyclerViewId: Int, cardPosition: Int) {
    Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            cardPosition,
            ViewActions.click()
        )
    )
}

private fun clickOnViewChild(viewId: Int) = object : ViewAction {
    override fun getDescription(): String = ""

    override fun getConstraints() = null

    override fun perform(uiController: UiController, view: View) =
        ViewActions.click().perform(uiController, view.findViewById<View>(viewId))
}
