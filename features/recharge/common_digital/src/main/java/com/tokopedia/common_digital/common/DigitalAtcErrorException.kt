package com.tokopedia.common_digital.common

import com.google.gson.Gson
import com.tokopedia.common_digital.atc.data.response.ErrorAtc
import com.tokopedia.common_digital.atc.data.response.Errors
import java.io.IOException

/**
 * created by @bayazidnasir on 11/8/2022
 */

class DigitalAtcErrorException : IOException {

    private var errorBody: String? = null

    override val message: String
        get() = getError().title

    constructor() : super()

    constructor(message: String?) : super(message){
        this.errorBody = message
    }

    constructor(message: String?, cause: Throwable?) : super(message, cause){
        this.errorBody = message
    }

    constructor(cause: Throwable?) : super(cause)

    fun getError(): ErrorAtc{
        return if (parseError().isNotEmpty()){
            parseError().first()
        }else{
            ErrorAtc()
        }
    }

    private fun parseError():List<ErrorAtc>{
        return if (!errorBody.isNullOrEmpty()){
            val json = Gson().fromJson(errorBody, Errors::class.java)
            json.errors
        }else{
            emptyList()
        }
    }
}