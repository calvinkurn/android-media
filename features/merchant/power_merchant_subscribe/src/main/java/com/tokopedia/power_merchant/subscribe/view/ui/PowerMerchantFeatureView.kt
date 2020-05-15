package com.tokopedia.power_merchant.subscribe.view.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        initView()
    }

    private fun initView() {
        inflate(context, R.layout.layout_power_merchant_feature, this)

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
}