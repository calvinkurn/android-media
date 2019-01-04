package com.tokopedia.broadcast.message.common.extensions

import com.tokopedia.broadcast.message.common.data.model.TopChatBlastSellerMetaData
import java.text.SimpleDateFormat
import java.util.*

fun String.toISO8601Date(): Date {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.parse(this)
}

fun Date.toStringDayMonth(): String{
    val formatter = SimpleDateFormat("dd MMMM", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(this)
}

val TopChatBlastSellerMetaData.dateToShow: Date?
    get() {
        return try {
            val date = this.expireAt.toISO8601Date()
            if (this.quota > 0)
                date
            else {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.add(Calendar.DATE, 1)
                return calendar.time
            }
        } catch (e: Exception){
            null
        }
    }