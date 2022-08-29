package com.tokopedia.vouchercreation.shop.create.view.uimodel.voucherreview

import com.tokopedia.vouchercreation.shop.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherImageType

data class VoucherReviewUiModel (
        @VoucherTargetType var targetType: Int = VoucherTargetType.PUBLIC,
        var voucherType: VoucherImageType = VoucherImageType.FreeDelivery(0),
        var shopName: String = "",
        var shopAvatarUrl: String = "",
        var voucherName: String = "",
        var promoCode: String = "",
        var voucherQuota: Int = 0,
        var minPurchase: Int = 0,
        var startDate: String = "",
        var endDate: String = "",
        var startHour: String = "",
        var endHour: String = ""
)