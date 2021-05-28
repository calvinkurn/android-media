package com.tokopedia.shop.common.data.model

import com.tokopedia.mvcwidget.AnimatedInfos

data class MerchantVoucherCouponUiModel (
        var resultStatus: ResultStatus? = null,
        var isShown: Boolean? = null,
        var shopId: String? = null,
        var animatedInfos: List<AnimatedInfos>?= null
)

data class ResultStatus(
        var code: String?,
        var message: List<String?>?,
        var status: String?
)