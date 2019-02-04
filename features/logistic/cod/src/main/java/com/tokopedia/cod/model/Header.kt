package com.tokopedia.cod.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fajarnuha on 07/01/19.
 */
data class Header(@SerializedName("process_time")
                  @Expose
                  var processTime: Double? = null,
                  @SerializedName("messages")
                  @Expose
                  var messages: List<Any>? = null,
                  @SerializedName("reason")
                  @Expose
                  var reason: String? = null,
                  @SerializedName("error_code")
                  @Expose
                  var errorCode: String? = null)