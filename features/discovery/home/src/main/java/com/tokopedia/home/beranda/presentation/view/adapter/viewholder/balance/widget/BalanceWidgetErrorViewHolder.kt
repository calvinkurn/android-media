package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.databinding.LayoutDynamicBalanceWidgetErrorBinding
import com.tokopedia.home.R as homeR
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceWidgetErrorViewHolder (
    itemView: View,
    private val listener: HomeCategoryListener
) :
    AbstractViewHolder<BalanceWidgetErrorUiModel>(itemView) {
    private var binding: LayoutDynamicBalanceWidgetErrorBinding? by viewBinding()

    companion object {
        val LAYOUT = homeR.layout.layout_dynamic_balance_widget_error
        private const val START_PROGRESS_STATE = true
    }

    override fun bind(element: BalanceWidgetErrorUiModel) {
        binding?.errorBalanceWidget?.refreshBtn?.setOnClickListener {
            binding?.errorBalanceWidget?.progressState = START_PROGRESS_STATE
            listener.refreshBalanceWidget()
        }
    }
}
