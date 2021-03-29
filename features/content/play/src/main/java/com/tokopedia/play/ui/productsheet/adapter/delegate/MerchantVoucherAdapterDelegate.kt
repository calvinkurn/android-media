package com.tokopedia.play.ui.productsheet.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.ui.productsheet.viewholder.MerchantVoucherViewHolder
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel

/**
 * Created by jegul on 03/03/20
 */
class MerchantVoucherAdapterDelegate(
        listener: MerchantVoucherViewHolder.Listener
) : TypedAdapterDelegate<MerchantVoucherUiModel, PlayVoucherUiModel, MerchantVoucherViewHolder>(R.layout.item_play_merchant_voucher), MerchantVoucherViewHolder.Listener by listener {

    override fun onBindViewHolder(item: MerchantVoucherUiModel, holder: MerchantVoucherViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): MerchantVoucherViewHolder {
        return MerchantVoucherViewHolder(basicView, this)
    }
}