package com.tokopedia.user_identification_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 15/11/18.
 */
data class KycStatusDetailPojo (
    @Expose @SerializedName("IsSuccess") var isSuccess: Int = 0,
    @Expose @SerializedName("Status") var status : Int = 0,
    @Expose @SerializedName("StatusName") var statusName : String = ""
)