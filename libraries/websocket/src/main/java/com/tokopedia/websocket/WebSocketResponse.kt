package com.tokopedia.websocket

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 06/12/18
 */
open class WebSocketResponse {
    @SerializedName("type")
    @Expose
    private var type: String = ""

    @SerializedName("code")
    @Expose
    private var code: Int = 0

    @SerializedName("data")
    @Expose
    private var jsonObject: JsonObject? = null

    fun getType(): String? {
        return type
    }

    fun setType(code: String) {
        this.type = code
    }

    fun getData(): JsonObject? {
        return jsonObject
    }

    fun setData(jsonObject: JsonObject) {
        this.jsonObject = jsonObject
    }

    fun getCode(): Int? {
        return code
    }

    fun setCode(code: Int) {
        this.code = code
    }
}
