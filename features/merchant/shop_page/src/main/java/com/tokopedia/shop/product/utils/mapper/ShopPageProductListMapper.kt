package com.tokopedia.shop.product.utils.mapper

import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.data.model.Actions
import com.tokopedia.shop.common.data.model.RestrictionEngineModel
import com.tokopedia.shop.common.data.response.RestrictionEngineDataResponse
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.common.data.viewmodel.BaseMembershipViewModel
import com.tokopedia.shop.common.data.viewmodel.ItemRegisteredViewModel
import com.tokopedia.shop.common.data.viewmodel.ItemUnregisteredViewModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.view.datamodel.LabelGroupUiModel
import com.tokopedia.shop.product.view.datamodel.ShopEtalaseItemDataModel
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel.Companion.THRESHOLD_VIEW_COUNT
import java.text.NumberFormat

object ShopPageProductListMapper {

    private const val POSTFIX_VIEW_COUNT = "%1s orang"
    private const val PRODUCT_RATING_DIVIDER = 20
    private const val ZERO_PRODUCT_DISCOUNT = "0"

    fun mapToShopProductEtalaseListDataModel(
        listShopEtalaseModel: List<ShopEtalaseModel>
    ): List<ShopEtalaseItemDataModel> {
        return listShopEtalaseModel.map { mapToShopEtalaseViewModel(it) }
    }

    private fun mapToShopEtalaseViewModel(shopEtalaseModel: ShopEtalaseModel): ShopEtalaseItemDataModel {
        val id = if (shopEtalaseModel.type == ShopEtalaseTypeDef.ETALASE_DEFAULT) shopEtalaseModel.alias else shopEtalaseModel.id
        return ShopEtalaseItemDataModel(
            id,
            shopEtalaseModel.alias,
            shopEtalaseModel.name,
            shopEtalaseModel.type,
            shopEtalaseModel.badge,
            shopEtalaseModel.count.toLong(),
            shopEtalaseModel.highlighted,
            shopEtalaseModel.rules
        )
    }

    fun mapShopProductToProductViewModel(
        shopProduct: ShopProduct,
        isMyOwnProduct: Boolean,
        etalaseId: String,
        etalaseType: Int? = null,
        isEnableDirectPurchase: Boolean
    ): ShopProductUiModel =
        with(shopProduct) {
            ShopProductUiModel().also {
                it.id = productId
                it.name = name
                it.displayedPrice = price.textIdr
                it.originalPrice = campaign.originalPriceFmt
                it.discountPercentage = campaign.discountedPercentage
                it.imageUrl = primaryImage.original
                it.imageUrl300 = primaryImage.resize300
                it.totalReview = stats.reviewCount.toString()
                it.rating = stats.rating.toDouble() / PRODUCT_RATING_DIVIDER
                if (cashback.cashbackPercent > 0) {
                    it.cashback = cashback.cashbackPercent.toDouble()
                }
                it.isWholesale = flags.isWholesale
                it.isPo = flags.isPreorder
                it.isFreeReturn = flags.isFreereturn
                it.isWishList = flags.isWishlist
                it.productUrl = appLink
                it.isSoldOut = flags.isSold
                it.isShowWishList = !isMyOwnProduct
                it.isShowFreeOngkir = freeOngkir.isActive
                it.freeOngkirPromoIcon = freeOngkir.imgUrl
                it.etalaseId = etalaseId
                it.labelGroupList = labelGroupList.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
                it.etalaseType = etalaseType
                it.stock = stock.toLong()
                it.maximumOrder = getMaximumOrder(shopProduct)
                it.isEnableDirectPurchase = isEnableDirectPurchase
                it.isFulfillment = shopProduct.labelGroupList.any { it.position == ShopPageTrackingConstant.LABEL_GROUP_POSITION_FULFILLMENT }
                it.warehouseId = shopProduct.warehouseId
                when (it.etalaseType) {
                    ShopEtalaseTypeDef.ETALASE_CAMPAIGN -> {
                        it.isUpcoming = campaign.isUpcoming
                        if (!campaign.isUpcoming) {
                            val viewCount = stats.viewCount
                            if (viewCount >= THRESHOLD_VIEW_COUNT) {
                                it.pdpViewCount = String.format(POSTFIX_VIEW_COUNT, viewCount.thousandFormatted())
                            }
                            it.stockLabel = labelGroupList.firstOrNull()?.title.takeIf { showStockBar }.orEmpty()
                            it.stockBarPercentage = campaign.stockSoldPercentage.toInt()
                        }
                        it.hideGimmick = campaign.hideGimmick
                        it.displayedPrice = campaign.discountedPriceFmt.toFloatOrNull()?.getCurrencyFormatted() ?: price.textIdr
                        it.originalPrice = campaign.originalPriceFmt.toFloatOrZero().getCurrencyFormatted()
                        setStockAndSoldOutForCampaignEtalase(it, shopProduct)
                    }
                    ShopEtalaseTypeDef.ETALASE_FLASH_SALE -> {
                        it.isUpcoming = campaign.isUpcoming
                        it.hideGimmick = campaign.hideGimmick
                        if (!campaign.isUpcoming) {
                            val viewCount = stats.viewCount
                            if (viewCount >= THRESHOLD_VIEW_COUNT) {
                                it.pdpViewCount = String.format(POSTFIX_VIEW_COUNT, viewCount.thousandFormatted())
                            }
                            it.stockLabel = labelGroupList.firstOrNull { labelGroup ->
                                labelGroup.position.isEmpty()
                            }?.title ?: ""
                            it.stockBarPercentage = campaign.stockSoldPercentage.toInt()
                            it.displayedPrice = campaign.discountedPriceFmt.toFloatOrNull()?.getCurrencyFormatted() ?: price.textIdr
                        } else {
                            // hide discount percentage when flash sale campaign is upcoming
                            it.discountPercentage = ZERO_PRODUCT_DISCOUNT
                            it.displayedPrice = campaign.discountedPriceFmt
                        }
                        it.originalPrice = campaign.originalPriceFmt.toFloatOrZero().getCurrencyFormatted()
                        setStockAndSoldOutForCampaignEtalase(it, shopProduct)
                    }
                    ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN -> {
                        it.isEnableDirectPurchase = false
                    }
                }
                it.isVariant = hasVariant
                it.minimumOrder = minimumOrder
                it.parentId = parentId
                it.averageRating = stats.averageRating
            }
        }

