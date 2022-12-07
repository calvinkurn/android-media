package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.interfaces.AffiliatePerformaClickInterfaces
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateUserPerformanceModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifyprinciples.Typography

class AffiliateHomeUserDataVH(
    itemView: View,
    onPerformaGridClick: AffiliatePerformaClickInterfaces?
) : AbstractViewHolder<AffiliateUserPerformanceModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_performa_list_item

        private const val SPAN_TWO = 2
        private const val SPAN_ONE = 1
    }

    private var adapter =
        AffiliateAdapter(AffiliateAdapterFactory(onPerformaGridClick = onPerformaGridClick))

    override fun bind(element: AffiliateUserPerformanceModel?) {
        val performRV = itemView.findViewById<RecyclerView>(R.id.performaItem_RV)
        performRV.layoutManager = GridLayoutManager(itemView.context, 2).apply {
            spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0) SPAN_TWO else SPAN_ONE
                }
            }
        }
        performRV.adapter = adapter
        adapter.resetList()
        adapter.addMoreData(element?.data)
        itemView.findViewById<Typography>(R.id.head).apply {
            setText(if (element?.affiliateShopAdpEnabled == true) R.string.link_dengan_performa else R.string.affiliate_promoted_products)
        }
    }
}
