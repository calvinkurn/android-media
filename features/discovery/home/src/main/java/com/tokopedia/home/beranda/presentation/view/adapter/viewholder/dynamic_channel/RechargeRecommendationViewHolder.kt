package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.RechargeRecommendationTracking
import com.tokopedia.home.beranda.domain.interactor.DeclineRechargeRecommendationUseCase.Companion.PARAM_CONTENT_ID
import com.tokopedia.home.beranda.domain.interactor.DeclineRechargeRecommendationUseCase.Companion.PARAM_UUID
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.RechargeRecommendationViewModel
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.home_recharge_recommendation_item.view.*

class RechargeRecommendationViewHolder(
        itemView: View,
        private val listener: RechargeRecommendationListener,
        private val categoryListener: HomeCategoryListener
) : AbstractViewHolder<RechargeRecommendationViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_recharge_recommendation_item
    }

    override fun bind(element: RechargeRecommendationViewModel) {
        with(itemView) {
            if (element.rechargeRecommendation.recommendations.isEmpty()) {
                home_recharge_recommendation_loading.show()
            } else {
                home_recharge_recommendation_loading.hide()

                val recommendation = element.rechargeRecommendation.recommendations[0]
                if (recommendation.backgroundColor.isNotEmpty()) {
                    try {
                        recharge_recommendation_widget_container.setBackgroundColor(Color.parseColor(recommendation.backgroundColor))
                    } catch (e: Exception) {
                    }
                }
                if (recommendation.title.isNotEmpty()) {
                    recharge_recommendation_title.text = recommendation.title
                }
                ic_recharge_recommendation_product.loadImage(recommendation.iconURL)
                recharge_recommendation_text_main.text = MethodChecker.fromHtml(recommendation.mainText)
                recharge_recommendation_text_sub.text = MethodChecker.fromHtml(recommendation.subText)

                if (recommendation.buttonText.isNotEmpty()) {
                    btn_recharge_recommendation.text = recommendation.buttonText
                }
                btn_recharge_recommendation.setOnClickListener {
                    categoryListener.getTrackingQueueObj()?.let {
                        RechargeRecommendationTracking.homeRechargeRecommendationOnClickTracker(
                                it, recommendation
                        )
                    }

                    listener.onContentClickListener(recommendation.applink)

                    // Trigger decline listener to remove widget from homepage
                    val requestParams = mapOf(
                            PARAM_UUID to element.rechargeRecommendation.UUID,
                            PARAM_CONTENT_ID to recommendation.contentID)
                    listener.onDeclineClickListener(requestParams)
                }

                addOnImpressionListener(element, object : ViewHintListener {
                    override fun onViewHint() {
                        categoryListener.getTrackingQueueObj()?.let {
                            RechargeRecommendationTracking.homeRechargeRecommendationImpressionTracker(
                                    it, recommendation
                            )
                        }
                    }
                })

                ic_close_recharge_recommendation.setOnClickListener {
                    RechargeRecommendationTracking.homeRechargeRecommendationOnCloseTracker(recommendation)

                    val requestParams = mapOf(
                            PARAM_UUID to element.rechargeRecommendation.UUID,
                            PARAM_CONTENT_ID to recommendation.contentID)
                    listener.onDeclineClickListener(requestParams)
                }
            }
        }
    }

    interface RechargeRecommendationListener {
        fun onContentClickListener(applink: String)
        fun onDeclineClickListener(requestParams: Map<String, String>)
    }
}