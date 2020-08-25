package com.tokopedia.shop.product.utils.mapper

import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef.ETALASE_CAMPAIGN
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.common.data.viewmodel.BaseMembershipViewModel
import com.tokopedia.shop.common.data.viewmodel.ItemRegisteredViewModel
import com.tokopedia.shop.common.data.viewmodel.ItemUnregisteredViewModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.view.datamodel.ShopProductViewModel.Companion.THRESHOLD_VIEW_COUNT
import java.text.NumberFormat
import java.text.ParseException
import kotlin.math.roundToInt

object ShopPageProductListMapper {

    private const val POSTFIX_VIEW_COUNT="%1s orang"

    fun mapToShopProductEtalaseListDataModel(
            listShopEtalaseModel: List<ShopEtalaseModel>
    ): List<ShopEtalaseItemDataModel>{
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
                shopEtalaseModel.highlighted
        )
    }

    fun mapShopProductToProductViewModel(shopProduct: ShopProduct, isMyOwnProduct: Boolean, etalaseId: String, etalaseType: Int? = null): ShopProductViewModel =
            with(shopProduct) {
                ShopProductViewModel().also {
                    it.id = productId
                    it.name = name
                    it.displayedPrice = price.textIdr
                    it.originalPrice = campaign.originalPriceFmt
                    it.discountPercentage = campaign.discountedPercentage
                    it.imageUrl = primaryImage.original
                    it.imageUrl300 = primaryImage.resize300
                    it.totalReview = stats.reviewCount.toString()
                    it.rating = (stats.rating.toDouble() / 20).roundToInt().toDouble()
                    if (cashback.cashbackPercent > 0) {
                        it.cashback = cashback.cashbackPercent.toDouble()
                    }
                    it.isWholesale = flags.isWholesale
                    it.isPo = flags.isPreorder
                    it.isFreeReturn = flags.isFreereturn
                    it.isWishList = flags.isWishlist
                    it.productUrl = productUrl
                    it.isSoldOut = flags.isSold
                    it.isShowWishList = !isMyOwnProduct
                    it.isShowFreeOngkir = freeOngkir.isActive
                    it.freeOngkirPromoIcon = freeOngkir.imgUrl
                    it.etalaseId = etalaseId
                    it.labelGroupList = labelGroupList.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
                    it.etalaseType = etalaseType
                    if(it.etalaseType == ETALASE_CAMPAIGN) {
                        if (!campaign.isUpcoming) {
                            val viewCount = stats.viewCount
                            if (viewCount >= THRESHOLD_VIEW_COUNT)
                                it.pdpViewCount = String.format(POSTFIX_VIEW_COUNT, viewCount.thousandFormatted())
                            it.stockLabel = labelGroupList.firstOrNull { labelGroup ->
                                labelGroup.position.isEmpty()
                            }?.title ?: ""
                            it.stockBarPercentage = campaign.stockSoldPercentage.toInt()
                        }
                        it.hideGimmick = campaign.hideGimmick
                        it.displayedPrice = campaign.discountedPriceFmt.toFloatOrZero().getCurrencyFormatted()
                        it.originalPrice = campaign.originalPriceFmt.toFloatOrZero().getCurrencyFormatted()
                    }
                }
            }

    private fun mapToLabelGroupViewModel(labelGroup: LabelGroup): LabelGroupViewModel {
        return LabelGroupViewModel(
                position = labelGroup.position,
                title = labelGroup.title,
                type = labelGroup.type
        )
    }

    fun mapShopFeaturedProductToProductViewModel(shopFeaturedProduct: ShopFeaturedProduct, isMyOwnProduct: Boolean): ShopProductViewModel =
            with(shopFeaturedProduct) {
                ShopProductViewModel().also {
                    it.id = productId.toString()
                    it.name = name
                    it.displayedPrice = price
                    it.originalPrice = originalPrice
                    it.discountPercentage = percentageAmount.toString()
                    it.imageUrl = imageUri
                    it.totalReview = totalReview
                    if (isRated) {
                        it.rating = rating.toDoubleOrZero()
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
        if (!data.membershipStampProgress.isShown || (data.membershipStampProgress.membershipProgram.membershipQuests.isEmpty() && data.membershipStampProgress.isUserRegistered)){
            return mutableListOf()
        } else {
            val url = data.membershipStampProgress.infoMessage.membershipCta.url
            if (!data.membershipStampProgress.isUserRegistered) {
                return mutableListOf(
                        ItemUnregisteredViewModel(bannerTitle = data.membershipStampProgress.infoMessage.title,
                                btnText = data.membershipStampProgress.infoMessage.membershipCta.text,
                                url = url))
            } else if (data.membershipStampProgress.membershipProgram.membershipQuests.isNotEmpty()) {
                val listOfMembershipQuests: MutableList<BaseMembershipViewModel> = mutableListOf()
                val quests = data.membershipStampProgress.membershipProgram.membershipQuests
                var count = 1
                listOfMembershipQuests.addAll(quests.map {
                    it.startCountTxt = count
                    count += it.targetProgress
                    ItemRegisteredViewModel(it, url)
                })
                return listOfMembershipQuests
            }
            return mutableListOf()
        }
    }

    fun mapToMerchantVoucherViewModel(
            merchantVoucherResponse: List<MerchantVoucherModel>
    ): List<MerchantVoucherViewModel> {
        return merchantVoucherResponse.map { MerchantVoucherViewModel(it) }
    }

    fun mapToProductCardModel(shopProductViewModel: ShopProductViewModel): ProductCardModel {
        val totalReview = try {
            NumberFormat.getInstance().parse(shopProductViewModel.totalReview).toInt()
        } catch (ignored: Exception) {
            0
        }

        val discountPercentage = if (shopProductViewModel.discountPercentage == "0") {
            ""
        } else {
            "${shopProductViewModel.discountPercentage}%"
        }

        val freeOngkirObject = ProductCardModel.FreeOngkir(shopProductViewModel.isShowFreeOngkir, shopProductViewModel.freeOngkirPromoIcon ?: "")

        return ProductCardModel(
                productImageUrl = shopProductViewModel.imageUrl ?: "",
                productName = shopProductViewModel.name ?: "",
                discountPercentage = discountPercentage.takeIf { !shopProductViewModel.hideGimmick } ?: "",
                slashedPrice = shopProductViewModel.originalPrice.orEmpty().takeIf { !shopProductViewModel.hideGimmick } ?: "",
                formattedPrice = shopProductViewModel.displayedPrice ?: "",
                ratingCount = shopProductViewModel.rating.toInt(),
                reviewCount = totalReview,
                freeOngkir = freeOngkirObject,
                labelGroupList = shopProductViewModel.labelGroupList.map {
                    mapToProductCardLabelGroup(it)
                },
                hasThreeDots = true,
                pdpViewCount = shopProductViewModel.pdpViewCount,
                stockBarLabel = shopProductViewModel.stockLabel,
                stockBarPercentage = shopProductViewModel.stockBarPercentage
        )
    }

    private fun mapToProductCardLabelGroup(labelGroupViewModel: LabelGroupViewModel): ProductCardModel.LabelGroup {
        return ProductCardModel.LabelGroup(
                position = labelGroupViewModel.position,
                title = labelGroupViewModel.title,
                type = labelGroupViewModel.type
        )
    }
}