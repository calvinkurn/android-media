package com.tokopedia.gopay.kyc.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gopay.kyc.R

class GoPayBenefitTitleViewHolder(itemView: View): AbstractViewHolder<EmptyModel>(itemView) {
    companion object{
         val LAYOUT_ID = R.layout.gopay_kyc_benefit_title_layout
    }

    override fun bind(element: EmptyModel?) {
    }
}