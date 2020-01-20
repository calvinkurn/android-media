package com.tokopedia.gm.common.data.source.cloud.model
import com.tokopedia.user_identification_common.domain.pojo.GetApprovalStatusPojo

data class PowerMerchantStatus(
        val goldGetPmOsStatus: GoldGetPmOsStatus = GoldGetPmOsStatus(),
        val getApprovalStatusPojo: GetApprovalStatusPojo = GetApprovalStatusPojo(),
        val shopScore: ShopScoreResult = ShopScoreResult()
)