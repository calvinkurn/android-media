package com.tokopedia.home_component.viewholders

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.ReminderWidgetListener
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import kotlinx.android.synthetic.main.home_component_reminder_widget.view.*
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*

/**
 * @author by firman on 10-06-2020
 */

class ReminderWidgetViewHolder(
        itemView: View,
        private val listener: ReminderWidgetListener,
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

                }

                addOnImpressionListener(element, object : ViewHintListener {
                    override fun onViewHint() {

                    }
                })

                ic_close_reminder_recommendation.setOnClickListener {

                }
            }
        }
    }
}

//class RechargeRecommendationViewHolder(
//        itemView: View,
//        private val listener: RechargeRecommendationListener,
//        private val categoryListener: HomeCategoryListener
//) : AbstractViewHolder<RechargeRecommendationViewModel>(itemView) {
//
//    companion object {
//        @LayoutRes
//        val LAYOUT = R.layout.home_recharge_recommendation_item
//    }
//
//    override fun bind(element: RechargeRecommendationViewModel) {
//        with(itemView) {
//            if (element.rechargeRecommendation.recommendations.isEmpty()) {
//                home_recharge_recommendation_loading.show()
//            } else {
//                home_recharge_recommendation_loading.hide()
//
//                val recommendation = element.rechargeRecommendation.recommendations[0]
//                if (recommendation.backgroundColor.isNotEmpty()) {
//                    try {
//                        recharge_recommendation_widget_container.setBackgroundColor(Color.parseColor(recommendation.backgroundColor))
//                    } catch (e: Exception) {
//                    }
//                }
//                if (recommendation.title.isNotEmpty()) {
//                    recharge_recommendation_title.text = recommendation.title
//                }
//                ic_recharge_recommendation_product.loadImage(recommendation.iconURL)
//                recharge_recommendation_text_main.text = MethodChecker.fromHtml(recommendation.mainText)
//                recharge_recommendation_text_sub.text = MethodChecker.fromHtml(recommendation.subText)
//
//                if (recommendation.buttonText.isNotEmpty()) {
//                    btn_recharge_recommendation.text = recommendation.buttonText
//                }
//                btn_recharge_recommendation.setOnClickListener {
//                    categoryListener.getTrackingQueueObj()?.let {
//                        RechargeRecommendationTracking.homeRechargeRecommendationOnClickTracker(
//                                it, recommendation
//                        )
//                    }
//
//                    listener.onContentClickListener(recommendation.applink)
//
//                    // Trigger decline listener to remove widget from homepage
//                    val requestParams = mapOf(
//                            PARAM_UUID to element.rechargeRecommendation.UUID,
//                            PARAM_CONTENT_ID to recommendation.contentID)
//                    listener.onDeclineClickListener(requestParams)
//                }
//
//                addOnImpressionListener(element, object : ViewHintListener {
//                    override fun onViewHint() {
//                        categoryListener.getTrackingQueueObj()?.let {
//                            RechargeRecommendationTracking.homeRechargeRecommendationImpressionTracker(
//                                    it, recommendation
//                            )
//                        }
//                    }
//                })
//
//                ic_close_recharge_recommendation.setOnClickListener {
//                    RechargeRecommendationTracking.homeRechargeRecommendationOnCloseTracker(recommendation)
//
//                    val requestParams = mapOf(
//                            PARAM_UUID to element.rechargeRecommendation.UUID,
//                            PARAM_CONTENT_ID to recommendation.contentID)
//                    listener.onDeclineClickListener(requestParams)
//                }
//            }
//        }
//    }
//
//    interface RechargeRecommendationListener {
//        fun onContentClickListener(applink: String)
//        fun onDeclineClickListener(requestParams: Map<String, String>)
//    }
//}