package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName


data class AffiliateKycDetailsData(
    @SerializedName("kycProjectInfo")
    var kycProjectInfo: KycProjectInfo?
) {

    data class KycProjectInfo(
        @SerializedName("IsAllowToRegister")
        var isAllowToRegister: Boolean?,
        @SerializedName("IsSelfie")
        var isSelfie: Boolean?,
        @SerializedName("Message")
        var message: String?,
        @SerializedName("Status")
        var status: Int?,
        @SerializedName("StatusName")
        var statusName: String?,
        @SerializedName("TypeList")
        var typeList: List<Type?>?
    ) {
        data class Type(
            @SerializedName("IsAllowToUpload")
            var isAllowToUpload: Boolean?,
            @SerializedName("Status")
            var status: Int?,
            @SerializedName("StatusName")
            var statusName: String?,
            @SerializedName("TypeID")
            var typeID: Int?
        )
    }
}