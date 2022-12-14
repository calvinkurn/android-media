package com.tokopedia.discovery2.discoverymapper

import com.tkpd.atcvariant.util.roundToIntOrZero
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.discovery2.*
import com.tokopedia.discovery2.Constant.MultipleShopMVCCarousel.CAROUSEL_ITEM_DESIGN
import com.tokopedia.discovery2.Constant.MultipleShopMVCCarousel.SINGLE_ITEM_DESIGN
import com.tokopedia.discovery2.Constant.ProductCardModel.PDP_VIEW_THRESHOLD
import com.tokopedia.discovery2.Constant.ProductCardModel.SOLD_PERCENTAGE_LOWER_LIMIT
import com.tokopedia.discovery2.Constant.ProductCardModel.SOLD_PERCENTAGE_UPPER_LIMIT
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.data.categorynavigationresponse.ChildItem
import com.tokopedia.discovery2.data.productcarditem.Badges
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleShopUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import kotlin.math.roundToInt

private const val CHIPS = "Chips"
private const val TABS_ITEM = "tabs_item"

class DiscoveryDataMapper {

    companion object {

        val discoveryDataMapper: DiscoveryDataMapper by lazy { DiscoveryDataMapper() }

        fun mapListToComponentList(itemList: List<DataItem>, subComponentName: String = "",
                                   parentComponentName: String?,
                                   position: Int, design: String = "", compId : String = "", properties: Properties? = null): ArrayList<ComponentsItem> {
            val list = ArrayList<ComponentsItem>()
            itemList.forEachIndexed { index, it ->
                val componentsItem = ComponentsItem()
                componentsItem.position = index
                val id = "${CHIPS}_$index"
                componentsItem.name = subComponentName
                componentsItem.id = id
                componentsItem.design = design
                componentsItem.parentComponentId = compId
                componentsItem.properties = properties
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
        parentListSize:Int = 0,
        parentSectionId:String? = "",
        parentComponentName: String? = null
    ): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        itemList?.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index + parentListSize
            componentsItem.name = subComponentName
            componentsItem.properties = properties
            componentsItem.creativeName = creativeName
            if(!parentComponentName.isNullOrEmpty()) {
                componentsItem.parentComponentName = parentComponentName
            }
            if(parentComponentPosition!=null){
                componentsItem.parentComponentPosition = parentComponentPosition
            }
            val dataItem = mutableListOf<DataItem>()
            it.typeProductCard = subComponentName
            it.creativeName = creativeName
            dataItem.add(it)
            componentsItem.data = dataItem
            if (parentSectionId?.isNotEmpty() == true)
                componentsItem.parentSectionId = parentSectionId
            list.add(componentsItem)
        }
        return list
    }

