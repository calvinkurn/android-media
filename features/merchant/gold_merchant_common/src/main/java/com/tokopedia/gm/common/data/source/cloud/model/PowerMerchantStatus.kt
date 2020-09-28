package com.tokopedia.gm.common.data.source.cloud.model

import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo

data class PowerMerchantStatus(
        val goldGetPmOsStatus: GoldGetPmOsStatus = GoldGetPmOsStatus(),
        val kycUserProjectInfoPojo: KycUserProjectInfoPojo = KycUserProjectInfoPojo(),
        val shopScore: ShopScoreResult = ShopScoreResult(),
        val freeShippingEnabled: Boolean = false
)