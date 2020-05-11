package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchAllBrandLabelViewModel
import kotlinx.android.synthetic.main.brandlist_all_brand_group_header.view.*

class BrandlistSearchAllBrandLabelViewHolder(view: View) : AbstractViewHolder<BrandlistSearchAllBrandLabelViewModel>(view){

    companion object {
        val LAYOUT = R.layout.brandlist_all_brand_group_header
    }

    override fun bind(element: BrandlistSearchAllBrandLabelViewModel) {
        itemView.tv_total_brand.text = element.letter
    }

}