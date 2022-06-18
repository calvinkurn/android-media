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
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.OrderReviewViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import org.hamcrest.Matchers

/**
 * Created by dhaba
 */
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_FAVORITE_SHOP = "tracker/home_nav/favorite_shop.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_ORDER_TRANSACTION =
    "tracker/home_nav/order_transaction.json"
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_WISHLIST = "tracker/home_nav/wishlist.json"

fun clickOnEachShop(viewHolder: RecyclerView.ViewHolder) {
    CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.favorite_shop_rv, 0)
}

fun clickOnWishlist(viewHolder: RecyclerView.ViewHolder) {
    CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.wishlist_rv, 0)
}

fun clickOnOrderHistory(viewHolder: RecyclerView.ViewHolder) {
    clickOnEachItemRecyclerViewOrderHistory(viewHolder.itemView, R.id.transaction_rv, 0)
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
                is OrderReviewViewHolder -> {
                    clickReviewProduct(recyclerViewId, i)
                }
            }
        } catch (e: PerformException) {
            e.printStackTrace()
        }
    }
    Espresso.onView(
        Matchers.allOf(
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

private fun clickOnViewChild(viewId: Int) = object : ViewAction {
    override fun getDescription(): String = ""

    override fun getConstraints() = null

    override fun perform(uiController: UiController, view: View) =
        ViewActions.click().perform(uiController, view.findViewById<View>(viewId))
}