package com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PurchaseProtectionPlanData(
        var isProtectionAvailable: Boolean = false,
        var protectionTypeId: Int = 0,
        var protectionPricePerProduct: Int = 0,
        var protectionPrice: Int = 0,
        var protectionTitle: String = "",
        var protectionSubtitle: String = "",
        var protectionLinkText: String = "",
        var protectionLinkUrl: String = "",
        var isProtectionOptIn: Boolean = false,
        var isProtectionCheckboxDisabled: Boolean = false,
        var unit: String = "",
        var source: String = "",
        var stateChecked: Int = 0
) : Parcelable {

    companion object {
        const val STATE_EMPTY = 0
        const val STATE_UNTICKED = 1
        const val STATE_TICKED = 2

        const val SOURCE_READINESS = "readiness"
    }

}