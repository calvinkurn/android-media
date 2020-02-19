package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchHeaderViewModel
import kotlinx.android.synthetic.main.brandlist_all_brand_header.view.*

class BrandlistSearchHeaderViewHolder(view: View) : AbstractViewHolder<BrandlistSearchHeaderViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.brandlist_all_brand_header
    }

    override fun bind(element: BrandlistSearchHeaderViewModel?) {
        itemView.tv_header.text = element?.headerText
        if(element?.totalBrand.isNullOrEmpty()) {
            itemView.tv_total_brand.visibility = View.GONE
        } else {
            itemView.tv_total_brand.text = StringBuilder().append(
                    element?.totalBrand)
                    .append(BrandlistSearchHeaderViewModel.TOTAL_BRANDS_DESCRIPTION)
                    .toString()
            itemView.tv_total_brand.visibility = View.VISIBLE
        }
    }
}