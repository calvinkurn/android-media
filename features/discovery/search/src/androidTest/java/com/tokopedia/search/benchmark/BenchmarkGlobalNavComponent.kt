package com.tokopedia.search.benchmark

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.search.createGlobalNavListener
import com.tokopedia.search.mock.MockSearchProductModel.getGlobalNavViewModel
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavViewHolder
import com.tokopedia.test.application.benchmark_component.BenchmarkObject
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import org.junit.Rule
import org.junit.Test

internal class BenchmarkGlobalNavComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onBind_ViewHolder_quick_filter() {
        val itemView = BenchmarkObject.simpleViewFromLayout(GlobalNavViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = GlobalNavViewHolder(
                itemView, createGlobalNavListener())
        val data = getGlobalNavViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_quick_filter() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                GlobalNavViewHolder.LAYOUT) {
            GlobalNavViewHolder(it, createGlobalNavListener())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }
}