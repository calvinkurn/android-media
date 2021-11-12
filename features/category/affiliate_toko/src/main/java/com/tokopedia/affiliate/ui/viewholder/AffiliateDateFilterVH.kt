package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.AffiliateHomeRvDateRangeInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateFilterModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifyprinciples.Typography

class AffiliateDateFilterVH(itemView: View,private val onDateRangeClickInterface : AffiliateHomeRvDateRangeInterface?)
    : AbstractViewHolder<AffiliateDateFilterModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_home_range_picker_item
    }

    override fun bind(element: AffiliateDateFilterModel?) {
        itemView.findViewById<Typography>(R.id.text).text = element?.data?.title
        itemView.findViewById<ConstraintLayout>(R.id.date_range).setOnClickListener {
            onDateRangeClickInterface?.buttonClicked()
        }


    }
}
