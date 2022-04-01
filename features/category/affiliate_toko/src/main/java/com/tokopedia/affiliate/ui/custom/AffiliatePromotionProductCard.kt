package com.tokopedia.affiliate.ui.custom

import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.productcard.ProductCardModel

class AffiliatePromotionProductCard  {

    enum class AdditionalInfoType(val type : Int){
        COMMISSION_AMOUNT_TYPE(1),
        DISCOUNT_PERCENTAGE_TYPE(2),
        SLASHED_PRICE_TYPE(3),
        FINAL_PRICE_TYPE(4),
        PRODUCT_STOCK_TYPE(5)
    }

    enum class FooterType(val type : Int){
        SHOP(1),
        RATING(2),
        SALES(3)
    }

    enum class MessageType(val type : Int){
        OVERLAY_IMAGE_TYPE(1)
    }

    companion object {

        private const val LABEL_POSITION = "category"
        private const val LABEL_TYPE = "status"
        private const val LABEL_COLOR = "transparentBlack"
        private const val LABEL_COLOR_COMMISSION = "textGreen"
        private const val LABEL_SISA = "gimmick"
        private const val LABEL_SISA_COLOR = "textDarkRed"
        private const val LABEL_INTEGRITY = "integrity"

        fun toAffiliateProductModel(item : AffiliateSearchData.SearchAffiliate.Data.Card.Item) : ProductCardModel{
            return ProductCardModel(
                    productImageUrl = item.image?.androidURL ?: item.image?.iosURL ?: "",
                    productName = item.title ?: "",
                    discountPercentage = getAdditionalDataFromType(item, AdditionalInfoType.DISCOUNT_PERCENTAGE_TYPE),
                    slashedPrice = getAdditionalDataFromType(item, AdditionalInfoType.SLASHED_PRICE_TYPE),
                    priceRange = getAdditionalDataFromType(item, AdditionalInfoType.FINAL_PRICE_TYPE),
                    labelGroupList = arrayListOf(
                            ProductCardModel.LabelGroup(LABEL_POSITION,
                                    getAdditionalDataFromType(item, AdditionalInfoType.COMMISSION_AMOUNT_TYPE),LABEL_COLOR_COMMISSION),
                            ProductCardModel.LabelGroup(LABEL_SISA,
                                    getAdditionalDataFromType(item, AdditionalInfoType.PRODUCT_STOCK_TYPE),LABEL_SISA_COLOR),
                            ProductCardModel.LabelGroup(LABEL_INTEGRITY,
                                    getFooterDataFromType(item, FooterType.SALES)?.footerText ?: ""),
                            ProductCardModel.LabelGroup(LABEL_TYPE,
                                    getMessageDataFromType(item,MessageType.OVERLAY_IMAGE_TYPE), LABEL_COLOR)),
                    shopBadgeList = arrayListOf(ProductCardModel.ShopBadge(getFooterDataFromType(item,FooterType.SHOP)?.footerIcon?.isNotEmpty() == true,
                            getFooterDataFromType(item,FooterType.SHOP)?.footerIcon ?: "")),
                    shopLocation = getFooterDataFromType(item,FooterType.SHOP)?.footerText ?: "",
                    countSoldRating = getFooterDataFromType(item,FooterType.RATING)?.footerText ?: ""
            )
        }

        private fun getAdditionalDataFromType(item : AffiliateSearchData.SearchAffiliate.Data.Card.Item, type : AdditionalInfoType) : String{
            val data = (item.additionalInformation?.find{ it?.type == type.type})?.htmlText
            return if (data?.isNotEmpty() == true){ data }else { "" }
        }

        private fun getFooterDataFromType(item : AffiliateSearchData.SearchAffiliate.Data.Card.Item, type : FooterType) : AffiliateSearchData.SearchAffiliate.Data.Card.Item.Footer?{
            return (item.footer?.find{ it?.footerType == type.type})
        }

        private fun getMessageDataFromType(item : AffiliateSearchData.SearchAffiliate.Data.Card.Item, type : MessageType) : String{
            val data = (item.status?.messages?.find{ it?.messageType == type.type})?.title
            return if (data?.isNotEmpty() == true){ data }else { "" }
        }
    }
}