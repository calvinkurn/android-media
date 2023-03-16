package com.tokopedia.home.beranda.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder

class KeywordSearchData : Parcelable {
    @SerializedName("universe_placeholder")
     val searchData: SearchPlaceholder = SearchPlaceholder()

    override fun writeToParcel(dest: Parcel, flags: Int) {}

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<KeywordSearchData> {
        override fun createFromParcel(parcel: Parcel): KeywordSearchData {
            return KeywordSearchData()
        }

        override fun newArray(size: Int): Array<KeywordSearchData?> {
            return arrayOfNulls(size)
        }
    }

}
