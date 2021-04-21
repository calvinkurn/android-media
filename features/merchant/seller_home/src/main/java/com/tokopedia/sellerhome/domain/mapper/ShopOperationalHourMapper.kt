package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.domain.model.ShopClosedInfoDetailResponse
import com.tokopedia.sellerhome.domain.model.ShopOperationalHourResponse
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.ShopOperationalUiModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.utils.time.DateFormatUtils

object ShopOperationalHourMapper {

    private const val OPERATIONAL_HOUR_RESPONSE_FORMAT = "HH:mm:ss"
    private const val OPERATIONAL_HOUR_UI_FORMAT = "HH:mm"
    private const val OPERATIONAL_HOUR_TIMEZONE = "WIB"
    private const val SHOP_CLOSED_INFO_DATE_FORMAT = "dd MMM"

    fun mapTopShopOperational(
        operationalHourResponse: ShopOperationalHourResponse,
        closedInfoResponse: ShopClosedInfoDetailResponse,
        shopSettingsAccess: Boolean
    ): ShopOperationalUiModel {
        val isShopOpen = closedInfoResponse.isOpen()
        val isShopClosed = closedInfoResponse.isClosed()
        val isShopActive = operationalHourResponse.statusActive
        val is24Hour = operationalHourResponse.is24Hour()

        val startTime = if(isShopClosed) {
            DateFormatUtils.getFormattedDate(
                closedInfoResponse.startDate,
                SHOP_CLOSED_INFO_DATE_FORMAT
            )
        } else {
            DateFormatUtils.formatDate(
                OPERATIONAL_HOUR_RESPONSE_FORMAT,
                OPERATIONAL_HOUR_UI_FORMAT,
                operationalHourResponse.startTime
            )
        }

        val endTime = if(isShopClosed) {
            DateFormatUtils.getFormattedDate(
                closedInfoResponse.endDate,
                SHOP_CLOSED_INFO_DATE_FORMAT
            )
        } else {
            DateFormatUtils.formatDate(
                OPERATIONAL_HOUR_RESPONSE_FORMAT,
                OPERATIONAL_HOUR_UI_FORMAT,
                operationalHourResponse.endTime
            )
        }

        val time = if(isShopOpen) {
            "$startTime - $endTime $OPERATIONAL_HOUR_TIMEZONE"
        } else {
            "$startTime - $endTime"
        }

        val timeLabel = when {
            is24Hour && isShopOpen -> R.string.shop_operational_hour_24_hour
            !isShopActive -> R.string.shop_operational_hour_set_operational_time
            else -> null
        }

        val status = when {
            isShopClosed -> R.string.settings_operational_hour_not_set
            isShopActive -> R.string.settings_operational_hour_open
            else -> R.string.settings_operational_hour_closed
        }

        val labelType = when {
            isShopClosed -> Label.GENERAL_LIGHT_RED
            isShopActive -> Label.GENERAL_LIGHT_GREEN
            else -> Label.GENERAL_LIGHT_GREY
        }

        val icon = if(isShopClosed) {
            R.drawable.ic_sah_calendar
        } else {
            R.drawable.ic_sah_clock
        }

        return ShopOperationalUiModel(time, timeLabel, status, icon, labelType, shopSettingsAccess)
    }
}