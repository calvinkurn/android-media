package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHomeHeaderModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTermsAndConditionModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifyprinciples.Typography

class AffiliateHomeHeaderVH(itemView: View)
    : AbstractViewHolder<AffiliateHomeHeaderModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_home_title_items
    }

    override fun bind(element: AffiliateHomeHeaderModel?) {

    }
}
