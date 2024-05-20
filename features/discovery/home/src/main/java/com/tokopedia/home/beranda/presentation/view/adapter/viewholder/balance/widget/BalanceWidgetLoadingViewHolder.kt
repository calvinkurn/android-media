package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.databinding.LayoutDynamicBalanceWidgetLoadingBinding
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home.R as homeR

/**
 * Created by frenzel
 */
class BalanceWidgetLoadingViewHolder (
    itemView: View,
) : AbstractViewHolder<BalanceWidgetLoadingUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = homeR.layout.layout_dynamic_balance_widget_loading
    }

    override fun bind(element: BalanceWidgetLoadingUiModel) {

    }
}
