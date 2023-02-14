package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpharmacyData(
    var showImageUpload: Boolean = false,
    var uploadText: String = "",
    var leftIconUrl: String = "",
    var checkoutId: String = "",
    var frontEndValidation: Boolean = false,
    var consultationFlow: Boolean = false,
    var rejectedWording: String = ""
) : Parcelable
