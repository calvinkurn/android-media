package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ProductTitleViewModel
import kotlinx.android.synthetic.main.item_af_product_title.view.*

/**
 * @author by milhamj on 18/03/19.
 */
class ProductTitleViewHolder(itemView: View) : AbstractViewHolder<ProductTitleViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_af_product_title
    }

    override fun bind(element: ProductTitleViewModel?) {
        if (element == null) {
            return
        }

        itemView.title.bind(element.title)
    }
}