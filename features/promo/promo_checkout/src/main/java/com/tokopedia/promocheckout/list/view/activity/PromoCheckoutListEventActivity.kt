package com.tokopedia.promocheckout.list.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.list.PromoCheckoutListComponentInstance
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.view.fragment.BasePromoCheckoutListFragment
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListEventFragment
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListEventFragment.Companion.EXTRA_EVENT_VERIFY

class PromoCheckoutListEventActivity : BaseSimpleActivity(), HasComponent<PromoCheckoutListComponent>{

    override fun getComponent(): PromoCheckoutListComponent {
        return PromoCheckoutListComponentInstance.getPromoCheckoutListComponent(application)
    }

    override fun getNewFragment(): Fragment {
        val categoryId = intent?.extras?.getInt(PromoCheckoutListEventFragment.EXTRA_EVENT_CATEGORY_ID)
        val couponActive = intent?.extras?.getBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE)
        val pageTracking = intent?.extras?.getInt(BasePromoCheckoutListFragment.PAGE_TRACKING) ?: 1
        val verify = intent?.extras?.getParcelable<EventVerifyBody>(EXTRA_EVENT_VERIFY)
        return PromoCheckoutListEventFragment.createInstance(
                couponActive, categoryId, pageTracking, verify)
    }


    companion object{
        fun newInstance(context: Context, isCouponActive: Boolean, categoryId:Int,
                        pageTracking: Int, eventVerifyBody: EventVerifyBody): Intent {
            val intent = Intent(context, PromoCheckoutListEventActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, isCouponActive)
            bundle.putInt(BasePromoCheckoutListFragment.PAGE_TRACKING, pageTracking)
            bundle.putInt(PromoCheckoutListEventFragment.EXTRA_EVENT_CATEGORY_ID, categoryId)
            bundle.putParcelable(EXTRA_EVENT_VERIFY, eventVerifyBody)
            intent.putExtras(bundle)
            return intent
        }
    }
}