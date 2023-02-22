package com.tokopedia.officialstore.cassava

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.officialstore.R
import org.hamcrest.CoreMatchers

private fun clickOnViewChild(viewId: Int) = object : ViewAction {
    override fun getDescription(): String = ""

    override fun getConstraints() = null

    override fun perform(uiController: UiController, view: View) =
        ViewActions.click().perform(uiController, view.findViewById<View>(viewId))
}

fun clickOnEachItemRecyclerViewMerchantVoucher(
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
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    i,
                    clickOnViewChild(R.id.container_shop)
                )
            )
            Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    i,
                    clickOnViewChild(R.id.container_product)
                )
            )
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

fun actionOnMerchantVoucherWidget(viewHolder: RecyclerView.ViewHolder) {
    clickOnEachItemRecyclerViewMerchantVoucher(viewHolder.itemView, R.id.home_component_mvc_rv, 0)
}
