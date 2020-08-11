package com.tokopedia.home_component

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home_component.mock.channel.MockChannelModel
import com.tokopedia.home_component.model.DynamicChannelLayout.LAYOUT_PRODUCT_HIGHLIGHT
import com.tokopedia.home_component.test.env.BlankTestActivity
import com.tokopedia.home_component.viewholders.ProductHighlightComponentViewHolder
import com.tokopedia.home_component.visitable.ProductHighlightDataModel
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleAdapter
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleViewFromLayout
import org.junit.Rule
import org.junit.Test

class BenchmarkProductHighlightComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var activityRule: ActivityTestRule<BlankTestActivity> = ActivityTestRule(BlankTestActivity::class.java)

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_product_highlight_component() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = simpleAdapter(
                ProductHighlightComponentViewHolder.LAYOUT) {
            ProductHighlightComponentViewHolder(it, null, null)
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_product_highlight_component() {
        val itemView = simpleViewFromLayout(ProductHighlightComponentViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = ProductHighlightComponentViewHolder(
                itemView, null, null
        )
        val data = ProductHighlightDataModel(MockChannelModel.get(LAYOUT_PRODUCT_HIGHLIGHT))
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }
}