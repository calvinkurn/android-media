package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.model.response.AffiliateRecommendedProductData
import com.tokopedia.affiliate.ui.custom.AffiliatePromotionStaggeredProductCard
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateStaggeredPromotionCardModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.unifycomponents.UnifyButton

class AffiliateStaggeredPromotionCardItemVH(
    itemView: View,
    private val promotionClickInterface: PromotionClickInterface?
) : AbstractViewHolder<AffiliateStaggeredPromotionCardModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_staggered_product_card_item_layout
        private const val SSA_LABEL = 7
        private const val SSA_MESSAGE = 8
    }

    override fun bind(element: AffiliateStaggeredPromotionCardModel?) {
        element?.product?.let {
            itemView.findViewById<ProductCardGridView>(R.id.affiliate_product_card).setProductModel(
                AffiliatePromotionStaggeredProductCard.toAffiliateProductModel(it)
            )
        }

        itemView.findViewById<UnifyButton>(com.tokopedia.productcard.R.id.buttonNotify)?.run {
            visibility = View.VISIBLE
            buttonType = UnifyButton.Type.MAIN
            buttonVariant = UnifyButton.Variant.GHOST
            text = context.getString(R.string.affiliate_promo)
            var commission = ""
            element?.product?.commission?.amount?.let {
                commission = it.toString()
            }
            setOnClickListener {
                promotionClickInterface?.onPromotionClick(
                    element?.product?.productID ?: "",
                    element?.product?.title ?: "",
                    element?.product?.image?.androidURL ?: "",
                    element?.product?.cardUrl?.desktopURL ?: "",
                    bindingAdapterPosition,
                    commission,
                    ssaInfo = AffiliatePromotionBottomSheetParams.SSAInfo(
                        element?.product?.ssaStatus.orFalse(),
                        element?.product?.commission?.expiryDateFormatted.orEmpty(),
                        getAdditionalInfo(element?.product, SSA_MESSAGE),
                        AffiliatePromotionBottomSheetParams.SSAInfo.Label(
                            labelText = getAdditionalInfo(element?.product, SSA_LABEL),
                            labelType = ""
                        )
                    )
                )
            }
            if (element?.product?.isLinkGenerationAllowed == false) {
                buttonType = UnifyButton.Type.ALTERNATE
                setOnClickListener(null)
            }
        }
    }

    private fun getAdditionalInfo(
        product: AffiliateRecommendedProductData.RecommendedAffiliateProduct.Data.Card.Item?,
        type: Int
    ): String {
        return product?.additionalInformation?.findLast { it?.type == type }?.htmlText.toString()
    }
}
