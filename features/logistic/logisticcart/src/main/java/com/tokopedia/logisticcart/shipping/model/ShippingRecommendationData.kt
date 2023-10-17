package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.logisticcart.scheduledelivery.domain.model.ScheduleDeliveryData
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 11/10/18.
 */
@Parcelize
class ShippingRecommendationData(
    var shippingDurationUiModels: List<ShippingDurationUiModel> = emptyList(),
    var logisticPromo: LogisticPromoUiModel? = null,
    var listLogisticPromo: List<LogisticPromoUiModel> = emptyList(),
    var errorMessage: String? = null,
    var errorId: String? = null,
    var scheduleDeliveryData: ScheduleDeliveryData? = null,
    var productShipmentDetailModel: ProductShipmentDetailModel? = null
) : Parcelable
