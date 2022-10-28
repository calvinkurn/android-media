package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.DeliveryProduct
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.DeliveryService
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.Notice
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduleDeliveryUiModel(
    // radio button value
    var isSelected: Boolean,
    val available: Boolean = false,
    val hidden: Boolean = false,
    val title: String = "",
    val text: String = "",
    val notice: Notice? = null,
    val deliveryServices: List<DeliveryService> = arrayListOf(),
    // service id
    var scheduleDate: String = "",
    // product id
    var timeslotId: Long = 0L,
    // selected delivery products
    var deliveryProduct: DeliveryProduct? = null,
) : Parcelable {

    fun setScheduleDateAndTimeslotId(
        scheduleDate: String,
        timeslotId: Long
    ) {
        getSelectedDeliveryServices(
            scheduleDate,
            timeslotId
        ) { selectedScheduleDate, selectedDeliveryProduct, isSelectedProduct ->
            this.isSelected = isSelectedProduct
            this.scheduleDate = selectedScheduleDate
            this.timeslotId = selectedDeliveryProduct.id
            this.deliveryProduct = selectedDeliveryProduct
        }
    }

    private fun getSelectedDeliveryServicesRecommend(
        callback: (scheduleDate: String, deliveryProduct: DeliveryProduct, isSelectedProduct: Boolean) -> Unit
    ) {
        deliveryServices.forEach { deliveryService ->
            val deliveryProduct = deliveryService.deliveryProducts.find { it.recommend }
            if (deliveryProduct != null) {
                callback(deliveryService.id, deliveryProduct, isSelected)
                return
            }
        }
    }

    private fun getSelectedDeliveryServices(
        scheduleDate: String,
        timeslotId: Long,
        callback: (selectedScheduleDate: String, selectedDeliveryProduct: DeliveryProduct, isSelectedProduct: Boolean) -> Unit
    ) {
        if (scheduleDate != null && timeslotId != null) {
            val deliveryService = deliveryServices.find { it.id == scheduleDate }
            val deliveryProduct = deliveryService?.deliveryProducts?.find {
                it.id == timeslotId && it.available
            }

            if (deliveryService != null && deliveryProduct != null) {
                callback(deliveryService.id, deliveryProduct, true)
            } else {
                getSelectedDeliveryServicesRecommend(callback)
            }
        } else {
            getSelectedDeliveryServicesRecommend(callback)
        }
    }
}
