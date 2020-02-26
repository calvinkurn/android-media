package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping

data class ServicesItemModel(
        val textsModel: TextsModel? = null,
        val serviceName: String? = null,
        val serviceId: Int? = null,
        val rangePriceModel: RangePriceModel? = null,
        val status: Int? = null,
        val productsItemModel: List<ProductsItemModel?>? = null
)