    private fun setStockAndSoldOutForCampaignEtalase(
        shopProductUiModel: ShopProductUiModel,
        shopProduct: ShopProduct
    ) {
        shopProductUiModel.stock = shopProduct.campaign.customStock.toLongOrZero().takeIf { !it.isZero() } ?: shopProduct.stock.toLong()
        shopProductUiModel.isSoldOut = shopProductUiModel.stock.isZero()
    }

    private fun mapToLabelGroupViewModel(labelGroup: LabelGroup): LabelGroupUiModel {
        return LabelGroupUiModel(
            position = labelGroup.position,
            title = labelGroup.title,
            type = labelGroup.type,
            url = labelGroup.url
        )
    }

    fun mapShopFeaturedProductToProductViewModel(shopFeaturedProduct: ShopFeaturedProduct, isMyOwnProduct: Boolean): ShopProductUiModel =
        with(shopFeaturedProduct) {
            ShopProductUiModel().also {
                it.id = productId.toString()
                it.name = name
                it.displayedPrice = price
                it.originalPrice = originalPrice
                it.discountPercentage = percentageAmount.toString()
                it.imageUrl = imageUri
                it.totalReview = totalReview
                if (isRated) {
                    it.rating = ratingAverage.toDoubleOrZero()
                }
                if (cashback) {
                    it.cashback = cashbackDetail.cashbackPercent.toDouble()
                }
                it.isWholesale = wholesale
                it.isPo = preorder
                it.isFreeReturn = returnable
                it.isWishList = isWishlist
                it.productUrl = uri
                it.isShowWishList = !isMyOwnProduct
                it.isShowFreeOngkir = freeOngkir.isActive
                it.freeOngkirPromoIcon = freeOngkir.imgUrl
                it.labelGroupList = labelGroupList.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
            }
        }

    fun mapTopMembershipViewModel(data: MembershipStampProgress): List<BaseMembershipViewModel> {
        if (!data.membershipStampProgress.isShown || (data.membershipStampProgress.membershipProgram.membershipQuests.isEmpty() && data.membershipStampProgress.isUserRegistered)) {
            return mutableListOf()
        } else {
            val url = data.membershipStampProgress.infoMessage.membershipCta.url
            if (!data.membershipStampProgress.isUserRegistered) {
                return mutableListOf(
                    ItemUnregisteredViewModel(
                        bannerTitle = data.membershipStampProgress.infoMessage.title,
                        btnText = data.membershipStampProgress.infoMessage.membershipCta.text,
                        url = url
                    )
                )
            } else if (data.membershipStampProgress.membershipProgram.membershipQuests.isNotEmpty()) {
                val listOfMembershipQuests: MutableList<BaseMembershipViewModel> = mutableListOf()
                val quests = data.membershipStampProgress.membershipProgram.membershipQuests
                var count = 1
                listOfMembershipQuests.addAll(
                    quests.map {
                        it.startCountTxt = count
                        count += it.targetProgress
                        ItemRegisteredViewModel(it, url)
                    }
                )
                return listOfMembershipQuests
            }
            return mutableListOf()
        }
    }

