package com.tokopedia.loginregister.registerinitial.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by alvinatin on 12/06/18.
 */
data class RegisterValidationPojo (
    @SerializedName("isExist")
    @Expose
    var exist: Boolean = false,

    @SerializedName("type")
    @Expose
    var type: String = "",

    @SerializedName("view")
    @Expose
    var view: String = ""
)