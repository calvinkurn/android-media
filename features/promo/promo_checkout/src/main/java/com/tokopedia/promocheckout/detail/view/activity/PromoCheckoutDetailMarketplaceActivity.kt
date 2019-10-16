package com.tokopedia.promocheckout.detail.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.detail.view.fragment.BasePromoCheckoutDetailFragment
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.view.fragment.PromoCheckoutDetailMarketplaceFragment

class PromoCheckoutDetailMarketplaceActivity : BaseSimpleActivity(), HasComponent<PromoCheckoutDetailComponent> {


    override fun getComponent(): PromoCheckoutDetailComponent = DaggerPromoCheckoutDetailComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getNewFragment(): Fragment {
        return PromoCheckoutDetailMarketplaceFragment.createInstance(intent.getStringExtra(BasePromoCheckoutDetailFragment.EXTRA_KUPON_CODE),
                intent.getBooleanExtra(BasePromoCheckoutDetailFragment.EXTRA_IS_USE, false),
                intent.getBooleanExtra(PromoCheckoutDetailMarketplaceFragment.ONE_CLICK_SHIPMENT, false),
                intent.getIntExtra(BasePromoCheckoutDetailFragment.PAGE_TRACKING, 1),
                intent.getParcelableExtra(PromoCheckoutDetailMarketplaceFragment.CHECK_PROMO_CODE_FIRST_STEP_PARAM)
        )
    }

    companion object {
        fun createIntent(context: Context?, codeCoupon: String?, isUse: Boolean? = false, oneClickShipment: Boolean? = false, pageTracking: Int, promo: Promo?): Intent {
            val intent = Intent(context, PromoCheckoutDetailMarketplaceActivity::class.java)
            intent.putExtra(BasePromoCheckoutDetailFragment.EXTRA_KUPON_CODE, codeCoupon)
            intent.putExtra(BasePromoCheckoutDetailFragment.EXTRA_IS_USE, isUse)
            intent.putExtra(PromoCheckoutDetailMarketplaceFragment.ONE_CLICK_SHIPMENT, oneClickShipment)
            intent.putExtra(BasePromoCheckoutDetailFragment.PAGE_TRACKING, pageTracking)
            intent.putExtra(PromoCheckoutDetailMarketplaceFragment.CHECK_PROMO_CODE_FIRST_STEP_PARAM, promo)
            return intent
        }
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }
}
