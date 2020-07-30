package com.tokopedia.brandlist.common.listener

interface BrandlistSearchTrackingListener {

    fun impressionBrand(shopId: String, shoplogoPosition: String, shopName: String, imgUrl: String)
    fun clickBrandOnSearchBox(shopId: String)
    fun clickBrand(shopId: String, shoplogoPosition: String, shopName: String, imgUrl: String)

}