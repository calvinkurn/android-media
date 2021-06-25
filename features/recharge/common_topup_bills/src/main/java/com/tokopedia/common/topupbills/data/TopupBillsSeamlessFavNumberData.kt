package com.tokopedia.common.topupbills.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopupBillsSeamlessFavNumberData(

        @SerializedName("rechargeFetchFavoriteNumber")
        val seamlessFavoriteNumber: TopupBillsSeamlessFavNumber
): Parcelable
