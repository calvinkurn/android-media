package com.tokopedia.checkout.old.view.uimodel

import android.os.Parcelable
import com.tokopedia.checkout.old.domain.model.cartshipmentform.Donation
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentDonationModel(
        var donation: Donation = Donation(),
        var isChecked: Boolean = false,
        var isEnabled: Boolean = true
) : Parcelable