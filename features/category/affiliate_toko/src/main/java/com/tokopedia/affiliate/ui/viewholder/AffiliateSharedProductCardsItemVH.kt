package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateSharedProductCardsModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class AffiliateSharedProductCardsItemVH(itemView: View, private val productClickInterface: ProductClickInterface?)
    : AbstractViewHolder<AffiliateSharedProductCardsModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_vertical_product_card_item_layout

        const val PRODUCT_ACTIVE = 1
    }

    override fun bind(element: AffiliateSharedProductCardsModel?) {
        element?.product?.let { product ->
            itemView.findViewById<ImageUnify>(R.id.product_image)?.setImageUrl(product.image?.androidURL ?: "")
            itemView.findViewById<Typography>(R.id.product_name)?.text = product.itemTitle
            if (product.status == PRODUCT_ACTIVE) {
                itemView.findViewById<ImageUnify>(R.id.status_bullet)?.setImageDrawable(MethodChecker.getDrawable(itemView.context, R.drawable.affiliate_circle_active))
                itemView.findViewById<Typography>(R.id.product_status)?.run {
                    setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                    text = getString(R.string.affiliate_active)
                }
            } else {
                itemView.findViewById<ImageUnify>(R.id.status_bullet)?.setImageDrawable(MethodChecker.getDrawable(itemView.context, R.drawable.affiliate_circle_inactive))
                itemView.findViewById<Typography>(R.id.product_status)?.run {
                    setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN500))
                    text = getString(R.string.affiliate_inactive)
                }
            }
            itemView.findViewById<Typography>(R.id.shop_name)?.text = product.footer?.firstOrNull()?.footerText
            itemView.setOnClickListener {
                productClickInterface?.onProductClick(product.itemID, product.itemTitle ?: "", product.image?.androidURL
                        ?: "", product.defaultLinkURL ?: "", product.itemID, product.status ?: 0)
            }
        }
    }
}
