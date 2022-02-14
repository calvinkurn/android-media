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
        val subheadView=itemView.findViewById<Typography>(R.id.sub_header)
        subheadView.isVisible= element?.headerItem?.isForPortfolio == true
        var header:String?=""
        if(element?.headerItem?.isForPortfolio==true) {
            header = element.headerItem.userName?.let { name ->
                getString(R.string.affiliate_header_text).replace(
                    "{name}",
                    name
                )
            }
            subheadView.text=getString(R.string.affiliate_subheader_text)
        }
        else{
            header=getString(R.string.affiliate_understand_terms)
        }
        itemView.findViewById<Typography>(R.id.header).text=header
    }
}
