package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecomCarouselBinding
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowServerErrorBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowServerErrorViewHolder(
    itemView: View,
    private val listener: ServerErrorListener? = null
): AbstractViewHolder<TokoNowServerErrorUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_server_error
    }

    private var binding: ItemTokopedianowServerErrorBinding? by viewBinding()

    override fun bind(item: TokoNowServerErrorUiModel) {
        binding?.emptyStateFailedToFetchData?.setActionClickListener {
            listener?.onClickRetryButton()
        }
    }

    interface ServerErrorListener {
        fun onClickRetryButton()
    }
}