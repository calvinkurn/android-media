package com.tokopedia.shop.flashsale.presentation.creation.information.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class VpsPackageUiModel(
    val remainingQuota: Int,
    val currentQuota: Int,
    val originalQuota: Int,
    val packageEndTime: Date,
    val packageId: Long,
    val packageName: String,
    val packageStartTime: Date,
    val isSelected: Boolean,
    val disabled: Boolean,
    val isShopTierBenefit: Boolean
) : Parcelable