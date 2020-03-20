package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.FilterViewHolder.*
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel.*
import com.tokopedia.unifycomponents.ImageUnify
import kotlinx.android.synthetic.main.item_product_manage_more_filter.view.*

class MoreFilterViewHolder(
    itemView: View,
    private val listener: ProductFilterListener
): AbstractViewHolder<MoreFilter>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_more_filter
    }

    private val filter by lazy { itemView.chipFilter }
    private val filterCount by lazy { itemView.filterCount }
    private val context by lazy { itemView.context }

    override fun bind(data: MoreFilter) {
        if(data.filterCount > 0) {
            filterCount.show()
            filterCount.text = data.filterCount.toString()

            filter.chip_image_icon.type = ImageUnify.TYPE_CIRCLE
            filter.chipImageResource = ContextCompat.getDrawable(context, R.color.unify_G500)
        } else {
            filterCount.hide()
            filter.chipImageResource = ContextCompat.getDrawable(context, R.drawable.unify_filter_ic)
        }

        filter.setOnClickListener { listener.onClickMoreFilter(data, filter.chipText.toString()) }
    }
}