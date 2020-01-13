package com.tokopedia.attachvoucher.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherTypeFactory
import com.tokopedia.merchantvoucher.common.gql.data.*

class Voucher(
        voucherId: Int,
        voucherName: String?,
        voucherCode: String?,
        merchantVoucherType: MerchantVoucherType?,
        merchantVoucherAmount: MerchantVoucherAmount?,
        minimumSpend: Int,
        merchantVoucherOwner: MerchantVoucherOwner,
        validThru: String,
        tnc: String?,
        merchantVoucherBanner: MerchantVoucherBanner?,
        merchantVoucherStatus: MerchantVoucherStatus?,
        restrictedForLiquidProduct: Boolean
) : MerchantVoucherModel(
        voucherId,
        voucherName,
        voucherCode,
        merchantVoucherType,
        merchantVoucherAmount,
        minimumSpend,
        merchantVoucherOwner,
        validThru,
        tnc,
        merchantVoucherBanner,
        merchantVoucherStatus,
        restrictedForLiquidProduct
), Visitable<AttachVoucherTypeFactory> {

    val availableAmount: String get() = merchantVoucherAmount?.amount?.toInt()?.toString() ?: ""

    override fun type(typeFactory: AttachVoucherTypeFactory): Int {
        return typeFactory.type(this)
    }

}