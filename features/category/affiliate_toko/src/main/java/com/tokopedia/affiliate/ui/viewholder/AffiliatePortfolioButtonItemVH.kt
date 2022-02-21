package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.AddSocialInterface
import com.tokopedia.affiliate.interfaces.PortfolioClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioButtonModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.UnifyButton

class AffiliatePortfolioButtonItemVH(itemView: View,
                                     private val portfolioClickInterface: PortfolioClickInterface?)
    : AbstractViewHolder<AffiliatePortfolioButtonModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_input_portfolio_button_item
    }

    override fun bind(element: AffiliatePortfolioButtonModel?) {
        itemView.findViewById<UnifyButton>(R.id.add_social_media_btn).apply {
            text = element?.buttonData?.text
            buttonType = element?.buttonData?.buttonType ?: UnifyButton.Type.MAIN
            buttonVariant = element?.buttonData?.buttonVariant ?: UnifyButton.Variant.FILLED

            if(element?.buttonData?.isSaveSocial == false){
                setOnClickListener {
                    portfolioClickInterface?.addSocialMediaButtonClicked()
                }
            }
        }
    }
}
