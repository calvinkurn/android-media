package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping

data class ServicesItemModel(
        var textsModel: TextsModel? = null,
        var serviceName: String? = null,
        var serviceId: Int? = null,
        var rangePriceModel: RangePriceModel? = null,
        var status: Int? = null,
        var productsItemModel: List<ProductsItemModel?>? = null
)