package com.tokopedia.shop.flashsale.presentation.creation.information.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class VpsPackageUiModel(
    val currentQuota: Int,
    val isDisabled: Boolean ,
    val originalQuota: Int,
    val packageEndTime: Date ,
    val packageId: Long,
    val packageName: String,
    val packageStartTime: Date,
    val isSelected: Boolean,
    val disabled: Boolean,
    val isShopTierBenefit: Boolean,
    val formattedPackageStartTime: String,
    val formattedPackageEndTime: String
) : Parcelable