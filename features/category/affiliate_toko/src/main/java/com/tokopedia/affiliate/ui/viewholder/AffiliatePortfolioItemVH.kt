package com.tokopedia.affiliate.ui.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.util.PatternsCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.PortfolioUrlTextUpdateInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioUrlModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.TextFieldUnify2

class AffiliatePortfolioItemVH(
    itemView: View,
    private val portfolioUrlTextUpdateInterface: PortfolioUrlTextUpdateInterface?
) :
    AbstractViewHolder<AffiliatePortfolioUrlModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_input_portfolio_text_field_item
    }
    private val urlEtView: TextFieldUnify2 = itemView.findViewById<TextFieldUnify2>(R.id.social_link_et)
    private var data: AffiliatePortfolioUrlModel? = null
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            data?.portfolioItm?.firstTime = false
            portfolioUrlTextUpdateInterface?.onUrlUpdate(
                bindingAdapterPosition,
                s.toString()
            )

            if (s.toString().isNotEmpty()) {
                data?.portfolioItm?.isError = !isValidUrl(s.toString(), data)
            } else {
                data?.portfolioItm?.isError = false
            }
            setState(data)
        }

        override fun afterTextChanged(s: Editable?) = Unit
    }
    override fun bind(element: AffiliatePortfolioUrlModel?) {
        setData(element)
        setEtListeners(element)
        setKeyListeners()
    }

    private fun setKeyListeners() {
        urlEtView.editText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if ((event?.action == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)
                ) {
                    portfolioUrlTextUpdateInterface?.onNextKeyPressed(bindingAdapterPosition, true)
                    return true
                }
                return false
            }
        })
    }

    private fun setEtListeners(element: AffiliatePortfolioUrlModel?) {
        setTextWatchers()
        setFocus(element)
    }

    private fun setFocus(element: AffiliatePortfolioUrlModel?) {
        if (element?.portfolioItm?.isFocus == true) {
            urlEtView.editText.requestFocus()
        }
        urlEtView.editText.setSelection(urlEtView.editText.text.length)
    }

    private fun setTextWatchers() {
        urlEtView.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                urlEtView.editText.addTextChangedListener(textWatcher)
            } else {
                urlEtView.editText.removeTextChangedListener(textWatcher)
            }
        }
    }

    private fun setData(element: AffiliatePortfolioUrlModel?) {
        data = element
        element?.portfolioItm?.title?.let { urlEtView.setLabel(it) }
        element?.portfolioItm?.text?.let { urlEtView.editText.setText(it) }
        setState(element)
    }

    private fun isValidUrl(text: String, element: AffiliatePortfolioUrlModel?): Boolean {
        return if (element?.portfolioItm?.regex != null) {
            val regex = Regex(element.portfolioItm.regex!!, setOf(RegexOption.IGNORE_CASE))
            regex.matches(text) && PatternsCompat.WEB_URL.matcher(text).matches()
        } else {
            PatternsCompat.WEB_URL.matcher(text).matches()
        }
    }

    private fun setState(element: AffiliatePortfolioUrlModel?) {
        urlEtView.isInputError = element?.portfolioItm?.isError == true
        if (element?.portfolioItm?.isError == true && element.portfolioItm.firstTime == false) {
            element.portfolioItm.errorContent?.let { message ->
                urlEtView.setMessage(message)
            }
        } else {
            element?.portfolioItm?.successContent?.let { message ->
                urlEtView.setMessage(message)
            }
        }
    }
}
