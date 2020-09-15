package com.tokopedia.home_component.viewholders

import android.text.Html
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.ReminderWidgetListener
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.model.ReminderState
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import kotlinx.android.synthetic.main.home_component_reminder_widget.view.*
import com.tokopedia.kotlin.extensions.view.*

/**
 * @author by firman on 10-06-2020
 */

class ReminderWidgetViewHolder(
        itemView: View,
        val reminderWidgetListener : ReminderWidgetListener?,
        val disableNetwork: Boolean = false
        ): AbstractViewHolder<ReminderWidgetModel>(itemView){

    private var performanceMonitoring: PerformanceMonitoring? = null
    private val performanceTraceName = "mp_home_recharge_widget_load_time"
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_reminder_widget
    }

    init {
        performanceMonitoring = PerformanceMonitoring()
    }

    override fun bind(element: ReminderWidgetModel) {
        if (!disableNetwork) performanceMonitoring?.startTrace(performanceTraceName)
        initView(element, itemView)
    }

    override fun bind(element: ReminderWidgetModel, payloads: MutableList<Any>) {
        bind(element)
    }

    fun initView(element: ReminderWidgetModel, itemView: View){
        with(itemView) {
            if(element.data.reminders.isEmpty()){
                home_reminder_recommendation_loading.show()
                if (!disableNetwork){
                    reminderWidgetListener?.getReminderWidgetData(element)
                    performanceMonitoring?.stopTrace()
                }
                performanceMonitoring = null
            } else {
                home_reminder_recommendation_loading.hide()

                val reminder = element.data.reminders.first()
                if(reminder.backgroundColor.isNotEmpty()){
                    reminder_recommendation_widget_container.setGradientBackground(ArrayList(reminder.backgroundColor))
                }

                if (reminder.title.isNotEmpty()) {
                    reminder_recommendation_title.text = reminder.title
                }

                ic_reminder_recommendation_product.loadImage(reminder.iconURL)
                val backgroundAsset = when (reminder.state) {
                    ReminderState.NEUTRAL -> R.drawable.bg_reminder_neutral
                    ReminderState.ATTENTION -> R.drawable.bg_reminder_attention
                }
                reminder_recommendation_card_bg.loadImageDrawable(backgroundAsset)

                reminder_recommendation_text_main.text = Html.fromHtml(reminder.mainText)
                reminder_recommendation_text_sub.text = Html.fromHtml(reminder.subText)

                if (reminder.buttonText.isNotEmpty()) {
                    btn_reminder_recommendation.text = reminder.buttonText
                }
                btn_reminder_recommendation.buttonType = reminder.buttonType

                btn_reminder_recommendation.setOnClickListener {
                    reminderWidgetListener?.onReminderWidgetClickListener(element)
                    reminderWidgetListener?.onReminderWidgetDeclineClickListener(element, false)
                }

                addOnImpressionListener(element, object : ViewHintListener {
                    override fun onViewHint() {
                        reminderWidgetListener?.onReminderWidgetImpressionListener(element)
                    }
                })

                ic_close_reminder_recommendation.setOnClickListener {
                    reminderWidgetListener?.onReminderWidgetDeclineClickListener(element, true)
                }
                if (!disableNetwork) performanceMonitoring?.stopTrace()
                performanceMonitoring = null
            }
        }
    }
}