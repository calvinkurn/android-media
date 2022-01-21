package com.tokopedia.common.topupbills.data.favorite_number_perso

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopupBillsPersoFavNumberData(
    @SerializedName("digiPersoGetPersonalizedItems")
    val persoFavoriteNumber: TopupBillsPersoFavNumber = TopupBillsPersoFavNumber()
): Parcelable