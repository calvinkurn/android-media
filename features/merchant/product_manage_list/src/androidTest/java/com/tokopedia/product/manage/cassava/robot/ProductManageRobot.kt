package com.tokopedia.product.manage.cassava.robot

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions

class ProductManageRobot {

    fun clickEditStockButton(activity: Activity) {
        val firstNonVariantIndex = getFirstNoVariantIndex(activity)
        if (firstNonVariantIndex != RecyclerView.NO_POSITION) {
            onView(withId(R.id.recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<ProductViewHolder>(
                        0,
                        CommonActions.clickChildViewWithId(R.id.btnEditStock)
                    )
                )
        }
    }

    fun clickEditPriceButton(activity: Activity) {
        val firstNonVariantIndex = getFirstNoVariantIndex(activity)
        if (firstNonVariantIndex != RecyclerView.NO_POSITION) {
            onView(withId(R.id.recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<ProductViewHolder>(
                        firstNonVariantIndex,
                        CommonActions.clickChildViewWithId(R.id.btnEditPrice)
                    )
                )
        }
    }

    private fun getFirstNoVariantIndex(activity: Activity): Int {
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recycler_view)
        return (recyclerView.adapter as? BaseListAdapter<*, *>)?.data?.indexOfFirst { it is ProductUiModel && !it.isVariant() }
            ?: RecyclerView.NO_POSITION
    }

    fun scrollRecyclerView() {
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(8))
    }

}

fun actionTest(action: ProductManageRobot.() -> Unit) = ProductManageRobot().run(action)