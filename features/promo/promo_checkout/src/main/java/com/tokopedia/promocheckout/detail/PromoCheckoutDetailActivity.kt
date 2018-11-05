package com.tokopedia.promocheckout.detail

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class PromoCheckoutDetailActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return PromoCheckoutDetailFragment.createInstance(intent.getStringExtra(PromoCheckoutDetailFragment.EXTRA_KUPON_CODE))
    }

    companion object {
        fun createIntent(context: Context?, codeCoupon: String?):Intent{
            val intent = Intent(context, PromoCheckoutDetailActivity::class.java)
            intent.putExtra(PromoCheckoutDetailFragment.EXTRA_KUPON_CODE, codeCoupon)
            return intent
        }
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }
}