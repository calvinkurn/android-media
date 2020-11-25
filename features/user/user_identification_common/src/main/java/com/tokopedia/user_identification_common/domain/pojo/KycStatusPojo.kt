package com.tokopedia.user_identification_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * @author by nisie on 15/11/18.
 */
data class KycStatusPojo (
    @Expose @SerializedName("Message") var message: List<String> = ArrayList(),
    @Expose @SerializedName("Detail") var kycStatusDetailPojo: KycStatusDetailPojo = KycStatusDetailPojo()
)