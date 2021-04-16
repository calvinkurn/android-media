package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.domain.model.ShopOperationalHourResponse
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.ShopOperationalHourUiModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.utils.time.DateFormatUtils

object ShopOperationalHourMapper {

    private const val OPERATIONAL_HOUR_RESPONSE_FORMAT = "HH:mm:ss"
    private const val OPERATIONAL_HOUR_UI_FORMAT = "HH:mm"

    fun mapTopShopOperationalHour(response: ShopOperationalHourResponse): ShopOperationalHourUiModel {
        val status = if(response.statusActive) {
            R.string.settings_operational_hour_open
        } else {
            R.string.settings_operational_hour_closed
        }

        val labelType = if(response.statusActive) {
            Label.GENERAL_LIGHT_GREEN
        } else {
            Label.GENERAL_LIGHT_GREY
        }

        val startTime = DateFormatUtils.formatDate(
            OPERATIONAL_HOUR_RESPONSE_FORMAT,
            OPERATIONAL_HOUR_UI_FORMAT,
            response.startTime
        )

        val endTime = DateFormatUtils.formatDate(
            OPERATIONAL_HOUR_RESPONSE_FORMAT,
            OPERATIONAL_HOUR_UI_FORMAT,
            response.endTime
        )

        return ShopOperationalHourUiModel(status, labelType, startTime, endTime)
    }
}