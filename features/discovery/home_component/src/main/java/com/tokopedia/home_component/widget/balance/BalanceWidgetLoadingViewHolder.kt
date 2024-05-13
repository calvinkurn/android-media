package com.tokopedia.home_component.widget.balance

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.LayoutBalanceWidgetLoadingBinding
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R as home_componentR

/**
 * Created by frenzel
 */
class BalanceWidgetLoadingViewHolder (
    itemView: View,
) : AbstractViewHolder<BalanceWidgetLoadingUiModel>(itemView) {

    private val binding: LayoutBalanceWidgetLoadingBinding? by viewBinding()

    companion object {
        val LAYOUT = home_componentR.layout.layout_balance_widget_loading
    }

    override fun bind(element: BalanceWidgetLoadingUiModel) {

    }
}
