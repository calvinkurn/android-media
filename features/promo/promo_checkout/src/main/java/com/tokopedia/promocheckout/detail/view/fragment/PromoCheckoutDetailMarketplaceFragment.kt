package com.tokopedia.promocheckout.detail.view.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.analytics.FROM_CART
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.di.PromoCheckoutQualifier
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailModule
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailPresenter
import javax.inject.Inject

class PromoCheckoutDetailMarketplaceFragment : BasePromoCheckoutDetailFragment() {
    @Inject
    lateinit var promoCheckoutDetailPresenter: PromoCheckoutDetailPresenter

    @Inject
    lateinit var trackingPromoCheckoutUtil: TrackingPromoCheckoutUtil

    private var isOneClickShipment: Boolean = false
    var pageTracking : Int = 1
    lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeCoupon = arguments?.getString(EXTRA_KUPON_CODE, "") ?: ""
        isUse = arguments?.getBoolean(EXTRA_IS_USE, false) ?: false
        isOneClickShipment = arguments?.getBoolean(ONE_CLICK_SHIPMENT, false)?:false
        pageTracking = arguments?.getInt(PAGE_TRACKING, 1)?:1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.title_loading))
    }

    override fun loadData() {
        super.loadData()
        promoCheckoutDetailPresenter.getDetailPromo(codeCoupon,isOneClickShipment)
    }

    override fun onClickUse() {
        promoCheckoutDetailPresenter.validatePromoUse(codeCoupon, isOneClickShipment,  resources)
    }

    override fun onClickCancel() {
        if(pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickCancelPromoCoupon(codeCoupon)
        }else{
            trackingPromoCheckoutUtil.checkoutClickCancelPromoCoupon(codeCoupon)
        }
        promoCheckoutDetailPresenter.cancelPromo()
    }

    override fun onSuccessValidatePromo(dataVoucher: DataVoucher) {
        if(pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCouponSuccess(dataVoucher.code?:"")
        }else{
            trackingPromoCheckoutUtil.checkoutClickUsePromoCouponSuccess(dataVoucher.code?:"")
        }
        super.onSuccessValidatePromo(dataVoucher)
    }

    override fun onErrorValidatePromo(e: Throwable) {
        if(pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCouponFailed()
        }else{
            trackingPromoCheckoutUtil.checkoutClickUsePromoCouponFailed()
        }
        super.onErrorValidatePromo(e)
    }

    override fun initInjector() {
        DaggerPromoCheckoutDetailComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .promoCheckoutDetailModule(PromoCheckoutDetailModule())
                .build()
                .inject(this)
        promoCheckoutDetailPresenter.attachView(this)
    }

    override fun hideProgressLoading() {
        progressDialog?.hide()
    }

    override fun showProgressLoading() {
        try {
            progressDialog?.show()
        } catch (exception: UnsupportedOperationException) {
            CommonUtils.dumper(exception)
        }
    }

    override fun onDestroy() {
        promoCheckoutDetailPresenter.detachView()
        super.onDestroy()
    }

    companion object {
        val EXTRA_KUPON_CODE = "EXTRA_KUPON_CODE"
        val EXTRA_IS_USE = "EXTRA_IS_USE"
        val ONE_CLICK_SHIPMENT = "ONE_CLICK_SHIPMENT"
        val PAGE_TRACKING= "PAGE_TRACKING"

        fun createInstance(codeCoupon: String, isUse: Boolean, oneClickShipment: Boolean, pageTracking : Int): PromoCheckoutDetailMarketplaceFragment {
            val promoCheckoutDetailFragment = PromoCheckoutDetailMarketplaceFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_KUPON_CODE, codeCoupon)
            bundle.putBoolean(EXTRA_IS_USE, isUse)
            bundle.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            promoCheckoutDetailFragment.arguments = bundle
            return promoCheckoutDetailFragment
        }
    }
}