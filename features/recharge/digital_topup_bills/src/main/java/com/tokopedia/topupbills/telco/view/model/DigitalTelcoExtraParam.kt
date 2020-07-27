package com.tokopedia.topupbills.telco.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DigitalTelcoExtraParam(var categoryId: String = "",
                             var productId: String = "",
                             var clientNumber: String = "",
                             var menuId: String = "") : Parcelable
