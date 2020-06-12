package com.tokopedia.home_component.viewholders

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.RechargeRecommendationListener
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import kotlinx.android.synthetic.main.home_component_reminder_widget.view.*
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.home_component.listener.SalamWidgetListener
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.kotlin.extensions.view.*

/**
 * @author by firman on 10-06-2020
 */

class ReminderWidgetViewHolder(
        itemView: View,
        private val rechargeListener: RechargeRecommendationListener,
        private val salamListener : SalamWidgetListener,
        val homeComponentListener: HomeComponentListener
        ): AbstractViewHolder<ReminderWidgetModel>(itemView){

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_reminder_widget
    }

    override fun bind(element: ReminderWidgetModel) {
        with(itemView) {
            if(element.data.reminders.isEmpty()){
                home_reminder_recommendation_loading.show()
            } else {
                home_reminder_recommendation_loading.hide()

                val reminder = element.data.reminders.first()
                if(reminder.backgroundColor.isNotEmpty()){
                    try {
                        reminder_recommendation_widget_container.setBackgroundColor(Color.parseColor(reminder.backgroundColor))
                    } catch (e: Exception) {
                    }
                }

                if (reminder.title.isNotEmpty()) {
                    reminder_recommendation_title.text = reminder.title
                }

                ic_reminder_recommendation_product.loadImage(reminder.iconURL)
                reminder_recommendation_text_main.text = MethodChecker.fromHtml(reminder.mainText)
                reminder_recommendation_text_sub.text = MethodChecker.fromHtml(reminder.subText)

                if (reminder.buttonText.isNotEmpty()) {
                    btn_reminder_recommendation.text = reminder.buttonText
                }

                btn_reminder_recommendation.setOnClickListener {
                    if(element.source == ReminderEnum.RECHARGE) {
                        rechargeListener.onRechargeRecommendationClickListener(reminder)
                        rechargeListener.onRechargeRecommendationDeclineClickListener(reminder)
                    } else if(element.source == ReminderEnum.SALAM){
                        salamListener.onSalamWidgetClickListener(reminder)
                    }
                }

                addOnImpressionListener(element, object : ViewHintListener {
                    override fun onViewHint() {
                        if(element.source == ReminderEnum.RECHARGE) {
                            rechargeListener.onRechargeRecommendationImpressionListener(reminder)
                        } else if(element.source == ReminderEnum.SALAM){
                            //to do salam tracking
                        }
                    }
                })

                ic_close_reminder_recommendation.setOnClickListener {
                    if(element.source == ReminderEnum.RECHARGE) {
                        rechargeListener.onRechargeRecommendationDeclineTrackingListener(reminder)
                        rechargeListener.onRechargeRecommendationDeclineClickListener(reminder)
                    } else if(element.source == ReminderEnum.SALAM){
                        //to do salam tracking
                    }
                }
            }
        }
    }
}