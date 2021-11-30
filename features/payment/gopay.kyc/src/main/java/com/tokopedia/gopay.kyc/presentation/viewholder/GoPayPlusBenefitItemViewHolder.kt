package com.tokopedia.gopay.kyc.presentation.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gopay.kyc.R
import com.tokopedia.gopay.kyc.domain.data.GoPayPlusBenefit
import kotlinx.android.synthetic.main.gopay_kyc_general_item_layout.view.*

class GoPayPlusBenefitItemViewHolder(val context: Context, private val layoutParams: ViewGroup.LayoutParams) {

    private fun getLayout() = R.layout.gopay_kyc_general_item_layout

    fun bindData(benefit: GoPayPlusBenefit): View {
        val benefitView = LayoutInflater.from(context).inflate(getLayout(), null)
        benefitView.apply {
            layoutParams = this@GoPayPlusBenefitItemViewHolder.layoutParams
            goPayPlusBenefitImage.setImageDrawable(MethodChecker.getDrawable(context, benefit.iconDrawable))
            goPayItemBenefitTitle.text = benefit.benefitTitle
            goPayItemBenefitDescription.text = benefit.benefitDescription
        }
        return benefitView
    }
}