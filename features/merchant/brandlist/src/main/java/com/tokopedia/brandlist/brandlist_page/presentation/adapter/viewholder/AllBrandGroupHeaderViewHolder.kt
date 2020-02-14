package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllBrandGroupHeaderViewModel

class AllBrandGroupHeaderViewHolder(itemView: View?) : AbstractViewHolder<AllBrandGroupHeaderViewModel>(itemView) {

    private var brandGroupHeaderView: AppCompatTextView? = null

    init {
        brandGroupHeaderView = itemView?.findViewById(R.id.tv_brand_group_header)
    }

    override fun bind(element: AllBrandGroupHeaderViewModel?) {
        brandGroupHeaderView?.text = element?.groupHeaderText
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.brandlist_all_brand_group_header
    }
}