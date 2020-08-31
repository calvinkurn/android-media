package com.tokopedia.rechargegeneral.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RechargeGeneralProductSelectData (
        val id: String = "",
        val title: String = "",
        val description: String = "",
        val price: String = "",
        val slashedPrice: String = "",
        val label: String = "",
        val isPromo: Boolean = false
): Parcelable