package com.tokopedia.home.beranda.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder

class KeywordSearchData : Parcelable {
    @SerializedName("universe_placeholder")
    lateinit var searchData: SearchPlaceholder

    constructor(parcel: Parcel) : super() {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {}

    override fun describeContents(): Int = 0

    fun getKeywordData() = searchData

    companion object CREATOR : Parcelable.Creator<KeywordSearchData> {
        override fun createFromParcel(parcel: Parcel): KeywordSearchData {
            return KeywordSearchData(parcel)
        }

        override fun newArray(size: Int): Array<KeywordSearchData?> {
            return arrayOfNulls(size)
        }
    }

}