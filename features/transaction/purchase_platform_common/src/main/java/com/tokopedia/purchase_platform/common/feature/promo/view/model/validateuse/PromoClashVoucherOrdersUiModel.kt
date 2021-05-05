package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 2020-03-05.
 */
@Parcelize
data class PromoClashVoucherOrdersUiModel(
        var code: String? = "",
        var shopName: String? = "",
        var potentialBenefit: Int? = -1,
        var promoName: String? = "",
        var uniqueId: String? = ""
) : Parcelable