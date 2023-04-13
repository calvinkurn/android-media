package com.tokopedia.product.manage.cassava.robot

import android.app.Activity
import android.app.Instrumentation
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonActions.displayChildViewWithId
import com.tokopedia.test.application.espresso_component.CommonActions.displayChildViewWithIdAndText
import com.tokopedia.test.application.espresso_component.CommonMatcher

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

    fun isDisplayProductOnView(@LayoutRes id: Int,
                                       index: Int = 0){
        onView(withId(R.id.recycler_view)).check(matches(ViewMatchers.isDisplayed()))
            .check(matches(displayChildViewWithId(index,id)))

    }
    fun isDisplayProductOnViewWithText(@LayoutRes id: Int,
                               index: Int = 0, text:String){
        onView(withId(R.id.recycler_view)).check(matches(ViewMatchers.isDisplayed()))
            .check(matches(displayChildViewWithIdAndText(index,id,text)))

    }

    fun clickProductCardOnView(@LayoutRes id: Int,
                               index: Int = 0) {
        onView(withId(R.id.recycler_view)).check(matches(ViewMatchers.isDisplayed()))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ProductViewHolder>(
                    index,
                    CommonActions.clickChildViewWithId(id)
                )
            )
    }
    private fun getFirstNoVariantIndex(activity: Activity): Int {
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recycler_view)
        return (recyclerView.adapter as? BaseListAdapter<*, *>)?.data?.indexOfFirst { it is ProductUiModel && !it.isVariant() }
            ?: RecyclerView.NO_POSITION
    }





}

fun actionTest(action: ProductManageRobot.() -> Unit) = ProductManageRobot().run(action)
