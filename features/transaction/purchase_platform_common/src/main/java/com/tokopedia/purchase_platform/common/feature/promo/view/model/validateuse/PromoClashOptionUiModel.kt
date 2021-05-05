package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 2020-03-05.
 */
@Parcelize
data class PromoClashOptionUiModel (
        var voucherOrders: List<PromoClashVoucherOrdersUiModel?>? = listOf()
) : Parcelable