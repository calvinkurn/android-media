package com.tokopedia.promocheckout.list.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailMarketplaceActivity
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel

class PromoCheckoutListDigitalFragment : BasePromoCheckoutListFragment() {
    override fun onPromoCodeUse(promoCode: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            isCouponActive = it.getBoolean(IS_COUPON_ACTIVE)?:true
            categoryId = it.getString(CATEGORY_ID, "0").toInt()
        }
        super.onCreate(savedInstanceState)
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
//        startActivityForResult(PromoCheckoutDetailMarketplaceActivity.createIntent(activity, promoCheckoutListModel?.code), REQUEST_CODE_DETAIL_PROMO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_DETAIL_PROMO){

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        val REQUEST_CODE_DETAIL_PROMO = 231
        val IS_COUPON_ACTIVE = "IS_COUPON_ACTIVE"
        val CATEGORY_ID = "CATEGORY_ID"
        val CART_ID = "CART_ID"
        val RESERVATION_CODE = "RESERVATION_CODE"
        val RESERVATION_ID = "RESERVATION_ID"

        fun createInstance(isCouponActive: Boolean?, categoryId: String?, cartId: String?, reservationCode: String?, reservationId: String?): PromoCheckoutListDigitalFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListDigitalFragment()
            val bundle = Bundle()
            bundle.putBoolean(IS_COUPON_ACTIVE, isCouponActive?:true)
            bundle.putString(CATEGORY_ID, categoryId)
            bundle.putString(CART_ID, cartId)
            bundle.putString(RESERVATION_CODE, reservationCode)
            bundle.putString(RESERVATION_ID, reservationId)
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }
    }

}
