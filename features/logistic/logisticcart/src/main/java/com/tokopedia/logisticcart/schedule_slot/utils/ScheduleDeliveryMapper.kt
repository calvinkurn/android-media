package com.tokopedia.logisticcart.schedule_slot.utils

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.AdditionalDeliveryData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.DeliveryProduct
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.DeliveryService
import com.tokopedia.logisticcart.schedule_slot.uimodel.BottomSheetUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ButtonDateUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseDateUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseTimeUiModel

object ScheduleDeliveryMapper {

    fun mapResponseToUiModel(additionalDeliveryData: AdditionalDeliveryData) : BottomSheetUiModel {
        val buttonDateUiModel = generateDateUiModel(additionalDeliveryData.deliveryServices)
        return BottomSheetUiModel(
            date = ChooseDateUiModel(content = buttonDateUiModel),
            // todo available title & unavailable title & bottomsheet info
        )
    }

    private fun generateDateUiModel(deliveryServices: List<DeliveryService>): List<ButtonDateUiModel> {
        return deliveryServices.filter { !it.hidden }.map { response ->
            ButtonDateUiModel(
                title = response.titleLabel,
                // todo
                date = "",
                isEnabled = response.available,
                id = response.id,
                // todo this only support default value from BE,
                // need selectedDate from previous user selection
                isSelected = response.deliveryProducts.any { it.recommend },
                availableTime = generateTimeUiModel(response.deliveryProducts.filter { it.available }),
                unavailableTime = generateTimeUiModel(response.deliveryProducts.filter { !it.available })
            )
        }
    }

    private fun generateTimeUiModel(timeOptions: List<DeliveryProduct>): List<ChooseTimeUiModel> {
        return timeOptions.map { time ->
            ChooseTimeUiModel(
                title = generateTimeTitle(time),
                content = time.text,
                // todo this only support default value from BE,
                // need selectedTime from previous user selection
                isEnabled = time.recommend
            )
        }
    }

    private fun generateTimeTitle(time: DeliveryProduct): String {
        val timeTitle = time.title
        // check price
        if (time.finalPrice == time.realPrice) {
            return timeTitle + " <b>(${time.textFinalPrice})</b>"
        } else {
            return timeTitle + " <b>(${time.textFinalPrice}<s>${time.textRealPrice}</s>)</b>"
        }
    }
}
