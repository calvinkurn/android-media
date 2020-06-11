package com.tokopedia.power_merchant.subscribe.view.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
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

    fun show(status: PowerMerchantFreeShippingStatus) {
        when {
            status.isTransitionPeriod -> showTransitionPeriod()
            status.isActive -> showActiveFreeShipping(status.isShopScoreEligible)
            else -> showInactiveFreeShipping(status.isEligible)
        }
        showLayout()
    }

    private fun showTransitionPeriod() {
        activeLayout.textTitle.text = context
            .getString(R.string.power_merchant_free_shipping_transition_title)
        activeLayout.textDescription.text = context
            .getString(R.string.power_merchant_free_shipping_transition_description)
        setActiveLayoutClickListener()

        activeLayout.show()
        inActiveLayout.hide()
    }

    private fun showActiveFreeShipping(shopScoreEligible: Boolean) {
        setActiveTitleText(shopScoreEligible)
        setActiveDescriptionText(shopScoreEligible)
        setActiveLayoutClickListener()

        activeLayout.show()
        inActiveLayout.hide()
    }

    private fun showInactiveFreeShipping(isEligible: Boolean) {
        setInactiveTitleText(isEligible)
        setInactiveDescriptionText()
        setCTABtnClickListener()

        inActiveLayout.show()
        activeLayout.hide()
    }

    private fun setActiveTitleText(shopScoreEligible: Boolean) {
        activeLayout.textTitle.text = if(shopScoreEligible) {
            context.getString(R.string.power_merchant_free_shipping_title)
        } else  {
            MethodChecker.fromHtml(
                context.getString(R.string.power_merchant_free_shipping_shop_score_title))
        }
    }

    private fun setActiveDescriptionText(shopScoreEligible: Boolean) {
        activeLayout.textDescription.text = if(shopScoreEligible) {
            context.getString(R.string.power_merchant_free_shipping_description)
        } else  {
            context.getString(R.string.power_merchant_free_shipping_shop_score_description)
        }
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
        RouteManager.route(context, ApplinkConst.WEBVIEW, URL_FREE_SHIPPING_TERMS_AND_CONDITION)
    }

    private fun showLayout() {
        visibility = View.VISIBLE
    }
}