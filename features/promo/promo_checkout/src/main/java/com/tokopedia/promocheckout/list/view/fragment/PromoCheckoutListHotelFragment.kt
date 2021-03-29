package com.tokopedia.promocheckout.list.view.fragment

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailHotelActivity
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListHotelPresenter
import javax.inject.Inject

class PromoCheckoutListHotelFragment : PromoCheckoutListDigitalFragment(), PromoCheckoutListContract.View {

    @Inject
    lateinit var promoCheckoutListHotelPresenter: PromoCheckoutListHotelPresenter

    var cartID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = HOTEL_CATEGORY_ID
        cartID = arguments?.getString(EXTRA_CART_ID) ?: ""
        promoCheckoutListHotelPresenter.attachView(this)
    }

    override fun navigateToPromoDetail(promoCheckoutListModel: PromoCheckoutListModel?) {
        startActivityForResult(PromoCheckoutDetailHotelActivity.newInstance(
                activity, promoCheckoutListModel?.code ?: "", cartID, false, pageTracking), REQUEST_CODE_PROMO_DETAIL)
    }

    override fun onPromoCodeUse(promoCode: String) {
        var hexColor: String = ""
        context?.run {
            hexColor = "#" + Integer.toHexString( ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_G200) and HEX_CODE_TRANSPARENCY)
        }
        if (promoCode.isNotEmpty()) promoCheckoutListHotelPresenter.checkPromoCode(cartID, promoCode, hexColor)
    }

    override fun initInjector() {
        getComponent(PromoCheckoutListComponent::class.java).inject(this)
    }

    override fun onDestroyView() {
        promoCheckoutListHotelPresenter.detachView()
        super.onDestroyView()
    }

    companion object {

        val HOTEL_CATEGORY_ID = 51
        val EXTRA_CART_ID = "EXTRA_CART_ID"
        const val HEX_CODE_TRANSPARENCY: Int = 0x00ffffff

        fun createInstance(isCouponActive: Boolean?, promoCode: String?, cartID: String?, pageTracking: Int?): PromoCheckoutListHotelFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListHotelFragment()
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_COUPON_ACTIVE, isCouponActive ?: true)
            bundle.putString(EXTRA_PROMO_CODE, promoCode ?: "")
            bundle.putString(EXTRA_CART_ID, cartID ?: "")
            bundle.putInt(PAGE_TRACKING, pageTracking ?: 1)
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }
    }

}
