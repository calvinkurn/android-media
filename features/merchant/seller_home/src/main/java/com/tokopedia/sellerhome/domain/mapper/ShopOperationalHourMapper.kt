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

    fun mapTopShopOperationalHour(
        operationalHourResponse: ShopOperationalHourResponse,
        closedInfoResponse: ShopClosedInfoDetailResponse,
        shopSettingsAccess: Boolean
    ): ShopOperationalUiModel {
        val startTime = if(closedInfoResponse.isClosed()) {
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

        val endTime = if(closedInfoResponse.isClosed()) {
            DateFormatUtils.getFormattedDate(
                closedInfoResponse.openDate,
                SHOP_CLOSED_INFO_DATE_FORMAT
            )
        } else {
            DateFormatUtils.formatDate(
                OPERATIONAL_HOUR_RESPONSE_FORMAT,
                OPERATIONAL_HOUR_UI_FORMAT,
                operationalHourResponse.endTime
            )
        }

        val time = if(closedInfoResponse.isOpen()) {
            "$startTime - $endTime $OPERATIONAL_HOUR_TIMEZONE"
        } else {
            "$startTime - $endTime"
        }

        val timeLabel = when {
            operationalHourResponse.is24Hour() -> R.string.shop_operational_hour_24_hour
            !operationalHourResponse.statusActive -> R.string.shop_operational_hour_set_operational_time
            else -> null
        }

        val status = when {
            closedInfoResponse.isClosed() -> R.string.settings_operational_hour_not_set
            operationalHourResponse.statusActive -> R.string.settings_operational_hour_open
            else -> R.string.settings_operational_hour_closed
        }

        val labelType = when {
            closedInfoResponse.isClosed() -> Label.GENERAL_LIGHT_RED
            operationalHourResponse.statusActive -> Label.GENERAL_LIGHT_GREEN
            else -> Label.GENERAL_LIGHT_GREY
        }

        val icon = if(closedInfoResponse.isClosed()) {
            R.drawable.ic_sah_calendar
        } else {
            R.drawable.ic_sah_clock
        }

        return ShopOperationalUiModel(time, timeLabel, status, icon, labelType, shopSettingsAccess)
    }
}