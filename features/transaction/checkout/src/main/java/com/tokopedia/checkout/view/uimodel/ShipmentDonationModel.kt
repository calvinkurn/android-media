package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.logisticcart.shipping.model.ShipmentData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShipmentDonationModel(
        var donation: Donation = Donation(),
        var isChecked: Boolean = false
) : ShipmentData, Parcelable