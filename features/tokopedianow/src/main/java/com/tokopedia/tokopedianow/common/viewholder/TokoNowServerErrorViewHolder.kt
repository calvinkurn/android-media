package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel

class TokoNowServerErrorViewHolder(
    itemView: View,
    private val listener: ServerErrorListener
): AbstractViewHolder<TokoNowServerErrorUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_server_error
    }

    private var emptyStateFailedToFetchData: GlobalError? = null

    init {
        emptyStateFailedToFetchData = itemView.findViewById(R.id.empty_state_failed_to_fetch_data)
    }

    override fun bind(item: TokoNowServerErrorUiModel) {
        emptyStateFailedToFetchData?.setActionClickListener {
            listener.onClickRetryButton()
        }
    }

    interface ServerErrorListener {
        fun onClickRetryButton()
    }
}