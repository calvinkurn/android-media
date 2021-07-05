package com.tokopedia.play.ui.pinnedvoucher.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.ui.pinnedvoucher.viewholder.PinnedVoucherViewHolder
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel


/**
 * Created by mzennis on 23/02/21.
 */
class PinnedVoucherAdapterDelegate(
        listener: PinnedVoucherViewHolder.Listener
) : TypedAdapterDelegate<MerchantVoucherUiModel, PlayVoucherUiModel, PinnedVoucherViewHolder>(R.layout.item_play_pinned_voucher), PinnedVoucherViewHolder.Listener by listener {

    override fun onBindViewHolder(item: MerchantVoucherUiModel, holder: PinnedVoucherViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PinnedVoucherViewHolder {
        return PinnedVoucherViewHolder(basicView, this)
    }
}