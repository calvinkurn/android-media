package com.tokopedia.search.benchmark

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.search.createBannerAdsListener
import com.tokopedia.search.env.BlankTestActivity
import com.tokopedia.search.mock.MockSearchProductModel.getCpmViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.CpmViewHolder
import com.tokopedia.test.application.benchmark_component.BenchmarkObject
import org.junit.Rule
import org.junit.Test

internal class BenchmarkCpmComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var activityRule: ActivityTestRule<BlankTestActivity> = ActivityTestRule(BlankTestActivity::class.java)

    @Test
    fun benchmark_onBind_ViewHolder_cpm() {
        val itemView = BenchmarkObject.simpleViewFromLayout(CpmViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = CpmViewHolder(
                itemView, createBannerAdsListener())
        val data = getCpmViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_cpm() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                CpmViewHolder.LAYOUT) {
            CpmViewHolder(it, createBannerAdsListener())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }
}