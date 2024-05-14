package com.tokopedia.home_component.widget.balance

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.LayoutDynamicBalanceItemErrorBinding
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceItemErrorViewHolder(
    v: View,
): AbstractViewHolder<BalanceItemErrorUiModel>(v) {
    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.layout_dynamic_balance_item_error
    }

    private val binding: LayoutDynamicBalanceItemErrorBinding? by viewBinding()

    override fun bind(
        model: BalanceItemErrorUiModel,
    ) {
        val binding = binding ?: return
        binding.textErrorBalance.text = "Coba Lagi"
    }
}
