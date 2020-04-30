package com.tokopedia.promocheckout.detail.view.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailEventActivity
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailEventPresenter
import timber.log.Timber
import javax.inject.Inject

class PromoCheckoutDetailEventFragment : BasePromoCheckoutDetailFragment() {

    @Inject
    lateinit var promoCheckoutDetailEventPresenter : PromoCheckoutDetailEventPresenter

    lateinit var promoCheckoutDetailComponent: PromoCheckoutDetailComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        codeCoupon = arguments?.getString(EXTRA_KUPON_CODE, "") ?: ""
        isUse = arguments?.getBoolean(EXTRA_IS_USE, false) ?: false
        pageTracking = arguments?.getInt(PAGE_TRACKING, 1) ?: 1

    }

    fun initView(){
        promoCheckoutDetailComponent = (activity as PromoCheckoutDetailEventActivity).getComponent()
        promoCheckoutDetailComponent.inject(this)
        promoCheckoutDetailEventPresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.title_loading))
    }

    override fun loadData() {
        super.loadData()
        promoCheckoutDetailEventPresenter.getDetailPromo(codeCoupon)
    }

    override fun hideProgressLoading() {
        progressDialog?.hide()
    }

    override fun showProgressLoading() {
        try {
            progressDialog?.show()
        } catch (exception: UnsupportedOperationException) {
            Timber.d(exception)
        }
    }

    override fun onDestroy() {
        promoCheckoutDetailEventPresenter.detachView()
        super.onDestroy()
    }

    override fun onClickUse() {

    }

    companion object{
        val EXTRA_KUPON_CODE = "EXTRA_KUPON_CODE"
        val EXTRA_IS_USE = "EXTRA_IS_USE"
        val PAGE_TRACKING = "PAGE_TRACKING"

        fun createInstance(codeCoupon: String, isUse: Boolean, pageTracking: Int): PromoCheckoutDetailEventFragment {
            val promoCheckoutDetailFragment = PromoCheckoutDetailEventFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_KUPON_CODE, codeCoupon)
            bundle.putBoolean(EXTRA_IS_USE, isUse)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            promoCheckoutDetailFragment.arguments = bundle
            return promoCheckoutDetailFragment
        }
    }
}