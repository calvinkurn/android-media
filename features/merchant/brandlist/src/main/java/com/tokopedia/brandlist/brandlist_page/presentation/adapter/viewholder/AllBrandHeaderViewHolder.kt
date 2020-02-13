package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllBrandHeaderViewModel
import com.tokopedia.unifyprinciples.Typography

class AllBrandHeaderViewHolder(itemView: View?) : AbstractViewHolder<AllBrandHeaderViewModel>(itemView) {

    private var headerView: Typography? = null
    private var totalBrandView: AppCompatTextView? = null

    init {
        headerView = itemView?.findViewById(R.id.tv_header)
        totalBrandView = itemView?.findViewById(R.id.tv_total_brand)
    }

    override fun bind(element: AllBrandHeaderViewModel?) {

        headerView?.text = element?.title

        val totalBrandAmount = element?.totalBrands?.toString()
        totalBrandAmount?.let {
            totalBrandView?.text = totalBrandAmount + " " + "Brand"
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.brandlist_all_brand_header
    }
}