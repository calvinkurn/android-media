package com.tokopedia.logisticseller.ui.requestpickup.util

import java.text.SimpleDateFormat

object DateMapper {

    fun formatDate(inputDate: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("HH:mm")
        val output: String = formatter.format(parser.parse(inputDate))
        return output
    }
}
