package com.tokopedia.home_component

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.home_component.mock.channel.MockChannelModel
import com.tokopedia.home_component.model.DynamicChannelLayout.LAYOUT_MIX_LEFT
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleAdapter
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleViewFromLayout
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import org.junit.Rule
import org.junit.Test

class BenchmarkMixLeftComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_mix_left_component() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
        val recyclerViewAdapter = simpleAdapter(
                MixLeftComponentViewHolder.LAYOUT) {
            MixLeftComponentViewHolder(it, null, null, null)
        }
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_mix_left_component() {
        val itemView = simpleViewFromLayout(MixLeftComponentViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = MixLeftComponentViewHolder(
                itemView, null, null, null
        )
        val data = MixLeftDataModel(MockChannelModel.get(LAYOUT_MIX_LEFT))
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }
}