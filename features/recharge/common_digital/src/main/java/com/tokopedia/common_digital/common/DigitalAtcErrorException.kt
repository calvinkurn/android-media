package com.tokopedia.common_digital.common

import com.google.gson.Gson
import com.tokopedia.common_digital.atc.data.response.ErrorAtc
import com.tokopedia.common_digital.atc.data.response.Errors
import java.io.IOException

/**
 * created by @bayazidnasir on 11/8/2022
 */

class DigitalAtcErrorException(private val errorBody: String) :IOException() {

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
        val json = Gson().fromJson(errorBody, Errors::class.java)
        return json.errors
    }
}