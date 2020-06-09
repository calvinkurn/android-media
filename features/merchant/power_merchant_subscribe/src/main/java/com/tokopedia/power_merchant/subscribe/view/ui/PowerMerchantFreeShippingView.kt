package com.tokopedia.power_merchant.subscribe.view.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.layout_power_merchant_free_shipping.view.*
import kotlinx.android.synthetic.main.layout_power_merchant_free_shipping_inactive.view.*

class PowerMerchantFreeShippingView: FrameLayout {

    constructor (context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.layout_power_merchant_free_shipping, this)
    }

    fun show(isActive: Boolean, isEligible: Boolean, isTransitionPeriod: Boolean) {
        when {
            isTransitionPeriod -> showTransitionPeriodLayout()
            isActive -> showActiveFreeShippingLayout()
            else -> showInactiveFreeShippingLayout(isEligible)
        }
    }

    private fun showTransitionPeriodLayout() {
        activeLayout.textTitle.text = context
            .getString(R.string.power_merchant_free_shipping_transition_title)
        activeLayout.textDescription.text = context
            .getString(R.string.power_merchant_free_shipping_transition_description)

        activeLayout.show()
        inActiveLayout.hide()
    }

    private fun showActiveFreeShippingLayout() {
        setActiveDescriptionText()

        activeLayout.show()
        inActiveLayout.hide()
    }

    private fun showInactiveFreeShippingLayout(isEligible: Boolean) {
        setInactiveTitleText(isEligible)
        setInactiveDescriptionText()
        setCTABtnText(isEligible)

        inActiveLayout.show()
        activeLayout.hide()
    }

    private fun setActiveDescriptionText() {
        activeLayout.textDescription.text = context
            .getString(R.string.power_merchant_free_shipping_description)
    }

    private fun setInactiveDescriptionText() {
        inActiveLayout.textDescription.text = context
            .getString(R.string.power_merchant_free_shipping_description)
    }

    private fun setInactiveTitleText(eligible: Boolean) {
        inActiveLayout.textTitle.text = if (eligible) {
            context.getString(R.string.power_merchant_free_shipping_eligible_title)
        } else {
            context.getString(R.string.power_merchant_free_shipping_not_eligible_title)
        }
    }

    private fun setCTABtnText(eligible: Boolean) {
        inActiveLayout.btnCTA.text = if (eligible) {
            context.getString(R.string.power_merchant_free_shipping_activate)
        } else {
            context.getString(R.string.power_merchant_learn_more)
        }
    }
}