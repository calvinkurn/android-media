package com.tokopedia.exploreCategory.ui.custom

import androidx.annotation.IntDef
import com.tokopedia.exploreCategory.model.AffiliateSearchData
import com.tokopedia.productcard.ProductCardModel

class AffiliatePromotionProductCard  {

    companion object {
        const val COMMISSION_AMOUNT_TYPE = 1
        const val DISCOUNT_PERCENTAGE_TYPE = 2
        const val SLASHED_PRICE_TYPE = 3
        const val FINAL_PRICE_TYPE = 4

        const val SHOP = 1
        const val RATING = 2

        @IntDef(value = [COMMISSION_AMOUNT_TYPE,DISCOUNT_PERCENTAGE_TYPE,SLASHED_PRICE_TYPE,FINAL_PRICE_TYPE])
        @Retention(AnnotationRetention.SOURCE)
        annotation class AdditionalInfoType


        @IntDef(value = [SHOP,RATING])
        @Retention(AnnotationRetention.SOURCE)
        annotation class FooterType

        fun toAffiliateProductModel(item : AffiliateSearchData.Cards.Items) : ProductCardModel{
            return ProductCardModel(
                    productImageUrl = item.image?.android ?: item.image?.ios ?: "",
                    productName = item.title ?: "",
                    discountPercentage = getAdditionalDataFromType(item, DISCOUNT_PERCENTAGE_TYPE),
                    slashedPrice = getAdditionalDataFromType(item, SLASHED_PRICE_TYPE),
                    priceRange = getAdditionalDataFromType(item, FINAL_PRICE_TYPE),
                    formattedPrice = getAdditionalDataFromType(item, FINAL_PRICE_TYPE),
                    labelGroupList = arrayListOf(ProductCardModel.LabelGroup("category",
                            getAdditionalDataFromType(item, COMMISSION_AMOUNT_TYPE),"textGreen"),
                            ProductCardModel.LabelGroup("status","Toko Tidak Aktif",
                                    "transparentBlack")),
                    shopBadgeList = arrayListOf(ProductCardModel.ShopBadge(getFooterDataFromType(item,SHOP)?.footerIcon?.isNotEmpty() == true,
                            getFooterDataFromType(item,SHOP)?.footerIcon ?: "")),
                    shopLocation = getFooterDataFromType(item,SHOP)?.footerText ?: "",
                    countSoldRating = getFooterDataFromType(item,RATING)?.footerText ?: ""
            )
        }

        private fun getAdditionalDataFromType(item : AffiliateSearchData.Cards.Items, @AdditionalInfoType type : Int) : String{
            val data = (item.additionalInformation?.find{ it.type == type})?.htmlText
            return if (data?.isNotEmpty() == true){
                data
            }else {
                ""
            }
        }

        private fun getFooterDataFromType(item : AffiliateSearchData.Cards.Items, @FooterType type : Int) : AffiliateSearchData.Cards.Items.Footer?{
            return (item.footer?.find{ it.footerType == type})
        }
    }
}