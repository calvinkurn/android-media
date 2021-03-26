package com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply

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
        var amount: Int = -1,
        var currencyDetailsStr: String = ""): Parcelable