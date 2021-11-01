package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioButtonModel
import com.tokopedia.affiliate_toko.R

class AffiliatePortfolioButtonItemVH(itemView: View)
    : AbstractViewHolder<AffiliatePortfolioButtonModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_input_portfolio_button_item
    }

    override fun bind(element: AffiliatePortfolioButtonModel?) {

    }
}
