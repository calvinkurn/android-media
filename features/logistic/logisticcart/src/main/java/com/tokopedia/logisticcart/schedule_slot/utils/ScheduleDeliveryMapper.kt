package com.tokopedia.logisticcart.schedule_slot.utils

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.DeliveryProduct
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.DeliveryService
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.Notice
import com.tokopedia.logisticcart.schedule_slot.uimodel.BottomSheetInfoUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.BottomSheetUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ButtonDateUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseDateUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseTimeUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.TitleSectionUiModel

object ScheduleDeliveryMapper {

    fun mapResponseToUiModel(
        deliveryServices: List<DeliveryService>,
        selectedDateId: String,
        selectedTimeSlot: DeliveryProduct,
        notice: Notice
    ): BottomSheetUiModel {
        val buttonDateUiModel = generateDateUiModel(
            deliveryServices,
            selectedTimeSlot,
            selectedDateId
        )
        return BottomSheetUiModel(
            date = ChooseDateUiModel(content = buttonDateUiModel),
            availableTitle = TitleSectionUiModel(
                title = "Jadwal Tersedia",
                // todo add error message here
                content = "",
                icon = IconUnify.INFORMATION,
            ),
            unavailableTitle = TitleSectionUiModel(
                title = "Jadwal habis atau tidak tersedia",
            ),
            infoUiModel = mapNoticeToScheduleInfoUiModel(notice)
        )
    }

    private fun mapNoticeToScheduleInfoUiModel(notice: Notice): BottomSheetInfoUiModel {
        return BottomSheetInfoUiModel(
            title = notice.title,
            description = notice.text,
            // todo
            imageUrl = ""
        )
    }

    private fun generateDateUiModel(
        deliveryServices: List<DeliveryService>,
        selectedTimeSlot: DeliveryProduct,
        selectedDateId: String
    ): List<ButtonDateUiModel> {
        val dateUiModel = mutableListOf<ButtonDateUiModel>()
        deliveryServices.filter { !it.hidden }.forEach { service ->
            val buttonDateUiModel = ButtonDateUiModel(
                title = service.titleLabel,
                // todo
                date = service.title,
                isEnabled = service.available,
                id = service.id,
                isSelected = selectedDateId == service.id,
                availableTime = generateTimeUiModel(
                    service.deliveryProducts.filter { it.available },
                    service.id,
                    selectedTimeSlot,
                    selectedDateId == service.id
                ),
                unavailableTime = generateTimeUiModel(
                    service.deliveryProducts.filter { !it.available },
                    service.id,
                    selectedTimeSlot,
                    selectedDateId == service.id
                )
            )
            dateUiModel.add(buttonDateUiModel)
        }
        return dateUiModel
    }

    private fun generateTimeUiModel(
        timeOptions: List<DeliveryProduct>,
        dayId: String,
        selectedTimeSlot: DeliveryProduct,
        isDateSelected: Boolean
    ): List<ChooseTimeUiModel> {
        return timeOptions.filter { !it.hidden }.map { time ->
            ChooseTimeUiModel(
                content = generateTimeTitle(time),
                note = time.text,
                isEnabled = time.available,
                isSelected = isDateSelected && time.id == selectedTimeSlot.id,
                timeId = time.id,
                dateId = dayId
            )
        }
    }

    private fun generateTimeTitle(time: DeliveryProduct): String {
        val timeTitle = time.title
        // check price
        return if (time.finalPrice == time.realPrice) {
            "<b>" + timeTitle + " (${time.textFinalPrice})</b>"
        } else {
            "<b>" + timeTitle + " (${time.textFinalPrice}</b> <s>${time.textRealPrice}</s><b>)</b>"
        }
    }
}
