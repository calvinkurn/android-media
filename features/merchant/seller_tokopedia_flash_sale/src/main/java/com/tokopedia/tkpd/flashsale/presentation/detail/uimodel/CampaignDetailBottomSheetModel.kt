package com.tokopedia.tkpd.flashsale.presentation.detail.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CampaignDetailBottomSheetModel(
    val timelineSteps: List<TimelineStepModel> = listOf(),
    val productCriterias: List<ProductCriteriaModel> = listOf(),
    val showTimeline: Boolean = false,
    val showCriteria: Boolean = false,
    val showProductCriteria: Boolean = false
) : Parcelable
