package com.tokopedia.home_component.widget.balance

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.LayoutDynamicBalanceItemBinding
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceItemLoadingViewHolder(
    v: View,
): AbstractViewHolder<BalanceItemUiModel>(v) {
    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.layout_dynamic_balance_item
    }

    override fun bind(
        model: BalanceItemUiModel,
    ) {

    }
}
