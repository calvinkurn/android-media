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
import com.tokopedia.promocheckout.detail.view.fragment.PromoCheckoutDetailDealsFragment
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListDealsFragment.Companion.EXTRA_CHECKOUT_DATA

class PromoCheckoutDetailDealsActivity : BaseSimpleActivity(), HasComponent<PromoCheckoutDetailComponent> {

    override fun getComponent(): PromoCheckoutDetailComponent = DaggerPromoCheckoutDetailComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getNewFragment(): Fragment {
        return PromoCheckoutDetailDealsFragment.createInstance(
                intent?.extras?.getString(BasePromoCheckoutDetailFragment.EXTRA_KUPON_CODE) ?: "",
                intent?.extras?.getBoolean(BasePromoCheckoutDetailFragment.EXTRA_IS_USE, false)
                        ?: false,
                intent?.extras?.getString(EXTRA_CHECKOUT_DATA, "")
        )
    }

    companion object {
        fun newInstance(context: Context?, codeCoupon: String, isUse: Boolean, checkoutData: String): Intent {
            val intent = Intent(context, PromoCheckoutDetailDealsActivity::class.java)
            val bundle = Bundle()
            bundle.putString(BasePromoCheckoutDetailFragment.EXTRA_KUPON_CODE, codeCoupon)
            bundle.putBoolean(BasePromoCheckoutDetailFragment.EXTRA_IS_USE, isUse)
            bundle.putString(EXTRA_CHECKOUT_DATA, checkoutData)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }
}