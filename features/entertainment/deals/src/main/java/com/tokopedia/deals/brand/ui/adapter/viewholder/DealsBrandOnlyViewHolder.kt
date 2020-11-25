package com.tokopedia.deals.brand.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.ui.adapter.DealsCommonBrandAdapter
import com.tokopedia.deals.common.ui.adapter.viewholder.DealsBrandViewHolder
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_deals_brand_only.view.*

class DealsBrandOnlyViewHolder(itemView: View,
                               private val brandActionListener: DealsBrandActionListener
) : BaseViewHolder(itemView) {

    fun bind(brands: DealsBrandsDataView) {
        with(itemView) {
            if (!brands.isLoaded){
                cl_containter_brand?.hide()
                shimmering?.show()
            } else {
                shimmering.hide()
                cl_containter_brand?.show()
                setupItemAdapter(itemView, brands)
            }
        }
    }

    private fun setupItemAdapter(itemView: View, brands: DealsBrandsDataView) {
        val adapter = DealsCommonBrandAdapter(brandActionListener, DealsBrandViewHolder.LAYOUT_WIDE)
        itemView.rv_brands?.adapter = adapter
        itemView.rv_brands?.layoutManager = object : GridLayoutManager(itemView.context,4) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                lp?.width = (width / 4)
                return true
            }
        }
        adapter.brandList = brands.brands
    }

    companion object {
        val LAYOUT = R.layout.item_deals_brand_only
    }
}