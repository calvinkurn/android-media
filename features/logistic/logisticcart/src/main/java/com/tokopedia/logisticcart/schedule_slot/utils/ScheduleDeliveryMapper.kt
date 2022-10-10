package com.tokopedia.logisticcart.schedule_slot.utils

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.AdditionalDeliveryData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.DeliveryProduct
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.DeliveryService
import com.tokopedia.logisticcart.schedule_slot.uimodel.BottomSheetUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ButtonDateUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseDateUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseTimeUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.TitleSectionUiModel

object ScheduleDeliveryMapper {

    fun mapResponseToUiModel(additionalDeliveryData: AdditionalDeliveryData): BottomSheetUiModel {
        val buttonDateUiModel = generateDateUiModel(additionalDeliveryData.deliveryServices)
        return BottomSheetUiModel(
            date = ChooseDateUiModel(content = buttonDateUiModel),
            availableTitle = TitleSectionUiModel(
                title = "Jadwal Tersedia",
                // todo add error message here
                content = "",
                icon = IconUnify.INFORMATION,
                // todo
                onClick = {}
            ),
            unavailableTitle = TitleSectionUiModel(
                title = "Jadwal habis atau tidak tersedia",
            )
        )
    }

    private fun generateDateUiModel(deliveryServices: List<DeliveryService>): List<ButtonDateUiModel> {
        val scheduleSelectedByUser =
            deliveryServices.find { it.deliveryProducts.any { time -> time.isSelected } }
        val dateUiModel = mutableListOf<ButtonDateUiModel>()
        deliveryServices.forEach { service ->
            val isDateSelected = if (scheduleSelectedByUser == null) {
                service.deliveryProducts.any { it.recommend }
            } else {
                scheduleSelectedByUser.id == service.id
            }
            val buttonDateUiModel = ButtonDateUiModel(
                title = service.titleLabel,
                // todo
                date = service.title,
                isEnabled = service.available,
                id = service.id,
                isSelected = isDateSelected,
                availableTime = generateTimeUiModel(service.deliveryProducts.filter { it.available }),
                unavailableTime = generateTimeUiModel(service.deliveryProducts.filter { !it.available })
            )
            dateUiModel.add(buttonDateUiModel)
        }
        return dateUiModel
    }

    private fun generateTimeUiModel(timeOptions: List<DeliveryProduct>): List<ChooseTimeUiModel> {
        return timeOptions.map { time ->
            ChooseTimeUiModel(
                title = generateTimeTitle(time),
                content = time.text,
                // todo this only support default value from BE,
                // need selectedTime from previous user selection
                isEnabled = time.recommend,
                // todo
                isSelected = time.isSelected
            )
        }
    }

    private fun generateTimeTitle(time: DeliveryProduct): String {
        val timeTitle = time.title
        // check price
        return if (time.finalPrice == time.realPrice) {
            timeTitle + " <b>(${time.textFinalPrice})</b>"
        } else {
            timeTitle + " <b>(${time.textFinalPrice}<s>${time.textRealPrice}</s>)</b>"
        }
    }
}
