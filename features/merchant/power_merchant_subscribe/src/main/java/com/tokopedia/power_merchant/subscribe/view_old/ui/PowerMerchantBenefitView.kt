package com.tokopedia.power_merchant.subscribe.view_old.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view_old.adapter.PowerMerchantViewAdapter
import com.tokopedia.power_merchant.subscribe.view_old.constant.PowerMerchantUrl
import com.tokopedia.power_merchant.subscribe.view_old.model.PowerMerchantItemView.PowerMerchantBenefit
import com.tokopedia.power_merchant.subscribe.view_old.viewholder.PowerMerchantItemViewHolder.*
import kotlinx.android.synthetic.main.layout_power_merchant_benefit.view.*

class PowerMerchantBenefitView: ConstraintLayout {

    private var powerMerchantTracking: PowerMerchantTracking? = null

    constructor (context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.layout_power_merchant_benefit, this)
    }

    fun show(powerMerchantTracking: PowerMerchantTracking) {
        setupBenefitList()
        setOnClickTextLearnMore()
        setTracker(powerMerchantTracking)
        showLayout()
    }

    private fun setupBenefitList() {
        val listener = createViewHolderListener()
        val adapter = PowerMerchantViewAdapter(listener)

        val benefits = listOf(
            PowerMerchantBenefit(
                    R.string.power_merchant_shop_benefit,
                    R.string.power_merchant_shop_benefit_description,
                    com.tokopedia.gm.common.R.drawable.ic_power_merchant
            ),
            PowerMerchantBenefit(
                R.string.power_merchant_top_ads_benefit,
                R.string.power_merchant_top_ads_benefit_description,
                R.drawable.ic_pm_star_rounded
            ),
            PowerMerchantBenefit(
                R.string.power_merchant_cost_benefit,
                R.string.power_merchant_cost_benefit_description,
                R.drawable.ic_pm_star_rounded
            ),
            PowerMerchantBenefit(
                R.string.power_merchant_broadcast_benefit,
                R.string.power_merchant_broadcast_benefit_description,
                R.drawable.ic_pm_broadcast_chat,
                R.string.power_merchant_broadcast_chat_clickable_text,
                PowerMerchantUrl.URL_BROADCAST_CHAT_TERMS_AND_CONDITION
            )
        )

        with(listBenefit) {
            this.adapter = adapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        adapter.items = benefits
    }

    private fun createViewHolderListener() = object : PmViewHolderListener {
        override fun onItemClicked(title: Int) {
            when (title) {
                R.string.power_merchant_broadcast_benefit -> trackClickBroadcastTnC()
            }
        }
    }

    private fun trackClickBroadcastTnC() {
        powerMerchantTracking?.eventClickBroadcastTnC()
    }

    private fun setOnClickTextLearnMore() {
        textLearnMore.setOnClickListener {
            trackClickLearnMore()
            goToLearnMorePage()
        }
    }

    private fun setTracker(powerMerchantTracking: PowerMerchantTracking) {
        this.powerMerchantTracking = powerMerchantTracking
    }

    private fun trackClickLearnMore() {
        powerMerchantTracking?.eventClickLearnMoreUpgradeShop()
    }

    private fun goToLearnMorePage() {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW,
            PowerMerchantUrl.URL_LEARN_MORE_BENEFIT)
    }

    private fun showLayout() {
        visibility = View.VISIBLE
    }
}