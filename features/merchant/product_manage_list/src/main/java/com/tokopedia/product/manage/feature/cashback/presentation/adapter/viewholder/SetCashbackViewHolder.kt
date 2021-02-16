package com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewholder

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.cashback.presentation.adapter.viewmodel.SetCashbackUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder.SelectViewHolder
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectWidget
import kotlinx.android.synthetic.main.item_select.view.*

class SetCashbackViewHolder(view: View, private val selectClickListener: SelectClickListener) : AbstractViewHolder<SetCashbackUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_select
        const val KEY_IS_SELECTED_CASHBACK = "key_is_selected_cashback"
    }

    override fun bind(element: SetCashbackUiModel) {
        val selectWidget: SelectWidget = itemView.selectWidget
        val selectViewModel = SelectUiModel(name = element.description, value =  element.cashback.toString(),
                isSelected = element.isSelected)
        selectWidget.bind(selectViewModel, selectClickListener)
    }

    override fun bind(element: SetCashbackUiModel?, payloads: MutableList<Any>) {
        if(element == null || payloads.isNullOrEmpty()) return

        val selectWidget: SelectWidget = itemView.selectWidget

        val bundle = payloads.getOrNull(0) as? Bundle
        bundle?.keySet()?.forEach { key ->
            if (key == KEY_IS_SELECTED_CASHBACK) {
                val isSelected = bundle.getBoolean(KEY_IS_SELECTED_CASHBACK)
                val selectViewModel = SelectUiModel(name = element.description, value =  element.cashback.toString(),
                        isSelected = isSelected)
                selectWidget.bindPayload(selectViewModel, selectClickListener)
            }
        }
    }
}