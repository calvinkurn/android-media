package com.tokopedia.common_digital.common

import com.google.gson.Gson
import com.tokopedia.common_digital.atc.data.response.ErrorAtc
import com.tokopedia.common_digital.atc.data.response.Errors
import java.io.IOException

/**
 * created by @bayazidnasir on 11/8/2022
 */

class DigitalAtcErrorException(message: String?) : IOException(message) {

    private var errorBody: String? = message

    override val message: String
        get() = getError().title

    fun getError(): ErrorAtc{
        return if (parseError().isNotEmpty()){
            parseError().first()
        }else{
            ErrorAtc()
        }
    }

    private fun parseError():List<ErrorAtc>{
        return try {
            if (!errorBody.isNullOrEmpty()) {
                val json = Gson().fromJson(errorBody, Errors::class.java)
                json.errors
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }
}
