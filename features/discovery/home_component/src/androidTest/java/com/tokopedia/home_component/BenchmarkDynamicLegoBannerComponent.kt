package com.tokopedia.home_component

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.home_component.mock.channel.MockChannelModel
import com.tokopedia.home_component.model.DynamicChannelLayout.LAYOUT_6_IMAGE
import com.tokopedia.home_component.model.DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE
import com.tokopedia.home_component.model.DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE
import com.tokopedia.home_component.model.DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleAdapter
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleViewFromLayout
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import org.junit.Rule
import org.junit.Test

class BenchmarkDynamicLegoBannerComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_dynamic_lego_component() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
        val recyclerViewAdapter = simpleAdapter(
                DynamicLegoBannerViewHolder.LAYOUT) {
            DynamicLegoBannerViewHolder(it, null, null)
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_dynamic_lego_6_component() {
        val itemView = simpleViewFromLayout(DynamicLegoBannerViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = DynamicLegoBannerViewHolder(
                itemView, null, null
        )
        val data = DynamicLegoBannerDataModel(MockChannelModel.get(LAYOUT_6_IMAGE))
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_dynamic_lego_4_component() {
        val itemView = simpleViewFromLayout(DynamicLegoBannerViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = DynamicLegoBannerViewHolder(
                itemView, null, null
        )
        val data = DynamicLegoBannerDataModel(MockChannelModel.get(LAYOUT_LEGO_4_IMAGE))
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_dynamic_lego_3_component() {
        val itemView = simpleViewFromLayout(DynamicLegoBannerViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = DynamicLegoBannerViewHolder(
                itemView, null, null
        )
        val data = DynamicLegoBannerDataModel(MockChannelModel.get(LAYOUT_LEGO_3_IMAGE))
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }
    @Test
    fun benchmark_onBind_ViewHolder_dynamic_lego_2_component() {
        val itemView = simpleViewFromLayout(DynamicLegoBannerViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = DynamicLegoBannerViewHolder(
                itemView, null, null
        )
        val data = DynamicLegoBannerDataModel(MockChannelModel.get(LAYOUT_LEGO_2_IMAGE))
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }
}