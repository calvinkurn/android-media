package com.tokopedia.promocheckout.list.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailEventActivity
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListEventPresenter
import javax.inject.Inject

class PromoCheckoutListEventFragment : BasePromoCheckoutListFragment(), PromoCheckoutListContract.View {

    @Inject
    lateinit var promoCheckoutListEventPresenter: PromoCheckoutListEventPresenter

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING

    var eventVerifyBody = EventVerifyBody()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCouponActive = arguments?.getBoolean(EXTRA_COUPON_ACTIVE) ?: true
        categoryId = arguments?.getInt(EXTRA_EVENT_CATEGORY_ID) ?: 1
        pageTracking = arguments?.getInt(PAGE_TRACKING) ?: 1
        eventVerifyBody = arguments?.getParcelable(EXTRA_EVENT_VERIFY) ?: EventVerifyBody()
        promoCheckoutListEventPresenter.attachView(this)
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        super.onItemClicked(promoCheckoutListModel)
        navigateToPromoDetail(promoCheckoutListModel)
    }

    open fun navigateToPromoDetail(promoCheckoutListModel: PromoCheckoutListModel?) {
        startActivityForResult(PromoCheckoutDetailEventActivity.newInstance(
                activity, promoCheckoutListModel?.code
                ?: "", false, eventVerifyBody), REQUEST_CODE_PROMO_DETAIL)
    }

    override fun loadData(page: Int) {
        context?.let { context ->
            if (isCouponActive) {
                promoCheckoutListPresenter.getListPromo(serviceId, categoryId, page, context.resources)
            }
        }
    }

    override fun initInjector() {
        getComponent(PromoCheckoutListComponent::class.java).inject(this)
    }

    override fun onDestroyView() {
        promoCheckoutListEventPresenter.detachView()
        super.onDestroyView()
    }

    override fun onPromoCodeUse(promoCode: String) {
        if (promoCode.isNotEmpty()) {
            eventVerifyBody.promocode = promoCode
            promoCheckoutListEventPresenter.checkPromoCode(promoCode, false, eventVerifyBody)
        }
    }

    override fun onSuccessCheckPromo(data: DataUiModel) {
        val intent = Intent()
        val promoData = PromoData(data.isCoupon, data.codes[0],
                data.message.text, data.titleDescription, state = data.message.state.mapToStatePromoCheckout())
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    companion object {
        const val EXTRA_EVENT_CATEGORY_ID = "EXTRA_EVENT_CATEGORY_ID"
        const val EXTRA_EVENT_VERIFY = "EXTRA_EVENT_VERIFY"


        fun createInstance(isCouponActive: Boolean?, categoryId: Int?,
                           pageTracking: Int, eventVerifyBody: EventVerifyBody?): PromoCheckoutListEventFragment {
            val promoCheckoutListFragment = PromoCheckoutListEventFragment()
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_COUPON_ACTIVE, isCouponActive ?: true)
            bundle.putInt(EXTRA_EVENT_CATEGORY_ID, categoryId ?: 1)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            bundle.putParcelable(EXTRA_EVENT_VERIFY, eventVerifyBody ?: EventVerifyBody())
            promoCheckoutListFragment.arguments = bundle
            return promoCheckoutListFragment
        }
    }
}
