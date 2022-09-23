package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpsellData(
        val isShow: Boolean = false,
        val title: String = "",
        val description: String = "",
        val appLink: String = "",
        val image: String = ""
): Parcelable