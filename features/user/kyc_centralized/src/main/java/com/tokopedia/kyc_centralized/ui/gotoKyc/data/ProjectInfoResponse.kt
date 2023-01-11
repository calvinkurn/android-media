package com.tokopedia.kyc_centralized.ui.gotoKyc.data

import com.google.gson.annotations.SerializedName

data class ProjectInfoResponse(
	@SerializedName("kycProjectInfo")
	val kycProjectInfo: KycProjectInfo = KycProjectInfo()
)

data class KycProjectInfo(
    @SerializedName("Status")
	val status: Int = 0,

    @SerializedName("AccountLinkingStatus")
	val accountLinkingStatus: Int = 0,

    @SerializedName("Message")
	val message: String = "",

    @SerializedName("IsSelfie")
	val isSelfie: Boolean = false,

    @SerializedName("StatusName")
	val statusName: String = "",

    @SerializedName("IsGoToKyc")
	val isGoToKyc: Boolean = false,

    @SerializedName("IsAllowToRegister")
	val isAllowToRegister: Boolean = false,

    @SerializedName("TypeList")
	val typeList: List<TypeListItem> = emptyList(),

    @SerializedName("Reason")
	val reason: List<String> = emptyList(),

    @SerializedName("GotoLinked")
	val gotoLinked: Boolean = false,

    @SerializedName("DataSource")
	val dataSource: Int = 0
)

data class TypeListItem(
	@SerializedName("Status")
	val status: Int = 0,

	@SerializedName("IsAllowToUpload")
	val isAllowToUpload: Boolean = false,

	@SerializedName("TypeID")
	val typeID: Int = 0,

	@SerializedName("StatusName")
	val statusName: String = ""
)
