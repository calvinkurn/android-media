package com.tokopedia.play.ui.pinnedvoucher.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel


/**
 * Created by mzennis on 23/02/21.
 */
class PinnedVoucherPlaceholderAdapterDelegate : TypedAdapterDelegate<PlayVoucherUiModel.Placeholder, PlayVoucherUiModel, RecyclerView.ViewHolder>(R.layout.item_play_pinned_voucher_placeholder) {

    override fun onBindViewHolder(item: PlayVoucherUiModel.Placeholder, holder: RecyclerView.ViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): RecyclerView.ViewHolder {
        return BaseViewHolder(basicView)
    }
}