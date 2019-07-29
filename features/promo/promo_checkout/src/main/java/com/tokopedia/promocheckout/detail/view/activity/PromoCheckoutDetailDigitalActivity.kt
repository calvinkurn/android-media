package com.tokopedia.promocheckout.detail.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.detail.view.fragment.PromoCheckoutDetailDigitalFragment

class PromoCheckoutDetailDigitalActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return PromoCheckoutDetailDigitalFragment.createInstance(
                intent?.extras?.getString(PromoCheckoutDetailDigitalFragment.EXTRA_KUPON_CODE) ?: "",
                intent?.extras?.getBoolean(PromoCheckoutDetailDigitalFragment.EXTRA_IS_USE, false) ?: false,
                intent?.extras?.getParcelable(PromoCheckoutDetailDigitalFragment.EXTRA_PROMO_DIGITAL_MODEL) as PromoDigitalModel,
                intent?.extras?.getInt(PromoCheckoutDetailDigitalFragment.PAGE_TRACKING, 1) ?: 1
        )
    }

    companion object {
        fun newInstance(context: Context?, codeCoupon: String, isUse: Boolean, promoDigitalModel: PromoDigitalModel, pageTracking: Int): Intent {
            val intent = Intent(context, PromoCheckoutDetailDigitalActivity::class.java)
            val bundle = Bundle()
            bundle.putString(PromoCheckoutDetailDigitalFragment.EXTRA_KUPON_CODE, codeCoupon)
            bundle.putBoolean(PromoCheckoutDetailDigitalFragment.EXTRA_IS_USE, isUse)
            bundle.putParcelable(PromoCheckoutDetailDigitalFragment.EXTRA_PROMO_DIGITAL_MODEL, promoDigitalModel)
            bundle.putInt(PromoCheckoutDetailDigitalFragment.PAGE_TRACKING, pageTracking)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }
}