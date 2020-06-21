package com.tokopedia.vouchercreation.create.view.enums

import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.CreatePromoCodeBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.VoucherDisplayBottomSheetFragment

enum class CreateVoucherBottomSheetType(var key: Int,
                                        var tag: String) {
    CREATE_PROMO_CODE(0, CreateVoucherBottomSheetTags.CREATE_PROMO_CODE),
    VOUCHER_DISPLAY(1, CreateVoucherBottomSheetTags.VOUCHER_DISPLAY)
}

object CreateVoucherBottomSheetTags {
    val CREATE_PROMO_CODE = CreatePromoCodeBottomSheetFragment::javaClass.name
    val VOUCHER_DISPLAY = VoucherDisplayBottomSheetFragment::javaClass.name
}