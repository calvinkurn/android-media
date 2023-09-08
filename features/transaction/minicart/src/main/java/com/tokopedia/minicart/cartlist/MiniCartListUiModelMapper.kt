package com.tokopedia.minicart.cartlist

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.data.model.request.ProductDetail
import com.tokopedia.atc_common.domain.model.response.ProductDataModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.minicart.cartlist.subpage.summarytransaction.MiniCartSummaryTransactionUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartAccordionUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductBundleRecomUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartSeparatorUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartShopUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerErrorUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerWarningUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableHeaderUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableReasonUiModel
import com.tokopedia.minicart.common.data.response.minicartlist.Action
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_DELETE
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_SHOWLESS
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_SHOWMORE
import com.tokopedia.minicart.common.data.response.minicartlist.BeliButtonConfig
import com.tokopedia.minicart.common.data.response.minicartlist.BundleDetail
import com.tokopedia.minicart.common.data.response.minicartlist.CartDetail
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.Product
import com.tokopedia.minicart.common.data.response.minicartlist.ShipmentInformation
import com.tokopedia.minicart.common.data.response.minicartlist.Shop
import com.tokopedia.minicart.common.data.tracker.ProductBundleRecomAtcItemTracker
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartItemType
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.domain.data.ProductBundleRecomResponse
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.min

class MiniCartListUiModelMapper @Inject constructor() {

    companion object {
        const val PLACEHOLDER_OVERWEIGHT_VALUE = "{{weight}}"

        private const val BUNDLE_NO_VARIANT_CONST = -1
    }

    fun mapUiModel(miniCartData: MiniCartData): MiniCartListUiModel {
        val totalProductAvailable = getTotalProductAvailable(miniCartData)
        val totalProductUnavailable = getTotalProductUnavailable(miniCartData)

        return MiniCartListUiModel().apply {
            title = miniCartData.data.headerTitle
            miniCartWidgetUiModel = mapMiniCartWidgetData(miniCartData, totalProductAvailable, totalProductUnavailable)
            miniCartSummaryTransactionUiModel = mapMiniCartSummaryTransactionUiModel(miniCartData, totalProductAvailable)
            val visitableListModel = mapVisitables(miniCartData, totalProductAvailable, totalProductUnavailable)
            visitables = visitableListModel.constructVisitableOrder
            availableProductIds = visitableListModel.availableProductIds
            availableBundleIds = visitableListModel.availableBundleIds

            if (miniCartData.data.availableSection.availableGroup.isNotEmpty()) {
                maximumShippingWeight = miniCartData.data.availableSection.availableGroup[0].shop.maximumShippingWeight
                maximumShippingWeightErrorMessage = miniCartData.data.availableSection.availableGroup[0].shop.maximumWeightWording
            }
        }
    }

    fun mapToProductBundleUiModel(
        widgetResponse: ProductBundleRecomResponse
    ) = MiniCartProductBundleRecomUiModel(
        title = widgetResponse.tokonowBundleWidget.data.widgetName,
        productBundleList = mapToProductBundleListItemUiModel(widgetResponse.tokonowBundleWidget.data.widgetData)
    )

    private fun mapToProductBundleListItemUiModel(
        widgetData: List<ProductBundleRecomResponse.TokonowBundleWidget.Data.WidgetData>
    ): List<ShopHomeProductBundleItemUiModel> {
        return widgetData.map {
            ShopHomeProductBundleItemUiModel().apply {
                warehouseId = it.warehouseID
                shopId = it.shopID
                bundleGroupId = it.bundleGroupID
                bundleType = it.bundleType
                bundleName = it.bundleName
                bundleDetails = it.bundleDetails.map { bundleDetail ->
                    ShopHomeProductBundleDetailUiModel().apply {
                        bundleId = bundleDetail.bundleID
                        originalPrice = bundleDetail.originalPrice
                        displayPrice = bundleDetail.displayPrice
                        displayPriceRaw = bundleDetail.displayPriceRaw
                        discountPercentage = bundleDetail.discountPercentage
                        isPreOrder = bundleDetail.isPO
                        isProductsHaveVariant = bundleDetail.isProductsHaveVariant
                        preOrderInfo = bundleDetail.preorderInfo
                        savingAmountWording = bundleDetail.savingAmountWording
                        minOrder = bundleDetail.minOrder
                        minOrderWording = bundleDetail.minOrderWording
                    }
                }
                bundleProducts = it.bundleProducts.map { bundleProduct ->
                    ShopHomeBundleProductUiModel().apply {
                        productId = bundleProduct.productID
                        productName = bundleProduct.productName
                        productImageUrl = bundleProduct.imageUrl
                        productAppLink = bundleProduct.appLink
                        minOrder = bundleProduct.minOrder
                    }
                }
            }
        }
    }

