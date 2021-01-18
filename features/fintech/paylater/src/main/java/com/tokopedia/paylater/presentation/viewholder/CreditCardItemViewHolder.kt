package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.CreditCardItem
import kotlinx.android.synthetic.main.credit_card_item.view.*

class CreditCardItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(creditCardItem: CreditCardItem, creditCardBankName: String?) {
        val labelBenefit = creditCardItem.specialLabel?.split(",")?.toList()
        val size = (labelBenefit?.size ?: 0) - 2
        view.apply {
            tvCreditCardName.text = creditCardItem.cardName
            tvBankName.text = creditCardBankName ?: ""
            ImageHandler.loadImage(context,
                    ivCreditCard,
                    creditCardItem.cardImageUrl,
                    R.drawable.ic_loading_image)
            benefitLabel1.text = labelBenefit?.getOrNull(0)
            benefitLabel2.text = labelBenefit?.getOrNull(1)
            if (size > 0)
                tvBenefitsMore.text = "+${size} Lainnya"
            else tvBenefitsMore.gone()
            if (creditCardItem.isSpecialOffer == true) {
                clCreditCard.background = ContextCompat.getDrawable(context, R.drawable.bg_credit_card_border_recommendation)
                ivRecommendationBadge.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bg_credit_card_header_recommendation))
                recommendationGroup.visible()
            } else {
                clCreditCard.background = null
                recommendationGroup.gone()
            }

        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardItemViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}