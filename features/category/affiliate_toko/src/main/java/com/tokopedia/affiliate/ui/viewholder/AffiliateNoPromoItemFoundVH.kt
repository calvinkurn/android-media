package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavBarInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateNoPromoItemFoundModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class AffiliateNoPromoItemFoundVH(
    itemView: View,
    private var bottomNavBarClickListener: AffiliateBottomNavBarInterface? = null
) : AbstractViewHolder<AffiliateNoPromoItemFoundModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_no_product_item
    }

    override fun bind(element: AffiliateNoPromoItemFoundModel?) {
        itemView.findViewById<GlobalError>(R.id.home_global_error).run {
            show()
            setButtonFull(true)
            errorIllustration.hide()
            errorSecondaryAction.gone()
            val filterType = element?.filterType ?: "Produk"
            errorTitle.text =
                getString(R.string.no_promoted_products_title, filterType.lowercase())
            errorDescription.text =
                if (filterType.equals("toko", true)) {
                    getString(R.string.no_promoted_toko_message, filterType.lowercase())
                } else {
                    getString(R.string.no_promoted_products_message, filterType.lowercase())
                }
            errorAction.text = getString(R.string.no_promoted_products_cta, filterType)
            setActionClickListener {
                bottomNavBarClickListener?.selectItem(
                    AffiliateActivity.PROMO_MENU,
                    R.id.menu_promo_affiliate,
                    true
                )
            }
        }

    }
}
