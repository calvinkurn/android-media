package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.DigitalRecommendationUiModel
import com.tokopedia.digital.digital_recommendation.presentation.widget.DigitalRecommendationWidget
import com.tokopedia.digital.digital_recommendation.utils.DigitalRecommendationData

/**
 * @author by furqan on 22/09/2021
 */
class DigitalRecommendationViewHolder(
    itemView: View,
    val digitalRecommendationData: DigitalRecommendationData,
    val listener: ActionListener
) :
    AbstractViewHolder<DigitalRecommendationUiModel>(itemView) {

    private val dgRecommendation =
        itemView.findViewById<DigitalRecommendationWidget>(R.id.dgRecommendationBuyerOrderDetail)
    private val dgRecommendationWidgetListener = object : DigitalRecommendationWidget.Listener {
        override fun onFetchFailed(throwable: Throwable) {
            listener.hideDigitalRecommendation()
        }

        override fun onEmptyResult() {
            listener.hideDigitalRecommendation()
        }
    }

    override fun bind(element: DigitalRecommendationUiModel?) {
        dgRecommendation.setViewModelFactory(digitalRecommendationData.viewModelFactory)
        dgRecommendation.setLifecycleOwner(digitalRecommendationData.lifecycleOwner)
        dgRecommendation.setAdditionalData(digitalRecommendationData.additionalTrackingData)
        dgRecommendation.setPage(digitalRecommendationData.page)
        dgRecommendation.listener = dgRecommendationWidgetListener
        dgRecommendation.build()
    }

    interface ActionListener {
        fun hideDigitalRecommendation()
    }

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_digital_recommendation
    }

}