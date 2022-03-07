package com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.ItemOrderDetailRechargeDigitalRecommendationBinding
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailDigitalRecommendationModel
import com.tokopedia.digital.digital_recommendation.presentation.widget.DigitalRecommendationWidget
import com.tokopedia.digital.digital_recommendation.utils.DigitalRecommendationData

/**
 * @author by furqan on 01/11/2021
 */
class RechargeOrderDetailDigitalRecommendationViewHolder(
        private val binding: ItemOrderDetailRechargeDigitalRecommendationBinding,
        private val digitalRecommendationData: DigitalRecommendationData,
        private val listener: ActionListener?
) : AbstractViewHolder<RechargeOrderDetailDigitalRecommendationModel>(binding.root) {

    override fun bind(element: RechargeOrderDetailDigitalRecommendationModel?) {
        with(binding) {
            digitalRecommendationRechargeOrderDetail.setViewModelFactory(digitalRecommendationData.viewModelFactory)
            digitalRecommendationRechargeOrderDetail.setLifecycleOwner(digitalRecommendationData.lifecycleOwner)
            digitalRecommendationRechargeOrderDetail.setAdditionalData(digitalRecommendationData.additionalTrackingData)
            digitalRecommendationRechargeOrderDetail.setPage(digitalRecommendationData.page)
            digitalRecommendationRechargeOrderDetail.listener = object : DigitalRecommendationWidget.Listener {
                override fun onFetchFailed(throwable: Throwable) {
                    listener?.hideDigitalRecommendation()
                }

                override fun onEmptyResult() {
                    listener?.hideDigitalRecommendation()
                }
            }
            digitalRecommendationRechargeOrderDetail.build()
        }
    }

    interface ActionListener {
        fun hideDigitalRecommendation()
    }

    companion object {
        val LAYOUT = R.layout.item_order_detail_recharge_digital_recommendation
    }
}