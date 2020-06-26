package com.tokopedia.promocheckout.list.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.promocheckout.list.PromoCheckoutListComponentInstance
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.view.fragment.BasePromoCheckoutListFragment
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListHotelFragment
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSession

class PromoCheckoutListHotelActivity : BaseSimpleActivity(), HasComponent<PromoCheckoutListComponent> {

    override fun getNewFragment(): Fragment {
        return PromoCheckoutListHotelFragment.createInstance(
                intent?.extras?.getBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, true),
                intent?.extras?.getString(BasePromoCheckoutListFragment.EXTRA_PROMO_CODE, ""),
                intent?.extras?.getString(PromoCheckoutListHotelFragment.EXTRA_CART_ID, ""),
                intent?.extras?.getInt(BasePromoCheckoutListFragment.PAGE_TRACKING, 1) ?: 1
        )
    }

    override fun sendScreenAnalytics() {
        screenName?.let {
            sendOpenScreenTracking(it)
        }
    }

    override fun getComponent(): PromoCheckoutListComponent {
        return PromoCheckoutListComponentInstance.getPromoCheckoutListComponent(application)
    }

    override fun getScreenName(): String = HOTEL_PROMO_SCREEN_NAME

    private fun sendOpenScreenTracking(screenName: String) {
        val map = mutableMapOf<String, String>()
        map[SCREEN_NAME] = screenName
        map[CURRENT_SITE] = TOKOPEDIA_DIGITAL_HOTEL
        map[CLIENT_ID] = TrackApp.getInstance().gtm.clientIDString ?: ""
        map[SESSION_IRIS] = IrisSession(this).getSessionId()
        map[USER_ID] = UserSession(this).userId
        map[BUSINESS_UNIT] = TRAVELENTERTAINMENT_LABEL
        map[CATEGORY_LABEL] = HOTEL_LABEL
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, map)
    }

    companion object {

        // for tracking purposes.
        const val HOTEL_PROMO_SCREEN_NAME = "/hotel/checkoutpromo"
        const val TOKOPEDIA_DIGITAL_HOTEL = "tokopediadigitalhotel"
        const val TRAVELENTERTAINMENT_LABEL = "travel & entertainment"
        const val HOTEL_LABEL = "hotel"
        const val SCREEN_NAME = "screenName"
        const val CURRENT_SITE = "currentSite"
        const val CLIENT_ID = "clientId"
        const val SESSION_IRIS = "sessionIris"
        const val USER_ID = "userId"
        const val BUSINESS_UNIT = "businessUnit"
        const val CATEGORY_LABEL = "category"

        fun newInstance(activity: Context, isCouponActive: Boolean, promoCode: String, cartID: String, pageTracking: Int): Intent {
            val intent = Intent(activity, PromoCheckoutListHotelActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EXTRA_COUPON_ACTIVE, isCouponActive)
            bundle.putString(BasePromoCheckoutListFragment.EXTRA_PROMO_CODE, promoCode)
            bundle.putString(PromoCheckoutListHotelFragment.EXTRA_CART_ID, cartID)
            bundle.putInt(BasePromoCheckoutListFragment.PAGE_TRACKING, pageTracking)
            intent.putExtras(bundle)
            return intent
        }
    }

}