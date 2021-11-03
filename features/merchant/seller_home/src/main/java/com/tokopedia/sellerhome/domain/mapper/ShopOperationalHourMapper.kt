package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.domain.model.ShopClosedInfoDetailResponse
import com.tokopedia.sellerhome.domain.model.ShopOperationalHourResponse
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.ShopOperationalData
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.ShopOperationalUiModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.utils.time.DateFormatUtils

object ShopOperationalHourMapper {

    private const val OPERATIONAL_HOUR_RESPONSE_FORMAT = "HH:mm:ss"
    private const val OPERATIONAL_HOUR_UI_FORMAT = "HH:mm"
    private const val OPERATIONAL_HOUR_TIMEZONE = "WIB"
    private const val OPERATIONAL_YEAR_FORMAT = "yyyy"
    private const val SHOP_CLOSED_INFO_DATE_FORMAT = "dd MMM"
    private const val SHOP_CLOSED_INFO_DATE_FORMAT_FULL = "dd MMM yy"

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
            !isShopActive && !isShopClosed -> R.string.shop_operational_hour_set_operational_time
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

    fun mapToShopOperationalData(
        operationalHourResponse: ShopOperationalHourResponse,
        closedInfoResponse: ShopClosedInfoDetailResponse,
        shopSettingsAccess: Boolean
    ): ShopOperationalData {
        val isShopOpen = closedInfoResponse.isOpen()
        val isShopClosed = closedInfoResponse.isClosed()
        val isShopActive = operationalHourResponse.statusActive
        val is24Hour = operationalHourResponse.is24Hour()

        val operationalTimeIcon: Int
        val operationalTimeColorRes: Int
        val timeLabelRes: Int?
        val timeLabel: String?

        when {
            is24Hour && isShopOpen -> {
                operationalTimeIcon = IconUnify.RELOAD_24H
                operationalTimeColorRes = com.tokopedia.unifyprinciples.R.color.Unify_GN500
                timeLabelRes = R.string.shop_operational_hour_24_hour
                timeLabel = null
            }
            !isShopActive && !isShopClosed -> {
                operationalTimeIcon = IconUnify.CLOCK
                operationalTimeColorRes = com.tokopedia.unifyprinciples.R.color.Unify_NN600
                timeLabelRes = R.string.shop_operational_hour_set_operational_time
                timeLabel = null
            }
            else -> {
                timeLabelRes = null
                val startTime: String
                val endTime: String

                if (isShopClosed) {
                    operationalTimeIcon = IconUnify.CALENDAR
                    operationalTimeColorRes = com.tokopedia.unifyprinciples.R.color.Unify_RN500
                    val startTimeYear =
                        DateFormatUtils.getFormattedDate(
                            closedInfoResponse.startDate,
                            OPERATIONAL_YEAR_FORMAT
                        )

                    val endTimeYear =
                        DateFormatUtils.getFormattedDate(
                            closedInfoResponse.endDate,
                            OPERATIONAL_YEAR_FORMAT
                        )
                    val dateFormat =
                        if (startTimeYear == endTimeYear) {
                            SHOP_CLOSED_INFO_DATE_FORMAT
                        } else {
                            SHOP_CLOSED_INFO_DATE_FORMAT_FULL
                        }
                    startTime =
                        DateFormatUtils.getFormattedDate(
                            closedInfoResponse.startDate,
                            dateFormat
                        )
                    endTime =
                        DateFormatUtils.getFormattedDate(
                            closedInfoResponse.endDate,
                            dateFormat
                        )
                } else {
                    operationalTimeIcon = IconUnify.CLOCK
                    operationalTimeColorRes = com.tokopedia.unifyprinciples.R.color.Unify_GN500
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
                }

                timeLabel =
                    if(isShopOpen) {
                        "$startTime - $endTime $OPERATIONAL_HOUR_TIMEZONE"
                    } else {
                        "$startTime - $endTime"
                    }
            }
        }

        return ShopOperationalData(isShopOpen, isShopClosed, operationalTimeIcon, operationalTimeColorRes, timeLabelRes, timeLabel, shopSettingsAccess)
    }
}