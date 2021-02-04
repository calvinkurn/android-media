package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectWidget
import kotlinx.android.synthetic.main.item_select.view.*

class SelectViewHolder(view: View, private val selectClickListener: SelectClickListener) : AbstractViewHolder<SelectUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_select
        const val KEY_IS_SELECTED = "key_is_selected"
    }

    private val selectWidget: SelectWidget = itemView.selectWidget

    override fun bind(element: SelectUiModel) {
        selectWidget.bind(element, selectClickListener)
    }

    override fun bind(element: SelectUiModel?, payloads: MutableList<Any>) {
        if (element == null || payloads.isNullOrEmpty()) return

        val bundle = payloads[0] as? Bundle
        bundle?.keySet()?.forEach { key ->
            if (key == KEY_IS_SELECTED) {
                val isSelected = bundle.getBoolean(KEY_IS_SELECTED)
                selectWidget.bind(isSelected)
            }
        }
    }
}