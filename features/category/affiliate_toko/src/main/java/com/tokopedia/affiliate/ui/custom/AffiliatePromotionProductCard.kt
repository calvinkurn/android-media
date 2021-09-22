package com.tokopedia.affiliate.ui.custom

import com.tokopedia.affiliate.model.AffiliateSearchData
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
        RATING(2)
    }

    companion object {

        const val LABEL_POSITION = "category"
        const val LABEL_TYPE = "status"
        const val LABEL_COLOR = "transparentBlack"
        const val LABEL_COLOR_COMMISSION = "textGreen"

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
                            ProductCardModel.LabelGroup(LABEL_TYPE,"Toko Tidak Aktif", LABEL_COLOR)),
                    shopBadgeList = arrayListOf(ProductCardModel.ShopBadge(getFooterDataFromType(item,FooterType.SHOP)?.footerIcon?.isNotEmpty() == true,
                            getFooterDataFromType(item,FooterType.SHOP)?.footerIcon ?: "")),
                    shopLocation = getFooterDataFromType(item,FooterType.SHOP)?.footerText ?: "",
                    countSoldRating = getFooterDataFromType(item,FooterType.RATING)?.footerText ?: "",
                    stockBarLabel = getAdditionalDataFromType(item, AdditionalInfoType.PRODUCT_STOCK_TYPE)
            )
        }

        private fun getAdditionalDataFromType(item : AffiliateSearchData.SearchAffiliate.Data.Card.Item, type : AdditionalInfoType) : String{
            val data = (item.additionalInformation?.find{ it?.type == type.type})?.htmlText
            return if (data?.isNotEmpty() == true){ data }else { "" }
        }

        private fun getFooterDataFromType(item : AffiliateSearchData.SearchAffiliate.Data.Card.Item,type : FooterType) : AffiliateSearchData.SearchAffiliate.Data.Card.Item.Footer?{
            return (item.footer?.find{ it?.footerType == type.type})
        }
    }
}