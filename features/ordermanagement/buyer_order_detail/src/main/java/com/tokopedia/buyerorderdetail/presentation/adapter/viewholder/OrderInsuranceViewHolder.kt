package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailInsuranceBinding
import com.tokopedia.buyerorderdetail.presentation.model.OrderInsuranceUiModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class OrderInsuranceViewHolder(
    itemView: View,
    private val navigator: BuyerOrderDetailNavigator
) : AbstractViewHolder<OrderInsuranceUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_insurance
    }

    private val binding by viewBinding<ItemBuyerOrderDetailInsuranceBinding>()

    override fun bind(element: OrderInsuranceUiModel?) {
        if (element != null) {
            binding?.bindLogo(element.logoUrl)
            binding?.bindTitle(element.title)
            binding?.bindSubtitle(element.subtitle)
            bindListener(element)
        }
    }

    override fun bind(element: OrderInsuranceUiModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun ItemBuyerOrderDetailInsuranceBinding.bindLogo(logoUrl: String) {
        ivBuyerOrderDetailInsuranceLogo.loadImage(logoUrl)
    }

    private fun ItemBuyerOrderDetailInsuranceBinding.bindTitle(title: String) {
        tvBuyerOrderDetailInsuranceTitle.text = title
    }

    private fun ItemBuyerOrderDetailInsuranceBinding.bindSubtitle(subtitle: String) {
        tvBuyerOrderDetailInsuranceSubtitle.text = subtitle
    }

    private fun bindListener(element: OrderInsuranceUiModel) {
        itemView.setOnClickListener {
            if (navigator.openAppLink(element.appLink, false)) {
                BuyerOrderDetailTracker.eventClickInsuranceWidget(element.trackerData)
            }
        }
        itemView.addOnImpressionListener(element.impressHolder) {
            BuyerOrderDetailTracker.eventImpressionInsuranceWidget(element.trackerData)
        }
    }
}