    fun mapToProductBundlRecomAtcItemTracker(
        productList: List<ProductDataModel>,
        productDetails: List<ShopHomeBundleProductUiModel>
    ): List<ProductBundleRecomAtcItemTracker> {
        return productList.map { product ->
            ProductBundleRecomAtcItemTracker(
                id = product.productId,
                name = productDetails.firstOrNull { it.productId == product.productId }?.productName.orEmpty(),
                cartId = product.cartId,
                quantity = product.quantity
            )
        }
    }

    fun mapToAddToCartBundleProductDetailParam(productDetails: List<ShopHomeBundleProductUiModel>, quantity: Int?, shopId: String, userId: String): List<ProductDetail> {
        return productDetails.map {
            ProductDetail(
                productId = it.productId,
                quantity = quantity ?: it.minOrder,
                shopId = shopId,
                customerId = userId
            )
        }
    }

    private fun getTotalProductAvailable(miniCartData: MiniCartData): Int {
        var count = 0
        miniCartData.data.availableSection.availableGroup.forEach { availableGroup ->
            availableGroup.cartDetails.forEach {
                count += it.products.size
            }
        }

        return count
    }

    private fun getTotalProductUnavailable(miniCartData: MiniCartData): Int {
        var count = 0
        miniCartData.data.unavailableSection.forEach { unavailableSection ->
            unavailableSection.unavailableGroup.forEach { unavailableGroup ->
                unavailableGroup.cartDetails.forEach {
                    count += it.products.size
                }
            }
        }

        return count
    }

    private fun mapMiniCartSummaryTransactionUiModel(miniCartData: MiniCartData, totalProductAvailable: Int): MiniCartSummaryTransactionUiModel {
        return MiniCartSummaryTransactionUiModel().apply {
            qty = totalProductAvailable
            totalWording = miniCartData.data.shoppingSummary.totalWording
            totalValue = miniCartData.data.shoppingSummary.totalValue
            discountTotalWording = miniCartData.data.shoppingSummary.discountTotalWording
            discountValue = miniCartData.data.shoppingSummary.discountValue
            paymentTotalWording = miniCartData.data.shoppingSummary.paymentTotalWording
            paymentTotal = miniCartData.data.shoppingSummary.paymentTotalValue
            sellerCashbackWording = miniCartData.data.shoppingSummary.sellerCashbackWording
            sellerCashbackValue = miniCartData.data.shoppingSummary.sellerCashbackValue
        }
    }

    private fun mapMiniCartWidgetData(miniCartData: MiniCartData, totalProductAvailable: Int, totalProductUnavailable: Int): MiniCartWidgetData {
        return MiniCartWidgetData().apply {
            totalProductCount = totalProductAvailable
            totalProductPrice = miniCartData.data.totalProductPrice
            totalProductError = totalProductUnavailable
            isOCCFlow = miniCartData.data.beliButtonConfig.buttonType == BeliButtonConfig.BUTTON_TYPE_OCC
            buttonBuyWording = miniCartData.data.beliButtonConfig.buttonWording
        }
    }

