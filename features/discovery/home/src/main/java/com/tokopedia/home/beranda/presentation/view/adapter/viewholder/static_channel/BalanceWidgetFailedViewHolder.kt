package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceWidgetFailedModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController
import com.tokopedia.home.databinding.LayoutBalanceWidgetFailedBinding
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class BalanceWidgetFailedViewHolder (itemView: View, val listener: HomeCategoryListener?) :
    AbstractViewHolder<BalanceWidgetFailedModel>(itemView) {
    private var binding: LayoutBalanceWidgetFailedBinding? by viewBinding()

    companion object {
        val LAYOUT = R.layout.layout_balance_widget_failed
        private const val START_PROGRESS_STATE = true
        private val ERROR_STATE_MARGIN_CONTROL = (-10f).toDpInt()
        private val ERROR_STATE_MARGIN_ATF2 = 6f.toDpInt()
    }

    override fun bind(element: BalanceWidgetFailedModel) {
        if(HomeRollenceController.isUsingAtf2Variant()) {
            binding?.containerFailed?.setPadding(ERROR_STATE_MARGIN_ATF2, Int.ZERO, ERROR_STATE_MARGIN_ATF2, Int.ZERO)
        } else {
            binding?.containerFailed?.setPadding(ERROR_STATE_MARGIN_CONTROL, Int.ZERO, ERROR_STATE_MARGIN_CONTROL, Int.ZERO)
        }
        binding?.localloadErrorBalanceWidget?.refreshBtn?.setOnClickListener {
            binding?.localloadErrorBalanceWidget?.progressState = START_PROGRESS_STATE
            listener?.refreshBalanceWidget()
        }
    }
}
