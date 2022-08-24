package com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize

data class CampaignManageProductBulkApplyBottomSheetConfigUiModel(
    val bottomSheetTitle: String = "",
    val isShowPeriodSection: Boolean = false,
    val isShowPeriodChips: Boolean = false,
    val defaultStartDate: Date? = null,
    val defaultEndDate: Date? = null,
    val isShowTextFieldProductDiscountBottomMessage: Boolean = false,
    val minimumDiscountPrice: Int = 0,
    val maximumDiscountPrice: Int = 0,
    val minimumDiscountPercentage: Int = 0,
    val maximumDiscountPercentage: Int = 0,
    val textStock: String = "",
    val textStockAdditionalInfo: String = "",
    val textStockDescription: String = "",
    val minimumStock: Int = 0,
    val maximumStock: Int = 0,
) : Parcelable