    private fun mapVisitables(miniCartData: MiniCartData, totalProductAvailable: Int, totalProductUnavailable: Int): VisitableListModel {
        var miniCartTickerErrorUiModel: MiniCartTickerErrorUiModel? = null
        val miniCartTickerWarningUiModel: MiniCartTickerWarningUiModel? = null
        var miniCartShopUiModel: MiniCartShopUiModel? = null
        val miniCartAvailableSectionUiModels: MutableList<MiniCartProductUiModel> = mutableListOf()
        val miniCartUnavailableSectionUiModels: MutableList<Visitable<*>> = mutableListOf()
        val productIds: ArrayList<String> = arrayListOf()
        val bundleIds: ArrayList<String> = arrayListOf()
        // Add error ticker
        if (totalProductUnavailable > 0 && totalProductAvailable > 0) {
            miniCartTickerErrorUiModel = mapTickerErrorUiModel(totalProductUnavailable, totalProductAvailable)
        }

        var weightTotal = 0
        miniCartData.data.availableSection.let { availableSection ->
            val groupCount = availableSection.availableGroup.count()
            availableSection.availableGroup.forEachIndexed { groupIndex, availableGroup ->
                // Add shop
                miniCartShopUiModel = mapShopUiModel(availableGroup.shop, availableGroup.shipmentInformation)

                // Add available product
                val miniCartProductUiModels = mutableListOf<MiniCartProductUiModel>()
                val cartItemsCount = availableGroup.cartDetails.count()
                val lastGroupItem = groupIndex == groupCount - 1
                availableGroup.cartDetails.forEachIndexed { cartIndex, cartDetail ->
                    val lastCartItem = cartIndex == cartItemsCount - 1
                    cartDetail.products.forEachIndexed { productIndex, product ->
                        weightTotal += product.productWeight * product.productQuantity
                        val miniCartProductUiModel = mapProductUiModel(
                            productIndex = productIndex,
                            cartDetail = cartDetail,
                            shop = availableGroup.shop,
                            product = product,
                            cartStringId = availableGroup.cartString,
                            shipmentInformation = availableGroup.shipmentInformation,
                            action = availableSection.action,
                            notesLength = miniCartData.data.maxCharNote,
                            placeholderNote = miniCartData.data.placeholderNote,
                            lastCartItem = lastCartItem,
                            lastGroupItem = lastGroupItem
                        )
                        miniCartProductUiModels.add(miniCartProductUiModel)
                        if (miniCartProductUiModel.isBundlingItem) {
                            bundleIds.add(miniCartProductUiModel.bundleId)
                        } else {
                            productIds.add(miniCartProductUiModel.productId)
                        }
                    }
                }
                miniCartAvailableSectionUiModels.addAll(miniCartProductUiModels)
            }
        }

        // Add unavailable separator
        if (totalProductUnavailable > 0 && totalProductAvailable > 0) {
            val miniCartSeparatorUiModel = mapSeparatorUiModel(MiniCartSeparatorUiModel.DEFAULT_SEPARATOR_HEIGHT)
            miniCartUnavailableSectionUiModels.add(miniCartSeparatorUiModel)
        }

        // Add unavailable header
        if (totalProductUnavailable > 0) {
            val miniCartUnavailableHeaderUiModel = mapUnavailableHeaderUiModel(totalProductUnavailable)
            miniCartUnavailableSectionUiModels.add(miniCartUnavailableHeaderUiModel)
        }

        miniCartData.data.unavailableSection.forEach { unavailableSection ->
            // Add unavailable reason
            val groupCount = unavailableSection.unavailableGroup.count()
            val unavailableReasonUiModel = mapUnavailableReasonUiModel(unavailableSection.title, unavailableSection.unavailableDescription)
            miniCartUnavailableSectionUiModels.add(unavailableReasonUiModel)
            unavailableSection.unavailableGroup.forEachIndexed { groupIndex, unavailableGroup ->
                // Add unavailable product
                val miniCartProductUiModels = mutableListOf<MiniCartProductUiModel>()
                val cartItemsCount = unavailableGroup.cartDetails.count()
                val lastGroupItem = groupIndex == groupCount - 1
                unavailableGroup.cartDetails.forEachIndexed { cartIndex, cartDetail ->
                    val lastCartItem = cartIndex == cartItemsCount - 1
                    cartDetail.products.forEachIndexed { productIndex, product ->
                        val miniCartProductUiModel = mapProductUiModel(
                            productIndex = productIndex,
                            cartDetail = cartDetail,
                            shop = unavailableGroup.shop,
                            product = product,
                            cartStringId = unavailableGroup.cartString,
                            shipmentInformation = unavailableGroup.shipmentInformation,
                            action = unavailableSection.action,
                            isDisabled = true,
                            unavailableActionId = unavailableSection.selectedUnavailableActionId,
                            unavailableReason = unavailableSection.title,
                            lastCartItem = lastCartItem,
                            lastGroupItem = lastGroupItem
                        )
                        miniCartProductUiModels.add(miniCartProductUiModel)
                    }
                }
                miniCartUnavailableSectionUiModels.addAll(miniCartProductUiModels)
            }
        }

        // Add unavailable accordion
        if (totalProductUnavailable > 1) {
            // Add unavailable accordion separator
            val miniCartSeparatorUiModel = mapSeparatorUiModel(MiniCartSeparatorUiModel.DEFAULT_SEPARATOR_HEIGHT)
            miniCartUnavailableSectionUiModels.add(miniCartSeparatorUiModel)

            val showLessUnavailableDataWording = miniCartData.data.unavailableSectionAction.find {
                return@find it.id == ACTION_SHOWLESS
            }?.message ?: ""
            val showMoreUnavailableDataWording = miniCartData.data.unavailableSectionAction.find {
                return@find it.id == ACTION_SHOWMORE
            }?.message ?: ""

            val miniCartAccordionUiModel = mapAccordionUiModel(showLessUnavailableDataWording, showMoreUnavailableDataWording)
            miniCartUnavailableSectionUiModels.add(miniCartAccordionUiModel)
        }

        return VisitableListModel(
            availableProductIds = productIds,
            availableBundleIds = bundleIds,
            constructVisitableOrder = constructVisitableOrder(
                miniCartTickerErrorUiModel,
                miniCartTickerWarningUiModel,
                miniCartShopUiModel,
                miniCartAvailableSectionUiModels,
                miniCartUnavailableSectionUiModels
            )
        )
    }

