package com.tokopedia.affiliate.ui.custom

import com.tokopedia.affiliate.model.AffiliateSearchData
import com.tokopedia.productcard.ProductCardModel

class AffiliatePromotionProductCard  {

    enum class AdditionalInfoType(val type : Int){
        COMMISSION_AMOUNT_TYPE(1),
        DISCOUNT_PERCENTAGE_TYPE(2),
        SLASHED_PRICE_TYPE(3),
        FINAL_PRICE_TYPE(4)
    }

    enum class FooterType(val type : Int){
        SHOP(1),
        RATING(2)
    }

    companion object {
        fun toAffiliateProductModel(item : AffiliateSearchData.Cards.Items) : ProductCardModel{
            return ProductCardModel(
                    productImageUrl = item.image?.android ?: item.image?.ios ?: "",
                    productName = item.title ?: "",
                    discountPercentage = getAdditionalDataFromType(item, AdditionalInfoType.DISCOUNT_PERCENTAGE_TYPE),
                    slashedPrice = getAdditionalDataFromType(item, AdditionalInfoType.SLASHED_PRICE_TYPE),
                    priceRange = getAdditionalDataFromType(item, AdditionalInfoType.FINAL_PRICE_TYPE),
                    labelGroupList = arrayListOf(ProductCardModel.LabelGroup("category",
                            getAdditionalDataFromType(item, AdditionalInfoType.COMMISSION_AMOUNT_TYPE),"textGreen"),
                            ProductCardModel.LabelGroup("status","Toko Tidak Aktif",
                                    "transparentBlack")),
                    shopBadgeList = arrayListOf(ProductCardModel.ShopBadge(getFooterDataFromType(item,FooterType.SHOP)?.footerIcon?.isNotEmpty() == true,
                            getFooterDataFromType(item,FooterType.SHOP)?.footerIcon ?: "")),
                    shopLocation = getFooterDataFromType(item,FooterType.SHOP)?.footerText ?: "",
                    countSoldRating = getFooterDataFromType(item,FooterType.RATING)?.footerText ?: ""
            )
        }

        private fun getAdditionalDataFromType(item : AffiliateSearchData.Cards.Items, type : AdditionalInfoType) : String{
            val data = (item.additionalInformation?.find{ it.type == type.type})?.htmlText
            return if (data?.isNotEmpty() == true){
                data
            }else {
                ""
            }
        }

        private fun getFooterDataFromType(item : AffiliateSearchData.Cards.Items,type : FooterType) : AffiliateSearchData.Cards.Items.Footer?{
            return (item.footer?.find{ it.footerType == type.type})
        }
    }
}