package com.tokopedia.promocheckout.detail.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailDigitalActivity
import com.tokopedia.promocheckout.detail.view.viewmodel.PromoCheckoutDetailDigitalViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import timber.log.Timber
import javax.inject.Inject

class PromoCheckoutDetailDigitalFragment : BasePromoCheckoutDetailFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val promoCheckoutDetailDigitalViewModel: PromoCheckoutDetailDigitalViewModel by lazy { viewModelProvider.get(PromoCheckoutDetailDigitalViewModel::class.java) }


    lateinit var promoDigitalModel: PromoDigitalModel
    lateinit var promoCheckoutDetailComponent: PromoCheckoutDetailComponent
    @Inject
    lateinit var userSession: UserSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        codeCoupon = arguments?.getString(EXTRA_KUPON_CODE, "") ?: ""
        isUse = arguments?.getBoolean(EXTRA_IS_USE, false) ?: false
        promoDigitalModel = arguments?.getParcelable(EXTRA_PROMO_DIGITAL_MODEL) ?: PromoDigitalModel()
        pageTracking = arguments?.getInt(PAGE_TRACKING, 1) ?: 1
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        promoCheckoutDetailDigitalViewModel.showLoadingPromoDigital.observe(viewLifecycleOwner, {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        })

        promoCheckoutDetailDigitalViewModel.showProgressLoadingPromoDigital.observe(viewLifecycleOwner, {
            if (it) {
                showProgressLoading()
            } else {
                hideProgressLoading()
            }
        })

        promoCheckoutDetailDigitalViewModel.digitalCheckVoucherResult.observe(viewLifecycleOwner, {
            when(it){
                is Success ->{
                    onSuccessCheckPromo(it.data)
                }
                is Fail ->{
                    if (it.throwable is MessageErrorException){
                        onErrorCheckPromo(it.throwable)
                    }else{
                        onErrorCheckPromoStacking(it.throwable)
                    }
                }
            }
        })

        promoCheckoutDetailDigitalViewModel.promoCheckoutDetail.observe(viewLifecycleOwner, {
            when(it){
                is Success ->{
                    onSuccessGetDetailPromo(it.data)
                }
                is Fail ->{
                    onErroGetDetail(it.throwable)
                }
            }
        })
    }

    fun initView(){
        promoCheckoutDetailComponent = (activity as PromoCheckoutDetailDigitalActivity).getComponent()
        promoCheckoutDetailComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
    }

    override fun loadData() {
        super.loadData()
        promoCheckoutDetailDigitalViewModel.getDetailPromo(codeCoupon)
    }

    override fun onClickUse() {
        promoCheckoutAnalytics.clickUseDigitalMyPromo(codeCoupon, userSession.userId)
        promoCheckoutDetailDigitalViewModel.checkPromoCode(codeCoupon, promoDigitalModel)
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
                data.message.text, data.titleDescription, data.discountAmount, state = data.message.state.mapToStatePromoCheckout())
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
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

    companion object {
        const val EXTRA_KUPON_CODE = "EXTRA_KUPON_CODE"
        const val EXTRA_IS_USE = "EXTRA_IS_USE"
        const val EXTRA_PROMO_DIGITAL_MODEL = "EXTRA_PROMO_DIGITAL_MODEL"
        const val PAGE_TRACKING = "PAGE_TRACKING"

        private val promoCheckoutAnalytics: PromoCheckoutAnalytics by lazy { PromoCheckoutAnalytics() }

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