package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.databinding.LayoutDynamicBalanceItemErrorBinding
import com.tokopedia.home.R as homeR
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceItemErrorViewHolder(
    v: View,
    private val listener: HomeCategoryListener
): AbstractViewHolder<BalanceItemErrorUiModel>(v) {
    companion object {
        @LayoutRes
        val LAYOUT = homeR.layout.layout_dynamic_balance_item_error
    }

    private val binding: LayoutDynamicBalanceItemErrorBinding? by viewBinding()

    override fun bind(
        model: BalanceItemErrorUiModel,
    ) {
        val binding = binding ?: return

        binding.containerBalance.setOnClickListener {
            listener.refreshBalanceWidget(model.contentType)
        }
    }
}
