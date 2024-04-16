package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.LOADING
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.SHOW
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

    override fun bind(data: TokoNowTitleUiModel) {
        binding?.apply {
            when(data.state) {
                SHOW -> {
                    headerLayout.show()
                    headerLayout.setModel(TokoNowDynamicHeaderUiModel(title = data.title))
                    shimmerLayout.hide()
                }
                LOADING -> {
                    shimmerLayout.show()
                    headerLayout.hide()
                }
                else -> { /* nothing to do */ }
            }
        }
    }
}
