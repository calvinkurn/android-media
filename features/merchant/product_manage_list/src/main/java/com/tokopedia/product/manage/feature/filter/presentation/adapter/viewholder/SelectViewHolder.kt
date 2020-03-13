package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectWidget
import kotlinx.android.synthetic.main.item_select.view.*

class SelectViewHolder(view: View, private val selectClickListener: SelectClickListener) : AbstractViewHolder<SelectViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_select
    }

    private val selectWidget: SelectWidget = itemView.selectWidget

    override fun bind(element: SelectViewModel) {
        selectWidget.bind(element, selectClickListener)
    }
}