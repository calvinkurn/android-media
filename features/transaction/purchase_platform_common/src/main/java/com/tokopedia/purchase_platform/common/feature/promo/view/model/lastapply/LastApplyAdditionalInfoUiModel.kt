package com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply

import android.os.Parcelable
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoSpIdUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LastApplyAdditionalInfoUiModel(
    var messageInfo: LastApplyMessageInfoUiModel = LastApplyMessageInfoUiModel(),
    var errorDetail: LastApplyErrorDetailUiModel = LastApplyErrorDetailUiModel(),
    var emptyCartInfo: LastApplyEmptyCartInfoUiModel = LastApplyEmptyCartInfoUiModel(),
    var usageSummaries: List<LastApplyUsageSummariesUiModel> = emptyList(),
    var promoSpIds: List<PromoSpIdUiModel> = emptyList(),
    var pomlAutoApplied: Boolean = false,
    var bebasOngkirInfo: LastApplyBebasOngkirInfoUiModel = LastApplyBebasOngkirInfoUiModel()
) : Parcelable
