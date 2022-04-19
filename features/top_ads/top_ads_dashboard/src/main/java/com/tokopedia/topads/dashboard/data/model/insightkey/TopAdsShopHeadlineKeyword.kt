package com.tokopedia.topads.dashboard.data.model.insightkey

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TopAdsShopHeadlineKeyword(

    @SerializedName("topadsHeadlineKeywordSuggestion")
    val suggestion: TopadsHeadlineKeywordSuggestion? = null
)

data class TopadsHeadlineKeywordSuggestion(

    @SerializedName("data")
    val recommendedKeywordData: RecommendedKeywordData? = null,

    @SerializedName("errors")
    val errors: List<ErrorsItem?>? = null
)

data class RecommendedKeywordData(
    val shopID: String? = null,
    var recommendedKeywordCount: Int = 0,
    val groupCount: Int = 0,
    val totalImpressionCount: String? = "",
    val recommendedKeywordDetails: MutableList<RecommendedKeywordDetail>? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.createTypedArrayList(RecommendedKeywordDetail)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shopID)
        parcel.writeInt(recommendedKeywordCount)
        parcel.writeInt(groupCount)
        parcel.writeString(totalImpressionCount)
        parcel.writeTypedList(recommendedKeywordDetails)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecommendedKeywordData> {
        override fun createFromParcel(parcel: Parcel): RecommendedKeywordData {
            return RecommendedKeywordData(parcel)
        }

        override fun newArray(size: Int): Array<RecommendedKeywordData?> {
            return arrayOfNulls(size)
        }
    }

}

data class RecommendedKeywordDetail(
    val keywordTag: String = "",
    val groupID: Int = 0,
    val groupName: String = "",
    val totalHits: Int = 0,
    val recommendedBid: Float = 0f,
    val minBid: Float = 0f,
    val maxBid: Float = 0f,
    val impressionCount: Int = 0
) : Parcelable {
    var isChecked: Boolean = true
    var priceBid: Int = 0
    var isError: Boolean = false
    var errorMessage: String? = null

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readInt()
    ) {
        isChecked = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(keywordTag)
        parcel.writeInt(groupID)
        parcel.writeString(groupName)
        parcel.writeInt(totalHits)
        parcel.writeFloat(recommendedBid)
        parcel.writeFloat(minBid)
        parcel.writeFloat(maxBid)
        parcel.writeInt(impressionCount)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecommendedKeywordDetail> {
        override fun createFromParcel(parcel: Parcel): RecommendedKeywordDetail {
            return RecommendedKeywordDetail(parcel)
        }

        override fun newArray(size: Int): Array<RecommendedKeywordDetail?> {
            return arrayOfNulls(size)
        }
    }
}

data class ErrorsItem(
    val code: String? = null,
    val detail: String? = null,
    val title: String? = null,
)
