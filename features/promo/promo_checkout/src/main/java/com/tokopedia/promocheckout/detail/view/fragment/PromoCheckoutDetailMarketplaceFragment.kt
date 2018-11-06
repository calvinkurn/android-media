package com.tokopedia.promocheckout.detail.view.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailModule
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailPresenter
import javax.inject.Inject

class PromoCheckoutDetailMarketplaceFragment : BasePromoCheckoutDetailFragment() {
    @Inject
    lateinit var promoCheckoutDetailPresenter: PromoCheckoutDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeCoupon = arguments?.getString(EXTRA_KUPON_CODE, "") ?: ""
    }

    override fun loadData() {
        super.loadData()
        promoCheckoutDetailPresenter.getDetailPromo(codeCoupon,resources)
    }

    override fun onClickUse() {
        promoCheckoutDetailPresenter.validatePromoUse(codeCoupon, resources)
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

        fun createInstance(codeCoupon: String): PromoCheckoutDetailMarketplaceFragment {
            val promoCheckoutDetailFragment = PromoCheckoutDetailMarketplaceFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_KUPON_CODE, codeCoupon)
            promoCheckoutDetailFragment.arguments = bundle
            return promoCheckoutDetailFragment
        }
    }
}