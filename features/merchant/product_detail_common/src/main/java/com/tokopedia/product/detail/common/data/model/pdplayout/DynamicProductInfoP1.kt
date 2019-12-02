package com.tokopedia.product.detail.common.data.model.pdplayout

data class DynamicProductInfoP1(
        val basic: BasicInfo = BasicInfo(),
        val data: ComponentData = ComponentData()
) {
    val shouldShowCod: Boolean
        get() = (!data.campaign.activeAndHasId) && data.isCOD

}