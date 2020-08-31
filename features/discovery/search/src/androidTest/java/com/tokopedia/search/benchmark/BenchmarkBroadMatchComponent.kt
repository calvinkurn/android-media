package com.tokopedia.search.benchmark

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.search.createBroadMatchListener
import com.tokopedia.search.mock.MockSearchProductModel.getBroadMatchViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BroadMatchViewHolder
import com.tokopedia.test.application.benchmark_component.BenchmarkObject
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import org.junit.Rule
import org.junit.Test

internal class BenchmarkBroadMatchComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onBind_ViewHolder_broad_match() {
        val itemView = BenchmarkObject.simpleViewFromLayout(BroadMatchViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = BroadMatchViewHolder(
                itemView, createBroadMatchListener())
        val data = getBroadMatchViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_broad_match() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                BroadMatchViewHolder.LAYOUT) {
            BroadMatchViewHolder(it, createBroadMatchListener())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }
}