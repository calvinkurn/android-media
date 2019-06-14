package com.tokopedia.gm.common.data.source.cloud.model

import com.tokopedia.user_identification_common.pojo.GetApprovalStatusPojo

data class PowerMerchantStatus(
        val shopStatusModel: ShopStatusModel = ShopStatusModel(),
        val getApprovalStatusPojo: GetApprovalStatusPojo = GetApprovalStatusPojo(),
        val shopScore: ShopScoreMainDomainModel = ShopScoreMainDomainModel()
)