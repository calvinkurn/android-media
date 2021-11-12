package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.model.AffiliatePerformanceData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHomeHeaderModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTermsAndConditionModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateUserPerformanceModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifyprinciples.Typography

class AffiliateHomeUserDataVH(itemView: View)
    : AbstractViewHolder<AffiliateUserPerformanceModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_performa_list_item
    }
    private var adapter = AffiliateAdapter(AffiliateAdapterFactory())
    override fun bind(element: AffiliateUserPerformanceModel?) {
        var performRV = itemView.findViewById<RecyclerView>(R.id.performaItem_RV)
        performRV.layoutManager = GridLayoutManager(itemView.context,2)
        performRV.adapter = adapter
    }
}
