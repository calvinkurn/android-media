package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.PAGE_TYPE_PDP
import com.tokopedia.affiliate.PAGE_TYPE_SHOP
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.model.response.AffiliatePerformanceData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateSharedProductCardsModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Label.Companion.HIGHLIGHT_LIGHT_GREEN
import com.tokopedia.unifycomponents.Label.Companion.HIGHLIGHT_LIGHT_GREY
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliateSharedProductCardsItemVH(
    itemView: View,
    private val productClickInterface: ProductClickInterface?
) : AbstractViewHolder<AffiliateSharedProductCardsModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_vertical_product_card_item_layout

        const val PRODUCT_ACTIVE = 1
        const val TYPE_PRODUCT = 0
    }

    override fun bind(element: AffiliateSharedProductCardsModel?) {
        element?.product?.let { product ->
            itemView.findViewById<ImageUnify>(R.id.product_image)
                ?.setImageUrl(product.image?.androidURL ?: "")
            itemView.findViewById<Typography>(R.id.product_name)?.text = product.itemTitle
            if (product.status == PRODUCT_ACTIVE) {
                itemView.findViewById<ImageUnify>(R.id.status_bullet)?.setImageDrawable(
                    MethodChecker.getDrawable(
                        itemView.context,
                        R.drawable.affiliate_circle_active
                    )
                )
                itemView.findViewById<Typography>(R.id.product_status)?.run {
                    setTextColor(
                        MethodChecker.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN500
                        )
                    )
                    text = getString(R.string.affiliate_active)
                }
                itemView.findViewById<Label>(R.id.ssa_label)?.apply {
                    isVisible = element.product.ssaStatus.orFalse()
                    text = element.product.label?.labelText
                    setLabelType(HIGHLIGHT_LIGHT_GREEN)
                }
            } else {
                itemView.findViewById<ImageUnify>(R.id.status_bullet)?.setImageDrawable(
                    MethodChecker.getDrawable(
                        itemView.context,
                        R.drawable.affiliate_circle_inactive
                    )
                )
                itemView.findViewById<Typography>(R.id.product_status)?.run {
                    setTextColor(
                        MethodChecker.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN500
                        )
                    )
                    text = getString(R.string.affiliate_inactive)
                }
                itemView.findViewById<Label>(R.id.ssa_label)?.apply {
                    isVisible = element.product.ssaStatus.orFalse()
                    text = element.product.label?.labelText
                    setLabelType(HIGHLIGHT_LIGHT_GREY)
                }
            }
            itemView.findViewById<Typography>(R.id.shop_name)?.text =
                product.footer?.firstOrNull()?.footerText
            itemView.setOnClickListener {
                sendSelectContentEvent(product)
                val type = if (product.itemType == TYPE_PRODUCT) PAGE_TYPE_PDP else PAGE_TYPE_SHOP
                productClickInterface?.onProductClick(
                    product.itemID,
                    product.itemTitle ?: "",
                    product.image?.androidURL
                        ?: "",
                    product.defaultLinkURL ?: "",
                    product.itemID,
                    product.status ?: 0,
                    type = type,
                    ssaInfo = AffiliatePromotionBottomSheetParams.SSAInfo(
                        product.ssaStatus.orFalse(),
                        product.ssaMessage.orEmpty(),
                        product.message.orEmpty(),
                        AffiliatePromotionBottomSheetParams.SSAInfo.Label(
                            labelText = product.label?.labelText.orEmpty(),
                            labelType = product.label?.labelType.orEmpty()
                        )
                    )
                )
            }
        }
    }

    private fun sendSelectContentEvent(
        product: AffiliatePerformanceData.GetAffiliateItemsPerformanceList.Data.SectionData.Item
    ) {
        var label =
            if (product.status == AffiliatePerformaSharedProductCardsItemVH.PRODUCT_ACTIVE) {
                AffiliateAnalytics.LabelKeys.ACTIVE
            } else {
                AffiliateAnalytics.LabelKeys.INACTIVE
            }
        if (product.ssaStatus == true) {
            label += " - komisi extra"
        }
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_PRODUCT_DAFTAR_LINK_PRODUK,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE_LINK_HISTORY,
            UserSession(itemView.context).userId,
            product.itemID,
            bindingAdapterPosition + 1,
            product.itemTitle,
            "${product.itemID} - $label",
            AffiliateAnalytics.ItemKeys.AFFILIATE_DAFTAR_LINK_PRODUK
        )
    }
}
