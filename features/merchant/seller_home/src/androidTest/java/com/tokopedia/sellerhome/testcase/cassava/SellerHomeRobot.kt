package com.tokopedia.sellerhome.testcase.cassava

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.analyticsdebugger.cassava.validator.Utils
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.RouteManager
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import com.tokopedia.sellerhomecommon.presentation.model.CarouselWidgetUiModel
import io.mockk.every
import io.mockk.mockkStatic
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.MatcherAssert
import org.json.JSONObject

class SellerHomeRobot {

    companion object {
        val queriesToValidate = arrayListOf<String>()
    }

    fun clearQueries() {
        queriesToValidate.clear()
    }

    fun addQueriesToValidate(context: Context, vararg queries: String) {
        queriesToValidate.addAll(queries.map {
            Utils.getJsonDataFromAsset(context, it)
                    ?: throw AssertionError("Validator Query not found")
        })
    }

    fun validate(gtmLogDbSource: GtmLogDBSource, vararg expectedMatcheCount: Int) {
        val errorMessage = queriesToValidate.mapIndexed { index, query ->
            val validationData = query.replace("\n", "")
            val jsonValidationData = JSONObject(validationData)
            try {
                val analyticsQuery = getAnalyticsWithQuery(gtmLogDbSource, query)
                MatcherAssert.assertThat(analyticsQuery, hasAllSuccess())
                val resultMatchCount = analyticsQuery.first().matches.size.orZero()
                if (expectedMatcheCount.getOrNull(index) == 0 || resultMatchCount == expectedMatcheCount.getOrNull(index)) {
                    ""
                } else {
                    "\n${jsonValidationData.get("readme")}\n must match exactly ${expectedMatcheCount.getOrNull(index).orZero()} times, but it match for $resultMatchCount times"
                }
            } catch (e: AssertionError) {
                "\n${jsonValidationData.get("readme")}\n${e.localizedMessage.orEmpty().replace(Regex("\\s{2,}"), " ").replace("\n", "")}"
            }
        }.joinToString("")

        assertTrue(errorMessage, errorMessage.isEmpty())
    }

    fun blockAllIntent() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    fun mockRouteManager(callback: (() -> Any)? = null) {
        mockkStatic(RouteManager::class)
        every {
            RouteManager.route(any(), any())
        } answers {
            callback?.invoke()
            true
        }
    }

    fun dismissTooltipBottomSheet(activity: FragmentActivity) {
        activity.supportFragmentManager.fragments.find { it is SellerHomeFragment }?.let { sellerHomeFragment ->
            // BottomSheetUnify is a BottomSheetDialogFragment and BottomSheetDialogFragment is a DialogFragment
            (sellerHomeFragment.childFragmentManager.findFragmentByTag("seller_home_tooltip") as? DialogFragment)?.dismiss()
        }
    }

