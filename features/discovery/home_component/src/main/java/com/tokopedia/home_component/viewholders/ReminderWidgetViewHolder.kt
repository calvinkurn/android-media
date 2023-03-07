package com.tokopedia.home_component.viewholders

import android.text.Html
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentReminderWidgetBinding
import com.tokopedia.home_component.listener.ReminderWidgetListener
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.model.ReminderState
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by firman on 10-06-2020
 */

class ReminderWidgetViewHolder(
        itemView: View,
        val reminderWidgetListener : ReminderWidgetListener?,
        val disableNetwork: Boolean = false,
        private val cardInteraction: Boolean = false
        ): AbstractViewHolder<ReminderWidgetModel>(itemView){

    private var binding: HomeComponentReminderWidgetBinding? by viewBinding()
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
        if (!disableNetwork) {
            performanceMonitoring?.startTrace(performanceTraceName)
        }
        initView(element, itemView)
    }

    override fun bind(element: ReminderWidgetModel, payloads: MutableList<Any>) {
        bind(element)
    }

    fun initView(element: ReminderWidgetModel, itemView: View){
        with(binding) {
            binding?.cardReminder?.apply {
                cardElevation = 0f
                cardType = CardUnify2.TYPE_CLEAR
                animateOnPress = if(cardInteraction) CardUnify2.ANIMATE_OVERLAY_BOUNCE else CardUnify2.ANIMATE_OVERLAY
            }
            if(element.data.reminders.isEmpty()){
                this?.homeReminderRecommendationLoading?.root?.show()
                if (!disableNetwork){
                    reminderWidgetListener?.getReminderWidgetData(element)
                    performanceMonitoring?.stopTrace()
                }
                performanceMonitoring = null
            } else {
                this?.homeReminderRecommendationLoading?.root?.hide()

                val reminder = element.data.reminders.first()
                if(reminder.backgroundColor.isNotEmpty()){
                    this?.reminderRecommendationWidgetContainer?.setGradientBackground(ArrayList(reminder.backgroundColor))
                }

                if (reminder.title.isNotEmpty()) {
                    this?.reminderRecommendationTitle?.text = reminder.title
                }

                this?.icReminderRecommendationProduct?.loadImage(reminder.iconURL)
                val backgroundAsset = when (reminder.state) {
                    ReminderState.NEUTRAL -> R.drawable.bg_reminder_neutral
                    ReminderState.ATTENTION -> R.drawable.bg_reminder_attention
                }
                this?.reminderRecommendationCardBg?.loadImageDrawable(backgroundAsset)

                this?.reminderRecommendationTextMain?.text = Html.fromHtml(reminder.mainText)
                this?.reminderRecommendationTextSub?.text = Html.fromHtml(reminder.subText)

                if (reminder.buttonText.isNotEmpty()) {
                    this?.btnReminderRecommendation?.text = reminder.buttonText
                }
                this?.btnReminderRecommendation?.buttonType = reminder.buttonType

                this?.btnReminderRecommendation?.setOnClickListener {
                    reminderWidgetListener?.onReminderWidgetClickListener(element)
                }

                this?.reminderRecommendationWidgetContainer?.addOnImpressionListener(element, object : ViewHintListener {
                    override fun onViewHint() {
                        reminderWidgetListener?.onReminderWidgetImpressionListener(element)
                    }
                })

                this?.icCloseReminderRecommendation?.setOnClickListener {
                    reminderWidgetListener?.onReminderWidgetDeclineClickListener(element, true)
                }
                if (!disableNetwork) {
                    performanceMonitoring?.stopTrace()
                }
                performanceMonitoring = null
            }
        }
    }
}