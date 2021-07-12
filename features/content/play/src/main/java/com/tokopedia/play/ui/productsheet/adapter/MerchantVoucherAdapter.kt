package com.tokopedia.play.ui.productsheet.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.productsheet.adapter.delegate.MerchantVoucherAdapterDelegate
import com.tokopedia.play.ui.productsheet.adapter.delegate.VoucherPlaceholderAdapterDelegate
import com.tokopedia.play.ui.productsheet.viewholder.MerchantVoucherViewHolder
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel

/**
 * Created by jegul on 03/03/20
 */
class MerchantVoucherAdapter(
        listener: MerchantVoucherViewHolder.Listener
) : BaseDiffUtilAdapter<PlayVoucherUiModel>() {

    init {
        delegatesManager
                .addDelegate(MerchantVoucherAdapterDelegate(listener))
                .addDelegate(VoucherPlaceholderAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: PlayVoucherUiModel, newItem: PlayVoucherUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayVoucherUiModel, newItem: PlayVoucherUiModel): Boolean {
        return oldItem == newItem
    }
}