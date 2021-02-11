package com.tokopedia.promocheckout.list.view.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailDealsActivity
import com.tokopedia.promocheckout.detail.view.fragment.PromoCheckoutDetailDealsFragment
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListDealsPresenter
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import timber.log.Timber
import javax.inject.Inject


/**
 * Abrar
 */

class PromoCheckoutListDealsFragment() : BasePromoCheckoutListFragment(), PromoCheckoutListContract.View {

    @Inject
    lateinit var promoCheckoutListDealsPresenter: PromoCheckoutListDealsPresenter

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING
    var categoryID: Int = 1
    var checkoutData: String = ""
    var promoCodeApplied: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryID = arguments?.getInt(EXTRA_CATEGORY_ID, 1) ?: 1
        checkoutData = arguments?.getString(EXTRA_CHECKOUT_DATA) ?: ""
        promoCheckoutListDealsPresenter.attachView(this)
        promoCodeApplied = arguments?.getString(EXTRA_PROMO_CODE) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (promoCodeApplied != null) {
            textInputCoupon.textFieldInput.setText(promoCodeApplied)
        }
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
    }

    override fun onPromoCodeUse(promoCode: String) {
        if (promoCheckoutListDealsPresenter.isViewAttached) promoCheckoutListDealsPresenter.attachView(this)
        if (promoCode.isNotEmpty()) {
            var requestBody: JsonObject? = null
            if (checkoutData.isNotBlank() || checkoutData.length > 0) {
                val jsonElement: JsonElement = JsonParser().parse(checkoutData)
                requestBody = jsonElement.asJsonObject
                requestBody.addProperty(PROMOCODE, promoCode)
                promoCheckoutListDealsPresenter.processCheckDealPromoCode(false, requestBody)
            }
        }
    }

    override fun onClickItemLastSeen(promoCheckoutLastSeenModel: PromoCheckoutLastSeenModel) {
        textInputCoupon.textFieldInput.setText(promoCheckoutLastSeenModel.promoCode)
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        super.onItemClicked(promoCheckoutListModel)
        navigateToPromoDetail(promoCheckoutListModel)
    }

    open fun navigateToPromoDetail(promoCheckoutListModel: PromoCheckoutListModel?) {
        startActivityForResult(PromoCheckoutDetailDealsActivity.newInstance(
                activity, promoCheckoutListModel?.code
                ?: "", false, checkoutData), REQUEST_CODE_PROMO_DETAIL)
    }

    override fun onSuccessCheckPromo(data: DataUiModel) {
        val intent = Intent()
        val promoData = PromoData(PromoData.VOUCHER_RESULT_CODE, data.codes[0],
                data.message.text, data.titleDescription, state = data.message.state.mapToStatePromoCheckout())
        intent.putExtra(PromoCheckoutDetailDealsFragment.IS_CANCEL, false)
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        intent.putExtra(VOUCHER_CODE, data.codes[0])
        intent.putExtra(VOUCHER_MESSAGE, data.message.text)
        intent.putExtra(VOUCHER_DISCOUNT_AMOUNT, data.discountAmount)
        activity?.setResult(PromoData.VOUCHER_RESULT_CODE, intent)
        activity?.finish()
    }

    override fun initInjector() {
        getComponent(PromoCheckoutListComponent::class.java).inject(this)
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

    override fun onDestroyView() {
        promoCheckoutListDealsPresenter.detachView()
        super.onDestroyView()
    }

    override fun loadData(page: Int) {
        if (isCouponActive) {
            promoCheckoutListPresenter.getListPromo(serviceId, categoryID, page, resources)
        }
        promoCheckoutListDealsPresenter.getListTravelCollectiveBanner(resources)
    }

    companion object {

        val EXTRA_CATEGORY_ID = "EXTRA_CATEGORYID"
        val EXTRA_PRODUCTID = "EXTRA_PRODUCTID"
        val VOUCHER_DISCOUNT_AMOUNT = "VOUCHER_DISCOUNT_AMOUNT"
        val EXTRA_CHECKOUT_DATA = "checkoutdata"
        val VOUCHER_CODE = "voucher_code"
        val VOUCHER_MESSAGE = "voucher_message"
        val PROMOCODE = "promocode"

        fun createInstance(isCouponActive: Boolean?, promoCode: String?, categoryId: Int?, pageTracking: Int?, productId: String?, checkoutData: String?): PromoCheckoutListDealsFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListDealsFragment()
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_COUPON_ACTIVE, isCouponActive ?: true)
            bundle.putString(EXTRA_PROMO_CODE, promoCode ?: "")
            bundle.putInt(EXTRA_CATEGORY_ID, categoryId ?: 1)
            bundle.putInt(PAGE_TRACKING, pageTracking ?: 1)
            bundle.putString(EXTRA_PRODUCTID, productId ?: "")
            bundle.putString(EXTRA_CHECKOUT_DATA, checkoutData ?: "")
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }
    }
}