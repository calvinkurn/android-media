package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTermsAndConditionModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifyprinciples.Typography

class AffiliateTermsAndConditionVH(itemView: View)
    : AbstractViewHolder<AffiliateTermsAndConditionModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_terms_condition_item
    }

    override fun bind(element: AffiliateTermsAndConditionModel?) {
        itemView.findViewById<Typography>(R.id.rules_tv).text=element?.data?.text
    }
}
