package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceWidgetFailedModel
import com.tokopedia.home.databinding.LayoutBalanceWidgetAtf2FailedBinding
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceWidgetAtf2FailedViewHolder (itemView: View, val listener: HomeCategoryListener?) :
    AbstractViewHolder<BalanceWidgetFailedModel>(itemView) {
    private var binding: LayoutBalanceWidgetAtf2FailedBinding? by viewBinding()

    companion object {
        val LAYOUT = R.layout.layout_balance_widget_atf2_failed
        private const val START_PROGRESS_STATE = true
    }

    override fun bind(element: BalanceWidgetFailedModel) {
        binding?.localloadErrorBalanceWidget?.refreshBtn?.setOnClickListener {
            binding?.localloadErrorBalanceWidget?.progressState = START_PROGRESS_STATE
            listener?.refreshBalanceWidget()
        }
    }
}
