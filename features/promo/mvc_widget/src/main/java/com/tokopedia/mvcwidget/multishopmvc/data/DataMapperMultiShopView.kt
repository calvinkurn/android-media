package com.tokopedia.mvcwidget.multishopmvc.data

object DataMapperMultiShopView {
    fun map(item: CatalogMVCWithProductsListItem?): MultiShopModel{
        return MultiShopModel(
            shopIcon = item?.shopInfo?.shopStatusIconURL?:"",
            shopName = item?.shopInfo?.name?:"",
            products = item?.products,
            cashBackTitle = item?.title?:"",
            cashBackValue = item?.maximumBenefitAmountStr?:"",
            couponCount = item?.subtitle?:"",
            id = item?.shopInfo?.id?:"",
            applink = item?.shopInfo?.appLink?:"",
            AdInfo = item?.AdInfo,
        )
    }
}