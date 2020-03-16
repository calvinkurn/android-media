package com.tokopedia.play.ui.productsheet.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.VoucherPlaceholderUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel

/**
 * Created by jegul on 13/03/20
 */
class VoucherPlaceholderAdapterDelegate : TypedAdapterDelegate<VoucherPlaceholderUiModel, PlayVoucherUiModel, RecyclerView.ViewHolder>(R.layout.item_play_voucher_placeholder) {

    override fun onBindViewHolder(item: VoucherPlaceholderUiModel, holder: RecyclerView.ViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): RecyclerView.ViewHolder {
        return BaseViewHolder(basicView)
    }
}