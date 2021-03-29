package com.tokopedia.promocheckout.detail.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailModule
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailHotelPresenter
import com.tokopedia.promocheckout.util.ColorUtil
import javax.inject.Inject

class PromoCheckoutDetailHotelFragment : BasePromoCheckoutDetailFragment() {

    @Inject
    lateinit var promoCheckoutDetailHotelPresenter: PromoCheckoutDetailHotelPresenter

    var cartID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        codeCoupon = arguments?.getString(EXTRA_KUPON_CODE, "") ?: ""
        cartID = arguments?.getString(EXTRA_CART_ID, "") ?: ""
        isUse = arguments?.getBoolean(EXTRA_IS_USE, false) ?: false
        pageTracking = arguments?.getInt(PAGE_TRACKING, 1) ?: 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
    }

    override fun loadData() {
        super.loadData()
        promoCheckoutDetailHotelPresenter.getDetailPromo(codeCoupon)
    }

    override fun onClickUse() {
        context?.run {
            promoCheckoutDetailHotelPresenter.checkVoucher(codeCoupon, cartID, ColorUtil.getColorFromResToString(this,  com.tokopedia.unifyprinciples.R.color.Unify_G200))
        }
    }

    override fun onClickCancel() {
        super.onClickCancel()
        promoCheckoutDetailHotelPresenter.cancelPromo()
    }

    override fun onSuccessCheckPromo(data: DataUiModel) {
        val intent = Intent()
        val promoData = PromoData(PromoData.TYPE_COUPON, data.codes[0],
                data.message.text, data.titleDescription, state = data.message.state.mapToStatePromoCheckout())
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onSuccessCancelPromo() {
        val intent = Intent()
        val promoData = PromoData(PromoData.TYPE_COUPON, state = TickerCheckoutView.State.EMPTY)
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    fun initView(){
        DaggerPromoCheckoutDetailComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .promoCheckoutDetailModule(PromoCheckoutDetailModule())
                .build()
                .inject(this)
        promoCheckoutDetailHotelPresenter.attachView(this)
    }

    override fun onDestroy() {
        promoCheckoutDetailHotelPresenter.detachView()
        super.onDestroy()
    }

    companion object {
        val EXTRA_CART_ID = "EXTRA_CART_ID"

        fun createInstance(codeCoupon: String, cartID: String, isUse: Boolean, pageTracking: Int): PromoCheckoutDetailHotelFragment {
            val promoCheckoutDetailFragment = PromoCheckoutDetailHotelFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_KUPON_CODE, codeCoupon)
            bundle.putString(EXTRA_CART_ID, cartID)
            bundle.putBoolean(EXTRA_IS_USE, isUse)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            promoCheckoutDetailFragment.arguments = bundle
            return promoCheckoutDetailFragment
        }
    }
}