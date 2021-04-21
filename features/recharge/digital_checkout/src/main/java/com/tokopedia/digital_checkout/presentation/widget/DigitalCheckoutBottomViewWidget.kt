package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.digital_checkout.R
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.layout_digital_checkout_bottom_view.view.*
import org.jetbrains.annotations.NotNull

class DigitalCheckoutBottomViewWidget @JvmOverloads constructor(@NotNull context: Context,
                                                                attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_digital_checkout_bottom_view, this, true)
    }

    var promoButtonTitle: String = ""
        set(title) {
            field = title
            digitalPromoBtnView.title = title
        }

    var promoButtonDescription: String = ""
        set(desc) {
            field = desc
            digitalPromoBtnView.desc = desc
        }

    var promoButtonState: ButtonPromoCheckoutView.State = ButtonPromoCheckoutView.State.ACTIVE
        set(state) {
            field = state
            digitalPromoBtnView.state = state
        }

    var promoButtonChevronIcon: Int = 0
        set(value) {
            field = value
            digitalPromoBtnView.chevronIcon = value
        }

    var totalPayment: String = ""
        set(total) {
            field = total
            tvTotalPayment.text = total
        }

    var promoButtonVisibility: Int = View.VISIBLE
        set(visibility) {
            field = visibility
            digitalPromoBtnView.visibility = visibility
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

    fun setDigitalPromoButtonListener(listener: () -> Unit) {
        digitalPromoBtnView.setOnClickListener { listener.invoke() }
    }

    fun setButtonChevronIconListener(listener: () -> Unit) {
        digitalPromoBtnView.setListenerChevronIcon { listener.invoke() }
    }

    fun setCheckoutButtonListener(listener: () -> Unit) {
        btnCheckout.setOnClickListener { listener.invoke() }
    }
}