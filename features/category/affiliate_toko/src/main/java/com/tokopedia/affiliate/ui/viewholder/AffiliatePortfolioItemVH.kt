package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioUrlModel
import com.tokopedia.affiliate_toko.R

class AffiliatePortfolioItemVH(itemView: View)
    : AbstractViewHolder<AffiliatePortfolioUrlModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_input_portfolio_text_field_item
    }

    override fun bind(element: AffiliatePortfolioUrlModel?) {

    }
}
