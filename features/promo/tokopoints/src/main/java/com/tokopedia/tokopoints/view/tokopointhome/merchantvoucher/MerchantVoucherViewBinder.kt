package com.tokopedia.tokopoints.view.tokopointhome.merchantvoucher

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder

class MerchantVoucherViewBinder()
    : SectionItemViewBinder<SectionContent, MerchantVoucherViewholder>(
        SectionContent::class.java) {

    override fun createViewHolder(parent: ViewGroup): MerchantVoucherViewholder {
        return MerchantVoucherViewholder(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: MerchantVoucherViewholder) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() : Int= R.layout.tp_layout_merchant_voucher

}
