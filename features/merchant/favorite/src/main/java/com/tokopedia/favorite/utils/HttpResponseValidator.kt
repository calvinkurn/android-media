package com.tokopedia.favorite.utils

import retrofit2.Response
import rx.functions.Action1

/**
 * @author madi on 4/10/17.
 */
object HttpResponseValidator {

    private const val HTTP_ERROR_500 = 500
    private const val HTTP_ERROR_600 = 600
    private const val HTTP_ERROR_400 = 400

    fun validate(listener: HttpValidationListener): Action1<Response<String?>?> {
        return Action1 { stringResponse ->
            if (stringResponse == null) {
                listener.onPassValidation(stringResponse)
            } else if (stringResponse.code() >= HTTP_ERROR_500
                    && stringResponse.code() < HTTP_ERROR_600) {
                throw RuntimeException("Server Error!")
            } else if (stringResponse.code() >= HTTP_ERROR_400
                    && stringResponse.code() < HTTP_ERROR_500) {
                throw RuntimeException("Client Error!")
            } else {
                listener.onPassValidation(stringResponse)
            }
        }
    }

    interface HttpValidationListener {
        fun onPassValidation(response: Response<String?>?)
    }
}
