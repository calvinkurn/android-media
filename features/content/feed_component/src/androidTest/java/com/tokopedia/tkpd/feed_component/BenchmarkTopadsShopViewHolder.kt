package com.tokopedia.tkpd.feed_component

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleAdapter
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleViewFromLayout
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import com.tokopedia.tkpd.feed_component.mock.MockDataModel
import com.tokopedia.tkpd.feed_component.mock.MockTitleModel
import org.junit.Rule
import org.junit.Test

class BenchmarkTopadsShopViewHolder {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_topads_shop() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
        val recyclerViewAdapter = simpleAdapter(
                TopadsShopViewHolder.LAYOUT) {
            TopadsShopViewHolder(it, null, null)
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_topads_shop() {
        val itemView = simpleViewFromLayout(TopadsShopViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = TopadsShopViewHolder(
                itemView, null, null
        )
        val data = TopadsShopViewModel(title = MockTitleModel.get(), dataList = mutableListOf((MockDataModel.get())))
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }
}