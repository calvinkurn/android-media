package com.tokopedia.common_digital.product.data.response

import android.util.Log
import com.google.gson.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.common.DigitalResponseErrorException
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.data.model.response.BaseResponseError
import com.tokopedia.network.exception.ResponseDataNullException
import java.io.IOException
import java.util.*

/**
 * Created by Rizky on 13/08/18.
 */
class TkpdDigitalResponse {
    var jsonElementData: JsonElement? = null
        private set
    var jsonElementIncluded: JsonElement? = null
        private set
    var jsonElementMeta: JsonElement? = null
    private var objData: Any? = null
    var objIncluded: Any? = null
    var objMeta: Any? = null
    var message: String? = null
    var strResponse: String? = null
        private set
    var strData: String? = null
        private set
    var strIncluded: String? = null
        private set
    var strMeta: String? = null

    private val gson = GsonBuilder().setPrettyPrinting().create()
    private fun setJsonElementData(jsonElementData: JsonElement) {
        this.jsonElementData = jsonElementData
    }

    private fun setStrResponse(strResponse: String) {
        this.strResponse = strResponse
    }

    private fun setStrData(strData: String) {
        this.strData = strData
    }

    private fun setJsonElementIncluded(jsonElementIncluded: JsonElement) {
        this.jsonElementIncluded = jsonElementIncluded
    }

    /**
     * @author anggaprasetiyo on 3/7/17.
     */
    class DigitalErrorResponse : BaseResponseError() {
        @SerializedName("errors")
        @Expose
        var errors: List<Error> = ArrayList()

        @SerializedName("message_error")
        @Expose
        var messageError: List<String> = ArrayList()

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null
        var errorCode = 0
        val typeOfError: Int
            get() = if (errors.isNotEmpty()) ERROR_DIGITAL else ERROR_SERVER
        val digitalErrorMessageFormatted: String
            get() {
                val stringBuilder = StringBuilder()
                for (i in errors.indices) {
                    stringBuilder.append(errors[i].title)
                    if (i < errors.size - 1) {
                        stringBuilder.append(", ")
                    }
                }
                return stringBuilder.toString().trim { it <= ' ' }
            }
        val serverErrorMessageFormatted: String
            get() {
                val stringBuilder = StringBuilder()
                for (i in messageError.indices) {
                    stringBuilder.append(messageError[i])
                    if (i < errors.size - 1) {
                        stringBuilder.append(", ")
                    }
                }
                return stringBuilder.toString().trim { it <= ' ' }
            }

        override fun getErrorKey(): String {
            return ERROR_KEY
        }

        override fun hasBody(): Boolean {
            return false
        }

        override fun createException(): IOException? {
            return null
        }

        class Error {
            @SerializedName("id")
            @Expose
            var id: String? = null

            @SerializedName("status")
            @Expose
            var status: String? = null

            @SerializedName("title")
            @Expose
            var title: String? = null
        }

        companion object {
            const val ERROR_DIGITAL = 1
            const val ERROR_SERVER = 2
            private const val ERROR_KEY = "errors"
            fun factory(errorBody: String?, code: Int): DigitalErrorResponse {
                return try {
                    val digitalErrorResponse = Gson().fromJson(errorBody, DigitalErrorResponse::class.java)
                    digitalErrorResponse.errorCode = code
                    digitalErrorResponse
                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()
                    factoryDefault(
                        ErrorNetMessage.MESSAGE_ERROR_DEFAULT,
                        code
                    )
                }
            }

            private fun factoryDefault(
                messageErrorDefault: String,
                errorCode: Int
            ): DigitalErrorResponse {
                val digitalErrorResponse = DigitalErrorResponse()
                val errorList: MutableList<Error> = ArrayList()
                val error = Error()
                error.id = "0"
                error.status = "0"
                error.title = messageErrorDefault
                errorList.add(error)
                digitalErrorResponse.errors = errorList
                digitalErrorResponse.errorCode = errorCode
                return digitalErrorResponse
            }
        }
    }

    companion object {
        private val TAG = TkpdDigitalResponse::class.java.simpleName
        private const val KEY_DATA = "data"
        private const val KEY_INCLUDED = "included"
        private const val KEY_META = "meta"
        private const val KEY_ERROR = "errors"
        private const val DEFAULT_ERROR_MESSAGE_DATA_NULL = "Tidak ada data"

        @Throws(IOException::class)
        fun factory(strResponse: String): TkpdDigitalResponse {
            Log.d(TAG, strResponse)
            val tkpdDigitalResponse = TkpdDigitalResponse()
            val jsonElement = JsonParser().parse(strResponse)
            val jsonResponse = jsonElement.asJsonObject
            val strData: String
            val strIncluded: String?
            val strMeta: String?
            strData = if (!jsonResponse.has(KEY_DATA) || jsonResponse[KEY_DATA].isJsonNull) {
                if (jsonResponse.has(KEY_ERROR)) {
                    try {
                        val digitalErrorResponse = Gson().fromJson(strResponse, DigitalErrorResponse::class.java)
                        throw DigitalResponseErrorException(
                            digitalErrorResponse.digitalErrorMessageFormatted
                        )
                    } catch (e: JsonSyntaxException) {
                        e.printStackTrace()
                        throw DigitalResponseErrorException()
                    }
                }
                throw ResponseDataNullException(DEFAULT_ERROR_MESSAGE_DATA_NULL)
            } else if (jsonResponse.has(KEY_DATA) && jsonResponse[KEY_DATA].isJsonObject) {
                jsonResponse[KEY_DATA].asJsonObject.toString()
            } else if (jsonResponse.has(KEY_DATA) && jsonResponse[KEY_DATA].isJsonArray) {
                jsonResponse[KEY_DATA].asJsonArray.toString()
            } else {
                throw ResponseDataNullException(DEFAULT_ERROR_MESSAGE_DATA_NULL)
            }
            strIncluded = if (!jsonResponse.has(KEY_INCLUDED) || jsonResponse[KEY_INCLUDED].isJsonNull) {
                null
            } else if (jsonResponse.has(KEY_INCLUDED) && jsonResponse[KEY_INCLUDED].isJsonObject) {
                jsonResponse[KEY_INCLUDED].asJsonObject.toString()
            } else if (jsonResponse.has(KEY_INCLUDED) && jsonResponse[KEY_INCLUDED].isJsonArray) {
                jsonResponse[KEY_INCLUDED].asJsonArray.toString()
            } else {
                null
            }
            strMeta = if (!jsonResponse.has(KEY_META) || jsonResponse[KEY_META].isJsonNull) {
                null
            } else if (jsonResponse.has(KEY_META) && jsonResponse[KEY_META].isJsonObject) {
                jsonResponse[KEY_META].asJsonObject.toString()
            } else if (jsonResponse.has(KEY_META) && jsonResponse[KEY_META].isJsonArray) {
                jsonResponse[KEY_META].asJsonArray.toString()
            } else {
                null
            }
            tkpdDigitalResponse.setJsonElementData(jsonResponse[KEY_DATA])
            tkpdDigitalResponse.setJsonElementIncluded(jsonResponse[KEY_INCLUDED])
            tkpdDigitalResponse.jsonElementMeta = jsonResponse[KEY_META]
            tkpdDigitalResponse.message = ""
            tkpdDigitalResponse.setStrData(strData)
            tkpdDigitalResponse.strIncluded = strIncluded
            tkpdDigitalResponse.setStrResponse(strResponse)
            tkpdDigitalResponse.strMeta = strMeta
            return tkpdDigitalResponse
        }
    }
}
