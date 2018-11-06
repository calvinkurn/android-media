package com.tokopedia.promocheckout.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.detail.PromoCheckoutDetailActivity
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel

class PromoCheckoutListMarketplaceFragment : BasePromoCheckoutListFragment() {

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        isCouponActive = arguments?.getBoolean(IS_COUPON_ACTIVE)?:true
        super.onCreate(savedInstanceState)
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        startActivityForResult(PromoCheckoutDetailActivity.createIntent(activity, promoCheckoutListModel?.code), REQUEST_CODE_DETAIL_PROMO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_DETAIL_PROMO){

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        val REQUEST_CODE_DETAIL_PROMO = 231
        val IS_COUPON_ACTIVE = "IS_COUPON_ACTIVE"

        fun createInstance(isCouponActive: Boolean?):PromoCheckoutListMarketplaceFragment{
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListMarketplaceFragment()
            val bundle = Bundle()
            bundle.putBoolean(IS_COUPON_ACTIVE, isCouponActive?:true)
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }
    }

}
