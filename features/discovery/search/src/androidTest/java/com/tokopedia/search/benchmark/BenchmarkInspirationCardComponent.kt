package com.tokopedia.search.benchmark

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.search.createInspirationCardListener
import com.tokopedia.search.env.BlankTestActivity
import com.tokopedia.search.mock.MockSearchProductModel.getInspirationCardViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BigGridInspirationCardViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridInspirationCardViewHolder
import com.tokopedia.test.application.benchmark_component.BenchmarkObject
import org.junit.Rule
import org.junit.Test

internal class BenchmarkInspirationCardComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var activityRule: ActivityTestRule<BlankTestActivity> = ActivityTestRule(BlankTestActivity::class.java)

    @Test
    fun benchmark_onBind_ViewHolder_inspiration_card_small_grid() {
        val itemView = BenchmarkObject.simpleViewFromLayout(SmallGridInspirationCardViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = SmallGridInspirationCardViewHolder(
                itemView, createInspirationCardListener())
        val data = getInspirationCardViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_inspiration_card_small_grid() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                SmallGridInspirationCardViewHolder.LAYOUT) {
            SmallGridInspirationCardViewHolder(it, createInspirationCardListener())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_inspiration_card_big_grid() {
        val itemView = BenchmarkObject.simpleViewFromLayout(BigGridInspirationCardViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = BigGridInspirationCardViewHolder(
                itemView, createInspirationCardListener())
        val data = getInspirationCardViewModel()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_inspiration_card_big_grid() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = BenchmarkObject.simpleAdapter(
                BigGridInspirationCardViewHolder.LAYOUT) {
            BigGridInspirationCardViewHolder(it, createInspirationCardListener())
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }
}