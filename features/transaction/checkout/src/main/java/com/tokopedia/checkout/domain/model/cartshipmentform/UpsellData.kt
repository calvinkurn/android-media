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

@Parcelize
data class NewUpsellData(
        val isShow: Boolean = false,
        val isSelected: Boolean = false,
        val description: String = "",
        val appLink: String = "",
        val image: String = "",
        val price: Long = 0,
        val duration: String = "",
        val wording: String = "",
        val buttonText: String = ""
): Parcelable