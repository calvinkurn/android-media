package com.tokopedia.wallet.ovoactivation.view.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 01/10/18.
 */
@Parcelize
class PhoneActionModel(
    var titlePhoneAction: String = "",
    var descPhoneAction: String = "",
    var labelBtnPhoneAction: String = "",
    var applinkPhoneAction: String = "")
    : Parcelable
