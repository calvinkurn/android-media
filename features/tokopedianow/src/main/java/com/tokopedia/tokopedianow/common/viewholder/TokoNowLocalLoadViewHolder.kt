package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowLocalLoadUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowLocalLoadBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowLocalLoadViewHolder(
    itemView: View,
    private val listener: TokoNowLocalLoadListener? = null
): AbstractViewHolder<TokoNowLocalLoadUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_local_load
    }

    private val binding: ItemTokopedianowLocalLoadBinding? by viewBinding()

    override fun bind(element: TokoNowLocalLoadUiModel?) {
        binding?.apply {
            root.setOnClickListener {
                root.progressState = false
                root.progressState = true
                listener?.onClickRetry()
            }
        }
    }

    interface TokoNowLocalLoadListener {
        fun onClickRetry()
    }
}
