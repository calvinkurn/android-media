package com.tokopedia.pdpsimulation.paylater.presentation.detail.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterPartnerBenefit
import kotlinx.android.synthetic.main.base_payment_offer_description_item.view.*


class PayLaterOfferDescriptionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(descriptionData: PayLaterPartnerBenefit) {
        view.apply {
            if (descriptionData.isHighlighted == true) {
                ivBenefitsHeader.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_paylater_card_info_star))
            } else {
                AppCompatResources.getDrawable(context, com.tokopedia.iconunify.R.drawable.iconunify_check_big)?.let {
                    val backgroundDrawable = DrawableCompat.wrap(it).mutate()
                    DrawableCompat.setTint(backgroundDrawable, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                    ivBenefitsHeader.setImageDrawable(backgroundDrawable)

                }
            }
            tvBenefitsDesc.text = descriptionData.partnerBenefitContent ?: ""
        }
    }


    companion object {
        private val LAYOUT_ID = R.layout.base_payment_offer_description_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = PayLaterOfferDescriptionViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}