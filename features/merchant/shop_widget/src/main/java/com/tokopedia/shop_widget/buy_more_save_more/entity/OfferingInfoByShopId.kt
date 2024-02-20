package com.tokopedia.shop_widget.buy_more_save_more.entity

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import kotlinx.parcelize.Parcelize


@Parcelize
data class OfferingInfoByShopIdUiModel(
    val offerId: Long = 0,
    val shopId: Long = 0,
    val offerType: Int = 1,
    val offerName: String = "",
    val offerCtaText: String = "",
    val warehouseIds: List<Long> = emptyList(),
    val thumbnails: List<String> = emptyList(),
    val redirectOfferUrl: String = "",
    val redirectOfferApplink: String = "",
    val offerBanner: GetOfferingInfoByShopIDResponseBanner = GetOfferingInfoByShopIDResponseBanner(),
    val offeringDetail: OfferingDetail = OfferingDetail(),
    val products: List<Product> = emptyList()
) : Parcelable {
    fun toOfferingInfoForBuyerUiModel(): OfferingInfoForBuyerUiModel {
        return OfferingInfoForBuyerUiModel(
            offerings = listOf(
                OfferingInfoForBuyerUiModel.Offering(
                    id = offerId,
                    offerName = offerName,
                    offerTypeId = offerType.toLong(),
                    startDate = offeringDetail.startDate,
                    endDate = offeringDetail.endDate,
                    tierList = offeringDetail.tierList.map { tier ->
                        OfferingInfoForBuyerUiModel.Offering.Tier(
                            tierId = tier.tierId,
                            tierWording = tier.tierWording,
                            level = tier.level,
                            rules = tier.rules.map { rule ->
                                OfferingInfoForBuyerUiModel.Offering.Tier.Rule(
                                    typeId = rule.typeId,
                                    operation = rule.operation,
                                    value = rule.value
                                )
                            },
                            benefits = tier.benefits.map { benefit ->
                                OfferingInfoForBuyerUiModel.Offering.Tier.Benefit(
                                    typeId = benefit.typeId,
                                    value = benefit.value,
                                    products = listOf(OfferingInfoForBuyerUiModel.Offering.Tier.Benefit.ProductBenefit())
                                )
                            }
                        )
                    },
                    tnc = offeringDetail.termAndConditions
                )
            )
        )
    }

    fun toProductListUiModel() : List<OfferingProductListUiModel.Product> {
        return products.map { product ->
            OfferingProductListUiModel.Product(
                offerId = offerId,
                parentId = product.parentId,
                productId = product.productId,
                warehouseId = product.warehouseId,
                productUrl = product.productUrl,
                imageUrl = product.imageUrl,
                name = product.name,
                price = product.price,
                rating = if (product.rating.isMoreThanZero()) product.rating.toDouble().toString() else "",
                soldCount = product.soldCount,
                stock = product.stock,
                isVbs = product.isVbs,
                minOrder = product.minOrder,
                labelGroup = product.labelGroups.map { labelGroup ->
                    OfferingProductListUiModel.Product.LabelGroup(
                        position = labelGroup.position,
                        title = labelGroup.title,
                        type = labelGroup.type,
                        url = labelGroup.url
                    )
                },
                campaign = OfferingProductListUiModel.Product.Campaign(
                    originalPrice = product.price,
                    discountedPrice = product.discountedPrice,
                    discountedPercentage = product.discountedPercentage.toIntOrZero()
                ),
                totalProduct = products.count()
            )
        }
    }
}

@Parcelize
data class OfferingDetail(
    val termAndConditions: List<String> = emptyList(),
    val startDate: String = "",
    val endDate: String = "",
    val tierList: List<Tier> = emptyList()
) : Parcelable {

    @Parcelize
    data class Tier(
        val tierId: Long = 0,
        val level: Int = 0,
        val tierWording: String = "",
        val rules: List<Rule> = emptyList(),
        val benefits: List<Benefit> = emptyList()
    ): Parcelable {

        @Parcelize
        data class Rule(
            val typeId: Long = 0,
            val operation: String = "",
            val value: Int = 0
        ): Parcelable

        @Parcelize
        data class Benefit(
            val typeId: Long = 0,
            val value: Int = 0
        ): Parcelable
    }
}

@Parcelize
data class Product(
    val parentId: Long = 0,
    val productId: Long = 0,
    val warehouseId: Long = 0,
    val productUrl: String = "",
    val imageUrl: String = "",
    val name: String = "",
    val price: String = "",
    val rating: Int = 0,
    val soldCount: Int = 0,
    val stock: Int = 0,
    val isVbs: Boolean = false,
    val minOrder: Int = 1,
    val discountedPrice: String = "",
    val discountedPercentage: String = "",
    val labelGroups: List<LabelGroup> = emptyList()
): Parcelable {

    @Parcelize
    data class LabelGroup(
        val position: String = "",
        val title: String = "",
        val type: String = "",
        val url: String = ""
    ): Parcelable
}

@Parcelize
data class GetOfferingInfoByShopIDResponseBanner(
    val bannerImage: String = ""
) : Parcelable

enum class CardState() {
    LOADING,
    SUCCESS,
    ERROR
}
