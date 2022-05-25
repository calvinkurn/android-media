package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.domain.model.ShopClosedInfoDetailResponse
import com.tokopedia.sellerhome.domain.model.ShopOperationalHourResponse
import com.tokopedia.sellerhome.domain.model.ShopStatusInfoResponse
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.ShopOperationalData
import com.tokopedia.shop.common.util.OperationalHoursUtil
import com.tokopedia.utils.time.DateFormatUtils
import java.util.*

object ShopOperationalHourMapper {

    private const val OPERATIONAL_HOUR_RESPONSE_FORMAT = "HH:mm:ss"
    private const val OPERATIONAL_HOUR_UI_FORMAT = "HH:mm"
    private const val OPERATIONAL_HOUR_TIMEZONE = "WIB"

    fun mapToShopOperationalData(
        operationalHourResponse: ShopOperationalHourResponse,
        closedInfoResponse: ShopClosedInfoDetailResponse,
        shopStatusInfoResponse: ShopStatusInfoResponse,
        shopSettingsAccess: Boolean
    ): ShopOperationalData {
        val isShopOpen = closedInfoResponse.isOpen()
        val isShopClosed = closedInfoResponse.isClosed()
        val isWeeklyOperationalClosed = operationalHourResponse.isWeeklyOperationalClosed() && !isShopClosed
        val isShopActive = operationalHourResponse.statusActive
        val isCanAtc = shopStatusInfoResponse.shopStatus != ShopClosedInfoDetailResponse.SHOP_STATUS_CLOSED
        val is24Hour = operationalHourResponse.is24Hour()

        val timeLabelRes: Int?
        val timeLabel: String?
        val startTime: String
        val endTime: String

        when {
            is24Hour && isShopOpen -> {
                // operational 24 Jam
                timeLabelRes = R.string.shop_operational_hour_24_hour
                timeLabel = null
            }
            !is24Hour && !isWeeklyOperationalClosed && isShopOpen -> {
                // operational hours range : 09:00 - 18:00 WIB
                startTime =
                        DateFormatUtils.formatDate(
                                OPERATIONAL_HOUR_RESPONSE_FORMAT,
                                OPERATIONAL_HOUR_UI_FORMAT,
                                operationalHourResponse.startTime
                        )
                endTime =
                        DateFormatUtils.formatDate(
                                OPERATIONAL_HOUR_RESPONSE_FORMAT,
                                OPERATIONAL_HOUR_UI_FORMAT,
                                operationalHourResponse.endTime
                        )
                timeLabel = "$startTime - $endTime $OPERATIONAL_HOUR_TIMEZONE"
                timeLabelRes = null
            }
            isWeeklyOperationalClosed && isCanAtc -> {
                // operational weekly closed, but buyer still can buy product
                timeLabelRes = R.string.shop_operational_hour_weekly_close_can_atc
                timeLabel = null
            }
            isWeeklyOperationalClosed && !isCanAtc -> {
                // operational weekly closed, buyer can't buy product
                timeLabelRes = R.string.shop_operational_hour_weekly_close_cannot_atc
                timeLabel = null
            }
            isShopClosed && !isWeeklyOperationalClosed -> {
                // scheduled holiday
                val startDate = Date(closedInfoResponse.startDate.toLongOrZero() * 1000L)
                val endDate = Date(closedInfoResponse.endDate.toLongOrZero() * 1000L)
                timeLabel = OperationalHoursUtil.toIndonesianDateRangeFormat(
                        startDate = startDate,
                        endDate = endDate,
                        isShortDateFormat = true,
                        isShowYear = false
                )
                timeLabelRes = null
            }
            else -> {
                timeLabelRes = R.string.shop_operational_hour_set_operational_time
                timeLabel = null
            }
        }

        return ShopOperationalData(
                isShopOpen,
                isShopClosed,
                isWeeklyOperationalClosed,
                isShopActive,
                timeLabelRes,
                timeLabel,
                shopSettingsAccess
        )
    }
}