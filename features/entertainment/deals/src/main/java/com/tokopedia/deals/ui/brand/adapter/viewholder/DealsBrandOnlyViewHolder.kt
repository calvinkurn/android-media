package com.tokopedia.deals.ui.brand.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.ui.adapter.DealsCommonBrandAdapter
import com.tokopedia.deals.common.ui.adapter.viewholder.DealsBrandViewHolder
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.databinding.ItemDealsBrandOnlyBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class DealsBrandOnlyViewHolder(itemView: View,
                               private val brandActionListener: DealsBrandActionListener
) : BaseViewHolder(itemView) {

    fun bind(brands: DealsBrandsDataView) {
        val binding = ItemDealsBrandOnlyBinding.bind(itemView)
        with(binding){
            if (!brands.isLoaded){
                clContainterBrand.hide()
                shimmering.root.show()
            } else {
                shimmering.root.hide()
                clContainterBrand.show()
                setupItemAdapter(binding, brands)
            }

        }
    }

    private fun setupItemAdapter(itemView: ItemDealsBrandOnlyBinding, brands: DealsBrandsDataView) {
        val adapter = DealsCommonBrandAdapter(brandActionListener, DealsBrandViewHolder.LAYOUT_WIDE)
        itemView.rvBrands.adapter = adapter
        itemView.rvBrands.layoutManager = object : GridLayoutManager(itemView.root.context,4) {
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
