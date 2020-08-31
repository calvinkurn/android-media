package com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewmodel.SetCashbackUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectWidget
import kotlinx.android.synthetic.main.item_select.view.*

class SetCashbackViewHolder(view: View, private val selectClickListener: SelectClickListener) : AbstractViewHolder<SetCashbackUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_select
    }

    override fun bind(element: SetCashbackUiModel) {
        val selectWidget: SelectWidget = itemView.selectWidget
        val selectViewModel = SelectUiModel(name = element.description, value =  element.cashback.toString(),
                isSelected = element.isSelected)
        selectWidget.bind(selectViewModel, selectClickListener)
    }
}