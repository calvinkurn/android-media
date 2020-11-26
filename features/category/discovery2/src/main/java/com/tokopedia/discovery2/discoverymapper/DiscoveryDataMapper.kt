package com.tokopedia.discovery2.discoverymapper

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant.BADGE_URL.OFFICIAL_STORE_URL
import com.tokopedia.discovery2.Constant.BADGE_URL.POWER_MERCHANT_URL
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.data.categorynavigationresponse.ChildItem
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.ProductCardModel

private const val CHIPS = "Chips"
private const val TABS_ITEM = "tabs_item"
private const val SALE_PRODUCT_STOCK = 100
private const val PRODUCT_STOCK = 0
private const val SOLD_PERCENTAGE_UPPER_LIMIT = 100
private const val SOLD_PERCENTAGE_LOWER_LIMIT = 0

class DiscoveryDataMapper {

    companion object {

        val discoveryDataMapper: DiscoveryDataMapper by lazy { DiscoveryDataMapper() }

        fun mapListToComponentList(itemList: List<DataItem>, subComponentName: String = "", parentComponentName: String?, position: Int, design: String = ""): ArrayList<ComponentsItem> {
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

        fun mapTabsListToComponentList(component: ComponentsItem, subComponentName: String = ""): ArrayList<ComponentsItem> {
            val list = ArrayList<ComponentsItem>()
            var isSelectedFound = false
            component.data?.forEachIndexed { index, it ->
                val id = "${TABS_ITEM}_$index"
                if (!it.name.isNullOrEmpty()) {
                    if (!component.pinnedActiveTabId.isNullOrEmpty()) {
                        var pinnedActiveIndex = component.pinnedActiveTabId.toIntOrZero()
                        if (pinnedActiveIndex.isMoreThanZero()) {
                            pinnedActiveIndex -= 1
                            if (index == pinnedActiveIndex) {
                                it.isSelected = true
                                isSelectedFound = true
                            }
                        }
                    } else if (it.isSelected) {
                        isSelectedFound = true
                    }
                    val componentsItem = ComponentsItem()
                    componentsItem.position = index
                    componentsItem.name = subComponentName
                    componentsItem.pageEndPoint = component.pageEndPoint
                    it.positionForParentItem = component.parentComponentPosition
                    val dataItem = mutableListOf<DataItem>()
                    dataItem.add(it)
                    componentsItem.data = dataItem
                    componentsItem.id = id
                    list.add(componentsItem)
                }
            }

            if (!isSelectedFound) {
                list.getOrNull(0)?.data?.getOrNull(0)?.isSelected = true
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

    fun mapListToComponentList(itemList: List<DataItem>?, subComponentName: String = "", properties: Properties?, creativeName: String? = ""): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        itemList?.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index
            componentsItem.name = subComponentName
            componentsItem.properties = properties
            componentsItem.creativeName = creativeName
            val dataItem = mutableListOf<DataItem>()
            it.typeProductCard = subComponentName
            it.creativeName = creativeName
            dataItem.add(it)
            componentsItem.data = dataItem
            list.add(componentsItem)
        }
        return list
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

    fun mapFiltersToDynamicFilterModel(dataItem: DataItem?): DynamicFilterModel? {
        val filter = dataItem?.filter
        filter?.forEach {
            if (it.options.isNullOrEmpty())
                filter.remove(it)
        }
        return DynamicFilterModel(data = DataValue(filter = filter as List<Filter>, sort = dataItem.sort as List<Sort>))
    }

    fun mapDataItemToProductCardModel(dataItem: DataItem, componentName: String?): ProductCardModel {
        val productName: String
        val slashedPrice: String
        val formattedPrice: String
        val isOutOfStock: Boolean

        if (componentName == ComponentNames.ProductCardSprintSaleItem.componentName || componentName == ComponentNames.ProductCardSprintSaleCarouselItem.componentName) {
            productName = dataItem.title ?: ""
            slashedPrice = dataItem.price ?: ""
            formattedPrice = dataItem.discountedPrice ?: ""
            isOutOfStock = outOfStockLabelStatus(dataItem.stockSoldPercentage, SALE_PRODUCT_STOCK)
        } else {
            productName = dataItem.name ?: ""
            slashedPrice = dataItem.discountedPrice ?: ""
            formattedPrice = dataItem.price ?: ""
            isOutOfStock = outOfStockLabelStatus(dataItem.stock, PRODUCT_STOCK)
        }
        return ProductCardModel(
                productImageUrl = dataItem.imageUrlMobile ?: "",
                productName = productName,
                slashedPrice = slashedPrice,
                formattedPrice = formattedPrice,
                discountPercentage = if (dataItem.discountPercentage?.toIntOrZero() != 0) {
                    "${dataItem.discountPercentage}%"
                } else {
                    ""
                },
                countSoldRating = dataItem.averageRating,
                isTopAds = dataItem.isTopads ?: false,
                freeOngkir = ProductCardModel.FreeOngkir(imageUrl = dataItem.freeOngkir?.freeOngkirImageUrl
                        ?: "", isActive = dataItem.freeOngkir?.isActive ?: false),
                pdpViewCount = dataItem.pdpView.takeIf { it.toIntOrZero() != 0 } ?: "",
                labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                    dataItem.labelsGroupList?.forEach { add(ProductCardModel.LabelGroup(it.position, it.title, it.type)) }
                },
                shopLocation = getShopLocation(dataItem),
                shopBadgeList = getShopBadgeList(dataItem),
                stockBarPercentage = setStockProgress(dataItem),
                stockBarLabel = dataItem.stockWording?.title ?: "",
                isOutOfStock = isOutOfStock,
                hasNotifyMeButton =  dataItem.hasNotifyMe
        )
    }

    private fun setStockProgress(dataItem: DataItem): Int {
        val stockSoldPercentage = dataItem.stockSoldPercentage
        if (stockSoldPercentage?.toIntOrNull() == null || stockSoldPercentage.isEmpty()) {
            dataItem.stockWording?.title = ""
        } else {
            if (stockSoldPercentage.toIntOrZero() !in (SOLD_PERCENTAGE_LOWER_LIMIT) until SOLD_PERCENTAGE_UPPER_LIMIT) {
                dataItem.stockWording?.title = ""
            }
        }
        return stockSoldPercentage.toIntOrZero()
    }

    private fun outOfStockLabelStatus(productStock: String?, saleStockValidation: Int = 0): Boolean {
        return when (saleStockValidation) {
            productStock?.toIntOrNull() -> {
                true
            }
            else -> {
                false
            }
        }
    }

    private fun getShopBadgeList(dataItem: DataItem): List<ProductCardModel.ShopBadge> {
        return ArrayList<ProductCardModel.ShopBadge>().apply {
            when {
                dataItem.goldMerchant == true && dataItem.officialStore == true ->
                    add(ProductCardModel.ShopBadge(isShown = true, imageUrl = OFFICIAL_STORE_URL))
                dataItem.goldMerchant == true ->
                    add(ProductCardModel.ShopBadge(isShown = true, imageUrl = POWER_MERCHANT_URL))
                dataItem.officialStore == true ->
                    add(ProductCardModel.ShopBadge(isShown = true, imageUrl = OFFICIAL_STORE_URL))
                else -> add(ProductCardModel.ShopBadge(isShown = false, imageUrl = ""))
            }
        }
    }

    private fun getShopLocation(dataItem: DataItem): String {
        return if (!dataItem.shopLocation.isNullOrEmpty()) {
            dataItem.shopLocation
        } else if (!dataItem.shopName.isNullOrEmpty()) {
            dataItem.shopName
        } else {
            ""
        }
    }
}