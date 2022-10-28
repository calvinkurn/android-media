package com.tokopedia.tokomember_seller_dashboard.domain.requestparam

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class TmProgramUpdateResponse(
	@Expose
	@SerializedName("input")
	val input: ProgramUpdateDataInput? = null
)

@Parcelize
data class ProgramUpdateDataInput(
	@Expose
	@SerializedName("actionType")
	var actionType: String? = null,
	@Expose
	@SerializedName("timeWindow")
	var timeWindow: TimeWindow? = null,
	@Expose
	@SerializedName("apiVersion")
	val apiVersion: String? = null,
	@Expose
	@SerializedName("programAttributes")
	var programAttributes: List<ProgramAttributesItem?>? = null,
	@Expose
	@SerializedName("cardID")
	var cardID: Int? = 0,
	@Expose
	@SerializedName("name")
	val name: String? = null,
	@Expose
	@SerializedName("id")
	val id: Int? = 0,

	@SerializedName("tierLevels")
    var tierLevels: List<TierLevelsItem?>? = null
) :Parcelable

@Parcelize
data class TimeWindow(
	@Expose
	@SerializedName("periodInMonth")
    var periodInMonth: Int? = null,
	@Expose
	@SerializedName("startTime")
	val startTime: String? = null,
	@Expose
	@SerializedName("id")
	var id: Int? = 0,
	@Expose
	@SerializedName("endTime")
	val endTime: String? = null,
) : Parcelable

@Parcelize
data class TierLevelsItem(
	@Expose
	@SerializedName("metadata")
    var metadata: String? = null,
	@Expose
	@SerializedName("tierGroupID")
	val tierGroupID: Int? = 0,
	@Expose
	@SerializedName("level")
	val level: Int? = 0,
	@Expose
	@SerializedName("activeTime")
	val activeTime: Int? = 0,
	@Expose
	@SerializedName("name")
	var name: String? = null,
	@Expose
	@SerializedName("threshold")
    var threshold: Int? = null,
	@Expose
	@SerializedName("id")
	var id: Int? = 0
) : Parcelable

@Parcelize
data class ProgramAttributesItem(
	@Expose
	@SerializedName("minimumTransaction")
	var minimumTransaction: Int? = null,
	@Expose
	@SerializedName("multiplierRates")
	var multiplierRates: Int? = null,
	@Expose
	@SerializedName("tierLevelID")
	var tierLevelID: Int? = null,
	@Expose
	@SerializedName("id")
	var id: Int? = 0,
	@Expose
	@SerializedName("isUseMultiplier")
	var isUseMultiplier: Boolean? = null,
	@Expose
	@SerializedName("programID")
	var programID: Int? = 0
) : Parcelable
