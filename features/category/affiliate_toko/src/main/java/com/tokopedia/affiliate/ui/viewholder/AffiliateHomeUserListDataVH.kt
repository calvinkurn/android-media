package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.model.AffiliatePerformanceData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHomeHeaderModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTermsAndConditionModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateUserPerformanceListModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateUserPerformanceModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifyprinciples.Typography

class AffiliateHomeUserListDataVH(itemView: View)
    : AbstractViewHolder<AffiliateUserPerformanceListModel>(itemView) {


    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_performa_item
    }

    override fun bind(element: AffiliateUserPerformanceListModel?) {
        itemView.findViewById<Typography>(R.id.performa_type).text = element?.data?.metricTitle
        itemView.findViewById<Typography>(R.id.value).text = element?.data?.metricValueFmt
        itemView.findViewById<Typography>(R.id.value_change_value).text = element?.data?.metricDifferenceValueFmt
    }
}
