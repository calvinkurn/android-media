package com.tokopedia.power_merchant.subscribe.view.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.constant.PowerMerchantUrl.URL_FREE_SHIPPING_TERMS_AND_CONDITION
import com.tokopedia.power_merchant.subscribe.view.model.PowerMerchantFreeShippingStatus
import kotlinx.android.synthetic.main.layout_power_merchant_free_shipping.view.*
import kotlinx.android.synthetic.main.layout_power_merchant_free_shipping_inactive.view.*

class PowerMerchantFreeShippingView: FrameLayout {

    constructor (context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.layout_power_merchant_free_shipping, this)
    }

    fun show(freeShipping: PowerMerchantFreeShippingStatus) {
        val transitionPeriod = freeShipping.isTransitionPeriod
        showHideContainer(transitionPeriod)

        when {
            transitionPeriod -> showTransitionPeriod()
            freeShipping.isActive -> showActiveFreeShipping(freeShipping.isShopScoreEligible)
            else -> showInactiveFreeShipping(freeShipping.isEligible)
        }

        showLayout()
    }

    private fun showTransitionPeriod() {
        val description = context.getString(R.string.power_merchant_free_shipping_transition_description)
        transitionLayout.textDescription.text = MethodChecker.fromHtml(description)

        transitionLayout.setOnClickListener {
            openFreeShippingPage()
        }

        transitionLayout.show()
    }

    private fun showActiveFreeShipping(shopScoreEligible: Boolean) {
        setActiveTitleText(shopScoreEligible)
        setActiveDescriptionText(shopScoreEligible)
        setActiveLayoutClickListener()

        activeLayout.show()
        inActiveLayout.hide()
        transitionLayout.hide()
    }

    private fun showInactiveFreeShipping(isEligible: Boolean) {
        setInactiveTitleText(isEligible)
        setInactiveDescriptionText(isEligible)
        setCTABtnText(isEligible)
        setCTABtnClickListener()

        inActiveLayout.show()
        activeLayout.hide()
        transitionLayout.hide()
    }

    private fun setActiveTitleText(shopScoreEligible: Boolean) {
        val title = if(shopScoreEligible) {
            context.getString(R.string.power_merchant_free_shipping_title)
        } else  {
            context.getString(R.string.power_merchant_free_shipping_shop_score_title)
        }
        activeLayout.textTitle.text = MethodChecker.fromHtml(title)
    }

    private fun setActiveDescriptionText(shopScoreEligible: Boolean) {
        activeLayout.textDescription.text = if(shopScoreEligible) {
            context.getString(R.string.power_merchant_free_shipping_active_description)
        } else  {
            context.getString(R.string.power_merchant_free_shipping_shop_score_description)
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

    private fun setCTABtnClickListener() {
        btnCTA.setOnClickListener {
            openFreeShippingPage()
        }
    }

    private fun setActiveLayoutClickListener() {
        activeLayout.setOnClickListener {
            openFreeShippingPage()
        }
    }

    private fun openFreeShippingPage() {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW,
            URL_FREE_SHIPPING_TERMS_AND_CONDITION)
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