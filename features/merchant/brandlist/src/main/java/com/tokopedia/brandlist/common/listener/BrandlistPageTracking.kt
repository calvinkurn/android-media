package com.tokopedia.brandlist.common.listener

interface BrandlistPageTracking {

    fun clickBrandPopular(shopId: String, shopLogoPosition: String, shopName: String, imgUrl: String)
    fun clickBrandPilihan(shopId: String, shopName: String, imgUrl: String, shoplogoPosition: String)
    fun clickLihatSemua()

    fun clickSearchBox()
    fun clickBrandOnSearchBox()
    fun clickCategory()
    fun impressionBrandPilihan()
    fun impressionBrandPopular()
    fun clickBrandBaruTokopedia()
    fun impressionBrandBaru()
    fun clickBrand()
    fun impressionBrand()

}