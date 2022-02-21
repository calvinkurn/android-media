package com.tokopedia.product.manage.cassava.robot

import android.app.Activity
import android.app.Instrumentation
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher
import org.hamcrest.Matcher
import org.hamcrest.Matchers

class ProductManageRobot {

    fun blockAllIntent() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    fun clickEditStockButton(activity: Activity) {
        val firstNonVariantIndex = getFirstNoVariantIndex(activity)
        if (firstNonVariantIndex != RecyclerView.NO_POSITION) {
            clickProductCardOnView(R.id.btnEditStock, firstNonVariantIndex)
        }
    }

    fun clickEditPriceButton(activity: Activity) {
        val firstNonVariantIndex = getFirstNoVariantIndex(activity)
        if (firstNonVariantIndex != RecyclerView.NO_POSITION) {
            clickProductCardOnView(R.id.btnEditPrice, firstNonVariantIndex)
        }
    }

    fun clickProduct() {
        clickProductCardOnView(R.id.textTitle)
    }

    fun clickFilterTab() {
        onView(CommonMatcher.firstView(withId(R.id.sort_filter_prefix)))
            .perform(ViewActions.click())
    }

    private fun getFirstNoVariantIndex(activity: Activity): Int {
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recycler_view)
        return (recyclerView.adapter as? BaseListAdapter<*, *>)?.data?.indexOfFirst { it is ProductUiModel && !it.isVariant() }
            ?: RecyclerView.NO_POSITION
    }

    private fun clickProductCardOnView(@LayoutRes id: Int,
                                       index: Int = 0) {
        onView(withId(R.id.recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ProductViewHolder>(
                    index,
                    CommonActions.clickChildViewWithId(id)
                )
            )
    }

    fun scrollRecyclerView() {
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(8))
    }

}

fun actionTest(action: ProductManageRobot.() -> Unit) = ProductManageRobot().run(action)