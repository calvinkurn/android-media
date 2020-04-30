package com.tokopedia.promocheckout.list.view.fragment

import android.os.Bundle
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailEventActivity
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListEventPresenter
import javax.inject.Inject

class PromoCheckoutListEventFragment : BasePromoCheckoutListFragment(), PromoCheckoutListContract.View{

    @Inject
    lateinit var promoCheckoutListEventPresenter : PromoCheckoutListEventPresenter

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCouponActive = arguments?.getBoolean(EXTRA_COUPON_ACTIVE) ?: true
        categoryId = arguments?.getInt(EXTRA_EVENT_CATEGORY_ID) ?: 1
        pageTracking = arguments?.getInt(PAGE_TRACKING) ?:1
        promoCheckoutListEventPresenter.attachView(this)
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        super.onItemClicked(promoCheckoutListModel)
        navigateToPromoDetail(promoCheckoutListModel)
    }

    open fun navigateToPromoDetail(promoCheckoutListModel: PromoCheckoutListModel?) {
        startActivityForResult(PromoCheckoutDetailEventActivity.newInstance(
                activity, promoCheckoutListModel?.code
                ?: "", false, pageTracking), REQUEST_CODE_PROMO_DETAIL)
    }

    override fun loadData(page: Int) {
        if (isCouponActive){
            promoCheckoutListPresenter.getListPromo(serviceId, categoryId, page, resources)
        }
        promoCheckoutListPresenter.getListLastSeen(listOf(categoryId),resources)
    }

    override fun initInjector() {
        getComponent(PromoCheckoutListComponent::class.java).inject(this)
    }

    override fun onDestroyView() {
        promoCheckoutListEventPresenter.detachView()
        super.onDestroyView()
    }

    override fun onPromoCodeUse(promoCode: String) {

    }

    override fun onSuccessCheckPromo(data: DataUiModel) {

    }

    companion object{
        val EXTRA_EVENT_CATEGORY_ID = "EXTRA_EVENT_CATEGORY_ID"

        fun createInstance(isCouponActive: Boolean?, categoryId: Int?, pageTracking:Int): PromoCheckoutListEventFragment{
            val promoCheckoutListFragment  = PromoCheckoutListEventFragment()
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_COUPON_ACTIVE, isCouponActive?:true)
            bundle.putInt(EXTRA_EVENT_CATEGORY_ID, categoryId?:1)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            promoCheckoutListFragment.arguments = bundle
            return promoCheckoutListFragment
        }
    }
}