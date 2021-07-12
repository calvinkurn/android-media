package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.Voucher
import com.tokopedia.play.view.type.MerchantVoucherType
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import javax.inject.Inject

/**
 * Created by jegul on 01/02/21
 */
class PlayMerchantVoucherUiMapper @Inject constructor() {

    fun mapMerchantVoucher(input: Voucher): MerchantVoucherUiModel {
        return MerchantVoucherUiModel(
                id = input.id,
                title = input.title,
                description = input.subtitle,
                type = if (input.isPrivate) MerchantVoucherType.Private else when(input.voucherType) {
                    /**
                     * 1 -> Free Ongkir
                     * 2 -> **Not Used**
                     * 3 -> Cashback
                     */
                    1 -> MerchantVoucherType.Shipping
                    2,3 -> MerchantVoucherType.Discount
                    else -> MerchantVoucherType.Unknown
                },
                code = input.code,
                copyable = input.isCopyable,
                highlighted = input.isHighlighted
        )
    }
}