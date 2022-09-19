package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramAttributesItem
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TierLevelsItem

data class TmGetProgramDetailResponse(

	@SerializedName("data")
	val data: ProgramDetailData? = null
)

data class ProgramDetailData(

	@SerializedName("membershipGetProgramForm")
	val membershipGetProgramForm: MembershipGetProgramForm? = null
)

data class LevelInfo(

	@SerializedName("levelList")
	val levelList: List<Level?>? = null
)

data class Level(

	@SerializedName("level")
	val level: Int? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("threshold")
	val threshold: Int? = null,

	@SerializedName("totalMember")
	val totalMember: String? = null,
)

data class MembershipGetProgramForm(

	@SerializedName("resultStatus")
	val resultStatus: ResultStatus? = null,

	@SerializedName("levelInfo")
	val levelInfo: LevelInfo? = null,

	@SerializedName("ticker")
	val ticker: Ticker? = null,

	@SerializedName("catalogInfo")
	val catalogInfo: CatalogInfo? = null,

	@SerializedName("programForm")
	val programForm: ProgramForm? = null,

	@Expose
	@SerializedName("timePeriodList")
	val timePeriodList: List<TimePeriodListItem?>? = null,

	@Expose
	@SerializedName("programThreshold")
	val programThreshold: ProgramThreshold? = null
)

data class TimePeriodListItem(
	@Expose
	@SerializedName("months")
	val months: Int? = null,
	@Expose
	@SerializedName("name")
	val name: String? = null,
	@Expose
	@SerializedName("isSelected")
	var isSelected: Boolean? = null
)

data class ProgramThreshold(
	@Expose
	@SerializedName("minThresholdLevel1")
	val minThresholdLevel1: Int? = null,
	@Expose
	@SerializedName("maxThresholdLevel1")
	val maxThresholdLevel1: Int? = null,
	@Expose
	@SerializedName("minThresholdLevel2")
	val minThresholdLevel2: Int? = null,
	@Expose
	@SerializedName("maxThresholdLevel2")
	val maxThresholdLevel2: Int? = null
)

data class DropdownLevelItem(

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("value")
	val value: String? = null
)

data class DropdownCatalogTypeItem(

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("value")
	val value: String? = null
)

data class Ticker(

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("title")
	val title: String? = null
)

data class ProgramForm(

	@SerializedName("timeWindow")
	val timeWindow: TimeWindow? = null,

	@SerializedName("programAttributes")
	val programAttributes: List<ProgramAttributesItem?>? = arrayListOf(),

	@SerializedName("cardID")
	val cardID: Int? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("id")
	val id: String? = null,

	@SerializedName("tierLevels")
	val tierLevels: List<TierLevelsItem?>? = null,

	@SerializedName("status")
	val status: Int? = null,

	@SerializedName("statusStr")
	val statusStr: String? = null,

	@SerializedName("analytics")
	val analytics: Analytics? = null
)

data class ProgramAttribute(

	@SerializedName("id")
	val id: String? = null,

	@SerializedName("minimumTransaction")
	val minimumTransaction: Int? = null
)

data class CatalogInfo(

	@SerializedName("catalogList")
	val catalogList: List<Any?>? = null,

	@SerializedName("dropdownCatalogType")
	val dropdownCatalogType: List<DropdownCatalogTypeItem?>? = null,

	@SerializedName("dropdownLevel")
	val dropdownLevel: List<DropdownLevelItem?>? = null
)