    private fun constructVisitableOrder(miniCartTickerErrorUiModel: MiniCartTickerErrorUiModel?, miniCartTickerWarningUiModel: MiniCartTickerWarningUiModel?, miniCartShopUiModel: MiniCartShopUiModel?, miniCartAvailableSectionUiModels: MutableList<MiniCartProductUiModel>, miniCartUnavailableSectionUiModels: MutableList<Visitable<*>>): MutableList<Visitable<*>> {
        val visitables = mutableListOf<Visitable<*>>()

        miniCartTickerErrorUiModel?.let {
            visitables.add(miniCartTickerErrorUiModel)
        }
        miniCartTickerWarningUiModel?.let {
            visitables.add(miniCartTickerWarningUiModel)
        }
        miniCartShopUiModel?.let {
            visitables.add(it)
        }
        visitables.addAll(miniCartAvailableSectionUiModels)
        visitables.addAll(miniCartUnavailableSectionUiModels)

        return visitables
    }

    private fun mapAccordionUiModel(wordingShowLess: String, wordingShowMore: String): MiniCartAccordionUiModel {
        return MiniCartAccordionUiModel().apply {
            isCollapsed = false
            showLessWording = wordingShowLess
            showMoreWording = wordingShowMore
        }
    }

    private fun mapProductUiModel(
        productIndex: Int,
        cartDetail: CartDetail,
        shop: Shop,
        product: Product,
        shipmentInformation: ShipmentInformation,
        action: List<Action>,
        isDisabled: Boolean = false,
        unavailableActionId: String = "",
        unavailableReason: String = "",
        notesLength: Int = 0,
        placeholderNote: String = "",
        lastCartItem: Boolean = false,
        lastGroupItem: Boolean = false,
        cartStringId: String = ""
    ): MiniCartProductUiModel {
        return MiniCartProductUiModel().apply {
            val products = cartDetail.products
            val productsCount = products.count()
            val bundleDetail = cartDetail.bundleDetail
            val bundlingItem = bundleDetail.isBundlingItem()

            val productQuantity = product.productQuantity
            val bundleQuantity = bundleDetail.bundleQty

            val firstProductItem = productIndex == 0
            val lastProductItem = productIndex == productsCount - 1
            val actionList = removeBundleDeleteAction(bundleDetail, action, lastProductItem)

            cartId = product.cartId
            productId = product.productId
            parentId = product.parentId
            cartString = cartStringId
            productImageUrl = product.productImage.imageSrc100Square
            productName = product.productName
            productVariantName = product.variantDescriptionDetail.variantName.joinToString(", ")
            productSlashPriceLabel = product.slashPriceLabel
            productOriginalPrice = product.productOriginalPrice
            productWholeSalePrice = 0.0
            productInitialPriceBeforeDrop = product.initialPrice
            productPrice = product.productPrice
            productInformation = product.productInformation
            productNotes = product.productNotes
            productQty = if (product.productSwitchInvenage == 0) {
                productQuantity
            } else {
                min(productQuantity, product.productInvenageValue)
            }
            productWeight = product.productWeight
            productMinOrder = product.productMinOrder
            productMaxOrder = if (product.productSwitchInvenage == 0) {
                product.productMaxOrder
            } else {
                min(product.productMaxOrder, product.productInvenageValue)
            }
            productActions = actionList
            wholesalePriceGroup = product.wholesalePrice.asReversed()
            isProductDisabled = isDisabled
            maxNotesLength = notesLength
            this.placeholderNote = placeholderNote
            campaignId = product.campaignId
            attribution = product.productTrackerData.attribution
            warehouseId = product.warehouseId
            categoryId = product.categoryId
            category = product.category
            shopId = shop.shopId
            shopName = shop.shopName
            shopType = shop.shopTypeInfo.titleFmt
            freeShippingType = product.freeShippingGeneral.boName
            errorType = unavailableReason
            if (isDisabled) {
                selectedUnavailableActionId = unavailableActionId
                selectedUnavailableActionLink = product.selectedUnavailableActionLink
            } else {
                productQtyLeft = product.productWarningMessage
            }
            productCashbackPercentage = product.productCashback
                .replace(" ", "")
                .replace("%", "")
                .toIntOrZero()
            bundleId = bundleDetail.bundleId
            bundleGroupId = bundleDetail.bundleGroupId
            bundleName = bundleDetail.bundleName
            bundlePrice = bundleDetail.bundlePrice
            bundlePriceFmt = bundleDetail.bundlePriceFmt
            bundleOriginalPrice = bundleDetail.bundleOriginalPrice
            bundleOriginalPriceFmt = bundleDetail.bundleOriginalPriceFmt
            bundleMinOrder = bundleDetail.bundleMinOrder
            bundleMaxOrder = if (bundleDetail.bundleQuota > BUNDLE_NO_VARIANT_CONST) {
                min(bundleDetail.bundleMaxOrder, bundleDetail.bundleQuota)
            } else {
                bundleDetail.bundleMaxOrder
            }
            bundleQty = bundleQuantity
            bundleIconUrl = bundleDetail.bundleIconUrl
            slashPriceLabel = bundleDetail.slashPriceLabel
            showBundlingHeader = firstProductItem && bundlingItem
            showBottomDivider = (bundlingItem && !lastCartItem && lastProductItem) ||
                (!bundlingItem && !lastCartItem || !lastProductItem) || !lastGroupItem
            isBundlingItem = bundlingItem
            isLastProductItem = lastProductItem
            editBundleApplink = cartDetail.bundleDetail.editBundleApplink

            if (bundlingItem) {
                bundleMultiplier = productQuantity / bundleQuantity
                bundleLabelQty = productQuantity / bundleQuantity
            }
        }
    }

