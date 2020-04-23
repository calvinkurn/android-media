package com.tokopedia.notifications.database.convertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.tokopedia.notifications.model.WebHookParam

class WebHookParamConverter {

    @TypeConverter
    fun toWebHookParam(value: String?): WebHookParam? {
        return Gson().fromJson<WebHookParam>(value, WebHookParam::class.java)
    }

    @TypeConverter
    fun toJson(webHookParam: WebHookParam?): String? {
        return Gson().toJson(webHookParam)
    }

    companion object{
        val instances =  WebHookParamConverter()
    }

}