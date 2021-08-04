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
import com.tokopedia.promocheckout.list.view.viewmodel.PromoCheckoutListEventViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class PromoCheckoutListEventFragment : BasePromoCheckoutListFragment(){

    private val promoCheckoutListEventViewModel: PromoCheckoutListEventViewModel by lazy { viewModelProvider.get(PromoCheckoutListEventViewModel::class.java) }

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING

    var eventVerifyBody = EventVerifyBody()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCouponActive = arguments?.getBoolean(EXTRA_COUPON_ACTIVE) ?: true
        categoryId = arguments?.getInt(EXTRA_EVENT_CATEGORY_ID) ?: 1
        pageTracking = arguments?.getInt(PAGE_TRACKING) ?: 1
        eventVerifyBody = arguments?.getParcelable(EXTRA_EVENT_VERIFY) ?: EventVerifyBody()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        promoCheckoutListEventViewModel.showLoadingPromoEvent.observe(viewLifecycleOwner,{
            if(it){
                showProgressLoading()
            }else{
                hideProgressLoading()
            }
        })

        promoCheckoutListEventViewModel.eventCheckVoucherResult.observe(viewLifecycleOwner,{
            when(it){
                is Success->{
                    onSuccessCheckPromo(it.data)
                }
                is Fail ->{
                    onErrorCheckPromo(it.throwable)
                }
            }
        })
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
        if (isCouponActive) {
            promoCheckoutListViewModel.getPromoList(serviceId, categoryId, page)
        }
    }

    override fun initInjector() {
        getComponent(PromoCheckoutListComponent::class.java).inject(this)
    }

    override fun onPromoCodeUse(promoCode: String) {
        if (promoCode.isNotEmpty()) {
            eventVerifyBody.promocode = promoCode
            promoCheckoutListEventViewModel.checkPromoCode( false, eventVerifyBody)
        }
    }

    fun onSuccessCheckPromo(data: DataUiModel) {
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