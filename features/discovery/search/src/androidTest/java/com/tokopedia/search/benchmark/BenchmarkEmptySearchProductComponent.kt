package com.tokopedia.search.benchmark

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.search.createBannerAdsListener
import com.tokopedia.search.createEmptyStateListener
import com.tokopedia.search.env.BlankTestActivity
import com.tokopedia.search.mock.MockSearchProductModel.getEmptySearchProductViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ProductEmptySearchViewHolder
import com.tokopedia.test.application.benchmark_component.BenchmarkObject
import com.tokopedia.topads.sdk.base.Config
import org.junit.Rule
import org.junit.Test

internal class BenchmarkEmptySearchProductComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var activityRule: ActivityTestRule<BlankTestActivity> = ActivityTestRule(BlankTestActivity::class.java)

    @Test
    fun benchmark_onBind_ViewHolder_empty_search_product() {
        val itemView = BenchmarkObject.simpleViewFromLayout(ProductEmptySearchViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = ProductEmptySearchViewHolder(itemView, createEmptyStateListener(), createBannerAdsListener(), Config.Builder().build())
        val data = getEmptySearchProductViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_empty_search_product() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                ProductEmptySearchViewHolder.LAYOUT) {
            ProductEmptySearchViewHolder(it, createEmptyStateListener(), createBannerAdsListener(), Config.Builder().build())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }
}