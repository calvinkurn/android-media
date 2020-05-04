package com.tokopedia.promocheckout.detail.view.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailEventActivity
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailEventPresenter
import timber.log.Timber
import javax.inject.Inject

class PromoCheckoutDetailEventFragment : BasePromoCheckoutDetailFragment() {

    @Inject
    lateinit var promoCheckoutDetailEventPresenter : PromoCheckoutDetailEventPresenter

    lateinit var promoCheckoutDetailComponent: PromoCheckoutDetailComponent

    var eventVerify = EventVerifyBody()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        codeCoupon = arguments?.getString(EXTRA_KUPON_CODE, "") ?: ""
        isUse = arguments?.getBoolean(EXTRA_IS_USE, false) ?: false
        eventVerify = arguments?.getParcelable(EXTRA_EVENT_VERIFY) ?:EventVerifyBody()

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
        promoCheckoutDetailEventPresenter.checkPromoCode(codeCoupon, false, eventVerify)
    }

    companion object{
        const val EXTRA_KUPON_CODE = "EXTRA_KUPON_CODE"
        const val EXTRA_IS_USE = "EXTRA_IS_USE"
        const val EXTRA_EVENT_VERIFY = "EXTRA_EVENT_VERIFY"

        fun createInstance(codeCoupon: String, isUse: Boolean, eventVerifyBody: EventVerifyBody): PromoCheckoutDetailEventFragment {
            val promoCheckoutDetailFragment = PromoCheckoutDetailEventFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_KUPON_CODE, codeCoupon)
            bundle.putBoolean(EXTRA_IS_USE, isUse)
            bundle.putParcelable(EXTRA_EVENT_VERIFY, eventVerifyBody)
            promoCheckoutDetailFragment.arguments = bundle
            return promoCheckoutDetailFragment
        }
    }
}