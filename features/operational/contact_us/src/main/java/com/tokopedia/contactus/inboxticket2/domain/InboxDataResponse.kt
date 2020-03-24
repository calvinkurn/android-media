package com.tokopedia.contactus.inboxticket2.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header

/**
 * Created by Nathaniel on 12/28/2016.
 */
class InboxDataResponse<T> {
    @SerializedName("header")
    @Expose
    val header: Header? = null
    @SerializedName(value = "data")
    @Expose
    var data: T? = null
    @SerializedName(value = "message_error")
    @Expose
    val errorMessage: List<String>? = null
}