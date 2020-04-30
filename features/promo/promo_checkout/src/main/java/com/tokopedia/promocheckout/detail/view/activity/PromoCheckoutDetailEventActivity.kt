package com.tokopedia.promocheckout.detail.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.view.fragment.BasePromoCheckoutDetailFragment
import com.tokopedia.promocheckout.detail.view.fragment.PromoCheckoutDetailEventFragment

class PromoCheckoutDetailEventActivity : BaseSimpleActivity(), HasComponent<PromoCheckoutDetailComponent> {

    override fun getComponent(): PromoCheckoutDetailComponent = DaggerPromoCheckoutDetailComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getNewFragment(): Fragment {
        return PromoCheckoutDetailEventFragment.createInstance(
                intent?.extras?.getString(BasePromoCheckoutDetailFragment.EXTRA_KUPON_CODE) ?: "",
                intent?.extras?.getBoolean(BasePromoCheckoutDetailFragment.EXTRA_IS_USE, false) ?: false,
                intent?.extras?.getInt(BasePromoCheckoutDetailFragment.PAGE_TRACKING, 1) ?: 1
        )
    }

    companion object {
        fun newInstance(context: Context?, codeCoupon: String, isUse: Boolean, pageTracking: Int): Intent {
            val intent = Intent(context, PromoCheckoutDetailEventActivity::class.java)
            val bundle = Bundle()
            bundle.putString(BasePromoCheckoutDetailFragment.EXTRA_KUPON_CODE, codeCoupon)
            bundle.putBoolean(BasePromoCheckoutDetailFragment.EXTRA_IS_USE, isUse)
            bundle.putInt(BasePromoCheckoutDetailFragment.PAGE_TRACKING, pageTracking)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }
}