    fun mapListToBannerComponentList(
            itemList: List<DataItem>?,
            subComponentName: String = "",
            properties: Properties?,
            parentComponentPosition: Int? = null,
            parentListSize:Int = 0,
            parentSectionId:String? = "",
            parentComponentName: String? = null
    ): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        itemList?.forEachIndexed { index, it ->
            val componentsItem = ComponentsItem()
            componentsItem.position = index + parentListSize
            componentsItem.name = subComponentName
            componentsItem.properties = properties
            componentsItem.creativeName = it.creativeName
            if(!parentComponentName.isNullOrEmpty()) {
                componentsItem.parentComponentName = parentComponentName
            }
            if(parentComponentPosition!=null){
                componentsItem.parentComponentPosition = parentComponentPosition
            }
            val dataItem = mutableListOf<DataItem>()
            it.typeProductCard = subComponentName
            dataItem.add(it)
            componentsItem.data = dataItem
            if (parentSectionId?.isNotEmpty() == true)
                componentsItem.parentSectionId = parentSectionId
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

    fun mapAnchorListToComponentList(itemList: List<DataItem>, subComponentName: String = "",
                               parentComponentName: String?,
                               position: Int, design: String = "", compId : String = "",anchorMap: MutableMap<String,Int>): ArrayList<ComponentsItem> {
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
            componentsItem.parentComponentPosition = position
            componentsItem.parentListSize = itemList.size
            val dataItem = mutableListOf<DataItem>()
            dataItem.add(it)
            componentsItem.data = dataItem
            it.targetSectionID?.let { targetId ->
                anchorMap[targetId] = index
            }
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
        val labelGroupList : ArrayList<ProductCardModel.LabelGroup> = ArrayList()

        if (componentName == ComponentNames.ProductCardSprintSaleItem.componentName
                || componentName == ComponentNames.ProductCardSprintSaleCarouselItem.componentName
                || componentName == ComponentNames.ProductCardSprintSaleCarousel.componentName
                || componentName == ComponentNames.ProductCardSprintSale.componentName) {
            productName = dataItem.title ?: ""
            slashedPrice = setSlashPrice(dataItem.discountedPrice, dataItem.price)
            formattedPrice = setFormattedPrice(dataItem.discountedPrice, dataItem.price)
        } else {
            productName = dataItem.name ?: ""
            slashedPrice = setSlashPrice(dataItem.price, dataItem.discountedPrice)
            formattedPrice = setFormattedPrice(dataItem.price, dataItem.discountedPrice)
        }
        return ProductCardModel(
                productImageUrl = dataItem.imageUrlMobile ?: "",
                productName = productName,
                slashedPrice = slashedPrice,
                formattedPrice = formattedPrice,
                discountPercentage = if (!dataItem.discountPercentage.isNullOrEmpty() && dataItem.discountPercentage?.toIntOrZero() != 0) {
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
                isOutOfStock = (dataItem.isActiveProductCard == false),
                hasNotifyMeButton = if(dataItem.stockWording?.title?.isNotEmpty() == true)false else dataItem.hasNotifyMe,
                hasThreeDots = dataItem.hasThreeDots,
                hasButtonThreeDotsWishlist = dataItem.hasThreeDotsWishlist,
                hasAddToCartWishlist = dataItem.hasATCWishlist,
                hasSimilarProductWishlist = dataItem.hasSimilarProductWishlist == true,
                variant = variantProductCard(dataItem),
                nonVariant = nonVariantProductCard(dataItem),
                cardInteraction = true
        )
    }

    fun mapListToBundleProductList(dataItem: List<DataItem>): ArrayList<BundleUiModel> {
        val bundleModelList: ArrayList<BundleUiModel> = arrayListOf()
        dataItem.forEach { bundleData ->
            val bundleShopUiModel = BundleShopUiModel(
                shopId = bundleData.shopId ?: "",
                shopName = bundleData.shopName ?: "",
                shopIconUrl = bundleData.shopLogo ?: ""
            )
            val bundleDetailUiModelList: ArrayList<BundleDetailUiModel> = arrayListOf()
            val bundleProductUiModel: ArrayList<BundleProductUiModel> = arrayListOf()
            val bundleModel = BundleUiModel(
                bundleName = bundleData.bundleName ?: "",
                bundleType = if (bundleData.bundleType == "multiple_bundling") BundleTypes.MULTIPLE_BUNDLE else BundleTypes.SINGLE_BUNDLE,
                bundleDetails = bundleDetailUiModelList.apply {
                    bundleData.bundleDetails?.forEach { bundleDetails ->
                        add(BundleDetailUiModel(
                            bundleId = (bundleDetails?.bundleId ?: "").toString(),
                            originalPrice = bundleDetails?.originalPrice ?: "",
                            displayPrice = bundleDetails?.displayPrice ?: "",
                            displayPriceRaw = bundleDetails?.displayPriceRaw ?: 0,
                            discountPercentage = bundleDetails?.discountPercentage?.roundToIntOrZero()
                                ?: 0,
                            isPreOrder = bundleDetails?.preOrder ?: false,
                            preOrderInfo = bundleDetails?.preOrderInfo ?: "",
                            savingAmountWording = bundleDetails?.savingAmountWording ?: "",
                            minOrder = bundleDetails?.minOrder.toZeroIfNull(),
                            minOrderWording = bundleDetails?.minOrderWording ?: "",
                            isSelected = false,
                            totalSold = 0,
                            shopInfo = bundleShopUiModel,
                            bundleType = bundleData.bundleType ?: "",
                            products = bundleProductUiModel.apply {
                                bundleData.bundleProducts?.forEach { bundleProducts ->
                                    add(BundleProductUiModel(
                                        productId = (bundleProducts?.productId
                                            ?: "").toString(),
                                        productName = bundleProducts?.productName.toString(),
                                        productImageUrl = bundleProducts?.imageUrl
                                            ?: "",
                                        productAppLink = bundleProducts?.applink ?: "",
                                        hasVariant = bundleDetails?.isProductHaveVariant
                                            ?: false
                                    ))
                                }
                            }
                        ))
                    }
                }
            )
            bundleModelList.add(bundleModel)
        }
        return bundleModelList
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
