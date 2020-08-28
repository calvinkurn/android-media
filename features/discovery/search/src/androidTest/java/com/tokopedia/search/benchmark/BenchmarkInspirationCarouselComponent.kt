package com.tokopedia.search.benchmark

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.search.createInspirationCarouselListener
import com.tokopedia.search.mock.MockSearchProductModel.getInspirationCarouselListViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCarouselViewHolder
import com.tokopedia.test.application.benchmark_component.BenchmarkObject
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import org.junit.Rule
import org.junit.Test

internal class BenchmarkInspirationCarouselComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onBind_ViewHolder_inspiration_carousel_list() {
        val itemView = BenchmarkObject.simpleViewFromLayout(InspirationCarouselViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = InspirationCarouselViewHolder(
                itemView, createInspirationCarouselListener())
        val data = getInspirationCarouselListViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_inspiration_carousel_list() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                InspirationCarouselViewHolder.LAYOUT) {
            InspirationCarouselViewHolder(it, createInspirationCarouselListener())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }
}