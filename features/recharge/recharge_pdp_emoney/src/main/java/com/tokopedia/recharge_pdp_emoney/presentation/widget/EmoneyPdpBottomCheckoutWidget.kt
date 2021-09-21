package com.tokopedia.recharge_pdp_emoney.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_emoney_pdp_checkout_view.view.*
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 30/04/21
 */
class EmoneyPdpBottomCheckoutWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                              defStyleAttr: Int = 0) : BaseCustomView(context, attrs, defStyleAttr) {

    var listener: ActionListener? = null
        set(value) {
            field = value
            listener?.run {
                emoneyPdpCheckoutViewButton.setOnClickListener {
                    onClickNextBuyButton()
                }
            }
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_emoney_pdp_checkout_view, this, true)
    }

    fun onBuyButtonLoading(isLoading: Boolean) {
        emoneyPdpCheckoutViewButton.isLoading = isLoading
    }

    fun setVisibilityLayout(show: Boolean) {
        if (show) {
            emoneyPdpCheckoutViewLayout.show()
        } else {
            emoneyPdpCheckoutViewLayout.hide()
        }
    }

    fun setTotalPrice(price: String) {
        emoneyPdpCheckoutViewTotalPayment.text = price
    }

    interface ActionListener {
        fun onClickNextBuyButton()
    }

}