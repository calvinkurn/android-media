package com.tokopedia.promocheckout.detail.view.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailModule
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailPresenter
import javax.inject.Inject

class PromoCheckoutDetailMarketplaceFragment : BasePromoCheckoutDetailFragment() {
    @Inject
    lateinit var promoCheckoutDetailPresenter: PromoCheckoutDetailPresenter

    private var isOneClickShipment: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeCoupon = arguments?.getString(EXTRA_KUPON_CODE, "") ?: ""
        isUse = arguments?.getBoolean(EXTRA_IS_USE, false) ?: false
        isOneClickShipment = arguments?.getBoolean(ONE_CLICK_SHIPMENT, false)?:false
    }

    override fun loadData() {
        super.loadData()
        promoCheckoutDetailPresenter.getDetailPromo(codeCoupon,resources)
    }

    override fun onClickUse() {
        promoCheckoutDetailPresenter.validatePromoUse(codeCoupon, isOneClickShipment,  resources)
    }

    override fun onClickCancel() {
        promoCheckoutDetailPresenter.cancelPromo()
    }

    override fun onErrorCancelPromo(e: Throwable) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
    }

    override fun onSuccessCancelPromo() {
        isUse = false
        validateButton()
    }

    override fun initInjector() {
        DaggerPromoCheckoutDetailComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .promoCheckoutDetailModule(PromoCheckoutDetailModule())
                .build()
                .inject(this)
        promoCheckoutDetailPresenter.attachView(this)
    }

    override fun onDestroy() {
        promoCheckoutDetailPresenter.detachView()
        super.onDestroy()
    }

    companion object {
        val EXTRA_KUPON_CODE = "EXTRA_KUPON_CODE"
        val EXTRA_IS_USE = "EXTRA_IS_USE"
        val ONE_CLICK_SHIPMENT = "ONE_CLICK_SHIPMENT"

        fun createInstance(codeCoupon: String, isUse: Boolean, oneClickShipment: Boolean): PromoCheckoutDetailMarketplaceFragment {
            val promoCheckoutDetailFragment = PromoCheckoutDetailMarketplaceFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_KUPON_CODE, codeCoupon)
            bundle.putBoolean(EXTRA_IS_USE, isUse)
            bundle.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment)
            promoCheckoutDetailFragment.arguments = bundle
            return promoCheckoutDetailFragment
        }
    }
}