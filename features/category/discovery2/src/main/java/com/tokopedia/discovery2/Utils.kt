package com.tokopedia.discovery2

import android.content.Context
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.datamapper.discoComponentQuery
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor


const val LIGHT_GREY = "lightGrey"
const val LIGHT_BLUE = "lightBlue"
const val LIGHT_GREEN = "lightGreen"
const val LIGHT_RED = "lightRed"
const val LIGHT_ORANGE = "lightOrange"
const val DARK_GREY = "darkGrey"
const val DARK_BLUE = "darkBlue"
const val DARK_GREEN = "darkGreen"
const val DARK_RED = "darkRed"
const val DARK_ORANGE = "darkOrange"
const val TRANSPARENT_BLACK = "transparentBlack"
const val LABEL_PRODUCT_STATUS = "status"
const val LABEL_PRICE = "price"
const val PDP_APPLINK = "tokopedia://product/"
const val TIME_DISPLAY_FORMAT = "%1$02d"
const val DEFAULT_TIME_DATA : Long = 0

class Utils {

    companion object {
        const val TIME_ZONE = "Asia/Jakarta"
        const val TIMER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm"
        const val TIMER_SPRINT_SALE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val DEFAULT_BANNER_WIDTH = 800
        const val DEFAULT_BANNER_HEIGHT = 150
        const val BANNER_SUBSCRIPTION_DEFAULT_STATUS = -1
        const val SEARCH_DEEPLINK = "tokopedia://search-autocomplete"
        private const val SERIBU = 1000
        private const val SEJUTA = 1000000
        private const val SEMILIAR = 1000000000
        private const val VIEW_LIMIT = 1.0
        private const val SERIBU_TEXT = "rb orang"
        private const val SEJUTA_TEXT = "jt orang"
        private const val SEMILIAR_TEXT = "M orang"
        var preSelectedTab = -1
        private const val IDENTIFIER = "identifier"
        private const val COMPONENT_ID = "component_id"
        private const val DEVICE = "device"
        private const val DEVICE_VALUE = "Android"
        private const val FILTERS = "filters"


        fun extractDimension(url: String?, dimension: String = "height"): Int? {
            val uri = Uri.parse(url)
            return uri?.getQueryParameter(dimension)?.toInt()
        }

        fun shareData(context: Context?, shareTxt: String?, productUri: String?) {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_TEXT, shareTxt + "\n" + productUri)
            context?.startActivity(Intent.createChooser(share, shareTxt))
        }

        fun getCountView(countView: Double, notifyMeText: String = ""): String = when {
            countView >= SERIBU && countView < SEJUTA -> {
                getDisplayValue(getDecimalFormatted(countView / SERIBU), SERIBU_TEXT, notifyMeText)
            }
            countView >= SEJUTA && countView < SEMILIAR -> {
                getDisplayValue(getDecimalFormatted(countView / SEJUTA), SEJUTA_TEXT, notifyMeText)
            }
            countView >= SEMILIAR -> {
                getDisplayValue(getDecimalFormatted(countView / SEMILIAR), SEMILIAR_TEXT, notifyMeText)
            }
            else -> ""
        }

        private fun getDecimalFormatted(currentViewCount: Double) = floor(currentViewCount * 1e1) / 1e1

        private fun getDisplayValue(convertedValue: Double, text: String, notifyMeText: String): String {
            return if (convertedValue > VIEW_LIMIT) {
                "${convertedValue.toString().replace('.', ',')} $text $notifyMeText"
            } else {
                "${convertedValue.toInt()} $text $notifyMeText"
            }
        }

        fun getQueryMap(componentId: String, pageIdentifier: String): Map<String, Any> {
            val queryParameterMap = mutableMapOf<String, Any>()
            queryParameterMap[IDENTIFIER] = pageIdentifier
            queryParameterMap[DEVICE] = DEVICE_VALUE
            queryParameterMap[COMPONENT_ID] = componentId

            discoComponentQuery?.let { map ->
                val queryString = StringBuilder()
                map.forEach { (key, value) ->
                    if (!value.isNullOrEmpty()) {
                        if (queryString.isNotEmpty()) {
                            queryString.append('&')
                        }
                        queryString.append(key).append('=').append(value)
                    }
                }
                if (queryString.isNotEmpty()) queryParameterMap[FILTERS] = queryString.toString()
            }
            return queryParameterMap
        }

        fun isFutureSale(saleStartDate: String): Boolean {
            if (saleStartDate.isEmpty()) return false
            val currentSystemTime = Calendar.getInstance().time
            val parsedDate = parseData(saleStartDate)
            return if (parsedDate != null) {
                currentSystemTime.time < parsedDate.time
            } else {
                false
            }
        }


        fun isFutureSaleOngoing(saleStartDate: String, saleEndDate: String): Boolean {
            if (saleStartDate.isEmpty() || saleEndDate.isEmpty()) return false
            val currentSystemTime = Calendar.getInstance().time
            val parsedSaleStartDate = parseData(saleStartDate)
            val parsedSaleEndDate = parseData(saleEndDate)
            return if (parsedSaleStartDate != null && parsedSaleEndDate != null) {
                (parsedSaleStartDate.time <= currentSystemTime.time) && (currentSystemTime.time < parsedSaleEndDate.time)
            } else {
                false
            }
        }

        fun isSaleOver(saleEndDate: String, timerFormat : String = TIMER_SPRINT_SALE_DATE_FORMAT): Boolean {
            if (saleEndDate.isEmpty()) return true
            val currentSystemTime = Calendar.getInstance().time
            val parsedDate = parseData(saleEndDate, timerFormat)
            return if (parsedDate != null) {
                currentSystemTime.time >= parsedDate.time
            } else {
                false
            }
        }

        fun parseData(date: String?, timerFormat : String  = TIMER_SPRINT_SALE_DATE_FORMAT): Date? {
            return date?.let {
                try {
                    SimpleDateFormat(timerFormat, Locale.getDefault())
                            .parse(date)
                } catch (parseException: ParseException) {
                    null
                }
            }
        }

        fun parseFlashSaleDate(saleTime: String?): String {
            if (!saleTime.isNullOrEmpty() && saleTime.length >= 19) {
                    val date = saleTime.substring(0, 10)
                    val time = saleTime.substring(11, 19)
                    return "${date}T${time}"
            }
            return ""
        }

        fun parsedColor(context: Context, fontColor: String, defaultColor: Int): Int {
            return try {
                Color.parseColor(fontColor)
            } catch (exception: Exception) {
                MethodChecker.getColor(context, defaultColor)
            }
        }

        fun setTimerBoxDynamicBackground(view: View, color: Int) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    view.background.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
                } else {
                    view.background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                }
            } catch (exception: Exception) {
                view.setBackgroundColor(MethodChecker.getColor(view.context, color))
            }
        }

        fun getElapsedTime(endDate: String): Long {
            if (endDate.isNotEmpty()) {
                try {
                    TimeZone.setDefault(TimeZone.getTimeZone(TIME_ZONE))
                    val currentSystemTime = Calendar.getInstance().time
                    SimpleDateFormat(TIMER_DATE_FORMAT, Locale.getDefault()).parse(endDate)?.let {
                        return it.time - currentSystemTime.time
                    }
                } catch (e: Exception) {
                    return DEFAULT_TIME_DATA
                }
            }
            return DEFAULT_TIME_DATA
        }
    }
}