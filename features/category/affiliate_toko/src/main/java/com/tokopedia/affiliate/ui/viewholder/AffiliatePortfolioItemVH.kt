package com.tokopedia.affiliate.ui.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.webkit.URLUtil
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.PortfolioUrlTextUpdateInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioUrlModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.TextFieldUnify2

class AffiliatePortfolioItemVH(itemView: View,private val portfolioUrlTextUpdateInterface: PortfolioUrlTextUpdateInterface?)
    : AbstractViewHolder<AffiliatePortfolioUrlModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_input_portfolio_text_field_item
    }
    private val urlEtView: TextFieldUnify2 = itemView.findViewById<TextFieldUnify2>(R.id.social_link_et)
    override fun bind(element: AffiliatePortfolioUrlModel?) {
        element?.portfolioItm?.title?.let { urlEtView.setLabel(it) }
        element?.portfolioItm?.text?.let { urlEtView.editText.setText(it) }
        setState(element)
        urlEtView.editText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                portfolioUrlTextUpdateInterface?.onUrlUpdate(adapterPosition,
                    s.toString())
                if(s.toString().isNotEmpty()){
                    element?.portfolioItm?.isError = !Patterns.WEB_URL.matcher(s.toString()).matches()
                }else {
                    element?.portfolioItm?.isError = false
                }
                setState(element)
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        if(element?.portfolioItm?.isFocus == true){
            urlEtView.editText.requestFocus()
        }

        urlEtView.editText.setOnKeyListener(object  : View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if ((event?.action == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    portfolioUrlTextUpdateInterface?.onNextKeyPressed(adapterPosition,true)
                    return true
                }
                return false
            }
        })
    }

    private fun setState(element: AffiliatePortfolioUrlModel?) {
        urlEtView.isInputError = element?.portfolioItm?.isError == true
        if(element?.portfolioItm?.isError == true) {
            element.portfolioItm.errorContent?.let { message ->
                urlEtView.setMessage(message)
            }
        }
        else{
            element?.portfolioItm?.successContent?.let { message ->
                urlEtView.setMessage(message)
            }
        }
    }
}
