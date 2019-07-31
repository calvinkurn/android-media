package com.tokopedia.promocheckout.list.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailDigitalActivity
import com.tokopedia.promocheckout.list.di.DaggerPromoCheckoutListComponent
import com.tokopedia.promocheckout.list.di.PromoCheckoutListModule
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListDigitalPresenter
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import javax.inject.Inject

class PromoCheckoutListDigitalFragment : BasePromoCheckoutListFragment(), PromoCheckoutListContract.View {

    @Inject
    lateinit var promoCheckoutListDigitalPresenter: PromoCheckoutListDigitalPresenter

    lateinit var promoDigitalModel: PromoDigitalModel

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        isCouponActive = arguments?.getBoolean(EXTRA_IS_COUPON_ACTIVE) ?: true
        promoCode = arguments?.getString(EXTRA_PROMO_CODE) ?: ""
        promoDigitalModel = arguments?.getParcelable(EXTRA_PROMO_DIGITAL_MODEL) ?: PromoDigitalModel()
        categoryId = promoDigitalModel.categoryId
        pageTracking = arguments?.getInt(PAGE_TRACKING) ?: 1
        super.onCreate(savedInstanceState)
        promoCheckoutListDigitalPresenter.attachView(this)
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        super.onItemClicked(promoCheckoutListModel)
        startActivityForResult(PromoCheckoutDetailDigitalActivity.newInstance(
                activity, promoCheckoutListModel?.code ?: "", false, promoDigitalModel, pageTracking), REQUEST_CODE_PROMO_DETAIL)
    }

    override fun onPromoCodeUse(promoCode: String) {
        if (promoCode.isNotEmpty()) promoCheckoutListDigitalPresenter.checkPromoCode(promoCode, promoDigitalModel)
    }

    override fun onSuccessCheckPromoCode(data: DataUiModel) {
        trackSuccessCheckPromoCode(data)
        val intent = Intent()
        val promoData = PromoData(data.isCoupon, data.codes[0],
                data.message.text, data.titleDescription, state = data.message.state.mapToStatePromoCheckout())
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onClickItemLastSeen(promoCheckoutLastSeenModel: PromoCheckoutLastSeenModel) {
        textInputCoupon.setText(promoCheckoutLastSeenModel.promoCode)
    }

    override fun loadData(page: Int) {
        super.loadData(page)
        if(isCouponActive) {
            promoCheckoutListPresenter.getListLastSeen(listOf(categoryId), resources)
        }
    }

    override fun initInjector() {
        super.initInjector()
        DaggerPromoCheckoutListComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .promoCheckoutListModule(PromoCheckoutListModule())
                .build()
                .inject(this)
    }

    override fun onDestroyView() {
        promoCheckoutListDigitalPresenter.detachView()
        super.onDestroyView()
    }

    companion object {
        val EXTRA_IS_COUPON_ACTIVE = "EXTRA_IS_COUPON_ACTIVE"
        val EXTRA_PROMO_CODE = "EXTRA_PROMO_CODE"
        val EXTRA_PROMO_DIGITAL_MODEL = "EXTRA_PROMO_DIGITAL_MODEL"
        val PAGE_TRACKING = "PAGE_TRACKING"

        fun createInstance(isCouponActive: Boolean?, promoCode: String?, promoDigitalModel: PromoDigitalModel, pageTracking: Int): PromoCheckoutListDigitalFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListDigitalFragment()
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_IS_COUPON_ACTIVE, isCouponActive ?: true)
            bundle.putString(EXTRA_PROMO_CODE, promoCode ?: "")
            bundle.putParcelable(EXTRA_PROMO_DIGITAL_MODEL, promoDigitalModel)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }
    }

}
