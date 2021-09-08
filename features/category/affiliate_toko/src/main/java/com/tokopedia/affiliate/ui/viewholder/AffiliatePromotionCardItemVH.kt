package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.ui.custom.AffiliatePromotionProductCard
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionCardModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.unifycomponents.UnifyButton

class AffiliatePromotionCardItemVH(itemView: View, private val promotionClickInterface: PromotionClickInterface?)
    : AbstractViewHolder<AffiliatePromotionCardModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_promotion_product_card_item_layout

    }

    override fun bind(element: AffiliatePromotionCardModel?) {
        element?.promotionItem?.let {
            itemView.findViewById<ProductCardListView>(R.id.affiliate_product_card).setProductModel(
                    AffiliatePromotionProductCard.toAffiliateProductModel(it))
        }

        itemView.findViewById<UnifyButton>(R.id.buttonNotify)?.run {
            visibility = View.VISIBLE
            text = context.getString(R.string.affiliate_promo)
            setOnClickListener {
                promotionClickInterface?.onPromotionClick(element?.promotionItem?.title ?: "",
                        element?.promotionItem?.image?.android ?:"","",
                        "")
            }
        }
    }
}
