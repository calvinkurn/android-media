package com.tokopedia.promocheckout.detail.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailModule
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailDigitalPresenter
import javax.inject.Inject

class PromoCheckoutDetailDigitalFragment : BasePromoCheckoutDetailFragment() {
    @Inject
    lateinit var promoCheckoutDetailDigitalPresenter: PromoCheckoutDetailDigitalPresenter

    lateinit var promoDigitalModel: PromoDigitalModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeCoupon = arguments?.getString(EXTRA_KUPON_CODE, "") ?: ""
        isUse = arguments?.getBoolean(EXTRA_IS_USE, false) ?: false
        promoDigitalModel = arguments?.getParcelable(EXTRA_PROMO_DIGITAL_MODEL) ?: PromoDigitalModel()
        pageTracking = arguments?.getInt(PAGE_TRACKING, 1) ?: 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.title_loading))
    }

    override fun loadData() {
        super.loadData()
        promoCheckoutDetailDigitalPresenter.getDetailPromo(codeCoupon)
    }

    override fun onClickUse() {
        promoCheckoutDetailDigitalPresenter.checkVoucher(codeCoupon, promoDigitalModel)
    }

    override fun onClickCancel() {
        super.onClickCancel()
        val intent = Intent()
        val promoData = PromoData(PromoData.TYPE_COUPON, state = TickerCheckoutView.State.EMPTY)
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onSuccessCheckPromo(data: DataUiModel) {
        val intent = Intent()
        val promoData = PromoData(PromoData.TYPE_COUPON, data.codes[0],
                data.message.text, data.titleDescription, state = data.message.state.mapToStatePromoCheckout())
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun initInjector() {
        DaggerPromoCheckoutDetailComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .promoCheckoutDetailModule(PromoCheckoutDetailModule())
                .build()
                .inject(this)
        promoCheckoutDetailDigitalPresenter.attachView(this)
    }

    override fun onDestroy() {
        promoCheckoutDetailDigitalPresenter.detachView()
        super.onDestroy()
    }

    companion object {
        val EXTRA_PROMO_DIGITAL_MODEL = "EXTRA_PROMO_DIGITAL_MODEL"

        fun createInstance(codeCoupon: String, isUse: Boolean, promoDigitalModel: PromoDigitalModel, pageTracking: Int): PromoCheckoutDetailDigitalFragment {
            val promoCheckoutDetailFragment = PromoCheckoutDetailDigitalFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_KUPON_CODE, codeCoupon)
            bundle.putBoolean(EXTRA_IS_USE, isUse)
            bundle.putParcelable(EXTRA_PROMO_DIGITAL_MODEL, promoDigitalModel)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            promoCheckoutDetailFragment.arguments = bundle
            return promoCheckoutDetailFragment
        }
    }
}