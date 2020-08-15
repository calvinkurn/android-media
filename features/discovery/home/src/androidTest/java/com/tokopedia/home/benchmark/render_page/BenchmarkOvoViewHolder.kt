package com.tokopedia.home.benchmark.render_page

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.OvoViewHolder
import com.tokopedia.home.environment.BlankTestActivity
import com.tokopedia.home.mock.MockOvoModel
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleAdapter
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleViewFromLayout
import org.junit.Rule
import org.junit.Test

class BenchmarkOvoViewHolder {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var activityRule: ActivityTestRule<BlankTestActivity> = ActivityTestRule(BlankTestActivity::class.java)

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_home_ovo_tokopoint() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = simpleAdapter(
                OvoViewHolder.LAYOUT) {
            OvoViewHolder(it, null)
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_home_ovo_tokopoint() {
        val itemView = simpleViewFromLayout(OvoViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = OvoViewHolder(
                itemView, null
        )
        val data = MockOvoModel.get()
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }
}