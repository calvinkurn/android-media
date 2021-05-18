package com.tokopedia.shop.common.data.model

import com.tokopedia.mvcwidget.AnimatedInfos

data class MerchantVoucherCouponUiModel (
        var resultStatus: ResultStatus? = null,
        var titles: List<Titles?>? = null,
        var isShown: Boolean? = null,
        var subTitle: String? = null,
        var imageURL: String? = null,
        var shopId: String? = null,
        var animatedInfos: List<AnimatedInfos>?= null
)

data class Titles(
        var text: String?,
        var icon: String?
)

data class ResultStatus(
        var code: String?,
        var message: List<String?>?,
        var status: String?
)