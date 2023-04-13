package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentUpsellModel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ShipmentUpsellViewHolder(itemView: View, private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(itemView) {

    private val card: CardUnify2 = itemView.findViewById(R.id.checkout_upsell_card)
    private val image: ImageUnify = itemView.findViewById(R.id.checkout_upsell_image)
    private val title: Typography = itemView.findViewById(R.id.checkout_upsell_title)
    private val description: Typography = itemView.findViewById(R.id.checkout_upsell_description)

    fun bind(data: ShipmentUpsellModel) {
        card.animateOnPress = CardUnify2.ANIMATE_OVERLAY
        card.setOnClickListener {
            shipmentAdapterActionListener.onClickUpsellCard(data)
        }
        image.setImageUrl(data.image)
        title.text = HtmlLinkHelper(description.context, data.title).spannedString
        description.text = HtmlLinkHelper(description.context, data.description).spannedString

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
