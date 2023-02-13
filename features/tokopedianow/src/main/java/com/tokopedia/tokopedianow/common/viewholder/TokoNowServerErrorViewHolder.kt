package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowServerErrorBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowServerErrorViewHolder(
    itemView: View,
    private val serverErrorListener: ServerErrorListener? = null,
    private val serverErrorAnalytics: ServerErrorAnalytics? = null
): AbstractViewHolder<TokoNowServerErrorUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_server_error
    }

    private var binding: ItemTokopedianowServerErrorBinding? by viewBinding()

    override fun bind(item: TokoNowServerErrorUiModel) {
        binding?.apply {
            serverErrorAnalytics?.trackImpressErrorPage()

            emptyStateFailedToFetchData.setActionClickListener {
                serverErrorListener?.onClickRetryButton()
                serverErrorAnalytics?.trackClickRetryPage()
            }
        }
    }

    interface ServerErrorListener {
        fun onClickRetryButton()
    }

    interface ServerErrorAnalytics {
        fun trackImpressErrorPage()
        fun trackClickRetryPage()
    }
}