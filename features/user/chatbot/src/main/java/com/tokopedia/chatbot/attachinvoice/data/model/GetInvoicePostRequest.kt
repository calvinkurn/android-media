package com.tokopedia.chatbot.attachinvoice.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Created by Hendri on 28/03/18.
 */

class GetInvoicePostRequest(@field:SerializedName("message_id")
                            @field:Expose
                            val messageId: Int, @field:SerializedName("user_id")
                            @field:Expose
                            val userId: Int, @field:SerializedName("show_all")
                            @field:Expose
                            val isShowAll: Boolean, @field:SerializedName("page")
                            @field:Expose
                            val page: Int, @field:SerializedName("limit")
                            @field:Expose
                            val limit: Int) {
    @SerializedName("start_time")
    @Expose
    val startTime: String

    init {
        this.startTime = generateStartTime()
    }

    fun generateStartTime(): String {
        val date = SimpleDateFormat(
                START_TIME_FORMAT, Locale.US)
        date.timeZone = TimeZone.getTimeZone("UTC")
        return date.format(Calendar.getInstance().time)
    }

    companion object {

        val START_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    }
}
