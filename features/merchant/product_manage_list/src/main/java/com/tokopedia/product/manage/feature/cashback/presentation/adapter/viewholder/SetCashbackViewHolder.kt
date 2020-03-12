package com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewmodel.SetCashbackViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectWidget
import kotlinx.android.synthetic.main.item_select.view.*

class SetCashbackViewHolder(view: View, private val selectClickListener: SelectClickListener) : AbstractViewHolder<SetCashbackViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_select
    }

    override fun bind(element: SetCashbackViewModel) {
        val selectWidget: SelectWidget = itemView.select_widget
        val selectViewModel = SelectViewModel(name = element.description, value =  element.cashback.toString(),
                isSelected = element.isSelected)
        selectWidget.bind(selectViewModel, selectClickListener)
    }
}