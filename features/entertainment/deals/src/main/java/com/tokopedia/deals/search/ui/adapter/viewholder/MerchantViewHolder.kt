package com.tokopedia.deals.search.ui.adapter.viewholder

import android.content.res.Resources
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.search.model.visitor.MerchantModelModel
import com.tokopedia.deals.search.listener.DealsSearchListener
import com.tokopedia.deals.search.ui.adapter.DealsBrandAdapter

class MerchantViewHolder(itemView: View, searchListener: DealsSearchListener): AbstractViewHolder<MerchantModelModel>(itemView) {

    private val brandRv: RecyclerView = itemView.findViewById(R.id.rv_brands)
    private val brandAdapter: DealsBrandAdapter = DealsBrandAdapter(searchListener)

    init {
        brandRv.adapter = brandAdapter
        brandRv.layoutManager = object: LinearLayoutManager(itemView.context, HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                lp?.width = (width / 4) -  dpToPx(12)
                return true
            }
        }
    }

    private fun dpToPx(dp: Int): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()

    override fun bind(element: MerchantModelModel?) {
        element?.let {
            brandAdapter.brandList = it.merchantList
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_deals_brand_list_search
    }

}