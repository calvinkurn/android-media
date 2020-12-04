package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.OfferDescriptionItem
import kotlinx.android.synthetic.main.paylater_cards_content_info_item.view.*

class PayLaterOfferDescriptionViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    private val tvBenifitDescription: TextView = view.tvBenefitsDesc
    private val ivBenifitsHeader: ImageView = view.ivBenifitsHeader

    fun bindData(descriptionData: OfferDescriptionItem) {
        if (descriptionData.isHighlight == true) {
            ivBenifitsHeader.setImageDrawable(AppCompatResources.getDrawable(view.context, R.drawable.ic_paylater_card_info_star))
        } else {
            ivBenifitsHeader.setImageDrawable(AppCompatResources.getDrawable(view.context, R.drawable.ic_paylater_card_info_check))
        }
        tvBenifitDescription.text = descriptionData.offerItemPoint
    }


    companion object {
        private val LAYOUT_ID = R.layout.paylater_cards_content_info_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = PayLaterOfferDescriptionViewHolder(
                inflater.inflate(PayLaterOfferDescriptionViewHolder.LAYOUT_ID, parent, false)
        )
    }
}