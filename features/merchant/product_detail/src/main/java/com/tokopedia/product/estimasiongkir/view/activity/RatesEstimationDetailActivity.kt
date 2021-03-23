package com.tokopedia.product.estimasiongkir.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.estimasiongkir.data.constant.RatesEstimationConstant
import com.tokopedia.product.estimasiongkir.di.DaggerRatesEstimationComponent
import com.tokopedia.product.estimasiongkir.di.RatesEstimationComponent
import com.tokopedia.product.estimasiongkir.di.RatesEstimationModule
import com.tokopedia.product.estimasiongkir.view.fragment.RatesEstimationDetailFragment

class RatesEstimationDetailActivity : BaseSimpleActivity(), HasComponent<RatesEstimationComponent> {

    override fun getNewFragment(): Fragment {
        val shopDomain = intent.getStringExtra(RatesEstimationConstant.PARAM_SHOP_DOMAIN)
        val weight = intent.getFloatExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, 0f)
        val weightUnit = intent.getStringExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT_UNIT)
        val isFreeOngkir = intent.getBooleanExtra(RatesEstimationConstant.PARAM_ISFREEONGKIR, false)
        val shopId = intent.getStringExtra(RatesEstimationConstant.PARAM_SHOP_ID)
        val productId = intent.getStringExtra(RatesEstimationConstant.PARAM_PRODUCT_ID)
        return RatesEstimationDetailFragment.createInstance(shopDomain, weight, weightUnit,
                if (intent.hasExtra(RatesEstimationConstant.PARAM_ORIGIN))
                    intent.getStringExtra(RatesEstimationConstant.PARAM_ORIGIN)
                else null, isFreeOngkir, shopId, productId
        )
    }

    override fun getComponent(): RatesEstimationComponent {
        return DaggerRatesEstimationComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .ratesEstimationModule(RatesEstimationModule()).build()
    }

    companion object {

        @JvmStatic
        fun createIntent(context: Context, shopDomain: String, productWeight: Float,
                         productWeightUnit: String, origin: String?, isFreeOngkir: Boolean,
                         shopId: String, productId: String
        ): Intent {
            return Intent(context, RatesEstimationDetailActivity::class.java)
                    .putExtra(RatesEstimationConstant.PARAM_SHOP_DOMAIN, shopDomain)
                    .putExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, productWeight)
                    .putExtra(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT_UNIT, productWeightUnit)
                    .putExtra(RatesEstimationConstant.PARAM_ORIGIN, origin)
                    .putExtra(RatesEstimationConstant.PARAM_ISFREEONGKIR, isFreeOngkir)
                    .putExtra(RatesEstimationConstant.PARAM_SHOP_ID, shopId)
                    .putExtra(RatesEstimationConstant.PARAM_PRODUCT_ID, productId)
        }
    }
}
