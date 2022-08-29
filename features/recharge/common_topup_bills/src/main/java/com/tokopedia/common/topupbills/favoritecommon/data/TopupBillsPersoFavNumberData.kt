package com.tokopedia.common.topupbills.favoritecommon.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
open class TopupBillsPersoFavNumberData(
    @SerializedName("digiPersoGetPersonalizedItems")
    val persoFavoriteNumber: TopupBillsPersoFavNumber = TopupBillsPersoFavNumber()
): Parcelable