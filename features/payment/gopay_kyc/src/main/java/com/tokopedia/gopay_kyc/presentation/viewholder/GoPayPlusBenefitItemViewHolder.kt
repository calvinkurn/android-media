package com.tokopedia.gopay_kyc.presentation.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.domain.data.GoPayPlusBenefit
import kotlinx.android.synthetic.main.gopay_kyc_general_item_layout.view.*

class GoPayPlusBenefitItemViewHolder(val context: Context, val layoutParams: ViewGroup.LayoutParams) {

    private fun getLayout() = R.layout.gopay_kyc_general_item_layout

    fun bindData(benefit: GoPayPlusBenefit): View {
        val benefitView = LayoutInflater.from(context).inflate(getLayout(), null)
        benefitView.apply {
            layoutParams = this@GoPayPlusBenefitItemViewHolder.layoutParams
            goPayPlusBenefitImage.setImageDrawable(ContextCompat.getDrawable(context, benefit.iconDrawable))
            goPayItemBenefitTitle.text = benefit.benefitTitle
            goPayItemBenefitDescription.text = benefit.benefitDescription
        }
        return benefitView
    }
}