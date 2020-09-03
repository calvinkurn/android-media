package com.tokopedia.search.benchmark

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.search.createSuggestionListener
import com.tokopedia.search.mock.MockSearchProductModel.getSuggestionViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SuggestionViewHolder
import com.tokopedia.test.application.benchmark_component.BenchmarkObject
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import org.junit.Rule
import org.junit.Test

internal class BenchmarkSuggestionComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onBind_ViewHolder_suggestion() {
        val itemView = BenchmarkObject.simpleViewFromLayout(SuggestionViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = SuggestionViewHolder(
                itemView, createSuggestionListener())
        val data = getSuggestionViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_suggestion() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                SuggestionViewHolder.LAYOUT) {
            SuggestionViewHolder(it, createSuggestionListener())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }
}