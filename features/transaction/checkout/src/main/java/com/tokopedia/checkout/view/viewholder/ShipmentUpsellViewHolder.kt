package com.tokopedia.checkout.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemUpsellBinding
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentUpsellModel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.HtmlLinkHelper

class ShipmentUpsellViewHolder(private val binding: ItemUpsellBinding, private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ShipmentUpsellModel) {
        binding.checkoutUpsellCard.animateOnPress = CardUnify2.ANIMATE_OVERLAY
        binding.checkoutUpsellCard.setOnClickListener {
            shipmentAdapterActionListener.onClickUpsellCard(data)
        }
        binding.checkoutUpsellImage.setImageUrl(data.image)
        binding.checkoutUpsellTitle.text = HtmlLinkHelper(binding.root.context, data.title).spannedString
        binding.checkoutUpsellDescription.text = HtmlLinkHelper(binding.root.context, data.description).spannedString

        if (!data.hasSeenUpsell) {
            data.hasSeenUpsell = true
            shipmentAdapterActionListener.onViewUpsellCard(data)
        }
    }

    companion object {
        @JvmField
        val ITEM_VIEW_UPSELL = R.layout.item_upsell
    }
}
