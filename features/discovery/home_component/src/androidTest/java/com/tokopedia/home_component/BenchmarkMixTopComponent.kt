package com.tokopedia.home_component

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.home_component.mock.channel.MockChannelModel
import com.tokopedia.home_component.model.DynamicChannelLayout.LAYOUT_MIX_TOP
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleAdapter
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleViewFromLayout
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import org.junit.Rule
import org.junit.Test

class BenchmarkMixTopComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_mix_top_component() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
        val recyclerViewAdapter = simpleAdapter(
                MixTopComponentViewHolder.LAYOUT) {
            MixTopComponentViewHolder(it, null, null)
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_mix_top_component() {
        val itemView = simpleViewFromLayout(MixTopComponentViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = MixTopComponentViewHolder(
                itemView, null, null
        )
        val data = MixTopDataModel(MockChannelModel.get(LAYOUT_MIX_TOP))
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }
}