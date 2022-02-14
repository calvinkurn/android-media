package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by fwidjaja on 2020-03-05.
 */
@Parcelize
data class PromoCashbackDetailsUiModel (
        var amountId: Float = 0.0F,
        var amountPoints: Float = 0.0F,
        var benefitType: String = ""
) : Parcelable