package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 16/03/20.
 */
@Parcelize
data class LastApplyUsageSummariesUiModel (
        var description: String = "",
        var type: String = "",
        var amountStr: String = "",
        var amount: Int = -1): Parcelable