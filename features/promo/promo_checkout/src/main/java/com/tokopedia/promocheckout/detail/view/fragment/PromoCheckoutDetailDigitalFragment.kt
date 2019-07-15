package com.tokopedia.promocheckout.detail.view.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.analytics.FROM_CART
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.util.EXTRA_CLASHING_DATA
import com.tokopedia.promocheckout.common.util.RESULT_CLASHING
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailModule
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailDigitalPresenter
import javax.inject.Inject

class PromoCheckoutDetailDigitalFragment : BasePromoCheckoutDetailFragment() {
    @Inject
    lateinit var promoCheckoutDetailDigitalPresenter: PromoCheckoutDetailDigitalPresenter

    @Inject
    lateinit var trackingPromoCheckoutUtil: TrackingPromoCheckoutUtil

    var pageTracking: Int = 1
    var promo: Promo? = Promo()
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeCoupon = arguments?.getString(EXTRA_KUPON_CODE, "") ?: ""
        isUse = arguments?.getBoolean(EXTRA_IS_USE, false) ?: false
        pageTracking = arguments?.getInt(PAGE_TRACKING, 1) ?: 1
        promo = arguments?.getParcelable(CHECK_PROMO_CODE_FIRST_STEP_PARAM)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.title_loading))
    }

    override fun loadData() {
        super.loadData()
        promoCheckoutDetailDigitalPresenter.getDetailPromo(codeCoupon, promo = promo)
    }

    override fun onClickUse() {
        promoCheckoutDetailDigitalPresenter.validatePromoStackingUse(codeCoupon, promo, false)
    }

    override fun onClickCancel() {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickCancelPromoCoupon(codeCoupon)
        } else {
            trackingPromoCheckoutUtil.checkoutClickCancelPromoCoupon(codeCoupon)
        }
        promoCheckoutDetailDigitalPresenter.cancelPromo(codeCoupon)
    }

    override fun onSuccessValidatePromoStacking(data: DataUiModel) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCouponSuccess(data.codes[0])
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCouponSuccess(data.codes[0])
        }
        super.onSuccessValidatePromoStacking(data)
    }

    override fun onClashCheckPromo(clasingInfoDetailUiModel: ClashingInfoDetailUiModel) {
        val intent = Intent()
        intent.putExtra(EXTRA_CLASHING_DATA, clasingInfoDetailUiModel)
        activity?.setResult(RESULT_CLASHING, intent)
        activity?.finish()

        super.onClashCheckPromo(clasingInfoDetailUiModel)
    }

    override fun onErrorValidatePromo(e: Throwable) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCouponFailed()
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCouponFailed()
        }
        super.onErrorValidatePromo(e)
    }

    override fun onErrorValidatePromoStacking(e: Throwable) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCouponFailed()
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCouponFailed()
        }
        super.onErrorValidatePromoStacking(e)
    }

    override fun initInjector() {
        DaggerPromoCheckoutDetailComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .promoCheckoutDetailModule(PromoCheckoutDetailModule())
                .build()
                .inject(this)
        promoCheckoutDetailDigitalPresenter.attachView(this)
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
        promoCheckoutDetailDigitalPresenter.detachView()
        super.onDestroy()
    }

    companion object {
        val EXTRA_KUPON_CODE = "EXTRA_KUPON_CODE"
        val EXTRA_IS_USE = "EXTRA_IS_USE"
        val ONE_CLICK_SHIPMENT = "ONE_CLICK_SHIPMENT"
        val PAGE_TRACKING = "PAGE_TRACKING"
        val CHECK_PROMO_CODE_FIRST_STEP_PARAM = "CHECK_PROMO_CODE_FIRST_STEP_PARAM"

        fun createInstance(codeCoupon: String, isUse: Boolean, pageTracking: Int): PromoCheckoutDetailDigitalFragment {
            val promoCheckoutDetailFragment = PromoCheckoutDetailDigitalFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_KUPON_CODE, codeCoupon)
            bundle.putBoolean(EXTRA_IS_USE, isUse)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            promoCheckoutDetailFragment.arguments = bundle
            return promoCheckoutDetailFragment
        }
    }
}