package com.tokopedia.discovery2.discoverymapper

import com.tkpd.atcvariant.util.roundToIntOrZero
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant.MultipleShopMVCCarousel.CAROUSEL_ITEM_DESIGN
import com.tokopedia.discovery2.Constant.MultipleShopMVCCarousel.SINGLE_ITEM_DESIGN
import com.tokopedia.discovery2.Constant.ProductCardModel.PDP_VIEW_THRESHOLD
import com.tokopedia.discovery2.Constant.ProductCardModel.PRODUCT_STOCK
import com.tokopedia.discovery2.Constant.ProductCardModel.SALE_PRODUCT_STOCK
import com.tokopedia.discovery2.Constant.ProductCardModel.SOLD_PERCENTAGE_LOWER_LIMIT
import com.tokopedia.discovery2.Constant.ProductCardModel.SOLD_PERCENTAGE_UPPER_LIMIT
import com.tokopedia.discovery2.LABEL_PRODUCT_STATUS
import com.tokopedia.discovery2.TRANSPARENT_BLACK
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.data.categorynavigationresponse.ChildItem
import com.tokopedia.discovery2.data.productcarditem.Badges
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.productcard.ProductCardModel
import kotlin.math.roundToInt

private const val CHIPS = "Chips"
private const val TABS_ITEM = "tabs_item"
private const val TERJUAL_HABIS = "Terjual Habis"

class DiscoveryDataMapper {

