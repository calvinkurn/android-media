package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchHeaderUiModel
import kotlinx.android.synthetic.main.brandlist_all_brand_header.view.*
import java.text.NumberFormat
import java.util.*

class BrandlistSearchHeaderViewHolder(view: View) : AbstractViewHolder<BrandlistSearchHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.brandlist_all_brand_header
    }

    override fun bind(element: BrandlistSearchHeaderUiModel?) {
        itemView.tv_header.text = element?.headerText
        itemView.tv_total_brand.visibility = View.GONE
        element?.totalBrand?.let{
            itemView.tv_total_brand.text = StringBuilder().append(
                    NumberFormat.getNumberInstance(Locale.US).format(element?.totalBrand).toString())
                    .append(" ")
                    .append(getString(R.string.brandlist_brand_label))
                    .toString().replace(",",".")
            itemView.tv_total_brand.visibility = View.VISIBLE
        }
    }
}