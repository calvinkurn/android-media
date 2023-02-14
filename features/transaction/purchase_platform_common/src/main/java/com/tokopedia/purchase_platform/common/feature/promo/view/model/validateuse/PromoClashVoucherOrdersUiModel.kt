package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by fwidjaja on 2020-03-05.
 */
@Parcelize
data class PromoClashVoucherOrdersUiModel(
    var cartId: Int = -1,
    var code: String = "",
    var shopName: String = "",
    var potentialBenefit: Int = -1,
    var promoName: String = "",
    var uniqueId: String = ""
) : Parcelable
