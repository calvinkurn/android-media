package com.tokopedia.home.benchmark.render_page

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicIconSectionViewHolder
import com.tokopedia.home.mock.MockDynamicIconModel
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleAdapter
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleViewFromLayout
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import org.junit.Rule
import org.junit.Test

class BenchmarkDynamicIconViewHolder {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_dynamic_icon() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
        val recyclerViewAdapter = simpleAdapter(
                DynamicIconSectionViewHolder.LAYOUT) {
            DynamicIconSectionViewHolder(it, null)
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_dynamic_icon() {
        val itemView = simpleViewFromLayout(DynamicIconSectionViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = DynamicIconSectionViewHolder(
                itemView, null
        )
        val data = MockDynamicIconModel.get()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }
}