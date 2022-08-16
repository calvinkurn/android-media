package com.tokopedia.tkpd.flashsale.presentation.detail.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CampaignDetailBottomSheetModel(
    val timelineSteps: List<TimelineStepModel> = listOf(),
    val productCriterias: List<ProductCriteriaModel> = listOf(),
    var showTimeline: Boolean = false,
    var showCriteria: Boolean = false,
    var showProductCriteria: Boolean = false
) : Parcelable
