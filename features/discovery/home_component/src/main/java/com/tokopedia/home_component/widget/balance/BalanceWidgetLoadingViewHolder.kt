package com.tokopedia.home_component.widget.balance

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.LayoutDynamicBalanceWidgetLoadingBinding
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R as home_componentR

/**
 * Created by frenzel
 */
class BalanceWidgetLoadingViewHolder (
    itemView: View,
) : AbstractViewHolder<BalanceWidgetLoadingUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.layout_dynamic_balance_widget_loading
    }

    override fun bind(element: BalanceWidgetLoadingUiModel) {

    }
}
