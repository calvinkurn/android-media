package com.tokopedia.product.detail.estimasiongkir.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.detail.estimasiongkir.data.constant.RatesEstimationConstant
import com.tokopedia.product.detail.estimasiongkir.di.RatesEstimationModule
import com.tokopedia.product.detail.estimasiongkir.di.RatesEstimationComponent
import com.tokopedia.product.detail.estimasiongkir.view.fragment.RatesEstimationDetailFragment
import com.tokopedia.product.detail.estimasiongkir.di.DaggerRatesEstimationComponent

class RatesEstimationDetailActivity : BaseSimpleActivity(), HasComponent<RatesEstimationComponent> {

    override fun getNewFragment(): Fragment {
        val shopDomain = intent.getStringExtra(RatesEstimationConstant.PARAM_SHOP_DOMAIN)
        val weight = intent.getIntExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, 0)
        val weightUnit = intent.getIntExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT_UNIT, 0)
        return RatesEstimationDetailFragment.createInstance(shopDomain, weight, weightUnit)
    }

    override fun getComponent(): RatesEstimationComponent {
        return DaggerRatesEstimationComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .ratesEstimationModule(RatesEstimationModule()).build()
    }

    companion object {

        @JvmStatic
        fun createIntent(context: Context, shopDomain: String, productWeight: Int, productWeightUnit: Int): Intent {
            return Intent(context, RatesEstimationDetailActivity::class.java)
                    .putExtra(RatesEstimationConstant.PARAM_SHOP_DOMAIN, shopDomain)
                    .putExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, productWeight)
                    .putExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT_UNIT, productWeightUnit)
        }
    }
}
