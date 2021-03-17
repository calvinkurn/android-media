package com.tokopedia.shop.settings.etalase.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by hendry on 20/08/18.
 */
@Parcelize
data class ShopEtalaseUiModel(
        var id: String = "",
        var name: String = ""
): Parcelable