package com.tokopedia.play.ui.productsheet.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.ui.productsheet.viewholder.MerchantVoucherViewHolder
import com.tokopedia.play.view.type.ProductSheetContent
import com.tokopedia.play.view.type.ProductSheetVoucher

/**
 * Created by jegul on 03/03/20
 */
class MerchantVoucherAdapterDelegate : TypedAdapterDelegate<ProductSheetVoucher, ProductSheetContent, MerchantVoucherViewHolder>(R.layout.item_play_merchant_voucher_list) {

    override fun onBindViewHolder(item: ProductSheetVoucher, holder: MerchantVoucherViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): MerchantVoucherViewHolder {
        return MerchantVoucherViewHolder(basicView)
    }
}