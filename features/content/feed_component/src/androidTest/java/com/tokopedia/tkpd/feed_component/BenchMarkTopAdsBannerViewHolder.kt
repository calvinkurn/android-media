package com.tokopedia.tkpd.feed_component

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsBannerViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.banner.TopAdsBannerViewModel
import com.tokopedia.test.application.benchmark_component.BenchmarkObject
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import com.tokopedia.tkpd.feed_component.mock.MockTitleModel
import com.tokopedia.tkpd.feed_component.mock.MockTopAdsImageViewModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import org.junit.Rule
import org.junit.Test

class BenchMarkTopAdsBannerViewHolder {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_topads_banner() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                TopAdsBannerViewHolder.LAYOUT) {
            TopAdsBannerViewHolder(it, null, null)
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_topads_banner() {
        val itemView = BenchmarkObject.simpleViewFromLayout(TopAdsBannerViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
        val viewHolder = TopAdsBannerViewHolder(
                itemView, null, null
        )
        val data = TopAdsBannerViewModel(title = MockTitleModel.get(), topAdsBannerList = listOf((MockTopAdsImageViewModel.get())) as ArrayList<TopAdsImageViewModel>)
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }
}