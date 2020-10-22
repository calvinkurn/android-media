package com.tokopedia.home_component

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.home_component.mock.channel.MockChannelModel
import com.tokopedia.home_component.model.DynamicChannelLayout.LAYOUT_MIX_LEFT
import com.tokopedia.home_component.viewholders.Lego4AutoBannerViewHolder
import com.tokopedia.home_component.visitable.Lego4AutoDataModel
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleAdapter
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleViewFromLayout
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import org.junit.Rule
import org.junit.Test
/**
 * @author by yoasfs on 28/07/20
 */
class BenchmarkLego4AutoComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onCreateViewHolder_lego4_auto_component() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
        val recyclerViewAdapter = simpleAdapter(
                Lego4AutoBannerViewHolder.LAYOUT) {
            Lego4AutoBannerViewHolder(it, null, null, null)
        }
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_lego4_auto_component() {
        val itemView = simpleViewFromLayout(Lego4AutoBannerViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = Lego4AutoBannerViewHolder(
                itemView, null, null, null
        )
        val data = Lego4AutoDataModel(MockChannelModel.get(LAYOUT_MIX_LEFT))
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }
}