package com.tokopedia.expresscheckout.domain.model.atc

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class PurchaseProtectionPlanDataModel(
        var protectionAvailable: Boolean? = null,
        var protectionTypeId: Int? = null,
        var protectionPricePerProduct: Int? = null,
        var protectionPrice: Int? = null,
        var protectionTitle: String? = null,
        var protectionSubtitle: String? = null,
        var protectionLinkText: String? = null,
        var protectionLinkUrl: String? = null,
        var protectionOptIn: Boolean? = null
)