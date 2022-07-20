package com.tokopedia.search.benchmark

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.search.createEmptyStateListener
import com.tokopedia.search.mock.MockSearchProductModel.getEmptySearchProductViewModel
import com.tokopedia.search.result.product.emptystate.EmptyStateKeywordViewHolder
import com.tokopedia.test.application.benchmark_component.BenchmarkObject
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import org.junit.Rule
import org.junit.Test

internal class BenchmarkEmptySearchProductComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onBind_ViewHolder_empty_search_product() {
        val itemView = BenchmarkObject.simpleViewFromLayout(EmptyStateKeywordViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = EmptyStateKeywordViewHolder(itemView, createEmptyStateListener())
        val data = getEmptySearchProductViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_empty_search_product() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                EmptyStateKeywordViewHolder.LAYOUT) {
            EmptyStateKeywordViewHolder(it, createEmptyStateListener())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }
}