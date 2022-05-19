package com.tokopedia.promocheckout.detail.view.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
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
    var categoryName: String = ""
    var grandTotal: Int = 0
    var metaData: String = ""

    @Inject
    lateinit var userSession: UserSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        codeCoupon = arguments?.getString(EXTRA_KUPON_CODE, "") ?: ""
        isUse = arguments?.getBoolean(EXTRA_IS_USE, false) ?: false
        metaData = arguments?.getString(EXTRA_META_DATA, "") ?: ""
        categoryName = arguments?.getString(EXTRA_CATEGORY_NAME) ?: ""
        grandTotal = arguments?.getInt(EXTRA_GRAND_TOTAL, 0) ?: 0
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
        promoCheckoutDetailDealsPresenter.processCheckDealPromoCode(listOf(codeCoupon), categoryName, metaData, grandTotal)
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
        val EXTRA_META_DATA = "EXTRA_META_DATA"
        val EXTRA_GRAND_TOTAL = "EXTRA_GRAND_TOTAL"
        val EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME"
        val COUPON_MESSAGE = "coupon_message"
        val COUPON_AMOUNT = "coupon_amount"
        val COUPON_CODE = "coupon_code"
        val IS_CANCEL = "IS_CANCEL"

        fun createInstance(codeCoupon: String, isUse: Boolean, categoryName: String?, grandTotal:Int?, metaData: String?): PromoCheckoutDetailDealsFragment {
            val promoCheckoutDetailDealsFragment = PromoCheckoutDetailDealsFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_KUPON_CODE, codeCoupon)
            bundle.putBoolean(EXTRA_IS_USE, isUse)
            bundle.putString(EXTRA_CATEGORY_NAME, categoryName.orEmpty())
            bundle.putInt(EXTRA_GRAND_TOTAL, grandTotal ?: 0)
            bundle.putString(EXTRA_META_DATA, metaData.orEmpty())
            promoCheckoutDetailDealsFragment.arguments = bundle
            return promoCheckoutDetailDealsFragment
        }
    }
}