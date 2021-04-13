package com.tokopedia.power_merchant.subscribe.view_old.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view_old.adapter.PowerMerchantViewAdapter
import com.tokopedia.power_merchant.subscribe.view_old.constant.PowerMerchantUrl
import com.tokopedia.power_merchant.subscribe.view_old.model.PowerMerchantItemView.*
import com.tokopedia.power_merchant.subscribe.view_old.viewholder.PowerMerchantItemViewHolder.*
import kotlinx.android.synthetic.main.layout_power_merchant_feature.view.*

class PowerMerchantFeatureView: LinearLayout {

    private var tracker: PowerMerchantTracking? = null

    constructor (context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.layout_power_merchant_feature, this)
    }

    fun show(powerMerchantStatus: PowerMerchantStatus, tracker: PowerMerchantTracking) {
        val data = powerMerchantStatus.goldGetPmOsStatus.result.data
        val powerMerchantInactive = data.isPowerMerchantInactive()

        setTracker(tracker)
        setupTitle(powerMerchantInactive)
        setupFeatureList(powerMerchantInactive)
        showLayout()
    }

    private fun setTracker(tracker: PowerMerchantTracking) {
        this.tracker = tracker
    }

    private fun setupTitle(powerMerchantInactive: Boolean) {
        textTitle.text = if(powerMerchantInactive) {
            context.getString(R.string.power_merchant_exclusive_feature)
        } else {
            context.getString(R.string.power_merchant_benefit_feature)
        }
    }

    private fun setupFeatureList(powerMerchantInactive: Boolean) {
        val listener = createViewHolderListener()
        val adapter = PowerMerchantViewAdapter(listener)
        val features = mutableListOf<PowerMerchantFeature>()

        if(powerMerchantInactive) {
            val freeShipping = PowerMerchantFeature(
                R.string.power_merchant_bebas_ongkir,
                R.string.power_merchant_bebas_ongkir_description,
                R.drawable.ic_pm_free_shipping,
                R.string.power_merchant_free_shipping_clickable_text,
                PowerMerchantUrl.URL_FREE_SHIPPING_TERMS_AND_CONDITION
            )
            features.add(freeShipping)
        }

        val featuredFeatureBoldText = listOf(
            R.string.power_merchant_featured_feature_description_bold_1,
            R.string.power_merchant_featured_feature_description_bold_2
        )

        features.addAll(listOf(
            PowerMerchantFeature(
                R.string.power_merchant_featured_feature,
                R.string.power_merchant_featured_feature_description,
                R.drawable.ic_pm_featured_badge,
                R.string.power_merchant_featured_feature_clickable_text,
                PowerMerchantUrl.URL_LEARN_MORE_BENEFIT,
                featuredFeatureBoldText
            ),
            PowerMerchantFeature(
                R.string.power_merchant_premium_account,
                R.string.power_merchant_premium_account_description,
                R.drawable.ic_pm_premium_account,
                R.string.power_merchant_premium_account_clickable_text,
                PowerMerchantUrl.URL_PREMIUM_ACCOUNT
            )
        ))

        with(listFeature) {
            this.adapter = adapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(FeatureItemDecoration())
        }

        adapter.items = features
    }

    private fun createViewHolderListener() = object : PmViewHolderListener {
        override fun onItemClicked(title: Int) {
            when (title) {
                R.string.power_merchant_bebas_ongkir -> trackClickFreeShippingTnC()
            }
        }
    }

    private fun trackClickFreeShippingTnC() {
        tracker?.eventClickFreeShippingTnC()
    }

    private fun showLayout() {
        visibility = View.VISIBLE
    }
}