    companion object {

        val discoveryDataMapper: DiscoveryDataMapper by lazy { DiscoveryDataMapper() }

        fun mapListToComponentList(itemList: List<DataItem>, subComponentName: String = "",
                                   parentComponentName: String?,
                                   position: Int, design: String = "", compId : String = ""): ArrayList<ComponentsItem> {
            val list = ArrayList<ComponentsItem>()
            itemList.forEachIndexed { index, it ->
                val componentsItem = ComponentsItem()
                componentsItem.position = index
                val id = "${CHIPS}_$index"
                componentsItem.name = subComponentName
                componentsItem.id = id
                componentsItem.design = design
                componentsItem.parentComponentId = compId
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

    fun mapProductListToCircularModel(listItem: List<DataItem>) : ArrayList<CircularModel> {
        val bannerList = ArrayList<CircularModel>()
        listItem.forEachIndexed { index, it ->
            val circularModel = CircularModel(index, it.imageUrlDynamicMobile ?: "")
            bannerList.add(circularModel)
        }
        return bannerList
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

    fun mapDataItemToMerchantVoucherComponent(itemList: List<DataItem>?, subComponentName: String = "", properties: Properties?, creativeName: String? = ""): ArrayList<ComponentsItem>{
        val list = ArrayList<ComponentsItem>()
        itemList?.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index
            componentsItem.name = subComponentName
            componentsItem.properties = properties
            componentsItem.creativeName = creativeName
            val dataItem = mutableListOf<DataItem>()
            dataItem.add(it)
            componentsItem.data = dataItem
            componentsItem.design = if(itemList.size>1) CAROUSEL_ITEM_DESIGN else SINGLE_ITEM_DESIGN
            list.add(componentsItem)
        }
        return list
    }

    fun mapListToComponentList(
        itemList: List<DataItem>?,
        subComponentName: String = "",
        properties: Properties?,
        creativeName: String? = "",
        parentComponentPosition: Int? = null,
        parentListSize:Int = 0
    ): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        itemList?.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index + parentListSize
            componentsItem.name = subComponentName
            componentsItem.properties = properties
            componentsItem.creativeName = creativeName
            if(parentComponentPosition!=null){
                componentsItem.parentComponentPosition = parentComponentPosition
            }
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
        return DynamicFilterModel(data = DataValue(filter = filter as List<Filter>, sort = dataItem.sort as List<Sort>),defaultSortValue = "")
    }

    fun mapDataItemToProductCardModel(dataItem: DataItem, componentName: String?): ProductCardModel {
        val productName: String
        val slashedPrice: String
        val formattedPrice: String
        val isOutOfStock: Boolean
        val labelGroupList : ArrayList<ProductCardModel.LabelGroup> = ArrayList()

        if (componentName == ComponentNames.ProductCardSprintSaleItem.componentName
                || componentName == ComponentNames.ProductCardSprintSaleCarouselItem.componentName
                || componentName == ComponentNames.ProductCardSprintSaleCarousel.componentName
                || componentName == ComponentNames.ProductCardSprintSale.componentName) {
            productName = dataItem.title ?: ""
            slashedPrice = setSlashPrice(dataItem.discountedPrice, dataItem.price)
            formattedPrice = setFormattedPrice(dataItem.discountedPrice, dataItem.price)
            isOutOfStock = outOfStockLabelStatus(dataItem.stockSoldPercentage?.roundToIntOrZero().toString(), SALE_PRODUCT_STOCK)
            if(isOutOfStock) labelGroupList.add(ProductCardModel.LabelGroup(LABEL_PRODUCT_STATUS, TERJUAL_HABIS, TRANSPARENT_BLACK))
        } else {
            productName = dataItem.name ?: ""
            slashedPrice = setSlashPrice(dataItem.price, dataItem.discountedPrice)
            formattedPrice = setFormattedPrice(dataItem.price, dataItem.discountedPrice)
            isOutOfStock = outOfStockLabelStatus(dataItem.stock, PRODUCT_STOCK)
            if(isOutOfStock) labelGroupList.add(ProductCardModel.LabelGroup(LABEL_PRODUCT_STATUS, TERJUAL_HABIS, TRANSPARENT_BLACK))
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
                pdpViewCount = getPDPViewCount(dataItem.pdpView),
                labelGroupList = labelGroupList.apply {
                    dataItem.labelsGroupList?.forEach {
                        add(ProductCardModel.LabelGroup(it.position,
                                it.title,
                                it.type,
                                it.url))
                    }
                },
                shopLocation = getShopLocation(dataItem),
                shopBadgeList = getShopBadgeList(dataItem.badges),
                stockBarPercentage = setStockProgress(dataItem),
                stockBarLabel = dataItem.stockWording?.title ?: "",
                stockBarLabelColor = dataItem.stockWording?.color ?: "",
                isOutOfStock = isOutOfStock,
                hasNotifyMeButton = if(dataItem.stockWording?.title?.isNotEmpty() == true)false else dataItem.hasNotifyMe,
                hasThreeDots = dataItem.hasThreeDots,
                variant = variantProductCard(dataItem),
                nonVariant = nonVariantProductCard(dataItem)
        )
    }

    private fun nonVariantProductCard(dataItem: DataItem): ProductCardModel.NonVariant? {
        return if (!dataItem.hasATC || checkForVariantProductCard(dataItem.parentProductId)) {
            null
        } else {
            ProductCardModel.NonVariant(
                dataItem.quantity,
                dataItem.minQuantity,
                dataItem.maxQuantity
            )
        }
    }

    private fun variantProductCard(dataItem: DataItem): ProductCardModel.Variant? {
        return if (dataItem.hasATC && checkForVariantProductCard(dataItem.parentProductId)) {
            ProductCardModel.Variant(
                dataItem.quantity,
            )
        } else {
            null
        }
    }

    private fun checkForVariantProductCard(parentProductId: String?): Boolean {
        return parentProductId != null && parentProductId.toLongOrZero()>0
    }

    private fun setSlashPrice(discountedPrice: String?, price: String?): String {
        if(discountedPrice.isNullOrEmpty()){
            return ""
        }else if(discountedPrice == price){
            return ""
        }
        return price ?: ""
    }

    private fun setFormattedPrice(discountedPrice: String?, price: String?): String {
        if (discountedPrice.isNullOrEmpty()) {
            return price ?: ""
        }
        return discountedPrice ?: ""
    }

    private fun getPDPViewCount(pdpView: String): String {
        val pdpViewData = pdpView.toDoubleOrZero()
        return if (pdpViewData >= PDP_VIEW_THRESHOLD) {
            Utils.getCountView(pdpViewData)
        } else {
            ""
        }
    }

    private fun setStockProgress(dataItem: DataItem): Int {
        val stockSoldPercentage = dataItem.stockSoldPercentage
        if (stockSoldPercentage?.roundToInt() == null) {
            dataItem.stockWording?.title = ""
        } else {
            if (stockSoldPercentage.roundToIntOrZero() !in (SOLD_PERCENTAGE_LOWER_LIMIT) until SOLD_PERCENTAGE_UPPER_LIMIT) {
                dataItem.stockWording?.title = ""
            }
        }
        return stockSoldPercentage?.roundToIntOrZero() ?: 0
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

    private fun getShopBadgeList(showBadges: List<Badges?>?): List<ProductCardModel.ShopBadge> {
        return ArrayList<ProductCardModel.ShopBadge>().apply {
            showBadges?.firstOrNull()?.let {
                add(ProductCardModel.ShopBadge(isShown = true, imageUrl = it.image_url))
            }
        }
    }

    private fun getShopLocation(dataItem: DataItem): String {
        return if (!dataItem.shopLocation.isNullOrEmpty()) {
            dataItem.shopLocation!!
        } else if (!dataItem.shopName.isNullOrEmpty()) {
            dataItem.shopName!!
        } else {
            ""
        }
    }
}