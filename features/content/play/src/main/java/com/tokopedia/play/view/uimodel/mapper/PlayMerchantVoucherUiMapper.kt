package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.Voucher
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.type.MerchantVoucherType
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import javax.inject.Inject

/**
 * Created by jegul on 01/02/21
 */
@PlayScope
class PlayMerchantVoucherUiMapper @Inject constructor() {

    fun mapMerchantVoucher(input: Voucher): PlayVoucherUiModel.Merchant {
        return PlayVoucherUiModel.Merchant(
            id = input.id,
            title = input.title,
            description = input.subtitle,
            type = when (input.voucherType) {
                /**
                 * 1 -> Free Ongkir
                 * 2 -> **Not Used**
                 * 3 -> Cashback
                 */
                1 -> MerchantVoucherType.Shipping
                2, 3 -> MerchantVoucherType.Discount
                else -> MerchantVoucherType.Unknown
            },
            isPrivate = input.isPrivate,
            code = input.code,
            copyable = input.isCopyable,
            highlighted = input.isHighlighted,
            expiredDate = input.finishTime,
            voucherStock = input.quota
        )
    }
}
