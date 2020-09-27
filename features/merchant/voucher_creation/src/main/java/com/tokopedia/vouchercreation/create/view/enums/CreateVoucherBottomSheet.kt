package com.tokopedia.vouchercreation.create.view.enums

import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.CreatePromoCodeBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.VoucherDisplayBottomSheetFragment

enum class CreateVoucherBottomSheetType(var key: Int,
                                        var tag: String) {
    CREATE_PROMO_CODE(0, CreatePromoCodeBottomSheetFragment.TAG),
    VOUCHER_DISPLAY(1, VoucherDisplayBottomSheetFragment.TAG)
}