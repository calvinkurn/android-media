package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by fajarnuha on 26/12/18.
 */
@Parcelize
class CodModel : Parcelable {
    var isCod = false
    var counterCod = 0
}