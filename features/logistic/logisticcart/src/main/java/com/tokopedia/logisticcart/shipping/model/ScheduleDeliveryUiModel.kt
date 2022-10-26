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
        if (scheduleDate != "" && timeslotId != 0L) {
            getSelectedDeliveryServices(scheduleDate, timeslotId, { recommendScheduleDate, recommendDeliveryProduct ->
                this.isSelected = true
                this.scheduleDate = recommendScheduleDate
                this.timeslotId = recommendDeliveryProduct.id
                this.deliveryProduct = recommendDeliveryProduct
            },
            {
                this.isSelected = false
            })
        } else {
            getSelectedDeliveryServicesRecommend { recommendScheduleDate, recommendDeliveryProduct ->
                this.scheduleDate = recommendScheduleDate
                this.timeslotId = recommendDeliveryProduct.id
                this.deliveryProduct = recommendDeliveryProduct
            }
        }
    }

    private fun getSelectedDeliveryServicesRecommend(
        callback: (scheduleDate: String, deliveryProduct: DeliveryProduct) -> Unit
    ) {
        deliveryServices.forEach { deliveryService ->
            val deliveryProduct = deliveryService.deliveryProducts.find { it.recommend }
            if (deliveryProduct != null) {
                callback(deliveryService.id, deliveryProduct)
                return
            }
        }
    }

    private fun getSelectedDeliveryServices(
        scheduleDate: String, timeslotId: Long,
        callback: (scheduleDate: String, deliveryProduct: DeliveryProduct) -> Unit,
        notFound: () -> Unit,
    ) {
        val deliveryService = deliveryServices.find { it.id == scheduleDate }
        val deliveryProduct = deliveryService?.deliveryProducts?.find { it.id == timeslotId }

        if (deliveryService != null && deliveryProduct != null) {
            callback(deliveryService.id, deliveryProduct)
        }
        else
            notFound()
    }
}
