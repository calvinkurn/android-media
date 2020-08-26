package com.tokopedia.user_identification_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 12/11/18.
 */
class GetApprovalStatusPojo {
    @Expose
    @SerializedName("kycStatus")
    var kycStatus: KycStatusPojo = KycStatusPojo()

}