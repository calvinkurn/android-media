package com.tokopedia.promocheckout.detail.view.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.util.EXTRA_CLASHING_DATA
import com.tokopedia.promocheckout.common.util.RESULT_CLASHING
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailModule
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailPresenter
import javax.inject.Inject

class PromoCheckoutDetailMarketplaceFragment : BasePromoCheckoutDetailFragment() {
    @Inject
    lateinit var promoCheckoutDetailPresenter: PromoCheckoutDetailPresenter

    private var isOneClickShipment: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        codeCoupon = arguments?.getString(EXTRA_KUPON_CODE, "") ?: ""
        isUse = arguments?.getBoolean(EXTRA_IS_USE, false) ?: false
        isOneClickShipment = arguments?.getBoolean(ONE_CLICK_SHIPMENT, false) ?: false
        pageTracking = arguments?.getInt(PAGE_TRACKING, 1) ?: 1
    }

    fun initView(){
        DaggerPromoCheckoutDetailComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .promoCheckoutDetailModule(PromoCheckoutDetailModule())
                .build()
                .inject(this)
                promoCheckoutDetailPresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
    }

    override fun showActionButtonUse(): Boolean {
        return false
    }

    override fun loadData() {
        super.loadData()
        promoCheckoutDetailPresenter.getDetailPromo(codeCoupon, isOneClickShipment)
    }

    override fun onClickUse() {
        promoCheckoutDetailPresenter.validatePromoStackingUse(codeCoupon, null, false)
    }

    override fun onClickCancel() {
        super.onClickCancel()
        promoCheckoutDetailPresenter.cancelPromo(codeCoupon)
    }

    override fun onClashCheckPromo(clasingInfoDetailUiModel: ClashingInfoDetailUiModel) {
        val intent = Intent()
        intent.putExtra(EXTRA_CLASHING_DATA, clasingInfoDetailUiModel)
        activity?.setResult(RESULT_CLASHING, intent)
        activity?.finish()

        super.onClashCheckPromo(clasingInfoDetailUiModel)
    }

    override fun onDestroy() {
        promoCheckoutDetailPresenter.detachView()
        super.onDestroy()
    }

    companion object {
        val EXTRA_KUPON_CODE = "EXTRA_KUPON_CODE"
        val EXTRA_IS_USE = "EXTRA_IS_USE"
        val ONE_CLICK_SHIPMENT = "ONE_CLICK_SHIPMENT"
        val PAGE_TRACKING = "PAGE_TRACKING"

        fun createInstance(codeCoupon: String, isUse: Boolean, oneClickShipment: Boolean, pageTracking: Int): PromoCheckoutDetailMarketplaceFragment {
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