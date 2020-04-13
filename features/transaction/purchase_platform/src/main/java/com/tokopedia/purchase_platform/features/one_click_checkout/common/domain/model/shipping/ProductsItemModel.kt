package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping

class ProductsItemModel (
        var insuranceModel: InsuranceModel? = null,
        var shipperProductId: Int? = null,
        var recommend: Boolean? = null,
        var isShowMap: Int? = null,
        var priority: Int? = null,
        var featuresModel: FeaturesModel? = null,
        var checkSum: String? = null,
        var shipperProductName: String? = null,
        var etdModel: EtdModel? = null,
        var textsModel: TextsModel? = null,
        var priceModel: PriceModel? = null,
        var shipperProductDesc: String? = null,
        var shipperWeight: Int? = null,
        var shipperId: Int? = null,
        var shipperName: String? = null,
        var status: Int? = null,
        var ut: String? = null
)