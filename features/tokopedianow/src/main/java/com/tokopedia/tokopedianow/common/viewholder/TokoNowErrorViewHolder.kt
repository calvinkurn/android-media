package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowErrorUiModel
import com.tokopedia.tokopedianow.common.util.GlobalErrorUtil.setupLayout
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowErrorBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowErrorViewHolder(
    itemView: View,
    private val listener: TokoNowErrorListener? = null
): AbstractViewHolder<TokoNowErrorUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_error
    }

    private var binding: ItemTokopedianowErrorBinding? by viewBinding()

    override fun bind(element: TokoNowErrorUiModel) {
        binding?.apply {
            globalError.layoutParams = if (element.isFullPage) {
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            } else {
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            globalError.setupLayout(element.throwable) {
                listener?.onClickRefresh()
            }
        }
    }

    interface TokoNowErrorListener {
        fun onClickRefresh()
    }
}
