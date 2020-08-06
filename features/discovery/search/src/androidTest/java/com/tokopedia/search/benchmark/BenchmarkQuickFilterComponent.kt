package com.tokopedia.search.benchmark

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.search.createQuickFilterListener
import com.tokopedia.search.env.BlankTestActivity
import com.tokopedia.search.mock.MockSearchProductModel.getQuickFilterViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.QuickFilterViewHolder
import com.tokopedia.test.application.benchmark_component.BenchmarkObject
import org.junit.Rule
import org.junit.Test

internal class BenchmarkQuickFilterComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var activityRule: ActivityTestRule<BlankTestActivity> = ActivityTestRule(BlankTestActivity::class.java)

    @Test
    fun benchmark_onBind_ViewHolder_quick_filter() {
        val itemView = BenchmarkObject.simpleViewFromLayout(QuickFilterViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = QuickFilterViewHolder(
                itemView, createQuickFilterListener())
        val data = getQuickFilterViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_quick_filter() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                QuickFilterViewHolder.LAYOUT) {
            QuickFilterViewHolder(it, createQuickFilterListener())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }
}