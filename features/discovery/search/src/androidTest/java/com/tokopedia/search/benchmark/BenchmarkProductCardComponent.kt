package com.tokopedia.search.benchmark

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.search.createProductItemListener
import com.tokopedia.search.env.BlankTestActivity
import com.tokopedia.search.mock.MockSearchProductModel.getProductItemViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BigGridProductItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ListProductItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder
import com.tokopedia.test.application.benchmark_component.BenchmarkObject
import org.junit.Rule
import org.junit.Test

internal class BenchmarkProductCardComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var activityRule: ActivityTestRule<BlankTestActivity> = ActivityTestRule(BlankTestActivity::class.java)

    @Test
    fun benchmark_onBind_ViewHolder_product_card_small_grid() {
        val itemView = BenchmarkObject.simpleViewFromLayout(SmallGridProductItemViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = SmallGridProductItemViewHolder(
                itemView, createProductItemListener())
        val data = getProductItemViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_product_card_small_grid() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                SmallGridProductItemViewHolder.LAYOUT) {
            SmallGridProductItemViewHolder(it, createProductItemListener())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_product_card_big_grid() {
        val itemView = BenchmarkObject.simpleViewFromLayout(BigGridProductItemViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = BigGridProductItemViewHolder(
                itemView, createProductItemListener())
        val data = getProductItemViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_product_card_big_grid() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                BigGridProductItemViewHolder.LAYOUT) {
            BigGridProductItemViewHolder(it, createProductItemListener())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_product_card_list() {
        val itemView = BenchmarkObject.simpleViewFromLayout(ListProductItemViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = ListProductItemViewHolder(
                itemView, createProductItemListener())
        val data = getProductItemViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_product_card_list() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                ListProductItemViewHolder.LAYOUT) {
            ListProductItemViewHolder(it, createProductItemListener())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }
}