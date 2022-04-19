package com.tokopedia.shop.common.data.model

import com.tokopedia.mvcwidget.AnimatedInfos

data class MerchantVoucherCouponUiModel (
        var resultStatus: ResultStatus? = null,
        val animatedInfoList: List<AnimatedInfos?>?,
        var isShown: Boolean? = null,
        var shopId: String? = null
)


data class ResultStatus(
        var code: String?,
        var message: List<String?>?,
        var status: String?
)