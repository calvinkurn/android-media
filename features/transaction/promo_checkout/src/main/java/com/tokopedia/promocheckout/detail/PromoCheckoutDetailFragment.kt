package com.tokopedia.promocheckout.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailModule
import javax.inject.Inject

class PromoCheckoutDetailFragment : BaseDaggerFragment(), PromoCheckoutDetailContract.View {

    @Inject
    lateinit var promoCheckoutDetailPresenter: PromoCheckoutDetailPresenter

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_checkout_detail_layout, container, false)
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
        fun createInstance():PromoCheckoutDetailFragment{
            return PromoCheckoutDetailFragment()
        }
    }
}
