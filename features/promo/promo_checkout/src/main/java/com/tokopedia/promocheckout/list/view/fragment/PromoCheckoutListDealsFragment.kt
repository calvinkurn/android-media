package com.tokopedia.promocheckout.list.view.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.constant.IRouterConstant
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
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListPresenter
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import timber.log.Timber
import javax.inject.Inject


/**
 * Abrar
 */

class PromoCheckoutListDealsFragment() : BasePromoCheckoutListFragment(), PromoCheckoutListContract.View {

    @Inject
    lateinit var promoCheckoutListDealsPresenter: PromoCheckoutListDealsPresenter

    override var serviceId: String = PromoCheckoutListPresenter.SERVICE_ID_NEW_DEALS
    var categoryID: Int = 1
    var categoryName: String = ""
    var grandTotal: Int = 0
    var metaData: String = ""
    var promoCodeApplied: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        metaData = arguments?.getString(EXTRA_META_DATA, "") ?: ""
        categoryName = arguments?.getString(EXTRA_CATEGORY_NAME) ?: ""
        categoryID = arguments?.getInt(EXTRA_CATEGORY_ID, 1) ?: 1
        grandTotal = arguments?.getInt(EXTRA_GRAND_TOTAL, 0) ?: 0
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
        if (promoCode.isNotEmpty() && metaData.isNotEmpty()) {
             promoCheckoutListDealsPresenter.processCheckDealPromoCode(listOf(promoCode), categoryName, metaData, grandTotal)
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
                ?: "", false, categoryName, grandTotal, metaData), REQUEST_CODE_PROMO_DETAIL)
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
            promoCheckoutListPresenter.getListPromo(serviceId, OMP_CATEGORY_ID, page, resources)
        }
        promoCheckoutListDealsPresenter.getListTravelCollectiveBanner(resources)
    }

    companion object {

        val EXTRA_GRAND_TOTAL = "EXTRA_GRAND_TOTAL"
        val EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME"
        val EXTRA_CATEGORY_ID = "EXTRA_CATEGORYID"
        val EXTRA_PRODUCTID = "EXTRA_PRODUCTID"
        val VOUCHER_DISCOUNT_AMOUNT = "VOUCHER_DISCOUNT_AMOUNT"
        val EXTRA_META_DATA = "EXTRA_META_DATA"
        val VOUCHER_CODE = "voucher_code"
        val VOUCHER_MESSAGE = "voucher_message"
        val OMP_CATEGORY_ID = 0
        val PROMOCODE = "promocode"

        fun createInstance(isCouponActive: Boolean?, promoCode: String?, categoryId: Int?, categoryName: String?, grandTotal:Int?, metaData: String?, pageTracking: Int?, productId: String?): PromoCheckoutListDealsFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListDealsFragment()
            val bundle = Bundle()
            bundle.putBoolean(EXTRA_COUPON_ACTIVE, isCouponActive ?: true)
            bundle.putString(EXTRA_PROMO_CODE, promoCode.orEmpty())
            bundle.putString(EXTRA_CATEGORY_NAME, categoryName.orEmpty())
            bundle.putInt(EXTRA_CATEGORY_ID, categoryId ?: 1)
            bundle.putInt(EXTRA_GRAND_TOTAL, grandTotal ?: 0)
            bundle.putInt(PAGE_TRACKING, pageTracking ?: 1)
            bundle.putString(EXTRA_PRODUCTID, productId.orEmpty())
            bundle.putString(EXTRA_META_DATA, metaData.orEmpty())
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }
    }
}