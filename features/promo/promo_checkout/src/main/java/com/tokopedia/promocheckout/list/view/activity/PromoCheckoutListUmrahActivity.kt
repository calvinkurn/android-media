package com.tokopedia.promocheckout.list.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.list.PromoCheckoutListComponentInstance
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.view.fragment.BasePromoCheckoutListFragment
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListUmrahFragment

class PromoCheckoutListUmrahActivity : BaseSimpleActivity(), HasComponent<PromoCheckoutListComponent>{
    override fun getComponent(): PromoCheckoutListComponent {
        return PromoCheckoutListComponentInstance.getPromoCheckoutListComponent(application)
    }

    override fun getNewFragment(): Fragment? {
        return PromoCheckoutListUmrahFragment.createInstance(
                intent?.extras?.getString(BasePromoCheckoutListFragment.EXTRA_PROMO_CODE, ""),
                intent?.extras?.getInt(EXTRA_TOTAL_PRICE,0)
        )
    }

    companion object{

        val EXTRA_TOTAL_PRICE = "EXTRA_TOTAL_PRICE"

        fun newInstance(activity: Context, promoCode: String, totalPrice:Int): Intent {
            val intent = Intent(activity, PromoCheckoutListUmrahActivity::class.java)
            val bundle = Bundle()
            bundle.putString(BasePromoCheckoutListFragment.EXTRA_PROMO_CODE, promoCode)
            bundle.putInt(EXTRA_TOTAL_PRICE,totalPrice)
            intent.putExtras(bundle)
            return intent
        }
    }
}