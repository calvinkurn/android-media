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
        var stateChecked: Boolean = false
) : Parcelable