    fun mapToProductCardModel(
        shopProductUiModel: ShopProductUiModel,
        isWideContent: Boolean,
        isShowThreeDots: Boolean = true,
        isForceLightMode: Boolean = false
    ): ProductCardModel {
        val totalReview = try {
            NumberFormat.getInstance().parse(shopProductUiModel.totalReview).toInt()
        } catch (ignored: Exception) {
            0
        }

        val discountPercentage = if (shopProductUiModel.discountPercentage == "0") {
            ""
        } else {
            "${shopProductUiModel.discountPercentage}%"
        }

        val freeOngkirObject = ProductCardModel.FreeOngkir(shopProductUiModel.isShowFreeOngkir, shopProductUiModel.freeOngkirPromoIcon ?: "")

        val baseProductCardModel = ProductCardModel(
            productImageUrl = shopProductUiModel.imageUrl ?: "",
            productName = shopProductUiModel.name ?: "",
            discountPercentage = discountPercentage.takeIf { !shopProductUiModel.hideGimmick } ?: "",
            slashedPrice = shopProductUiModel.originalPrice.orEmpty().takeIf { !shopProductUiModel.hideGimmick } ?: "",
            formattedPrice = shopProductUiModel.displayedPrice ?: "",
            countSoldRating = shopProductUiModel.averageRating,
            freeOngkir = freeOngkirObject,
            labelGroupList = shopProductUiModel.labelGroupList.map {
                mapToProductCardLabelGroup(it)
            },
            hasThreeDots = if (shopProductUiModel.etalaseType == ShopEtalaseTypeDef.ETALASE_FLASH_SALE) false else isShowThreeDots,
            pdpViewCount = shopProductUiModel.pdpViewCount,
            stockBarLabel = shopProductUiModel.stockLabel,
            stockBarPercentage = shopProductUiModel.stockBarPercentage,
            isWideContent = isWideContent,
            forceLightModeColor = isForceLightMode
        )
        return if (shopProductUiModel.isEnableDirectPurchase && isProductCardIsNotSoldOut(shopProductUiModel.isSoldOut)) {
            val productCardModel = if (shopProductUiModel.isVariant) {
                createProductCardWithVariantAtcModel(
                    shopProductUiModel,
                    baseProductCardModel
                )
            } else {
                if (shopProductUiModel.productInCart.isZero()) {
                    createProductCardWithDefaultAddToCardModel(baseProductCardModel)
                } else {
                    createProductCardWithNonVariantAtcModel(
                        shopProductUiModel,
                        baseProductCardModel
                    )
                }
            }
            productCardModel.copy(
                hasThreeDots = false
            )
        } else {
            baseProductCardModel.copy(
                hasThreeDots = isShowThreeDots
            )
        }
    }

    private fun isProductCardIsNotSoldOut(isProductSoldOut: Boolean): Boolean {
        return !isProductSoldOut
    }

    private fun createProductCardWithDefaultAddToCardModel(baseProductCardModel: ProductCardModel): ProductCardModel {
        return baseProductCardModel.copy(
            variant = null,
            nonVariant = null,
            hasAddToCartButton = true
        )
    }

    private fun createProductCardWithVariantAtcModel(
        shopProductUiModel: ShopProductUiModel,
        baseProductCardModel: ProductCardModel
    ): ProductCardModel {
        return baseProductCardModel.copy(
            variant = ProductCardModel.Variant(
                shopProductUiModel.productInCart
            )
        )
    }

    private fun createProductCardWithNonVariantAtcModel(
        shopProductUiModel: ShopProductUiModel,
        baseProductCardModel: ProductCardModel
    ): ProductCardModel {
        return baseProductCardModel.copy(
            nonVariant = ProductCardModel.NonVariant(
                quantity = shopProductUiModel.productInCart,
                minQuantity = shopProductUiModel.minimumOrder,
                maxQuantity = shopProductUiModel.maximumOrder
            )
        )
    }

    fun mapRestrictionEngineResponseToModel(restrictionEngineResponse: RestrictionEngineDataResponse?): RestrictionEngineModel {
        return RestrictionEngineModel().apply {
            status = restrictionEngineResponse?.status ?: ""
            val list: MutableList<Actions> = mutableListOf()
            restrictionEngineResponse?.actions?.map {
                list.add(
                    Actions(
                        actionType = it.actionType,
                        title = it.title,
                        description = it.description,
                        actionUrl = it.actionUrl,
                        attributeName = it.attributeName
                    )
                )
            }
            actions = list
        }
    }

    private fun mapToProductCardLabelGroup(labelGroupUiModel: LabelGroupUiModel): ProductCardModel.LabelGroup {
        return ProductCardModel.LabelGroup(
            position = labelGroupUiModel.position,
            title = labelGroupUiModel.title,
            type = labelGroupUiModel.type,
            imageUrl = labelGroupUiModel.url
        )
    }

    fun convertCommaValue(productIdList: List<String?>): String {
        val stringBuilder = StringBuilder()
        for (i in productIdList.indices) {
            stringBuilder.append(productIdList[i])
            if (i != productIdList.size - 1) {
                stringBuilder.append(",")
            }
        }
        return stringBuilder.toString()
    }

    private fun getMaximumOrder(shopProductResponse: ShopProduct): Int {
        return shopProductResponse.campaign.maxOrder.takeIf { !it.isZero() }
            ?: shopProductResponse.maximumOrder.takeIf { !it.isZero() }
            ?: shopProductResponse.campaign.customStock.toIntOrZero().takeIf { !it.isZero() }
            ?: shopProductResponse.stock
    }
}
