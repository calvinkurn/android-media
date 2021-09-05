package com.tokopedia.deals.common.ui.adapter.viewholder

import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.ui.adapter.DealsCommonBrandAdapter
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.databinding.ItemDealsBrandPageBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class DealsBrandGridViewHolder(
        itemView: View,
        private val brandActionListener: DealsBrandActionListener
) : BaseViewHolder(itemView) {

    fun bind(brands: DealsBrandsDataView) {
        val binding = ItemDealsBrandPageBinding.bind(itemView)
        with(binding) {
            if (!brands.isLoaded) {
                clContainterBrand.hide()
                showShimmering(binding, brands)
            } else {
                shimmering.root.hide()
                oneRowShimmering.root.hide()

                if (brands.brands.isNotEmpty()) {
                    clContainterBrand.show()
                    showTitle(binding, brands)
                    showSeeAllText(binding, brands)
                    setupItemAdapter(binding, brands)
                } else {
                    clContainterBrand?.hide()
                }
            }
        }
    }

    private fun showSeeAllText(itemView:  ItemDealsBrandPageBinding, brands: DealsBrandsDataView) {
        if (brands.seeAllText.isEmpty()) {
            itemView.txtBrandSeeAll.hide()
            itemView.unusedLine.show()
        } else {
            itemView.txtBrandSeeAll.show()
            itemView.unusedLine.hide()
            itemView.txtBrandSeeAll.text = brands.seeAllText
            itemView.txtBrandSeeAll.setOnClickListener {
                brandActionListener.onClickSeeAllBrand(brands.seeAllUrl)
            }
        }
    }

    private fun showTitle(itemView:  ItemDealsBrandPageBinding, brands: DealsBrandsDataView) {
        if (brands.title.isEmpty()) {
            itemView.tvBrandTitle.hide()
            itemView.unusedLine.hide()
        } else {
            itemView.tvBrandTitle.show()
            itemView.unusedLine.show()
            itemView.tvBrandTitle.text = brands.title
        }
    }

    private fun setupItemAdapter(itemView:  ItemDealsBrandPageBinding, brands: DealsBrandsDataView) {
        val adapter = DealsCommonBrandAdapter(brandActionListener, DealsBrandViewHolder.LAYOUT_WIDE)
        itemView.rvBrands.tag = brands.category
        itemView.rvBrands.adapter = adapter
        itemView.rvBrands.layoutManager = object : GridLayoutManager(itemView.root.context, BRAND_SPAN_COUNT) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                lp?.width = (width / BRAND_SPAN_COUNT)
                return true
            }
        }
        adapter.brandList = brands.brands
    }

    private fun showShimmering(itemView: ItemDealsBrandPageBinding, brands: DealsBrandsDataView) {
        if (brands.oneRow) {
            itemView.oneRowShimmering.root.show()
            itemView.shimmering.root.hide()
        } else {
            itemView.shimmering.root.show()
            itemView.oneRowShimmering.root.hide()
        }
    }


    companion object {
        val LAYOUT = R.layout.item_deals_brand_page
        private const val BRAND_SPAN_COUNT = 4
    }
}