    private fun removeBundleDeleteAction(
        bundleDetail: BundleDetail,
        action: List<Action>,
        isLastItem: Boolean
    ): List<Action> {
        val actionList = action.toMutableList()
        val isBundlingItem = bundleDetail.isBundlingItem()

        if (isBundlingItem && !isLastItem) {
            actionList.find { it.id == ACTION_DELETE }?.let {
                actionList.remove(it)
            }
        }

        return actionList
    }

    private fun mapSeparatorUiModel(separatorHeight: Int): MiniCartSeparatorUiModel {
        return MiniCartSeparatorUiModel().apply {
            height = separatorHeight
        }
    }

    private fun mapShopUiModel(shop: Shop, shipmentInformation: ShipmentInformation): MiniCartShopUiModel {
        return MiniCartShopUiModel().apply {
            shopId = shop.shopId
            shopBadgeUrl = shop.shopTypeInfo.badge
            shopLocation = shipmentInformation.shopLocation
            estimatedTimeArrival = shipmentInformation.estimation
        }
    }

    private fun mapTickerErrorUiModel(totalProductUnavailable: Int, totalProductAvailable: Int): MiniCartTickerErrorUiModel {
        return MiniCartTickerErrorUiModel().apply {
            unavailableItemCount = totalProductUnavailable
            isShowErrorActionLabel = totalProductAvailable > 1
        }
    }

