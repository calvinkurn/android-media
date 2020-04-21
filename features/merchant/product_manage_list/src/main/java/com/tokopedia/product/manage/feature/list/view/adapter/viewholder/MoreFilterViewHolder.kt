package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.FilterTabViewHolder.*
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel.*
import com.tokopedia.unifycomponents.NotificationUnify
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

            val count = data.filterCount.toString()
            filterCount.setNotification(count, NotificationUnify.COLOR_TEXT_TYPE, NotificationUnify.COLOR_SECONDARY)
            filter.chipImageResource = ContextCompat.getDrawable(context, com.tokopedia.unifyprinciples.R.color.Neutral_N0)
        } else {
            filterCount.hide()
            filter.chipImageResource = ContextCompat.getDrawable(context, com.tokopedia.sortfilter.R.drawable.unify_filter_ic)
        }

        filter.setOnClickListener { listener.onClickMoreFilter(data, filter.chipText.toString()) }
    }
}