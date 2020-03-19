package com.tokopedia.gamification.giftbox.presentation.helpers

import android.content.Context
import java.io.IOException

//todo Rahul delete this
object Util {
    fun getJsonDataFromAsset(context: Context, id: Int): String? {
        val jsonString: String
        try {
            jsonString = context.resources.openRawResource(id).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}