package com.tokopedia.vouchercreation.create.view.bottomsheet

import com.tokopedia.vouchercreation.create.view.customview.bottomsheet.CreatePromoCodeBottomSheetView

enum class CreateVoucherBottomSheetType(var key: Int,
                                        var tag: String) {
    CREATE_PROMO_CODE(0, CreateVoucherBottomSheetTags.CREATE_PROMO_CODE)
}

object CreateVoucherBottomSheetTags {
    var CREATE_PROMO_CODE = CreatePromoCodeBottomSheetView::javaClass.name
}