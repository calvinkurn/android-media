package com.tokopedia.discovery2

import android.content.Context
import android.content.Intent
import android.net.Uri
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
const val LABEL_GIMMICK = "gimmick"
const val LABEL_INTEGRITY = "integrity"
const val LABEL_SHIPPING = "shipping"
val TIME_DISPLAY_FORMAT = "%1$02d"

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
            val saleStartDate = parseData(saleStartDate)
            val saleEndDate = parseData(saleEndDate)
            return if (saleStartDate != null && saleEndDate != null) {
                (saleStartDate.time <= currentSystemTime.time) && (currentSystemTime.time < saleEndDate.time)
            } else {
                false
            }
        }

        fun isSaleOver(saleEndDate: String): Boolean {
            if (saleEndDate.isEmpty()) return true
            val currentSystemTime = Calendar.getInstance().time
            val parsedDate = parseData(saleEndDate)
            return if (parsedDate != null) {
                currentSystemTime.time >= parsedDate.time
            } else {
                false
            }
        }

        fun parseData(date: String?): Date? {
            return date?.let {
                try {
                    SimpleDateFormat(TIMER_SPRINT_SALE_DATE_FORMAT, Locale.getDefault())
                            .parse(date)
                } catch (parseException: ParseException) {
                    null
                }
            }
        }
    }
}