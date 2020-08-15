package com.tokopedia.home.benchmark.render_page

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicIconSectionViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.OvoViewHolder
import com.tokopedia.home.environment.BlankTestActivity
import com.tokopedia.home.mock.MockDynamicIconModel
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleAdapter
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleViewFromLayout
import org.junit.Rule
import org.junit.Test

class BenchmarkDynamicIconViewHolder {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var activityRule: ActivityTestRule<BlankTestActivity> = ActivityTestRule(BlankTestActivity::class.java)

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_dynamic_icon() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = simpleAdapter(
                DynamicIconSectionViewHolder.LAYOUT) {
            DynamicIconSectionViewHolder(it, null)
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_dynamic_icon() {
        val itemView = simpleViewFromLayout(DynamicIconSectionViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = DynamicIconSectionViewHolder(
                itemView, null
        )
        val data = MockDynamicIconModel.get()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }
}