package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.OfferDescriptionItem
import kotlinx.android.synthetic.main.paylater_cards_content_info_item.view.*

class PayLaterOfferDescriptionViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val tvBenifitDescription: TextView = view.tvBenefitsDesc

    fun bindData(descriptionData: OfferDescriptionItem) {
        tvBenifitDescription.text = descriptionData.offerItemPoint
    }


    companion object {
        val LAYOUT_ID = R.layout.paylater_cards_content_info_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = PayLaterOfferDescriptionViewHolder(
                inflater.inflate(PayLaterOfferDescriptionViewHolder.LAYOUT_ID, parent, false)
        )
    }
}