package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTitleUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowTitleBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowTitleViewHolder(
    itemView: View,
) : AbstractViewHolder<TokoNowTitleUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_title
    }

    private var binding: ItemTokopedianowTitleBinding? by viewBinding()

    override fun bind(element: TokoNowTitleUiModel) {
        binding?.apply {
            root.setModel(TokoNowDynamicHeaderUiModel(title = element.title))
        }
    }
}
