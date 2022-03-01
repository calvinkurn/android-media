package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateWithdrawalTitleItemModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifyprinciples.Typography

class AffiliateWithdrawalTitleItemVH(itemView: View)
    : AbstractViewHolder<AffiliateWithdrawalTitleItemModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_withdrawal_title_item
    }

    override fun bind(element: AffiliateWithdrawalTitleItemModel?) {
        itemView.findViewById<Typography>(R.id.title)?.apply {
            text = element?.count?.let {
                itemView.context.getString(R.string.affiliate_withdrwal_title_terms).replace("{count}",
                    it
                )
            }
        }
    }
}
