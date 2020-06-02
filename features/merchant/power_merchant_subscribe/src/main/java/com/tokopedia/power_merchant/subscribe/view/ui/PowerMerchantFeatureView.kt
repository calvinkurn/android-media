package com.tokopedia.power_merchant.subscribe.view.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.adapter.PowerMerchantViewAdapter
import com.tokopedia.power_merchant.subscribe.view.constant.PowerMerchantUrl
import com.tokopedia.power_merchant.subscribe.view.model.PowerMerchantItemView.*
import kotlinx.android.synthetic.main.layout_power_merchant_feature.view.*

class PowerMerchantFeatureView: LinearLayout {

    constructor (context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    private val adapter by lazy { PowerMerchantViewAdapter() }

    init {
        inflate(context, R.layout.layout_power_merchant_feature, this)
    }

    fun show(powerMerchantStatus: PowerMerchantStatus) {
        setupFeatureList()
        setupLearnMoreBtn(powerMerchantStatus)
        showLayout()
    }

    private fun setupFeatureList() {
        val features = listOf(
            PowerMerchantFeature(
                R.string.power_merchant_bebas_ongkir,
                R.string.power_merchant_bebas_ongkir_description,
                R.drawable.ic_free_shipping,
                R.string.power_merchant_free_shipping_clickable_text,
                PowerMerchantUrl.URL_FREE_SHIPPING_TERMS_AND_CONDITION
            ),
            PowerMerchantFeature(
                R.string.power_merchant_featured_feature,
                R.string.power_merchant_featured_feature_description,
                R.drawable.ic_pm_featured_badge
            )
        )

        with(listFeature) {
            isNestedScrollingEnabled = false
            adapter = this@PowerMerchantFeatureView.adapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(FeatureItemDecoration())
        }

        adapter.items = features
    }

    private fun setupLearnMoreBtn(powerMerchantStatus: PowerMerchantStatus) {
        val shopStatus = powerMerchantStatus.goldGetPmOsStatus.result.data
        val shouldShow = shopStatus.isPowerMerchantActive() || shopStatus.isPowerMerchantIdle()

        if(shouldShow) {
            btnLearnMore.show()
            btnLearnMore.setOnClickListener {
                goToLearnMorePage()
            }
        } else {
            btnLearnMore.hide()
        }
    }

    private fun goToLearnMorePage() {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW,
            PowerMerchantUrl.URL_LEARN_MORE_BENEFIT)
    }

    private fun showLayout() {
        visibility = View.VISIBLE
    }
}