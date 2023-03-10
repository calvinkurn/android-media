package com.tokopedia.kyc_centralized.ui.gotoKyc.data

import com.google.gson.annotations.SerializedName

data class KycStatusResponse(
	@SerializedName("kycStatus")
	val kycStatus: KycStatus
)

data class KycStatusDetail(
	@SerializedName("Status")
	val status: String = "",

	@SerializedName("IsSuccess")
	val isSuccess: Int,

	@SerializedName("UserId")
	val userId: Int,

	@SerializedName("CreateTime")
	val createTime: String,

	@SerializedName("ProjectId")
	val projectId: Int,

	@SerializedName("CreateBy")
	val createBy: Int,

	@SerializedName("UpdateTime")
	val updateTime: String,

	@SerializedName("StatusName")
	val statusName: String,

	@SerializedName("DataSource")
	val dataSource: Int
)

data class KycStatus(
	@SerializedName("Message")
	val message: List<String>,

	@SerializedName("Detail")
	val detail: KycStatusDetail
)
