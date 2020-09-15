package com.tokopedia.promocheckout.list.view.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListDealsPresenter
import javax.inject.Inject


/**
 * Abrar
 */

class PromoCheckoutListDealsFragment() : BasePromoCheckoutListFragment(), PromoCheckoutListContract.View {

    @Inject
    lateinit var promoCheckoutListDealsPresenter: PromoCheckoutListDealsPresenter

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        promoCheckoutListDealsPresenter.attachView(this)
    }

    override fun onPromoCodeUse(promoCode: String) {
        if (promoCode.isNotEmpty()) promoCheckoutListDealsPresenter.dealsCheckoutMapper
    }


    override fun initInjector() {
        getComponent(PromoCheckoutListComponent::class.java).inject(this)
    }

    override fun onDestroyView() {
        promoCheckoutListDealsPresenter.detachView()
        super.onDestroyView()
    }

    override fun loadData(page: Int) {
        if (isCouponActive) {
            promoCheckoutListPresenter.getListPromo(serviceId, categoryId, page, resources)
        }
    }

    companion object {

        val EXTRA_CART_ID = "EXTRA_CART_ID"
        val EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID"

        fun createInstance(isCouponActive: Boolean?, promoCode: String?, cartID: String?, pageTracking: Int?): PromoCheckoutListHotelFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListHotelFragment()
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_COUPON_ACTIVE, isCouponActive ?: true)
            bundle.putString(EXTRA_PROMO_CODE, promoCode ?: "")
            bundle.putString(PromoCheckoutListHotelFragment.EXTRA_CART_ID, cartID ?: "")
            bundle.putInt(PAGE_TRACKING, pageTracking ?: 1)
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }

    }

    override fun onSuccessCheckPromo(data: DataUiModel) {
        //
    }
}