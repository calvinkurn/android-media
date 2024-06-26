package com.tokopedia.play.ui.promosheet.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.promosheet.adapter.delegate.MerchantVoucherAdapterDelegate
import com.tokopedia.play.ui.promosheet.adapter.delegate.VoucherInfoHeaderAdapterDelegate
import com.tokopedia.play.ui.promosheet.adapter.delegate.VoucherPlaceholderAdapterDelegate
import com.tokopedia.play.ui.promosheet.viewholder.MerchantVoucherNewViewHolder
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel

/**
 * Created by jegul on 03/03/20
 */
class MerchantVoucherAdapter(
        listener: MerchantVoucherNewViewHolder.Listener
) : BaseDiffUtilAdapter<PlayVoucherUiModel>() {

    init {
        delegatesManager
                .addDelegate(VoucherInfoHeaderAdapterDelegate())
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