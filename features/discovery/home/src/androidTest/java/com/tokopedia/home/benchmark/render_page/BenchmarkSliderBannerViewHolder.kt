package com.tokopedia.home.benchmark.render_page

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.BannerViewHolder
import com.tokopedia.home.environment.BlankTestActivity
import com.tokopedia.home.mock.MockSliderBannerModel
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleAdapter
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleViewFromLayout
import org.junit.Rule
import org.junit.Test

class BenchmarkSliderBannerViewHolder {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var activityRule: ActivityTestRule<BlankTestActivity> = ActivityTestRule(BlankTestActivity::class.java)

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_home_page_slider_banner() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = simpleAdapter(
                BannerViewHolder.LAYOUT) {
            BannerViewHolder(it, null)
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_dynamic_home_page_slider_banner() {
        val itemView = simpleViewFromLayout(BannerViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = BannerViewHolder(
                itemView, null
        )
        val data = MockSliderBannerModel.get()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }
}