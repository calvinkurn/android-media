package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.PayLaterPartnerBenefit
import kotlinx.android.synthetic.main.paylater_cards_content_info_item.view.*

class PayLaterOfferDescriptionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(descriptionData: PayLaterPartnerBenefit) {
        view.apply {
            if (descriptionData.isHighlighted == true) {
                ivBenifitsHeader.setImageDrawable(AppCompatResources.getDrawable(view.context, R.drawable.ic_paylater_card_info_star))
            } else {
                ivBenifitsHeader.setImageDrawable(AppCompatResources.getDrawable(view.context, R.drawable.ic_paylater_card_info_check))
            }
            tvBenefitsDesc.text = descriptionData.partnerBenefitContent ?: ""
        }
    }


    companion object {
        private val LAYOUT_ID = R.layout.paylater_cards_content_info_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = PayLaterOfferDescriptionViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}