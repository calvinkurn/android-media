package com.tokopedia.affiliate.ui.custom

import com.tokopedia.affiliate.EMPTY_STOCK
import com.tokopedia.affiliate.PRODUCT_INACTIVE
import com.tokopedia.affiliate.SHOP_CLOSED
import com.tokopedia.affiliate.SHOP_INACTIVE
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.productcard.ProductCardModel

class AffiliatePromotionProductCard {

    enum class AdditionalInfoType(val type: Int) {
        COMMISSION_AMOUNT_TYPE(COMMISSION_AMOUNT),
        DISCOUNT_PERCENTAGE_TYPE(DISCOUNT_PERCENTAGE),
        SLASHED_PRICE_TYPE(SLASHED_PRICE),
        FINAL_PRICE_TYPE(FINAL_PRICE),
        PRODUCT_STOCK_TYPE(PRODUCT_STOCK)
    }

    enum class FooterType(val type: Int) {
        SHOP(SHOP_TYPE),
        RATING(RATING_TYPE),
        SALES(SALES_TYPE)
    }

    companion object {

        private const val LABEL_POSITION = "category"
        private const val SSA_LABEL_POSITION = "costperunit"
        private const val SSA_LABEL_TYPE = "textGreen"
        private const val LABEL_TYPE = "status"
        private const val LABEL_COLOR = "transparentBlack"
        private const val LABEL_COLOR_COMMISSION = "textGreen"
        private const val LABEL_SISA = "gimmick"
        private const val LABEL_SISA_COLOR = "textDarkRed"
        private const val LABEL_INTEGRITY = "integrity"
        private const val COMMISSION_AMOUNT = 1
        private const val DISCOUNT_PERCENTAGE = 2
        private const val SLASHED_PRICE = 3
        private const val FINAL_PRICE = 4
        private const val PRODUCT_STOCK = 5
        private const val SHOP_TYPE = 1
        private const val RATING_TYPE = 2
        private const val SALES_TYPE = 3
        fun toAffiliateProductModel(item: AffiliateSearchData.SearchAffiliate.Data.Card.Item): ProductCardModel {
            val labelGroupList = arrayListOf(
                ProductCardModel.LabelGroup(
                    LABEL_POSITION,
                    getAdditionalDataFromType(item, AdditionalInfoType.COMMISSION_AMOUNT_TYPE),
                    LABEL_COLOR_COMMISSION
                ),
                ProductCardModel.LabelGroup(
                    LABEL_SISA,
                    getAdditionalDataFromType(item, AdditionalInfoType.PRODUCT_STOCK_TYPE),
                    LABEL_SISA_COLOR
                ),
                ProductCardModel.LabelGroup(
                    LABEL_INTEGRITY,
                    getFooterDataFromType(item, FooterType.SALES)?.footerText ?: ""
                ),
                ProductCardModel.LabelGroup(
                    LABEL_TYPE,
                    getMessageDataFromType(item),
                    LABEL_COLOR
                )
            )
            if (item.ssaStatus == true) {
                labelGroupList.add(
                    ProductCardModel.LabelGroup(
                        SSA_LABEL_POSITION,
                        item.label?.labelText ?: "komisi extra",
                        SSA_LABEL_TYPE
                    )
                )
            }
            return ProductCardModel(
                productImageUrl = item.image?.androidURL ?: item.image?.iosURL ?: "",
                productName = item.title ?: "",
                discountPercentage = getAdditionalDataFromType(
                    item,
                    AdditionalInfoType.DISCOUNT_PERCENTAGE_TYPE
                ),
                slashedPrice = getAdditionalDataFromType(
                    item,
                    AdditionalInfoType.SLASHED_PRICE_TYPE
                ),
                priceRange = getAdditionalDataFromType(item, AdditionalInfoType.FINAL_PRICE_TYPE),
                labelGroupList = labelGroupList,
                shopBadgeList = arrayListOf(
                    ProductCardModel.ShopBadge(
                        getFooterDataFromType(
                            item,
                            FooterType.SHOP
                        )?.footerIcon?.isNotEmpty() == true,
                        getFooterDataFromType(item, FooterType.SHOP)?.footerIcon ?: ""
                    )
                ),
                shopLocation = getFooterDataFromType(item, FooterType.SHOP)?.footerText ?: "",
                countSoldRating = getFooterDataFromType(item, FooterType.RATING)?.footerText ?: ""
            )
        }

        private fun getAdditionalDataFromType(
            item: AffiliateSearchData.SearchAffiliate.Data.Card.Item,
            type: AdditionalInfoType
        ): String {
            val data = (item.additionalInformation?.find { it?.type == type.type })?.htmlText
            return if (data?.isNotEmpty() == true) {
                data
            } else {
                ""
            }
        }

        private fun getFooterDataFromType(
            item: AffiliateSearchData.SearchAffiliate.Data.Card.Item,
            type: FooterType
        ): AffiliateSearchData.SearchAffiliate.Data.Card.Item.Footer? {
            return (item.footer?.find { it?.footerType == type.type })
        }

        private fun getMessageDataFromType(
            item: AffiliateSearchData.SearchAffiliate.Data.Card.Item
        ): String {
            return when (item.status?.messages?.firstOrNull()?.messageType) {
                EMPTY_STOCK, PRODUCT_INACTIVE, SHOP_INACTIVE, SHOP_CLOSED ->
                    item.status?.messages?.firstOrNull()?.title.orEmpty()
                else -> ""
            }
        }
    }
}
