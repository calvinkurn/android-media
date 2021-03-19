package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.digital_checkout.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.layout_digital_checkout_bottom_view.view.*
import org.jetbrains.annotations.NotNull

class DigitalCheckoutBottomViewWidget @JvmOverloads constructor(@NotNull context: Context,
                                                                attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_digital_checkout_bottom_view, this, true)
    }

    var totalPayment: String = ""
        set(total) {
            field = total
            tvTotalPayment.text = total
        }

    var checkoutButtonText: String = ""
        set(value) {
            field = value
            btnCheckout.text = value
        }

    var isCheckoutButtonEnabled: Boolean = true
        set(isEnabled) {
            field = isEnabled
            btnCheckout.isEnabled = isEnabled
        }

    fun setCheckoutButtonListener(listener: () -> Unit) {
        btnCheckout.setOnClickListener { listener.invoke() }
    }
}