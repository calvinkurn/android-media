package com.tokopedia.user_identification_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 15/11/18.
 */
class KycStatusDetailPojo {
    @Expose
    @SerializedName("IsSuccess")
    var isSuccess = 0

    @Expose
    @SerializedName("Status")
    var status = 0

    @Expose
    @SerializedName("StatusName")
    var statusName = ""

}