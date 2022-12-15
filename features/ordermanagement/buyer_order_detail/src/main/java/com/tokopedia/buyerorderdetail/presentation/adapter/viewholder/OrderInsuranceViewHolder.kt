package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailInsuranceBinding
import com.tokopedia.buyerorderdetail.presentation.model.OrderInsuranceUiModel
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
            bindListener(element.appLink, element.trackerData)
        }
    }

    override fun bind(element: OrderInsuranceUiModel?, payloads: MutableList<Any>) {
        binding?.run {
            payloads.firstOrNull()?.let {
                if (it is Pair<*, *>) {
                    val (oldItem, newItem) = it
                    if (oldItem is OrderInsuranceUiModel && newItem is OrderInsuranceUiModel) {
                        root.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                        if (oldItem.logoUrl != newItem.logoUrl) {
                            bindLogo(newItem.logoUrl)
                        }
                        if (oldItem.title != newItem.title) {
                            bindTitle(newItem.title)
                        }
                        if (oldItem.subtitle != newItem.subtitle) {
                            bindSubtitle(newItem.subtitle)
                        }
                        if (oldItem.appLink != newItem.appLink) {
                            bindListener(newItem.appLink, newItem.trackerData)
                        }
                        root.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                        return
                    }
                }
            }
        }
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

    private fun bindListener(appLink: String, trackerData: OrderInsuranceUiModel.TrackerData) {
        itemView.setOnClickListener {
            if (navigator.openAppLink(appLink, false)) {
                BuyerOrderDetailTracker.eventClickInsuranceWidget(trackerData)
            }
        }
    }
}
