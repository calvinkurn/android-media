package com.tokopedia.power_merchant.subscribe.view_old.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view_old.model.PowerMerchantFreeShippingStatus
import kotlinx.android.synthetic.main.layout_power_merchant_free_shipping.view.*
import kotlinx.android.synthetic.main.layout_power_merchant_free_shipping_inactive.view.*

class PowerMerchantFreeShippingView: FrameLayout {

    var tracker: PowerMerchantTracking? = null
    var onClickListener: (() -> Unit)? = null

    constructor (context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.layout_power_merchant_free_shipping, this)
    }

    fun show(freeShipping: PowerMerchantFreeShippingStatus) {
        val transitionPeriod = freeShipping.isTransitionPeriod
        val isPowerMerchantIdle = freeShipping.isPowerMerchantIdle
        showHideContainer(transitionPeriod)

        when {
            transitionPeriod -> showTransitionPeriod()
            freeShipping.isActive || isPowerMerchantIdle -> {
                showRegisteredFreeShippingWidget(isPowerMerchantIdle)
            }
            else -> showNotRegisteredFreeShippingWidget(freeShipping.isEligible)
        }

        showLayout()
    }

    private fun showTransitionPeriod() {
        val description = context.getString(R.string.power_merchant_free_shipping_transition_description)
        transitionLayout.textDescription.text = MethodChecker.fromHtml(description)

        transitionLayout.setOnClickListener {
            onClickListener?.invoke()
        }

        transitionLayout.show()
    }

    private fun showRegisteredFreeShippingWidget(isPowerMerchantIdle: Boolean) {
        setActiveTitleText(isPowerMerchantIdle)
        setActiveDescriptionText(isPowerMerchantIdle)
        setActiveLayoutClickListener()

        activeLayout.show()
        inActiveLayout.hide()
        transitionLayout.hide()
    }

    private fun showNotRegisteredFreeShippingWidget(isEligible: Boolean) {
        setInactiveTitleText(isEligible)
        setInactiveDescriptionText(isEligible)
        setCTABtnText(isEligible)
        setCTABtnClickListener(isEligible)

        inActiveLayout.show()
        activeLayout.hide()
        transitionLayout.hide()
    }

    private fun setActiveTitleText(isPowerMerchantIdle: Boolean) {
        val title = if(isPowerMerchantIdle) {
            context.getString(R.string.power_merchant_free_shipping_shop_score_title)
        } else  {
            context.getString(R.string.power_merchant_free_shipping_title)
        }
        activeLayout.textTitle.text = MethodChecker.fromHtml(title)
    }

    private fun setActiveDescriptionText(isPowerMerchantIdle: Boolean) {
        activeLayout.textDescription.text = if(isPowerMerchantIdle) {
            context.getString(R.string.power_merchant_free_shipping_shop_score_description)
        } else  {
            context.getString(R.string.power_merchant_free_shipping_active_description)
        }
    }

    private fun setInactiveDescriptionText(eligible: Boolean) {
        inActiveLayout.textDescription.text = if(eligible) {
            MethodChecker.fromHtml(context.getString(R.string.power_merchant_free_shipping_description))
        } else {
            context.getString(R.string.power_merchant_free_shipping_not_eligible_description)
        }
    }

    private fun setInactiveTitleText(eligible: Boolean) {
        inActiveLayout.textTitle.text = if(eligible) {
            context.getString(R.string.power_merchant_free_shipping_eligible_title)
        } else {
            context.getString(R.string.power_merchant_free_shipping_not_eligible_title)
        }
    }


    private fun setCTABtnText(eligible: Boolean) {
        btnCTA.text = if(eligible) {
            context.getString(R.string.power_merchant_free_shipping_activate)
        } else {
            context.getString(R.string.power_merchant_free_shipping_learn_more)
        }
    }

    private fun setCTABtnClickListener(eligible: Boolean) {
        btnCTA.setOnClickListener {
            if(!eligible) {
                trackLearMoreFreeShipping()
            }
            onClickListener?.invoke()
        }
    }

    private fun trackLearMoreFreeShipping() {
        tracker?.eventLearMoreFreeShipping()
    }

    private fun setActiveLayoutClickListener() {
        activeLayout.setOnClickListener {
            onClickListener?.invoke()
        }
    }

    private fun showHideContainer(transitionPeriod: Boolean) {
        if(transitionPeriod) {
            container.hide()
        } else {
            container.show()
        }
    }

    private fun showLayout() {
        visibility = View.VISIBLE
    }
}