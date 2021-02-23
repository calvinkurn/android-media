package com.tokopedia.play.ui.pinned.voucher.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.pinned.voucher.adapter.delegate.PinnedVoucherAdapterDelegate
import com.tokopedia.play.ui.pinned.voucher.adapter.delegate.PinnedVoucherPlaceholderAdapterDelegate
import com.tokopedia.play.ui.pinned.voucher.viewholder.PinnedVoucherViewHolder
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel


/**
 * Created by mzennis on 23/02/21.
 */
class PinnedVoucherAdapter(
        listener: PinnedVoucherViewHolder.Listener
) : BaseDiffUtilAdapter<PlayVoucherUiModel>() {

    init {
        delegatesManager
                .addDelegate(PinnedVoucherAdapterDelegate(listener))
                .addDelegate(PinnedVoucherPlaceholderAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: PlayVoucherUiModel, newItem: PlayVoucherUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayVoucherUiModel, newItem: PlayVoucherUiModel): Boolean {
        return oldItem == newItem
    }
}