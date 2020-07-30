package com.tokopedia.attachvoucher.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherTypeFactory
import com.tokopedia.merchantvoucher.common.gql.data.*

class EmptyVoucherUiModel(
        voucherId: Int = 0,
        voucherName: String = "",
        voucherCode: String = "",
        merchantVoucherType: MerchantVoucherType = MerchantVoucherType(),
        merchantVoucherAmount: MerchantVoucherAmount = MerchantVoucherAmount(),
        minimumSpend: Int = 0,
        merchantVoucherOwner: MerchantVoucherOwner = MerchantVoucherOwner(),
        validThru: String = "",
        tnc: String = "",
        merchantVoucherBanner: MerchantVoucherBanner = MerchantVoucherBanner(),
        merchantVoucherStatus: MerchantVoucherStatus = MerchantVoucherStatus(),
        restrictedForLiquidProduct: Boolean = false
) : VoucherUiModel(
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

    override fun type(typeFactory: AttachVoucherTypeFactory): Int {
        return typeFactory.type(this)
    }

}