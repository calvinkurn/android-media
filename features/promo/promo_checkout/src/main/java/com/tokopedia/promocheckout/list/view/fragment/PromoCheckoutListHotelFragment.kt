package com.tokopedia.promocheckout.list.view.fragment

import android.os.Bundle
import com.tokopedia.promocheckout.analytics.HotelPromoCheckoutAnalytics
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailHotelActivity
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.view.viewmodel.PromoCheckoutListHotelViewModel
import com.tokopedia.promocheckout.util.ColorUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class PromoCheckoutListHotelFragment : PromoCheckoutListDigitalFragment() {

    private val hotelPromoViewModel: PromoCheckoutListHotelViewModel by lazy { viewModelProvider.get(PromoCheckoutListHotelViewModel::class.java) }

    var cartID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = HOTEL_CATEGORY_ID
        cartID = arguments?.getString(EXTRA_CART_ID) ?: ""
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        hotelPromoViewModel.showLoadingPromoHotel.observe(viewLifecycleOwner,{
            if(it){
                showProgressLoading()
            }else{
                hideProgressLoading()
            }
        })

        hotelPromoViewModel.hotelCheckVoucherResult.observe(viewLifecycleOwner,{
            when(it){
                is Success ->{
                    onSuccessCheckPromo(it.data)
                }
                is Fail->{
                    onErrorCheckPromo(it.throwable)
                }
            }
        })
    }

    override fun navigateToPromoDetail(promoCheckoutListModel: PromoCheckoutListModel?) {
        startActivityForResult(PromoCheckoutDetailHotelActivity.newInstance(
                activity, promoCheckoutListModel?.code ?: "", cartID, false, pageTracking), REQUEST_CODE_PROMO_DETAIL)
    }

    override fun onPromoCodeUse(promoCode: String) {
        context?.run {
            if (promoCode.isNotEmpty()) {
                hotelPromoViewModel.checkPromoCode(cartID, promoCode, ColorUtil.getColorFromResToString(this, com.tokopedia.unifyprinciples.R.color.Unify_G200))
                hotelPromoCheckoutAnalytics.hotelApplyPromo(this, promoCode, HotelPromoCheckoutAnalytics.HOTEL_BOOKING_SCREEN_NAME)
            }
        }
    }

    override fun initInjector() {
        getComponent(PromoCheckoutListComponent::class.java).inject(this)
    }
    companion object {

        val HOTEL_CATEGORY_ID = 51
        val EXTRA_CART_ID = "EXTRA_CART_ID"
        private val hotelPromoCheckoutAnalytics: HotelPromoCheckoutAnalytics by lazy { HotelPromoCheckoutAnalytics() }

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
