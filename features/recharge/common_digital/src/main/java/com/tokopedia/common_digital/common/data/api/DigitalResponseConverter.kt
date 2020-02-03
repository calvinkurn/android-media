package com.tokopedia.common_digital.common.data.api

import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse

import java.io.IOException
import java.lang.reflect.Type

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

/**
 * Created by Rizky on 30/08/18.
 */
class DigitalResponseConverter : Converter.Factory() {

    override fun responseBodyConverter(type: Type?,
                                       annotations: Array<Annotation>?,
                                       retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return if (TkpdDigitalResponse::class.java == type) {
            Converter<ResponseBody, TkpdDigitalResponse> { value -> TkpdDigitalResponse.factory(value.string()) }
        } else null
    }

    override fun requestBodyConverter(type: Type?,
                                      parameterAnnotations: Array<Annotation>?,
                                      methodAnnotations: Array<Annotation>?,
                                      retrofit: Retrofit?): Converter<*, RequestBody>? {
        return if (TkpdDigitalResponse::class.java == type) {
            Converter<TkpdDigitalResponse, RequestBody> { value -> RequestBody.create(MEDIA_TYPE, value.strResponse) }
        } else null
    }

    companion object {

        private val MEDIA_TYPE = MediaType.parse("text/plain")

        fun create(): DigitalResponseConverter {
            return DigitalResponseConverter()
        }
    }

}
