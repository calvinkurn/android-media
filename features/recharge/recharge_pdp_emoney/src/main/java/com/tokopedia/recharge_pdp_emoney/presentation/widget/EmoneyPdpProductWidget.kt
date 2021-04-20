package com.tokopedia.recharge_pdp_emoney.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_emoney_pdp_product_list.view.*
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 31/03/21
 */
class EmoneyPdpProductWidget @JvmOverloads constructor(@NotNull context: Context,
                                                       attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_emoney_pdp_product_list, this, true)
    }

    var titleText: String = ""
        set(title) {
            field = title
            emoneyProductWidgetTitle.text = title
        }
}