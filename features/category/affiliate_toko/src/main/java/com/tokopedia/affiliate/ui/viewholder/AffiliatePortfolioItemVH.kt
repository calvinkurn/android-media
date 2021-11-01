package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import android.webkit.URLUtil.isValidUrl
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.PortfolioUrlTextUpdateInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioUrlModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.TextFieldUnify2

class AffiliatePortfolioItemVH(itemView: View,private val onFocusChangeInterface: PortfolioUrlTextUpdateInterface?)
    : AbstractViewHolder<AffiliatePortfolioUrlModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_input_portfolio_text_field_item
    }
    val urlEtView=itemView.findViewById<TextFieldUnify2>(R.id.social_link_et)
    override fun bind(element: AffiliatePortfolioUrlModel?) {
        element?.portfolioItm?.title?.let { urlEtView.setLabel(it) }
        setState(element)
        urlEtView.addOnFocusChangeListener={_, hasFocus->
            if(!hasFocus){
                if(isValidUrl(urlEtView.getEditableValue().toString())) {
                    setState(element)
                    onFocusChangeInterface?.onUrlUpdate(
                        adapterPosition,
                        urlEtView.getEditableValue().toString()
                    )
                }
                else{
                    urlEtView.isInputError=true
                    urlEtView.setMessage("Link tidak valid.")
                }
            }
        }
    }

    private fun setState(element: AffiliatePortfolioUrlModel?) {
        urlEtView.isInputError = element?.portfolioItm?.isError==true
        element?.portfolioItm?.content?.let { message->
            urlEtView.setMessage(message)
        }
    }
}
