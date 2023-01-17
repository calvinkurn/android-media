package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.logisticcart.scheduledelivery.domain.model.DeliveryProduct
import com.tokopedia.logisticcart.scheduledelivery.domain.model.DeliveryService
import com.tokopedia.logisticcart.scheduledelivery.domain.model.Notice
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduleDeliveryUiModel(
    // radio button value
    var isSelected: Boolean,
    val available: Boolean = false,
    val hidden: Boolean = false,
    val title: String = "",
    val text: String = "",
    val notice: Notice = Notice(),
    val ratesId: Long = 0L,
    val deliveryServices: List<DeliveryService> = arrayListOf(),
    // service id
    var scheduleDate: String = "",
    // product id
    var timeslotId: Long = 0L,
    // selected delivery products
    var deliveryProduct: DeliveryProduct = DeliveryProduct(),
) : Parcelable {

    fun setScheduleDateAndTimeslotId(
        scheduleDate: String = "",
        timeslotId: Long = 0L,
        validationMetadata: String = ""
    ) {
        getSelectedDeliveryServices(
            scheduleDate,
            timeslotId,
            validationMetadata
        ) { selectedScheduleDate, selectedDeliveryProduct, isSelectedProduct ->
            this.isSelected = isSelectedProduct
            if (selectedScheduleDate != null && selectedDeliveryProduct != null) {
                this.scheduleDate = selectedScheduleDate
                this.timeslotId = selectedDeliveryProduct.timeslotId
                this.deliveryProduct = selectedDeliveryProduct
            }
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
        validationMetadata: String,
        callback: (selectedScheduleDate: String?, selectedDeliveryProduct: DeliveryProduct?, isSelectedProduct: Boolean) -> Unit
    ) {
        var deliveryService: DeliveryService? = null
        var deliveryProduct: DeliveryProduct? = null
        if (scheduleDate.isNotEmpty() && timeslotId != 0L) {
            deliveryService = deliveryServices.find { it.id == scheduleDate }
            deliveryProduct = deliveryService?.deliveryProducts?.find {
                it.timeslotId == timeslotId && it.available
            }
        }
        else if (validationMetadata.isNotEmpty()) {
            deliveryServices.forEach { service ->
                deliveryProduct = service.deliveryProducts.find { product ->
                    product.validationMetadata == validationMetadata
                }
                if (deliveryProduct != null) {
                    deliveryService = service
                    return@forEach
                }
            }
        }
        if (deliveryService != null && deliveryProduct != null) {
            callback(deliveryService?.id, deliveryProduct, true)
        } else {
            getSelectedDeliveryServicesRecommend(callback)
        }
    }
}
