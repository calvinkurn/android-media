package com.tokopedia.home_component.widget.balance

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.LayoutDynamicBalanceItemBinding
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceItemViewHolder(
    v: View,
): AbstractViewHolder<BalanceItemUiModel>(v) {
    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.layout_dynamic_balance_item
    }

    private val binding: LayoutDynamicBalanceItemBinding? by viewBinding()

    override fun bind(
        model: BalanceItemUiModel,
    ) {
        val binding = binding ?: return
        binding.textBalance.text = model.text
        binding.iconBalance.loadImage(model.imageUrl)
    }
}
