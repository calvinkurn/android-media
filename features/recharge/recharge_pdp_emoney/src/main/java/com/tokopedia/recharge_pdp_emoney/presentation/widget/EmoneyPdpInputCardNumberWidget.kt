package com.tokopedia.recharge_pdp_emoney.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 29/03/21
 */

class EmoneyPdpInputCardNumberWidget @JvmOverloads constructor(@NotNull context: Context,
                                                               attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_emoney_input_card_number, this, true)
    }

    fun initView() {

    }
}
