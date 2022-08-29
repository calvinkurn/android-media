package com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel

import java.util.*

data class CampaignManageProductBulkApplyBottomSheetResultUiModel(
    val startDate: Date? = null,
    val endDate: Date? = null,
    val discountType : DiscountType = DiscountType.RUPIAH,
    val discountAmount: Long  = 0,
    val stock: Int =  0
)