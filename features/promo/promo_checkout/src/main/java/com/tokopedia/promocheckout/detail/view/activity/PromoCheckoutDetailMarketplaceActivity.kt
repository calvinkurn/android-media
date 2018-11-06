package com.tokopedia.promocheckout.detail.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.promocheckout.detail.view.fragment.BasePromoCheckoutDetailFragment
import com.tokopedia.promocheckout.detail.view.fragment.PromoCheckoutDetailMarketplaceFragment

class PromoCheckoutDetailMarketplaceActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return PromoCheckoutDetailMarketplaceFragment.createInstance(intent.getStringExtra(PromoCheckoutDetailMarketplaceFragment.EXTRA_KUPON_CODE))
    }

    companion object {
        fun createIntent(context: Context?, codeCoupon: String?):Intent{
            val intent = Intent(context, PromoCheckoutDetailMarketplaceActivity::class.java)
            intent.putExtra(PromoCheckoutDetailMarketplaceFragment.EXTRA_KUPON_CODE, codeCoupon)
            return intent
        }
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }
}