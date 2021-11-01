package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHeaderModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifyprinciples.Typography

class AffiliateHeaderItemVH(itemView: View)
    : AbstractViewHolder<AffiliateHeaderModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_input_portfolio_header_item
    }

    override fun bind(element: AffiliateHeaderModel?) {
        val header= element?.headerItem?.userName?.let { name->
            getString(R.string.affiliate_header_text).replace("{name}",
                name
            )
        }
        itemView.findViewById<Typography>(R.id.header).text=header
        val subheadView=itemView.findViewById<Typography>(R.id.sub_header)
        subheadView.isVisible= element?.headerItem?.isForPortfolio == true
        if(element?.headerItem?.isForPortfolio == true) {
            subheadView.text=getString(R.string.affiliate_subheader_text)
        }
    }
}
