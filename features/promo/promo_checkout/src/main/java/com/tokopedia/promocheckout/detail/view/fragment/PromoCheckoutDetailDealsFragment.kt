package com.tokopedia.promocheckout.detail.view.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailDealsActivity
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailDealsPresenter
import com.tokopedia.user.session.UserSession
import timber.log.Timber
import javax.inject.Inject

class PromoCheckoutDetailDealsFragment : BasePromoCheckoutDetailFragment() {
    @Inject
    lateinit var promoCheckoutDetailDealsPresenter: PromoCheckoutDetailDealsPresenter

    lateinit var promoCheckoutDetailComponent: PromoCheckoutDetailComponent
    var checkoutData: String = ""

    @Inject
    lateinit var userSession: UserSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        codeCoupon = arguments?.getString(EXTRA_KUPON_CODE, "") ?: ""
        isUse = arguments?.getBoolean(EXTRA_IS_USE, false) ?: false
        checkoutData = arguments?.getString(EXTRA_CHECKOUT_DATA) ?: ""
    }

    fun initView() {
        promoCheckoutDetailComponent = (activity as PromoCheckoutDetailDealsActivity).getComponent()
        promoCheckoutDetailComponent.inject(this)
        promoCheckoutDetailDealsPresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
    }

    override fun loadData() {
        super.loadData()
        promoCheckoutDetailDealsPresenter.getDetailPromo(codeCoupon)
    }

    override fun onClickUse() {
        var requestBody: JsonObject? = null
        val jsonElement: JsonElement = JsonParser().parse(checkoutData).asJsonObject
        requestBody = jsonElement.asJsonObject
        requestBody.addProperty("promocode", codeCoupon)
        promoCheckoutDetailDealsPresenter.processCheckDealPromoCode(codeCoupon, false, requestBody)
    }

    override fun onClickCancel() {
        super.onClickCancel()
        val intent = Intent()
        intent.putExtra(IS_CANCEL, true)
        activity?.setResult(PromoData.COUPON_RESULT_CODE, intent)
        activity?.finish()
    }

    override fun onSuccessCheckPromo(data: DataUiModel) {
        val intent = Intent()
        val promoData = PromoData(PromoData.COUPON_RESULT_CODE, data.codes[0],
                data.message.text, data.titleDescription, state = data.message.state.mapToStatePromoCheckout())
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        intent.putExtra(COUPON_CODE, data.codes[0])
        intent.putExtra(COUPON_MESSAGE, data.message.text)
        intent.putExtra(COUPON_AMOUNT, data.discountAmount)
        intent.putExtra(IS_CANCEL, false)
        activity?.setResult(PromoData.COUPON_RESULT_CODE, intent)
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

    override fun onDestroy() {
        promoCheckoutDetailDealsPresenter.detachView()
        super.onDestroy()
    }

    companion object {
        val EXTRA_IS_USE = "EXTRA_IS_USE"
        val EXTRA_CHECKOUT_DATA = "checkoutdata"
        val COUPON_MESSAGE = "coupon_message"
        val COUPON_AMOUNT = "coupon_amount"
        val COUPON_CODE = "coupon_code"
        val IS_CANCEL = "IS_CANCEL"

        fun createInstance(codeCoupon: String, isUse: Boolean, checkoutData: String?): PromoCheckoutDetailDealsFragment {
            val promoCheckoutDetailDealsFragment = PromoCheckoutDetailDealsFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_KUPON_CODE, codeCoupon)
            bundle.putBoolean(EXTRA_IS_USE, isUse)
            bundle.putString(EXTRA_CHECKOUT_DATA, checkoutData ?: "")
            promoCheckoutDetailDealsFragment.arguments = bundle
            return promoCheckoutDetailDealsFragment
        }
    }
}