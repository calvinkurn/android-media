package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DisabledFeaturesDetailData(
        var disabledMultiAddressMessage: String = ""
) : Parcelable