package com.tokopedia.product.detail.estimasiongkir.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.estimasiongkir.data.constant.RatesEstimationConstant
import com.tokopedia.product.detail.estimasiongkir.di.DaggerRatesEstimationComponent
import com.tokopedia.product.detail.estimasiongkir.di.RatesEstimationComponent
import com.tokopedia.product.detail.estimasiongkir.di.RatesEstimationModule
import com.tokopedia.product.detail.estimasiongkir.view.fragment.RatesEstimationDetailFragment

class RatesEstimationDetailActivity : BaseSimpleActivity(), HasComponent<RatesEstimationComponent> {

    override fun getNewFragment(): Fragment {
        val shopDomain = intent.getStringExtra(RatesEstimationConstant.PARAM_SHOP_DOMAIN)
        val weight = intent.getFloatExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, 0f)
        val weightUnit = intent.getStringExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT_UNIT)
        val forDeliveryInfo = intent.getBooleanExtra(RatesEstimationConstant.PARAM_FOR_DELIVERY_INFO, false)


        return RatesEstimationDetailFragment.createInstance(shopDomain, weight, weightUnit,
                if (intent.hasExtra(RatesEstimationConstant.PARAM_ORIGIN))
                    intent.getStringExtra(RatesEstimationConstant.PARAM_ORIGIN)
                else null, forDeliveryInfo)
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        val forDeliveryInfo = intent.getBooleanExtra(RatesEstimationConstant.PARAM_FOR_DELIVERY_INFO, false)

        if (supportActionBar != null && forDeliveryInfo) {
            supportActionBar!!.title = getString(R.string.delivery_label)
        }
    }

    override fun getComponent(): RatesEstimationComponent {
        return DaggerRatesEstimationComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .ratesEstimationModule(RatesEstimationModule()).build()
    }

    companion object {

        @JvmStatic
        fun createIntent(context: Context, shopDomain: String, productWeight: Float, productWeightUnit: String, origin: String?, forDeliveryInfo: Boolean): Intent {
            return Intent(context, RatesEstimationDetailActivity::class.java)
                    .putExtra(RatesEstimationConstant.PARAM_SHOP_DOMAIN, shopDomain)
                    .putExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, productWeight)
                    .putExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT_UNIT, productWeightUnit)
                    .putExtra(RatesEstimationConstant.PARAM_ORIGIN, origin)
                    .putExtra(RatesEstimationConstant.PARAM_FOR_DELIVERY_INFO, forDeliveryInfo)
        }
    }
}
