package com.tokopedia.kyc_centralized.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class KycUserProjectInfoPojo(
    @Expose
    @SerializedName("kycProjectInfo")
    var kycProjectInfo: KycProjectInfo? = null
)

data class KycProjectInfo(
    @Expose
    @SerializedName("Status")
    var status: Int? = null,
    @Expose
    @SerializedName("StatusName")
    var statusName: String? = null,
    @Expose
    @SerializedName("Message")
    var message: String? = null,
    @Expose
    @SerializedName("IsAllowToRegister")
    var isAllowToRegister: Boolean = false,
    @Expose
    @SerializedName("Reason")
    var reasonList: ArrayList<String>? = null,
    @Expose
    @SerializedName("TypeList")
    var typeLists: ArrayList<TypeList>? = null,
    @Expose
    @SerializedName("IsSelfie")
    var isSelfie: Boolean? = null,
)

data class TypeList(
    @Expose
    @SerializedName("TypeID")
    var iD: Int? = null,
    @Expose
    @SerializedName("Status")
    var status: Int? = null,
    @Expose
    @SerializedName("StatusName")
    var statusName: String? = null,
    @Expose
    @SerializedName("IsAllowToUpload")
    var isAllowToUpload: Boolean = false
)