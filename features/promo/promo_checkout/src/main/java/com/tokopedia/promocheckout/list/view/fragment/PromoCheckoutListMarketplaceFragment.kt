package com.tokopedia.promocheckout.list.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.analytics.FROM_CART
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoFirstStepParam
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeException
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailMarketplaceActivity
import com.tokopedia.promocheckout.list.di.DaggerPromoCheckoutListComponent
import com.tokopedia.promocheckout.list.di.PromoCheckoutListModule
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListMarketplaceContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListMarketplacePresenter
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import javax.inject.Inject

class PromoCheckoutListMarketplaceFragment : BasePromoCheckoutListFragment(), PromoCheckoutListMarketplaceContract.View {

    @Inject
    lateinit var promoCheckoutListMarketplacePresenter: PromoCheckoutListMarketplacePresenter

    @Inject
    lateinit var trackingPromoCheckoutUtil: TrackingPromoCheckoutUtil

    private var isOneClickShipment: Boolean = false
    lateinit var progressDialog: ProgressDialog
    var pageTracking: Int = 1
    private var checkPromoFirstStepParam: CheckPromoFirstStepParam? = null

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        isCouponActive = arguments?.getBoolean(IS_COUPON_ACTIVE) ?: true
        promoCode = arguments?.getString(PROMO_CODE) ?: ""
        isOneClickShipment = arguments?.getBoolean(ONE_CLICK_SHIPMENT) ?: false
        pageTracking = arguments?.getInt(PAGE_TRACKING) ?: 1
        checkPromoFirstStepParam = arguments?.getParcelable(CHECK_PROMO_FIRST_STEP_PARAM)
        super.onCreate(savedInstanceState)
        promoCheckoutListMarketplacePresenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.title_loading))
        textInputCoupon.setText(promoCode)
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickCoupon(promoCheckoutListModel?.code ?: "")
        } else {
            trackingPromoCheckoutUtil.checkoutClickCoupon(promoCheckoutListModel?.code ?: "")
        }
        startActivityForResult(PromoCheckoutDetailMarketplaceActivity.createIntent(
                activity, promoCheckoutListModel?.code, oneClickShipment = isOneClickShipment, pageTracking = pageTracking, checkPromoFirstStepParam = checkPromoFirstStepParam), REQUEST_CODE_DETAIL_PROMO)
    }

    override fun onPromoCodeUse(promoCode: String) {
        promoCheckoutListMarketplacePresenter.checkPromoStackingCode(promoCode, isOneClickShipment, checkPromoFirstStepParam)
    }

    override fun showProgressLoading() {
        progressDialog.show()
    }

    override fun hideProgressLoading() {
        progressDialog.hide()
    }

    override fun onErrorCheckPromoCode(e: Throwable) {
        if (e is CheckPromoCodeException || e is MessageErrorException) {
            textInputLayoutCoupon.error = e.message
        } else {
            NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
        }
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCodeFailed()
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCodeFailed()
        }
    }

    override fun onErrorEmptyPromoCode() {
        textInputLayoutCoupon.error = getString(R.string.promo_checkout_label_error_empty_voucher_code)
    }

    /*override fun onSuccessCheckPromoCode(dataVoucher: DataVoucher) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCodeSuccess(dataVoucher.code ?: "")
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCodeSuccess(dataVoucher.code ?: "")
        }
        val intent = Intent()
        val typePromo = if (dataVoucher.isCoupon == PromoData.VALUE_COUPON) PromoData.TYPE_COUPON else PromoData.TYPE_VOUCHER
        val promoData = PromoData(typePromo, dataVoucher.code ?: "",
                dataVoucher.message?.text ?: "", dataVoucher.titleDescription ?: "",
                dataVoucher.cashbackAmount, dataVoucher.message?.state?.mapToStatePromoCheckout()
                ?: TickerCheckoutView.State.EMPTY)
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }*/

    override fun onSuccessCheckPromoStackingCode(data: DataUiModel) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCodeSuccess(data.codes[0])
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCodeSuccess(data.codes[0])
        }
        val intent = Intent()
        val typePromo = if (data.isCoupon == PromoStackingData.VALUE_COUPON) PromoStackingData.TYPE_COUPON else PromoStackingData.TYPE_VOUCHER
        val promoStackingData = PromoStackingData(typePromo, data.codes[0],
                data.message.text, data.titleDescription,
                data.cashbackWalletAmount, data.message.state.mapToStatePromoStackingCheckout())
        intent.putExtra(EXTRA_PROMO_DATA, promoStackingData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onImpressionCoupon(promoCheckoutListModel: PromoCheckoutListModel?) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartImpressionCoupon(promoCheckoutListModel?.code ?: "")
        } else {
            trackingPromoCheckoutUtil.checkoutImpressionCoupon(promoCheckoutListModel?.code ?: "")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_DETAIL_PROMO) {
            activity?.setResult(Activity.RESULT_OK, data)
            activity?.finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initInjector() {
        super.initInjector()
        DaggerPromoCheckoutListComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .promoCheckoutListModule(PromoCheckoutListModule())
                .build()
                .inject(this)
    }

    override fun onDestroyView() {
        promoCheckoutListMarketplacePresenter.detachView()
        super.onDestroyView()
    }

    companion object {
        val REQUEST_CODE_DETAIL_PROMO = 231
        val IS_COUPON_ACTIVE = "IS_COUPON_ACTIVE"
        val PROMO_CODE = "PROMO_CODE"
        val ONE_CLICK_SHIPMENT = "ONE_CLICK_SHIPMENT"
        val PAGE_TRACKING = "PAGE_TRACKING"
        val CHECK_PROMO_FIRST_STEP_PARAM = "CHECK_PROMO_FIRST_STEP_PARAM"

        fun createInstance(isCouponActive: Boolean?, promoCode: String?, oneClickShipment: Boolean?, pageTracking: Int,
                           checkPromoFirstStepParam: CheckPromoFirstStepParam): PromoCheckoutListMarketplaceFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListMarketplaceFragment()
            val bundle = Bundle()
            bundle.putBoolean(IS_COUPON_ACTIVE, isCouponActive ?: true)
            bundle.putString(PROMO_CODE, promoCode ?: "")
            bundle.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment ?: false)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            bundle.putParcelable(CHECK_PROMO_FIRST_STEP_PARAM, checkPromoFirstStepParam)
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }
    }

}
