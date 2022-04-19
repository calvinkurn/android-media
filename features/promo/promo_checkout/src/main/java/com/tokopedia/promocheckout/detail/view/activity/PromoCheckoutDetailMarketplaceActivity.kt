package com.tokopedia.promocheckout.detail.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.promocheckout.common.data.EXTRA_IS_USE
import com.tokopedia.promocheckout.common.data.EXTRA_KUPON_CODE
import com.tokopedia.promocheckout.common.data.ONE_CLICK_SHIPMENT
import com.tokopedia.promocheckout.common.data.PAGE_TRACKING
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.view.fragment.PromoCheckoutDetailMarketplaceFragment

/**
 * For Marketplace Promo Detail, only for showing detail, cannot apply/remove promo from this page
 *
 * Used by: feature/transaction/promo_checkout_marketplace @minion-bob
 */
class PromoCheckoutDetailMarketplaceActivity : BaseSimpleActivity(), HasComponent<PromoCheckoutDetailComponent> {


    override fun getComponent(): PromoCheckoutDetailComponent = DaggerPromoCheckoutDetailComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getNewFragment(): Fragment {
        return PromoCheckoutDetailMarketplaceFragment.createInstance(
                intent.getStringExtra(EXTRA_KUPON_CODE) ?: "",
                intent.getBooleanExtra(EXTRA_IS_USE, false),
                intent.getBooleanExtra(ONE_CLICK_SHIPMENT, false),
                intent.getIntExtra(PAGE_TRACKING, 1) ?: 1
        )
    }

    companion object {
        fun createIntent(context: Context?, codeCoupon: String?, isUse: Boolean? = false, oneClickShipment: Boolean? = false, pageTracking: Int, promo: Promo?): Intent {
            val intent = Intent(context, PromoCheckoutDetailMarketplaceActivity::class.java)
            intent.putExtra(EXTRA_KUPON_CODE, codeCoupon)
            intent.putExtra(EXTRA_IS_USE, isUse)
            intent.putExtra(ONE_CLICK_SHIPMENT, oneClickShipment)
            intent.putExtra(PAGE_TRACKING, pageTracking)
            return intent
        }
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }
}