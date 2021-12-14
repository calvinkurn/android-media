package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.interfaces.AffiliatePerformaClickInterfaces
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateUserPerformanceModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifyprinciples.Typography

class AffiliateHomeUserDataVH(itemView: View,private val onPerformaGridClick: AffiliatePerformaClickInterfaces?)
    : AbstractViewHolder<AffiliateUserPerformanceModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_performa_list_item
    }
    private var adapter = AffiliateAdapter(AffiliateAdapterFactory(onPerformaGridClick = onPerformaGridClick))
    override fun bind(element: AffiliateUserPerformanceModel?) {
        val performRV = itemView.findViewById<RecyclerView>(R.id.performaItem_RV)
        performRV.layoutManager = GridLayoutManager(itemView.context,2)
        performRV.adapter = adapter
        adapter.resetList()
        adapter.addMoreData(element?.data?.data)
        itemView.findViewById<Typography>(R.id.head).isVisible = element?.data?.showProductCount == true
        itemView.findViewById<Typography>(R.id.sub_header).apply {
            isVisible = element?.data?.showProductCount == true
            text = "${element?.data?.itemCount} Produk"
        }
    }
}
