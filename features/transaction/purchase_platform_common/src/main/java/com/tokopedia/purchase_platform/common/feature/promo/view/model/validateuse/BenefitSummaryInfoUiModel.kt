package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BenefitSummaryInfoUiModel(
    var finalBenefitAmountStr: String = "",
    var finalBenefitAmount: Int = 0,
    var finalBenefitText: String = "",
    var summaries: List<SummariesItemUiModel> = listOf()
) : Parcelable
