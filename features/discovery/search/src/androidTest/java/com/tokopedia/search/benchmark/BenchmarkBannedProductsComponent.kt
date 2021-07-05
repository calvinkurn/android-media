package com.tokopedia.search.benchmark

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.search.mock.MockSearchProductModel.getBannedProductsEmptySearchViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BannedProductsEmptySearchViewHolder
import com.tokopedia.test.application.benchmark_component.BenchmarkObject
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import org.junit.Rule
import org.junit.Test

internal class BenchmarkBannedProductsComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onBind_ViewHolder_banned_products_empty_search() {
        val itemView = BenchmarkObject.simpleViewFromLayout(BannedProductsEmptySearchViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = BannedProductsEmptySearchViewHolder(itemView)
        val data = getBannedProductsEmptySearchViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_banned_products_empty_search() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(BannedProductsEmptySearchViewHolder.LAYOUT) {
            BannedProductsEmptySearchViewHolder(it)
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }
}