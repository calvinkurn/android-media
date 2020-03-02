package com.tokopedia.product.detail.common.data.model.pdplayout

data class DynamicProductInfoP1(
        val basic: BasicInfo = BasicInfo(),
        val data: ComponentData = ComponentData()
) {

    val parentProductId: String
        get() =
            if (data.variant.isVariant && data.variant.parentID.isNotEmpty() && data.variant.parentID.toInt() > 0) {
                data.variant.parentID
            } else {
                basic.productID
            }

    val shouldShowCod: Boolean
        get() = (!data.campaign.activeAndHasId) && data.isCOD

    val getProductName: String
        get() = data.name

}