package com.tokopedia.brandlist.common.listener

interface BrandlistPageTrackingListener {

    fun clickBrandPopular(shopId: String, shopLogoPosition: String, shopName: String, imgUrl: String)
    fun clickBrandPilihan(shopId: String, shopName: String, imgUrl: String, shoplogoPosition: String)
    fun clickBrandBaruTokopedia(shopId: String, shopName: String, imgUrl: String, shoplogoPosition: String)
    fun clickBrand(shopId: String, shoplogoPosition: String, shopName: String, imgUrl: String)
    fun clickLihatSemua()

    fun impressionBrandPilihan(shopId: String, shoplogoPosition: String, imgUrl: String, shopName: String)
    fun impressionBrandPopular(shopId: String, shoplogoPosition: String, shopName: String, imgUrl: String)
    fun impressionBrandBaru(shopId: String, shoplogoPosition: String, shopName: String, imgUrl: String)
    fun impressionBrand(shopId: String, shoplogoPosition: String, shopName: String, imgUrl: String)

}