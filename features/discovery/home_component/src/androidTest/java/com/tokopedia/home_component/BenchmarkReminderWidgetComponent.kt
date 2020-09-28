package com.tokopedia.home_component

import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.home_component.mock.reminder_widget.MockReminderWidget
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.viewholders.ReminderWidgetViewHolder
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleAdapter
import com.tokopedia.test.application.benchmark_component.BenchmarkObject.simpleViewFromLayout
import com.tokopedia.test.application.benchmark_component.BenchmarkViewRule
import org.junit.Rule
import org.junit.Test

class BenchmarkReminderWidgetComponent {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    val benchmarkViewRule = BenchmarkViewRule()

    @Test
    fun benchmark_onCreateViewHolder_ViewHolder_reminder_widget_component() {
        val viewGroup = FrameLayout(benchmarkViewRule.getBenchmarkActivity())
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
        val itemView = simpleViewFromLayout(ReminderWidgetViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
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
        val itemView = simpleViewFromLayout(ReminderWidgetViewHolder.LAYOUT, benchmarkViewRule.getBenchmarkActivity())
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