    fun scrollThrough(activity: Activity, recyclerViewId: Int) {
        val recyclerView = activity.findViewById<RecyclerView>(recyclerViewId)
        while (recyclerView.canScrollVertically(1)) {
            onView(allOf(withId(recyclerViewId), isAssignableFrom(RecyclerView::class.java), isDisplayed()))
                    .perform(GeneralSwipeAction(Swipe.SLOW, GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER, Press.FINGER))
            Thread.sleep(2500)
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified W> clickAllWidgetsWithType(activity: Activity, recyclerViewId: Int) {
        val recyclerView = activity.findViewById<RecyclerView>(recyclerViewId)
        val layoutManager = recyclerView.layoutManager
        val adapter = recyclerView.adapter
        if (layoutManager is LinearLayoutManager) {
            val data: List<Any> = (adapter as BaseListAdapter<*, *>).data
            var firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
            var lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

            clickAllVisibleWidgets<W>(firstVisiblePosition, lastVisiblePosition, data, activity, layoutManager)
            while (recyclerView.canScrollVertically(1)) {
                onView(allOf(withId(recyclerViewId), isAssignableFrom(RecyclerView::class.java), isDisplayed()))
                        .perform(GeneralSwipeAction(Swipe.SLOW, GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER, Press.FINGER))
                Thread.sleep(2500)
                firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                clickAllVisibleWidgets<W>(firstVisiblePosition, lastVisiblePosition, data, activity, layoutManager)
            }
        }
    }

    inline fun <reified W> clickAllVisibleWidgets(from: Int, to: Int, data: List<Any>, activity: Activity, layoutManager: RecyclerView.LayoutManager) {
        for (i in from..to) {
            val view = layoutManager.findViewByPosition(i)
            if (data.getOrNull(i) is W && view != null) {
                activity.runOnUiThread {
                    view.performClick()
                }
                Thread.sleep(2500)
            }
        }
    }

    inline fun <reified W> clickAllWidgetCtaWithType(activity: Activity, recyclerViewId: Int, ctaId: Int) {
        val recyclerView = activity.findViewById<RecyclerView>(recyclerViewId)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val data = (recyclerView.adapter as BaseListAdapter<*, *>).data
            var firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
            var lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

            clickAllVisibleCta<W>(firstVisiblePosition, lastVisiblePosition, data, activity, layoutManager, ctaId)
            while (recyclerView.canScrollVertically(1)) {
                onView(allOf(withId(recyclerViewId), isAssignableFrom(RecyclerView::class.java), isDisplayed()))
                        .perform(GeneralSwipeAction(Swipe.SLOW, GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER, Press.FINGER))
                Thread.sleep(2500)
                firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                clickAllVisibleCta<W>(firstVisiblePosition, lastVisiblePosition, data, activity, layoutManager, ctaId)
            }
        }
    }

    inline fun <reified W> clickAllVisibleCta(from: Int, to: Int, data: List<Any>, activity: Activity, layoutManager: RecyclerView.LayoutManager, ctaId: Int) {
        for (i in from..to) {
            val ctaView = layoutManager.findViewByPosition(i)?.findViewById<View>(ctaId)
            if (data.getOrNull(i) is W && ctaView?.isVisible == true) {
                activity.runOnUiThread {
                    ctaView.performClick()
                }
                Thread.sleep(2500)
            }
        }
    }

    inline fun <reified W, reified R> clickAllItemInChildRecyclerView(activity: Activity, recyclerViewId: Int, childRecyclerViewId: Int) {
        val recyclerView = activity.findViewById<RecyclerView>(recyclerViewId)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val data = (recyclerView.adapter as BaseListAdapter<*, *>).data
            var firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
            var lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

            clickAllItem<W, R>(firstVisiblePosition, lastVisiblePosition, data, activity, childRecyclerViewId)
            while (recyclerView.canScrollVertically(1)) {
                onView(allOf(withId(recyclerViewId), isAssignableFrom(RecyclerView::class.java), isDisplayed()))
                        .perform(GeneralSwipeAction(Swipe.SLOW, GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER, Press.FINGER))
                Thread.sleep(2500)
                firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                clickAllItem<W, R>(firstVisiblePosition, lastVisiblePosition, data, activity, childRecyclerViewId)
            }
        }
    }

    inline fun <reified W, reified R> clickAllItem(from: Int, to: Int, data: List<Any>, activity: Activity, childRecyclerViewId: Int) {
        for (i in from..to) {
            if (data.getOrNull(i) is W) {
                clickAllWidgetsWithType<R>(activity, childRecyclerViewId)
            }
        }
    }

    fun clickAllBannerItem(activity: Activity, recyclerViewId: Int, childRecyclerViewId: Int) {
        val recyclerView = activity.findViewById<RecyclerView>(recyclerViewId)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val data = (recyclerView.adapter as BaseListAdapter<*, *>).data
            var firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
            var lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

            clickAllBannerItem(firstVisiblePosition, lastVisiblePosition, data, activity, childRecyclerViewId)
            while (recyclerView.canScrollVertically(1)) {
                onView(allOf(withId(recyclerViewId), isAssignableFrom(RecyclerView::class.java), isDisplayed()))
                        .perform(GeneralSwipeAction(Swipe.SLOW, GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER, Press.FINGER))
                Thread.sleep(2500)
                firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                clickAllBannerItem(firstVisiblePosition, lastVisiblePosition, data, activity, childRecyclerViewId)
            }
        }
    }

    private fun clickAllBannerItem(from: Int, to: Int, data: List<Any>, activity: Activity, childRecyclerViewId: Int) {
        for (i in from..to) {
            if (data.getOrNull(i) is CarouselWidgetUiModel) {
                val recyclerView = activity.findViewById<RecyclerView>(childRecyclerViewId)
                val layoutManager = recyclerView.layoutManager
                for (j in 0 until recyclerView.adapter?.itemCount.orZero()) {
                    activity.runOnUiThread { layoutManager?.scrollToPosition(j) }
                    Thread.sleep(2500)
                    layoutManager?.getChildAt(j)?.let { view ->
                        activity.runOnUiThread { view.performClick() }
                        Thread.sleep(2500)
                    }
                }
            }
        }
    }
}

fun actionTest(action: SellerHomeRobot.() -> Unit) = SellerHomeRobot().apply(action)