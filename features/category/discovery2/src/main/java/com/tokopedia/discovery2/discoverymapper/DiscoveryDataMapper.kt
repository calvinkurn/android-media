package com.tokopedia.discovery2.discoverymapper

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.data.categorynavigationresponse.ChildItem
import com.tokopedia.discovery2.data.cpmtopads.BadgesItem
import com.tokopedia.discovery2.data.cpmtopads.Headline
import com.tokopedia.discovery2.data.cpmtopads.ImageProduct
import com.tokopedia.discovery2.data.cpmtopads.ProductItem

private const val CHIPS = "Chips"
private const val TABS_ITEM = "tabs_item"

class DiscoveryDataMapper {

    companion object {

        val discoveryDataMapper: DiscoveryDataMapper by lazy { DiscoveryDataMapper() }

        fun mapListToComponentList(itemList: List<DataItem>, subComponentName: String = "", parentComponentName: String?, position: Int, design : String = ""): ArrayList<ComponentsItem> {
            val list = ArrayList<ComponentsItem>()
            itemList.forEachIndexed { index, it ->
                val componentsItem = ComponentsItem()
                componentsItem.position = index
                val id = "${CHIPS}_$index"
                componentsItem.name = subComponentName
                componentsItem.id = id
                componentsItem.design = design
                it.parentComponentName = parentComponentName
                it.positionForParentItem = position
                val dataItem = mutableListOf<DataItem>()
                dataItem.add(it)
                componentsItem.data = dataItem
                list.add(componentsItem)
            }
            return list
        }

        fun mapTabsListToComponentList(component: ComponentsItem, subComponentName: String = "", position: Int): ArrayList<ComponentsItem> {
            val list = ArrayList<ComponentsItem>()
            var isSelectedFound = false
            component.data?.forEachIndexed { index, it ->
                val id = "${TABS_ITEM}_$index"
                if (it.isSelected) {
                    isSelectedFound = true
                }
                val componentsItem = ComponentsItem()
                componentsItem.position = index
                componentsItem.name = subComponentName
                componentsItem.pageEndPoint = component.pageEndPoint
                it.positionForParentItem = position
                val dataItem = mutableListOf<DataItem>()
                dataItem.add(it)
                componentsItem.data = dataItem
                componentsItem.id = id
                list.add(componentsItem)
            }
            if (!isSelectedFound) {
                list[0].data?.get(0)?.isSelected = true
            }
            return list
        }

        fun mapBannerComponentData(bannerComponent: ComponentsItem): ComponentsItem {
            return bannerComponent.apply {
                this.data?.forEach {
                    it.id = this.id
                }
            }
        }
    }

    fun mapDynamicCategoryListToComponentList(itemList: List<DataItem>, subComponentName: String = "", categoryHeaderName: String,
                                              categoryHeaderPosition: Int): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        itemList.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index
            componentsItem.name = subComponentName
            val dataItem = mutableListOf<DataItem>()
            it.title = categoryHeaderName
            it.positionForParentItem = categoryHeaderPosition
            dataItem.add(it)
            componentsItem.data = dataItem
            list.add(componentsItem)
        }
        return list
    }

    fun mapToCpmTopAdsData(headline: Headline, listComponentsItem: ArrayList<ComponentsItem>): CpmTopAdsData {
        val cpmTitleData = CpmTopAdsData()
        cpmTitleData.brandName = headline.name.toString()
        cpmTitleData.promotedText = headline.promotedText.toString()
        cpmTitleData.imageUrl = headline.badges?.getOrElse(0) { BadgesItem() }?.imageUrl.toString()
        cpmTitleData.componentList = listComponentsItem
        return cpmTitleData
    }

    fun addShopItemToProductList(item: com.tokopedia.discovery2.data.cpmtopads.DataItem): ArrayList<ProductItem?>? {
        val product = ProductItem()
        product.name = item.headline?.shop?.slogan
        product.buttonText = item.headline?.buttonText
        product.shopAdsClickUrl = item.adClickUrl
        product.shopAdsViewUrl = item.headline?.image?.fullUrl
        product.imageProduct = ImageProduct()
        product.imageProduct?.imageUrl = item.headline?.image?.fullEcs
        product.applinks = item.applinks
        item.headline?.shop?.product?.add(0, product)
        return item.headline?.shop?.product
    }

    fun mapProductListToComponentsList(listOfProduct: ArrayList<ProductItem?>?): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        listOfProduct?.forEachIndexed { index, element ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index
            componentsItem.name = getComponentName(index)
            val litDataItem = mutableListOf<DataItem>()
            val dataItem = DataItem()
            dataItem.name = element?.name
            dataItem.shopAdsClickURL = element?.shopAdsClickUrl
            dataItem.shopAdsViewURL = element?.shopAdsViewUrl
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

    fun mapListToComponentList(itemList: List<DataItem>?, subComponentName: String = "", properties: Properties?, typeProductCard: String = ""): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        itemList?.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index
            componentsItem.name = subComponentName
            componentsItem.properties = properties
            val dataItem = mutableListOf<DataItem>()
            it.typeProductCard = subComponentName
            dataItem.add(it)
            componentsItem.data = dataItem
            list.add(componentsItem)
        }
        return list
    }

    private fun getComponentName(index: Int): String {
        return if (index == 0)
            ComponentNames.CpmTopAdsShopItem.componentName
        else ComponentNames.CpmTopAdsProductItem.componentName
    }

    fun mapListToComponentList(child: List<ChildItem?>?): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        child?.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index
            componentsItem.name = ComponentNames.HorizontalCategoryNavigationIem.componentName
            val dataItemlist = mutableListOf<DataItem>()
            val dataItem = DataItem()
            dataItem.imageUrlMobile = it?.thumbnailImage
            dataItem.name = it?.name
            dataItem.id = it?.id
            dataItem.applinks = it?.applinks
            dataItemlist.add(dataItem)
            componentsItem.data = dataItemlist
            list.add(componentsItem)
        }
        return list
    }

    data class CpmTopAdsData(var promotedText: String = "",
                             var imageUrl: String = "",
                             var brandName: String = "",
                             var componentList: ArrayList<ComponentsItem> = ArrayList())
}