package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping

data class OnTimeDeliveryGuaranteeModel (
        val textLabel: String? = null,
        val iconUrl: String? = null,
        val urlText: String? = null,
        val bomModel: BomModel? = null,
        val textDetail: String? = null,
        val urlDetail: String? = null,
        val available: Boolean? = null,
        val value: Int? = null
    )