package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllBrandHeaderUiModel
import com.tokopedia.unifyprinciples.Typography
import java.text.NumberFormat
import java.util.*

class AllBrandHeaderViewHolder(itemView: View?) : AbstractViewHolder<AllBrandHeaderUiModel>(itemView) {

    private var headerView: Typography? = null
    private var totalBrandView: AppCompatTextView? = null

    init {
        headerView = itemView?.findViewById(R.id.tv_header)
        totalBrandView = itemView?.findViewById(R.id.tv_total_brand)
    }

    override fun bind(element: AllBrandHeaderUiModel?) {

        headerView?.text = element?.title

        element?.totalBrands?.let {
            val totalBrandContent: String = NumberFormat.getNumberInstance(Locale.US)
                    .format(it).toString().replace(",",".") + " " + getString(R.string.brandlist_brand_label)
            totalBrandView?.text = totalBrandContent
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.brandlist_all_brand_header
    }
}