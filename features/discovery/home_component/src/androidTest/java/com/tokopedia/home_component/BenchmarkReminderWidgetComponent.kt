package com.tokopedia.home_component

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home_component.mock.reminder_widget.MockReminderWidget
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.test.env.BlankTestActivity
import com.tokopedia.home_component.viewholders.ReminderWidgetViewHolder
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleAdapter
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleViewFromLayout
import org.junit.Rule
import org.junit.Test

class BenchmarkReminderWidgetComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var activityRule: ActivityTestRule<BlankTestActivity> = ActivityTestRule(BlankTestActivity::class.java)

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_reminder_widget_component() {
        val viewGroup = FrameLayout(activityRule.activity)
        val recyclerViewAdapter = simpleAdapter(
                ReminderWidgetViewHolder.LAYOUT) {
            ReminderWidgetViewHolder(it, null, disableNetwork = true)
        }

        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                recyclerViewAdapter.onCreateViewHolder(viewGroup, 0)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_reminder_widget_recharge_component() {
        val itemView = simpleViewFromLayout(ReminderWidgetViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = ReminderWidgetViewHolder(
                itemView, null, disableNetwork = true
        )
        val data = ReminderWidgetModel(MockReminderWidget.get(), ReminderEnum.RECHARGE)
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }

    @Test
    fun benchmark_onBind_ViewHolder_reminder_widget_salam_component() {
        val itemView = simpleViewFromLayout(ReminderWidgetViewHolder.LAYOUT, activityRule.activity)
        val viewHolder = ReminderWidgetViewHolder(
                itemView, null, disableNetwork = true
        )
        val data = ReminderWidgetModel(MockReminderWidget.get(), ReminderEnum.SALAM)
        benchmarkRule.measureRepeated {
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                viewHolder.bind(data)
            }
        }
    }
}