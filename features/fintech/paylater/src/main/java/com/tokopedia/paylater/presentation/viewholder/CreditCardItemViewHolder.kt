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

        view.apply {
            tvCreditCardName.text = creditCardItem.cardName
            tvBankName.text = creditCardBankName ?: ""
            ImageHandler.loadImage(context,
                    ivCreditCard,
                    creditCardItem.cardImageUrl,
                    R.drawable.ic_loading_image)
            setLabelData(this, creditCardItem.specialLabel)
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

    private fun setLabelData(itemView: View, labelBenefit: String?) {
        val labelBenefitList = labelBenefit?.split(",")?.toList()
        val size = (labelBenefitList?.size ?: 0) - 2
        itemView.apply {
            val benefitItemFirst = labelBenefitList?.getOrNull(0)?.trim()
            val benefitItemSecond = labelBenefitList?.getOrNull(1)?.trim()
            if (benefitItemFirst.isNullOrEmpty()) benefitLabel1.gone()
            else {
                benefitLabel1.visible()
                benefitLabel1.text = benefitItemFirst
            }
            if (benefitItemSecond.isNullOrEmpty()) benefitLabel2.gone()
            else {
                benefitLabel2.visible()
                benefitLabel2.text = benefitItemSecond
            }
            if (size > 0)
                tvBenefitsMore.text = "+${size} Lainnya"
            else tvBenefitsMore.gone()
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardItemViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}