package com.tokopedia.deals.common.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.ui.adapter.DealsCommonBrandAdapter
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_deals_brand_page.view.*

class DealsBrandGridViewHolder(
        itemView: View,
        private val brandActionListener: DealsBrandActionListener
) : BaseViewHolder(itemView) {

    fun bind(brands: DealsBrandsDataView) {
        with(itemView) {
            if (!brands.isLoaded) {
                cl_containter_brand?.hide()
                showShimmering(itemView, brands)
            } else {
                shimmering?.hide()
                one_row_shimmering?.hide()

                if (brands.brands.isNotEmpty()) {
                    cl_containter_brand?.show()
                    showTitle(itemView, brands)
                    showSeeAllText(itemView, brands)
                    setupItemAdapter(itemView, brands)
                } else {
                    cl_containter_brand?.hide()
                }
            }
        }
    }

    private fun showSeeAllText(itemView: View, brands: DealsBrandsDataView) {
        if (brands.seeAllText.isEmpty()) {
            itemView.txt_brand_see_all?.hide()
            itemView.unused_line?.show()
        } else {
            itemView.txt_brand_see_all?.show()
            itemView.unused_line?.hide()
            itemView.txt_brand_see_all?.text = brands.seeAllText
            itemView.txt_brand_see_all?.setOnClickListener {
                brandActionListener.onClickSeeAllBrand(brands.seeAllUrl)
            }
        }
    }

    private fun showTitle(itemView: View, brands: DealsBrandsDataView) {
        if (brands.title.isEmpty()) {
            itemView.tv_brand_title?.hide()
            itemView.unused_line?.hide()
        } else {
            itemView.tv_brand_title?.show()
            itemView.unused_line?.show()
            itemView.tv_brand_title?.text = brands.title
        }
    }

    private fun setupItemAdapter(itemView: View, brands: DealsBrandsDataView) {
        val adapter = DealsCommonBrandAdapter(brandActionListener, DealsBrandViewHolder.LAYOUT_WIDE)
        itemView.rv_brands?.tag = brands.category
        itemView.rv_brands?.adapter = adapter
        itemView.rv_brands?.layoutManager = object : GridLayoutManager(itemView.context, BRAND_SPAN_COUNT) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                lp?.width = (width / BRAND_SPAN_COUNT)
                return true
            }
        }
        adapter.brandList = brands.brands
    }

    private fun showShimmering(itemView: View, brands: DealsBrandsDataView) {
        if (brands.oneRow) {
            itemView.one_row_shimmering?.show()
            itemView.shimmering?.hide()
        } else {
            itemView.shimmering?.show()
            itemView.one_row_shimmering.hide()
        }
    }


    companion object {
        val LAYOUT = R.layout.item_deals_brand_page
        private const val BRAND_SPAN_COUNT = 4
    }
}