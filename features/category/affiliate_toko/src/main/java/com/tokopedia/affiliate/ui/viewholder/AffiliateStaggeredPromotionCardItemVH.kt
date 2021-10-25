package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.AffiliateSearchData
import com.tokopedia.affiliate.ui.custom.AffiliatePromotionProductCard
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateStaggeredPromotionCardModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.unifycomponents.UnifyButton

class AffiliateStaggeredPromotionCardItemVH(itemView: View, private val promotionClickInterface: PromotionClickInterface?)
    : AbstractViewHolder<AffiliateStaggeredPromotionCardModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_staggered_product_card_item_layout

    }

    override fun bind(element: AffiliateStaggeredPromotionCardModel?) {

        getDummyData().let { ele ->
            ele.let {
                itemView.findViewById<ProductCardGridView>(R.id.affiliate_product_card).setProductModel(
                        AffiliatePromotionProductCard.toAffiliateProductModel(it))
            }

            itemView.findViewById<UnifyButton>(com.tokopedia.productcard.R.id.buttonNotify)?.run {
                visibility = View.VISIBLE
                buttonType = UnifyButton.Type.MAIN
                buttonVariant = UnifyButton.Variant.GHOST
                text = context.getString(R.string.affiliate_promo)
                setOnClickListener {
                    promotionClickInterface?.onPromotionClick( ele.productID ?: "",
                            ele.title ?: "",
                            ele.image?.androidURL ?:"",
                            ele.cardUrl ?: "",
                            "")
                }
                if(ele.status?.isLinkGenerationAllowed == false){
                    buttonType = UnifyButton.Type.ALTERNATE
                    setOnClickListener(null)
                }
            }
        }

    }

    private fun getDummyData(): AffiliateSearchData.SearchAffiliate.Data.Card.Item {

        fun getAddtionalInfo(): ArrayList<AffiliateSearchData.SearchAffiliate.Data.Card.Item.AdditionalInformation> {
            val additionalInformation = arrayListOf<AffiliateSearchData.SearchAffiliate.Data.Card.Item.AdditionalInformation>()
            val addInfo1 = AffiliateSearchData.SearchAffiliate.Data.Card.Item.AdditionalInformation("#03AC0E",
                    "Komisi Rp5.400", 1)
            val addInfo2 = AffiliateSearchData.SearchAffiliate.Data.Card.Item.AdditionalInformation("#EF144A",
                    "10%", 2)
            val addInfo3 = AffiliateSearchData.SearchAffiliate.Data.Card.Item.AdditionalInformation("#31353B",
                    "Rp600.000", 3)
            val addInfo4 = AffiliateSearchData.SearchAffiliate.Data.Card.Item.AdditionalInformation("#31353B",
                    "Rp540.000", 4)
            additionalInformation.add(addInfo1)
            additionalInformation.add(addInfo2)
            additionalInformation.add(addInfo3)
            additionalInformation.add(addInfo4)
            return additionalInformation
        }

        fun getFooter(): ArrayList<AffiliateSearchData.SearchAffiliate.Data.Card.Item.Footer> {
            val footers = arrayListOf<AffiliateSearchData.SearchAffiliate.Data.Card.Item.Footer>()
            val footer1 = AffiliateSearchData.SearchAffiliate.Data.Card.Item.Footer("", "ABC", 1)
            val footer2 = AffiliateSearchData.SearchAffiliate.Data.Card.Item.Footer("", "4.5", 2)
            val footer3 = AffiliateSearchData.SearchAffiliate.Data.Card.Item.Footer("", "4000", 3)
            footers.add(footer1)
            footers.add(footer2)
            footers.add(footer3)
            return footers
        }

        fun getItem(): AffiliateSearchData.SearchAffiliate.Data.Card.Item {
            val imageUrl = "https://imagerouter.tokopedia.com/img/300/attachment/2019/9/13/43737554/43737554_b3d583de-47a7-45b0-8ca4-d1bd7719431e.jpg"
            val item = AffiliateSearchData.SearchAffiliate.Data.Card.Item(
                    "1",
                    getAddtionalInfo(),
                    "OEM Speaker Harman Kardon Onyx Mini Black",
                    AffiliateSearchData.SearchAffiliate.Data.Card.Item.Commission(5400,
                            "Rp5.400", 10, "10%"),
                    getFooter(),
                    AffiliateSearchData.SearchAffiliate.Data.Card.Item.Image(imageUrl, imageUrl, imageUrl, imageUrl),
                    4,
                    AffiliateSearchData.SearchAffiliate.Data.Card.Item.Status(true,null),
                    "Title"
            )
            return item
        }

        return getItem()
    }
}
