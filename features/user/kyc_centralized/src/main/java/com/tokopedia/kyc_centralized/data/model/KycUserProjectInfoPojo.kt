package com.tokopedia.kyc_centralized.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kyc_centralized.common.KycStatus
import java.util.ArrayList

data class KycUserProjectInfoPojo(
    @SerializedName("kycProjectInfo")
    var kycProjectInfo: KycProjectInfo? = KycProjectInfo()
)

data class KycProjectInfo(
    @SerializedName("Status")
    var status: Int? = KycStatus.DEFAULT.code,
    @SerializedName("StatusName")
    var statusName: String? = "",
    @SerializedName("Message")
    var message: String? = "",
    @SerializedName("IsAllowToRegister")
    var isAllowToRegister: Boolean = false,
    @SerializedName("Reason")
    var reasonList: ArrayList<String>? = arrayListOf(),
    @SerializedName("TypeList")
    var typeLists: ArrayList<TypeList>? = arrayListOf(),
    @SerializedName("IsSelfie")
    var isSelfie: Boolean? = null,
)

data class TypeList(
    @SerializedName("TypeID")
    var iD: Int? = 0,
    @SerializedName("Status")
    var status: Int? = 0,
    @SerializedName("StatusName")
    var statusName: String? = "",
    @SerializedName("IsAllowToUpload")
    var isAllowToUpload: Boolean = false
)
