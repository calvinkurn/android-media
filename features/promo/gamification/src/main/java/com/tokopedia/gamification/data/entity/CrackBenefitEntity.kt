package com.tokopedia.gamification.data.entity

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

data class CrackBenefitEntity(
    @ColumnInfo(name = "color")
    @SerializedName("color")
    var color: String = "",
    @Ignore
    @ColumnInfo(name = "size")
    @SerializedName("size")
    var size: String = "",
    @Ignore @SerializedName("templateText")
    var templateText: String = "",
    @Ignore @SerializedName("benefitType")
    var benefitType: String="",
    @Ignore @SerializedName("valueAfter")
    var valueAfter: Int = 0,
@SerializedName("multiplier")
var multiplier: String = "",

@ColumnInfo(name = "text")
@SerializedName("text")
var text: String="",

@Ignore
@SerializedName("valueBefore")
var valueBefore:Int = 0,

@Ignore
@SerializedName("tierInformation")
var tierInformation: String="",

@Ignore
@SerializedName("animationType")
var animationType: String="",

@SerializedName("imageUrl")
var imageUrl: String="",

@Ignore
@SerializedName("referenceID")
var referenceID: String="",

@Ignore
@SerializedName("isBigPrize")
var isBigPrize:Boolean = false,

@Ignore
@SerializedName("isAutoApply")
var isAutoApply:Boolean = false,

@Ignore
@SerializedName("autoApplyMsg")
var autoApplyMsg: String="",

@Ignore
@SerializedName("dummyCode")
var dummyCode: String="",

)