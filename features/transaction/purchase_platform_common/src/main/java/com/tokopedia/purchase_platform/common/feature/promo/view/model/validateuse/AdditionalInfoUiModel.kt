package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdditionalInfoUiModel(
        var messageInfoUiModel: MessageInfoUiModel = MessageInfoUiModel(),
        var errorDetailUiModel: ErrorDetailUiModel = ErrorDetailUiModel(),
        var emptyCartInfoUiModel: EmptyCartInfoUiModel = EmptyCartInfoUiModel(),
        var usageSummariesUiModel: List<UsageSummariesUiModel> = emptyList(),
        var promoSpIds: List<PromoSpIdUiModel> = emptyList(),
        var pomlAutoApplied: Boolean = false,
) : Parcelable
