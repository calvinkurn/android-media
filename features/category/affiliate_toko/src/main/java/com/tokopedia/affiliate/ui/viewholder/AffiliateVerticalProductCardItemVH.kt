package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateProductCardVHViewModel
import kotlinx.android.synthetic.main.affiliate_vertical_product_card_item_layout.view.*

class AffiliateVerticalProductCardItemVH(itemView: View)
    : AbstractViewHolder<AffiliateProductCardVHViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_vertical_product_card_item_layout

        const val PRODUCT_ACTIVE = 1
    }

    override fun bind(element: AffiliateProductCardVHViewModel?) {
        element?.product?.let { product->
            itemView.product_image.setImageUrl(product.image?.android ?: "")
            itemView.product_name.text = product.title
            if(product.status == PRODUCT_ACTIVE){
                itemView.status_bullet.setImageDrawable(MethodChecker.getDrawable(itemView.context, R.drawable.affiliate_circle_active))
                itemView.product_status.setTextColor(MethodChecker.getColor(itemView.context, R.color.unify_G500))
                itemView.product_status.text = getString(R.string.affiliate_active)
            } else {
                itemView.status_bullet.setImageDrawable(MethodChecker.getDrawable(itemView.context, R.drawable.affiliate_circle_inactive))
                itemView.product_status.setTextColor(MethodChecker.getColor(itemView.context, R.color.Unify_NN500))
                itemView.product_status.text = getString(R.string.affiliate_inactive)
            }
            itemView.shop_name.text = product.footer.firstOrNull()?.footerText
        }
    }
}
