package com.tokopedia.recharge_pdp_emoney.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_emoney_header_view.view.*
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 29/03/21
 */
class EmoneyPdpHeaderViewWidget @JvmOverloads constructor(@NotNull context: Context,
                                                          attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_emoney_header_view, this, true)
    }

    var titleText: String = ""
        set(title) {
            field = title
            emoneyHeaderViewTitle.text = title
        }

    var subtitleText: String = ""
        set(subtitle) {
            field = subtitle
            emoneyHeaderViewSubtitle.text = subtitle
        }

    var buttonCtaText: String = ""
        set(buttonText) {
            field = buttonText
            emoneyHeaderViewCtaButton.text = buttonText
        }

    fun setEmoneyHeaderViewButtonListener(listener: () -> Unit) {
        emoneyHeaderViewCtaButton.setOnClickListener { listener.invoke() }
    }
}