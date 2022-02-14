package com.tokopedia.common.topupbills.favorite.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopupBillsPersoFavNumberData(
    @SerializedName("digiPersoGetPersonalizedItems")
    val persoFavoriteNumber: TopupBillsPersoFavNumber = TopupBillsPersoFavNumber()
): Parcelable