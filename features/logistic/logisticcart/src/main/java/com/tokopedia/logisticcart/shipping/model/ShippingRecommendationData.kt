package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 11/10/18.
 */
@Parcelize
class ShippingRecommendationData(): Parcelable {
    var shippingDurationViewModels: List<ShippingDurationUiModel> = emptyList()
    var logisticPromo: LogisticPromoUiModel? = null
    var preOrderModel: PreOrderModel? = null
    var errorMessage: String? = null
    var errorId: String? = null
}