    fun mapTickerWarningUiModel(overWeight: Float, warningWording: String): MiniCartTickerWarningUiModel {
        return MiniCartTickerWarningUiModel().apply {
            val formattedOverWeight = NumberFormat.getNumberInstance(Locale("in", "id")).format(overWeight)
            warningMessage = warningWording.replace(PLACEHOLDER_OVERWEIGHT_VALUE, "$formattedOverWeight ")
        }
    }

    private fun mapUnavailableHeaderUiModel(totalProductUnavailable: Int): MiniCartUnavailableHeaderUiModel {
        return MiniCartUnavailableHeaderUiModel().apply {
            unavailableItemCount = totalProductUnavailable
        }
    }

    private fun mapUnavailableReasonUiModel(title: String, unavailableDescription: String): MiniCartUnavailableReasonUiModel {
        return MiniCartUnavailableReasonUiModel().apply {
            reason = title
            description = unavailableDescription
        }
    }

    fun reverseMapUiModel(miniCartListUiModel: MiniCartListUiModel?, tmpHiddenUnavailableItems: List<Visitable<*>>?): MiniCartSimplifiedData {
        return if (miniCartListUiModel == null) {
            MiniCartSimplifiedData()
        } else {
            MiniCartSimplifiedData().apply {
                val miniCartItemsMapResult = mapMiniCartItems(miniCartListUiModel.visitables, tmpHiddenUnavailableItems)
                miniCartItems = miniCartItemsMapResult.first
                isShowMiniCartWidget = miniCartItems.isNotEmpty()
                miniCartWidgetData = miniCartListUiModel.miniCartWidgetUiModel
                miniCartWidgetData.containsOnlyUnavailableItems = miniCartItemsMapResult.second
                miniCartWidgetData.unavailableItemsCount = miniCartItemsMapResult.third
            }
        }
    }

