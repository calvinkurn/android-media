package com.tokopedia.promocheckout.list.view.fragment

import android.os.Bundle
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.common.domain.model.TravelCollectiveBanner
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.view.adapter.PromoDealsAdapter
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListDealsPresenter
import javax.inject.Inject


/**
 * Abrar
 */

class PromoCheckoutListDealsFragment() : BasePromoCheckoutListFragment(), PromoCheckoutListContract.View {

    @Inject
    lateinit var promoCheckoutListDealsPresenter: PromoCheckoutListDealsPresenter
    private val promoDealsAdapter: PromoDealsAdapter by lazy { PromoDealsAdapter(arrayListOf(), this) }

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DEALS_STRING
    var categoryID: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryID = arguments?.getInt(EXTRA_CATEGORY_ID, 1) ?: 1
        promoCheckoutListDealsPresenter.attachView(this)
    }

    override fun onPromoCodeUse(promoCode: String) {
        //
    }

    override fun onClickItemPromo(promoCheckoutDealsPromoCode: TravelCollectiveBanner.Banner) {
        //
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
            promoCheckoutListPresenter.getListPromo(serviceId, categoryID, page, resources)
        }
        promoCheckoutListPresenter.getListTravelCollectiveBanner(resources)
    }

    companion object {

        val EXTRA_CATEGORY_ID = "EXTRA_CATEGORYID"
        val EXTRA_PRODUCTID = "EXTRA_PRODUCTID"

        fun createInstance(isCouponActive: Boolean?, promoCode: String?, categoryId: Int?, pageTracking: Int?, productId: String?): PromoCheckoutListDealsFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListDealsFragment()
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_COUPON_ACTIVE, isCouponActive ?: true)
            bundle.putString(EXTRA_PROMO_CODE, promoCode ?: "")
            bundle.putInt(EXTRA_CATEGORY_ID, categoryId ?: 1)
            bundle.putInt(PAGE_TRACKING, pageTracking ?: 1)
            bundle.putString(EXTRA_PRODUCTID, productId ?: "")
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }

    }

    override fun renderDealsPromo(data: List<TravelCollectiveBanner.Banner>) {
        promoDealsAdapter.listData.clear()
        promoDealsAdapter.listData.addAll(data)
        promoDealsAdapter.notifyDataSetChanged()
        populateDealsPromo(data)
    }

    override fun onSuccessCheckPromo(data: DataUiModel) {
        //
    }
}