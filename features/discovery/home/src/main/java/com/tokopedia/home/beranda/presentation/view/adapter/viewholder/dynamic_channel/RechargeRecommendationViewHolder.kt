package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
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

    var isPressed = false

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_recharge_recommendation_item

        private const val cardBg = "https://ecs7.tokopedia.net/android/others/review_home_bg.png"
    }

    override fun bind(element: RechargeRecommendationViewModel) {
        with(itemView) {
            if (element.rechargeRecommendation.recommendations.isEmpty()) {
                home_recharge_recommendation_loading.show()
            } else {
                home_recharge_recommendation_loading.hide()
                val recommendation = element.rechargeRecommendation.recommendations[0]
                ic_recharge_recommendation_product.loadImage(recommendation.iconURL)
                recharge_recommendation_description.text = recommendation.mainText
                btn_recharge_recommendation.setOnClickListener {
                    HomePageTracking.homeRechargeRecommendationOnClickTracker(
                            categoryListener.trackingQueue, "", recommendation)

                    listener.onContentClickListener(recommendation.applink)
                }

                addOnImpressionListener(element, object : ViewHintListener {
                    override fun onViewHint() {
                        HomePageTracking.homeRechargeRecommendationImpressionTracker(
                                categoryListener.trackingQueue, "", recommendation
                        )
                    }
                })

                ic_close_recharge_recommendation.setOnClickListener {
                    HomePageTracking.homeRechargeRecommendationOnCloseTracker("")

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