    private fun mapMiniCartItems(visitables: List<Visitable<*>>, tmpHiddenUnavailableItems: List<Visitable<*>>?): Triple<Map<MiniCartItemKey, MiniCartItem>, Boolean, Int> {
        val tmpVisitables = mutableListOf<Visitable<*>>()
        tmpVisitables.addAll(visitables)
        if (tmpHiddenUnavailableItems != null) {
            tmpVisitables.addAll(tmpHiddenUnavailableItems)
        }
        var hasAvailableItem = false
        var unavailableItemCount = 0
        val miniCartItems = hashMapOf<MiniCartItemKey, MiniCartItem>()
        tmpVisitables.forEach { visitable ->
            if (visitable is MiniCartProductUiModel) {
                val key = MiniCartItemKey(visitable.productId)
                val miniCartItem = MiniCartItem.MiniCartItemProduct().apply {
                    isError = visitable.isProductDisabled
                    cartId = visitable.cartId
                    productId = visitable.productId
                    productParentId = visitable.parentId
                    quantity = visitable.productQty
                    notes = visitable.productNotes
                    cartString = visitable.cartString
                    campaignId = visitable.campaignId
                    attribution = visitable.attribution
                    productWeight = visitable.productWeight
                    productSlashPriceLabel = visitable.productSlashPriceLabel
                    warehouseId = visitable.warehouseId
                    shopId = visitable.shopId
                    shopName = visitable.shopName
                    shopType = visitable.shopType
                    categoryId = visitable.categoryId
                    freeShippingType = visitable.freeShippingType
                    category = visitable.category
                    productName = visitable.productName
                    productVariantName = visitable.productVariantName
                    productPrice = visitable.productPrice
                }

                if (visitable.isBundlingItem) {
                    val bundleKey = MiniCartItemKey(visitable.bundleGroupId, type = MiniCartItemType.BUNDLE)
                    if (!miniCartItems.contains(bundleKey)) {
                        if (miniCartItem.isError) {
                            unavailableItemCount++
                        }
                        miniCartItems[bundleKey] = MiniCartItem.MiniCartItemBundleGroup(
                            isError = miniCartItem.isError,
                            bundleId = visitable.bundleId,
                            bundleGroupId = visitable.bundleGroupId,
                            bundleTitle = visitable.bundleName,
                            bundlePrice = visitable.bundlePrice,
                            bundleSlashPriceLabel = visitable.slashPriceLabel,
                            bundleOriginalPrice = visitable.bundleOriginalPrice,
                            bundleQuantity = visitable.bundleQty,
                            bundleMultiplier = visitable.bundleMultiplier,
                            bundleLabelQuantity = visitable.bundleLabelQty,
                            products = hashMapOf(key to miniCartItem)
                        )
                    } else {
                        val currentBundleItem = miniCartItems[bundleKey] as MiniCartItem.MiniCartItemBundleGroup
                        val products = HashMap(currentBundleItem.products)
                        products[key] = miniCartItem
                        miniCartItems[bundleKey] = currentBundleItem.copy(products = products)
                    }
                } else if (miniCartItem.isError) {
                    unavailableItemCount++
                    if (!miniCartItems.contains(key)) {
                        miniCartItems[key] = miniCartItem
                    }
                } else {
                    miniCartItems[key] = miniCartItem
                }

                if (miniCartItem.productParentId.isNotBlankOrZero()) {
                    val parentKey = MiniCartItemKey(miniCartItem.productParentId, type = MiniCartItemType.PARENT)
                    if (!miniCartItems.contains(parentKey)) {
                        miniCartItems[parentKey] = MiniCartItem.MiniCartItemParentProduct(
                            parentId = miniCartItem.productParentId,
                            totalQuantity = miniCartItem.quantity,
                            products = hashMapOf(key to miniCartItem)
                        )
                    } else {
                        val currentParentItem = miniCartItems[parentKey] as MiniCartItem.MiniCartItemParentProduct
                        val products = HashMap(currentParentItem.products)
                        products[key] = miniCartItem
                        val totalQuantity = currentParentItem.totalQuantity + miniCartItem.quantity
                        miniCartItems[parentKey] = MiniCartItem.MiniCartItemParentProduct(
                            miniCartItem.productParentId,
                            totalQuantity,
                            products
                        )
                    }
                }

                if (!hasAvailableItem && !miniCartItem.isError) {
                    hasAvailableItem = true
                }
            }
        }

        val isShowMiniCartWidget = miniCartItems.isNotEmpty()
        val containsOnlyUnavailableItems = isShowMiniCartWidget && !hasAvailableItem
        return Triple(miniCartItems, containsOnlyUnavailableItems, unavailableItemCount)
    }

    class VisitableListModel(
        val availableProductIds: ArrayList<String>,
        val availableBundleIds: ArrayList<String>,
        val constructVisitableOrder: MutableList<Visitable<*>>
    )
}
