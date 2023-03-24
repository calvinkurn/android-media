package com.tokopedia.logisticcart.scheduledelivery.domain.mapper

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.logisticCommon.util.StringFormatterHelper.appendHtmlBoldText
import com.tokopedia.logisticcart.scheduledelivery.domain.model.DeliveryProduct
import com.tokopedia.logisticcart.scheduledelivery.domain.model.DeliveryService
import com.tokopedia.logisticcart.scheduledelivery.domain.model.Notice
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.BottomSheetInfoUiModel
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.BottomSheetUiModel
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.ButtonDateUiModel
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.ChooseDateUiModel
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.ChooseTimeUiModel
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.TitleSectionUiModel

object ScheduleDeliveryBottomSheetMapper {

    private const val IMAGE_BOTTOMSHEET_INFO = "https://images.tokopedia.net/img/now/schedule-delivery/schedule-bottomsheet-info-icon.png"

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
                title = "Jadwal tersedia",
                content = selectedTimeSlot.promoText,
                icon = IconUnify.INFORMATION
            ),
            unavailableTitle = TitleSectionUiModel(
                title = "Jadwal tidak tersedia"
            ),
            infoUiModel = mapNoticeToScheduleInfoUiModel(notice)
        )
    }

    private fun mapNoticeToScheduleInfoUiModel(notice: Notice): BottomSheetInfoUiModel {
        return BottomSheetInfoUiModel(
            title = notice.title,
            description = notice.text,
            imageUrl = IMAGE_BOTTOMSHEET_INFO
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
                title = service.title,
                date = service.titleLabel,
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
        dateId: String,
        selectedTimeSlot: DeliveryProduct,
        isDateSelected: Boolean
    ): List<ChooseTimeUiModel> {
        return timeOptions.filter { !it.hidden }.map { time ->
            ChooseTimeUiModel(
                content = time.generateTimeTitle(),
                note = time.text,
                isEnabled = time.available,
                isSelected = time.available && (isDateSelected && time.timeslotId == selectedTimeSlot.timeslotId),
                timeId = time.timeslotId,
                dateId = dateId
            )
        }
    }

    private fun DeliveryProduct.generateTimeTitle(): String {
        return StringBuilder().apply {
            appendHtmlBoldText(title)
            if (available) {
                append(getFormattedPrice())
            }
        }.toString()
    }
}
