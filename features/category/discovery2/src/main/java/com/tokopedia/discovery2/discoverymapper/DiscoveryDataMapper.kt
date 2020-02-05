package com.tokopedia.discovery2.discoverymapper

import com.tokopedia.design.component.badge.Badge
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.cpmtopads.*

class DiscoveryDataMapper {

    fun mapToCpmTopAdsData(headline: Headline, listComponentsItem: ArrayList<ComponentsItem>): CpmTopAdsData {
        val cpmTitleData = CpmTopAdsData()
        cpmTitleData.brandName = headline.name.toString()
        cpmTitleData.promotedText = headline.promotedText.toString()
        cpmTitleData.imageUrl = headline.badges?.getOrElse(0) { BadgesItem() }?.imageUrl.toString()
        cpmTitleData.componentList = listComponentsItem
        return cpmTitleData
    }

    fun addShopItemToProductList(item: DataItem): ArrayList<ProductItem?>? {
        val product = ProductItem()
        product.name = item.headline?.shop?.slogan
        product.buttonText = item.headline?.buttonText
        product.imageProduct = ImageProduct()
        product.imageProduct?.imageUrl = item.headline?.image?.fullEcs
        product.applinks = item.applinks
        item.headline?.shop?.product?.add(0, product)
        return item.headline?.shop?.product
    }

    fun mapProductListToComponentsList(listOfProduct: ArrayList<ProductItem?>?): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        listOfProduct?.forEachIndexed() { index, element ->
            val componentsItem = ComponentsItem()
            componentsItem.name = getComponentName(index)
            val litDataItem = mutableListOf<com.tokopedia.discovery2.data.DataItem>()
            val dataItem = com.tokopedia.discovery2.data.DataItem()
            dataItem.name = element?.name
            dataItem.imageUrlMobile = element?.imageProduct?.imageUrl
            dataItem.applinks = element?.applinks
            dataItem.buttonText = element?.buttonText
            dataItem.priceFormat = element?.priceFormat
            dataItem.imageClickUrl = element?.imageProduct?.imageClickUrl
            litDataItem.add(dataItem)
            componentsItem.data = litDataItem
            list.add(componentsItem)
        }
        return list
    }

    private fun getComponentName(index: Int): String {
       return if (index == 0)
            ComponentNames.CpmTopAdsShopItem.componentName
        else ComponentNames.CpmTopAdsProductItem.componentName
    }


    data class CpmTopAdsData(var promotedText: String = "",
                             var imageUrl: String = "",
                             var brandName: String = "",
                             var componentList: ArrayList<ComponentsItem> = ArrayList())


}